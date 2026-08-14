# Gerenciador de Eventos

API REST desenvolvida em **Java + Spring Boot** para gerenciamento de eventos. Cada usuário pode criar e administrar seus próprios eventos (como organizador) e também se inscrever em eventos de outras pessoas (como participante) — a mesma conta cobre os dois papéis. Tickets são emitidos como QR code; o organizador escaneia o QR do participante na entrada para confirmar o check-in.

> ⚠️ **Projeto em desenvolvimento**, ainda não lançado publicamente. Veja a seção [Status do projeto](#status-do-projeto) antes de rodar.

## Sobre o projeto

Todo mundo que usa o sistema é um `Usuario` só — não existe uma conta separada para "quem organiza" e "quem participa". Com a mesma conta, você pode:

- Criar e gerenciar seus próprios eventos (você vira o **organizador**/dono daquele evento).
- Se inscrever em eventos de outras pessoas (você vira **participante** daquela inscrição).
- Emitir o ticket (QR code) da sua própria inscrição.
- Se você é organizador de um evento, escanear o QR code dos participantes na entrada para confirmar o check-in.

Um usuário `ADMIN` tem acesso total ao sistema, sem restrição de dono. **O primeiro usuário cadastrado no sistema vira `ADMIN` automaticamente** — os demais entram como `USER`.

Fluxo geral das entidades:

```
Usuario (organizador) ──> Evento ──> Inscricao ──> Ticket ──> CheckIn
                                        ↑
Usuario (participante) ─────────────────┘
```

## Tecnologias

- **Java 17**
- **Spring Boot 4.0.6**
  - Spring Web (MVC)
  - Spring Data JPA
  - Spring Security
  - Bean Validation
  - Flyway (migração de banco)
- **JWT** (`com.auth0:java-jwt`) para autenticação via token
- **ZXing** (`com.google.zxing`) para gerar o QR code dos tickets
- **springdoc-openapi** para documentação interativa da API (Swagger UI)
- **H2** (em memória, só em testes) + **JUnit 5** para os testes de integração
- **MySQL** (via `mysql-connector-j`)
- **Maven**

## Estrutura do projeto

```
src/main/java/Projeto/Gerenciador_Eventos/
├── controllers/     # Endpoints REST
├── dto/              # Records de entrada/saída (cadastro, listagem, detalhamento, erro, autenticação)
├── entity/            # Entidades JPA (Usuario, Evento, Inscricao, Ticket, CheckIn)
│   └── enums/          # StatusGeral, StatusCheckIn, Perfil (ADMIN/USER)
├── handler/           # Tratamento global de exceções (TratadorDeErros)
├── repository/       # Interfaces Spring Data JPA
├── security/          # Configuração do Spring Security e filtro de autenticação JWT
├── util/              # Geração de QR code (QrCodeGenerator)
└── service/           # Regras de negócio
```

## Domínio / Entidades

| Entidade | Descrição | Status |
|---|---|---|
| **Usuario** | Nome, login, senha (hash BCrypt), CPF, telefone, perfil (`ADMIN`/`USER`) | — |
| **Evento** | Nome, descrição, data, local, vagas totais/disponíveis, preço, **organizador** (dono) | `ATIVO` / `INATIVO` |
| **Inscricao** | Vincula um Usuario (participante) a um Evento, com data de inscrição | `ATIVO` / `INATIVO` |
| **Ticket** | Gerado a partir de uma Inscricao, com código único (usado no QR code) | `ATIVO` / `INATIVO` |
| **CheckIn** | Registro de check-in de um Ticket, com data/hora | `REALIZADO` / `NAO_REALIZADO` |

## Autenticação e permissões

A API usa **JWT**. Todas as rotas exigem token válido, exceto `/login` e `/usuario/cadastrar`.

- `POST /usuario/cadastrar` — público. Corpo: `{ "nome", "login", "senha", "cpf", "telefone" }`. Senha vira hash BCrypt. O primeiro usuário cadastrado vira `ADMIN`.
- `POST /login` — público. Corpo: `{ "login", "senha" }`. Retorna `{ "token" }`.
- Demais rotas: envie `Authorization: Bearer <token>`. Token expira em 2 horas.

**Regras de posse (quem pode fazer o quê):**

| Ação | Quem pode |
|---|---|
| Atualizar/ativar/inativar um evento | O organizador (dono) do evento, ou `ADMIN` |
| Ativar/cancelar uma inscrição | O próprio participante (dono da inscrição), o organizador do evento, ou `ADMIN` |
| Emitir ticket de uma inscrição | O próprio participante (dono da inscrição), ou `ADMIN` |
| Ver o QR code de um ticket | O próprio participante (dono do ticket), ou `ADMIN` |
| Confirmar check-in (escanear ticket) | O organizador do evento, ou `ADMIN` |
| Cadastrar/detalhar/listar eventos | Qualquer usuário autenticado (todo mundo pode ver/procurar eventos pra se inscrever) |

Violação de posse retorna `403 Forbidden`.

## Endpoints implementados

### `/usuario`
- `POST /usuario/cadastrar`

### `/login`
- `POST /login`

### `/evento`
- `POST /evento/cadastrar` — organizador = usuário logado
- `GET /evento/detalhar/{id}`
- `PUT /evento/atualizar`
- `DELETE /evento/inativar/{id}`
- `PUT /evento/reativar/{id}`
- `GET /evento/listar`
- `GET /evento/listar/filtro`

### `/inscricao`
- `POST /inscricao/cadastrar` — corpo: `{ "idEvento", "dataInscricao" }`. Participante = usuário logado (auto-inscrição)
- `GET /inscricao/detalhar/{id}`
- `DELETE /inscricao/inativar/{id}`
- `PUT /inscricao/reativar/{id}`
- `GET /inscricao/listar`
- `GET /inscricao/listar/filtro`

### `/ticket`
- `POST /ticket/cadastrar` — corpo: `{ "idInscricao" }`. Código do ticket é gerado pelo servidor
- `GET /ticket/detalhar/{id}`
- `GET /ticket/detalhar/{id}/qrcode` — devolve a imagem PNG do QR code
- `DELETE /ticket/inativar/{id}`
- `PUT /ticket/reativar/{id}`
- `GET /ticket/listar`
- `GET /ticket/listar/filtro`

### `/checkin`
- `POST /checkin/cadastrar` — cria um registro de check-in pendente (`NAO_REALIZADO`) pra um ticket
- `PUT /checkin/realizarcheckin/{id}` — confirma o check-in pelo id do registro
- `POST /checkin/escanear` — **fluxo principal pro app do organizador**: corpo `{ "codigoHashTicket" }` (o texto lido do QR code). Acha o ticket pelo código, valida que quem chama é o organizador do evento, e confirma o check-in em uma única chamada (cria o registro se não existir, ou atualiza se já existir)
- `GET /checkin/detalhar/{id}`
- `GET /checkin/listar`
- `GET /checkin/listar/filtro`

### Respostas de erro

- `403 Forbidden`: sem token válido, ou usuário sem permissão para a ação (violação de posse).
- `404 Not Found`: id informado não existe.
- `400 Bad Request`: falha de validação (`@Valid`) ou violação de regra de negócio, como:
  - login ou senha inválidos ao autenticar;
  - login ou CPF já cadastrado;
  - criar inscrição sem vagas disponíveis, ou inscrição duplicada;
  - emitir ticket para inscrição inativa;
  - escanear ticket inexistente, inativo, ou já com check-in realizado.

## Status do projeto

- [x] Banco de dados configurado via variáveis de ambiente + migrações Flyway.
- [x] Endpoints de detalhamento e atualização.
- [x] Regras de negócio de vagas e validações cruzadas (inscrição duplicada, ticket de inscrição inativa).
- [x] Tratamento global de erros (`404`/`400`/`403` padronizados).
- [x] Autenticação via JWT com Spring Security.
- [x] **Modelo de dono/organizador e permissões (`ADMIN`/`USER`)** — cada usuário administra seus próprios eventos e inscrições; `Participante` foi unificado com `Usuario` (participante também loga e se auto-inscreve).
- [x] **QR code dos tickets** — geração da imagem e endpoint de escaneamento para check-in.
- [x] **Testes automatizados**: teste de integração (`FluxoPrincipalIntegrationTest`) cobrindo o fluxo completo (cadastro → login → evento → inscrição → ticket → check-in) e as regras de posse/negócio, usando H2 em memória. Ainda é só um arquivo — cobertura pode crescer.
- [x] **Documentação da API (Swagger/OpenAPI)**: disponível em `/swagger-ui.html` com a aplicação rodando (JSON da especificação em `/v3/api-docs`).
- [x] **Gerenciamento de participantes pelo organizador**: o organizador do evento agora também pode ativar/cancelar inscrições de outras pessoas no próprio evento (antes só o próprio participante conseguia).
- [x] **CORS**: configurado via Spring Security, liberando o(s) domínio(s) do frontend definidos em `api.cors.allowed-origins` (variável de ambiente `CORS_ALLOWED_ORIGINS`).

## Documentação interativa (Swagger)

Com a aplicação rodando, acesse `http://localhost:8080/swagger-ui.html` para ver e testar todos os endpoints direto no navegador. Para chamar rotas autenticadas por ali, clique em "Authorize" (canto superior direito) e cole o token retornado por `/login` (sem o prefixo `Bearer `).

## Rodando os testes

```bash
./mvnw test
```

Os testes usam H2 em memória (não precisa MySQL rodando para testar) e o Hibernate cria o schema direto das entidades, sem passar pelas migrações Flyway — ou seja, os testes validam o comportamento da aplicação, mas não substituem rodar a aplicação de verdade contra o MySQL pra validar as migrações em si.

## Como rodar

Pré-requisitos: JDK 17, Maven (ou `./mvnw`) e uma instância MySQL.

⚠️ **Este projeto passou por uma mudança de estrutura de tabelas** (participante virou usuário, evento ganhou dono). Se você já tinha rodado uma versão anterior, **recrie o banco do zero**:
```sql
DROP DATABASE gerenciador_eventos;
CREATE DATABASE gerenciador_eventos;
```

1. Copie `src/main/resources/application.properties.example` para `application.properties` (não é versionado).
2. Defina as variáveis de ambiente:
   ```bash
   export DB_USERNAME=seu_usuario
   export DB_PASSWORD=sua_senha
   export JWT_SECRET=um_segredo_forte_e_aleatorio
   export CORS_ALLOWED_ORIGINS=http://localhost:3000
   ```
3. Rode a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
4. A API sobe em `http://localhost:8080`. O Flyway aplica as migrações automaticamente (agora `V1` cria a tabela `usuario`, `V2` cria `evento`/`inscricao`/`ticket`/`checkin`).
5. Cadastre o primeiro usuário (`POST /usuario/cadastrar`) — ele vira `ADMIN` automaticamente — e faça login (`POST /login`) pra pegar o token.

## Próximos passos sugeridos

1. ~~Banco de dados + Flyway.~~ ✅
2. ~~Endpoints de detalhamento.~~ ✅
3. ~~Atualização de Evento.~~ ✅
4. ~~Tratamento global de exceções.~~ ✅
5. ~~Autenticação JWT.~~ ✅
6. ~~Dono do evento + permissões (`ADMIN`/`USER`).~~ ✅
7. ~~QR code do ticket + escaneamento para check-in.~~ ✅
8. ~~Testes automatizados do fluxo principal.~~ ✅
9. ~~Documentação da API (Swagger/OpenAPI).~~ ✅
10. ~~Gerenciamento de participantes pelo organizador.~~ ✅
11. ~~Configurar CORS para o frontend.~~ ✅
12. Deploy de teste.
