# Gerenciador de Eventos

API REST desenvolvida em **Java + Spring Boot** para gerenciamento de eventos, participantes, inscrições, tickets e check-ins.

> ⚠️ **Projeto em desenvolvimento.** Banco de dados, endpoints de detalhamento/atualização, regras de negócio de vagas/inscrição, tratamento de erros e autenticação já funcionam, mas ainda faltam pontos importantes — veja a seção [Status do projeto](#status-do-projeto).

## Sobre o projeto

O sistema permite cadastrar eventos, registrar participantes, vincular participantes a eventos por meio de inscrições, emitir tickets para inscrições confirmadas e registrar o check-in do participante no dia do evento.

Fluxo geral das entidades:

```
Participante ─┐
              ├─> Inscricao ─> Ticket ─> CheckIn
Evento ───────┘
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
- **MySQL** (via `mysql-connector-j`)
- **Lombok**
- **Maven**

## Estrutura do projeto

```
src/main/java/Projeto/Gerenciador_Eventos/
├── controllers/     # Endpoints REST
├── dto/              # Records de entrada/saída (cadastro, listagem, detalhamento, erro, autenticação)
├── entity/            # Entidades JPA (Evento, Participante, Inscricao, Ticket, CheckIn, Usuario)
│   └── enums/          # StatusGeral, StatusCheckIn
├── handler/           # Tratamento global de exceções (TratadorDeErros)
├── repository/       # Interfaces Spring Data JPA
├── security/          # Configuração do Spring Security e filtro de autenticação JWT
└── service/           # Regras de negócio
```

## Domínio / Entidades

| Entidade | Descrição | Status |
|---|---|---|
| **Evento** | Nome, descrição, data, local, vagas totais/disponíveis, preço | `ATIVO` / `INATIVO` |
| **Participante** | Nome, e-mail (único), CPF (único), telefone | `ATIVO` / `INATIVO` |
| **Inscricao** | Vincula um Participante a um Evento, com data de inscrição | `ATIVO` / `INATIVO` |
| **Ticket** | Gerado a partir de uma Inscricao, com código hash único | `ATIVO` / `INATIVO` |
| **CheckIn** | Registro de check-in de um Ticket, com data/hora | `REALIZADO` / `NAO_REALIZADO` |

## Autenticação

A API usa autenticação via **JWT** (JSON Web Token). Todas as rotas exigem um token válido no header `Authorization`, exceto `/login` e `/usuario/cadastrar`.

### `/usuario`
- `POST /usuario/cadastrar` — público. Corpo: `{ "login": "...", "senha": "..." }`. A senha é armazenada com hash (BCrypt), nunca em texto puro.

### `/login`
- `POST /login` — público. Corpo: `{ "login": "...", "senha": "..." }`. Retorna `{ "token": "..." }` se as credenciais forem válidas.

### Usando o token

Em toda requisição para os demais endpoints, envie o token retornado no login no header:
```
Authorization: Bearer <token>
```
O token expira em 2 horas. Sem um token válido, a API retorna `403 Forbidden` antes mesmo de chegar no controller.

## Endpoints implementados

Cada recurso segue um padrão semelhante de CRUD parcial (cadastro, ativar/inativar, listagem e listagem com filtros):

### `/evento`
- `POST /evento/cadastrar`
- `GET /evento/detalhar/{id}`
- `PUT /evento/atualizar`
- `DELETE /evento/inativar/{id}`
- `PUT /evento/reativar/{id}`
- `GET /evento/listar`
- `GET /evento/listar/filtro`

### `/participante`
- `POST /participante/cadastrar`
- `GET /participante/detalhar/{id}`
- `DELETE /participante/inativar/{id}`
- `PUT /participante/reativar/{id}`
- `GET /participante/listar`
- `GET /participante/listar/filtro`

### `/inscricao`
- `POST /inscricao/cadastrar`
- `GET /inscricao/detalhar/{id}`
- `DELETE /inscricao/inativar/{id}`
- `PUT /inscricao/reativar/{id}`
- `GET /inscricao/listar`
- `GET /inscricao/listar/filtro`

### `/ticket`
- `POST /ticket/cadastrar`
- `GET /ticket/detalhar/{id}`
- `DELETE /ticket/inativar/{id}`
- `PUT /ticket/reativar/{id}`
- `GET /ticket/listar`
- `GET /ticket/listar/filtro`

### `/checkin`
- `POST /checkin/cadastrar`
- `GET /checkin/detalhar/{id}`
- `PUT /checkin/realizarcheckin/{id}`
- `GET /checkin/listar`
- `GET /checkin/listar/filtro`

### Respostas de erro

- `403 Forbidden`: requisição sem token JWT válido no header `Authorization` (barrada pelo Spring Security antes de chegar no controller).
- `404 Not Found`: id informado não existe.
- `400 Bad Request`: falha de validação (`@Valid`, retorna lista de `{campo, mensagem}`) ou violação de regra de negócio (retorna a mensagem do erro), como:
  - login ou senha inválidos ao tentar autenticar;
  - login já cadastrado ao tentar criar um novo usuário;
  - criar inscrição sem vagas disponíveis no evento;
  - criar inscrição duplicada (mesmo participante + mesmo evento, ambos ativos);
  - emitir ticket para uma inscrição que não está `ATIVO`.

## Status do projeto

Este projeto **ainda não está pronto para produção**. Progresso até agora:

- [x] **Configuração do banco de dados**: `application.properties` com as propriedades de conexão MySQL configuradas via variáveis de ambiente (`${DB_USERNAME}` / `${DB_PASSWORD}`). Esse arquivo **não é versionado** (está no `.gitignore`) por conter dados de acesso ao banco; existe um `application.properties.example` versionado como referência de quais propriedades preencher.
- [x] **Migrações Flyway**: primeira migração (`V1__criacao_tabelas.sql`) criando as tabelas `evento`, `participante`, `inscricao`, `ticket` e `checkin`, refletindo as entidades JPA.
- [x] **Endpoints de detalhamento (`GET /.../detalhar/{id}`)**: implementados para os 5 recursos (Evento, Participante, Inscricao, Ticket, CheckIn), seguindo o mesmo padrão de `getReferenceById` já usado em `ativar`/`inativar`.
- [x] **Atualização de Evento**: `PUT /evento/atualizar` implementado, usando `DadosAtualizarEvento` e o método `Evento.atualizarInformações(...)`. Atualização é parcial — só sobrescreve os campos enviados (diferentes de `null`) no corpo da requisição.
- [x] **Tratamento de erros**: `TratadorDeErros` (`@RestControllerAdvice`) implementado. `EntityNotFoundException` (id inexistente em `getReferenceById`) agora retorna `404`; `MethodArgumentNotValidException` (falha de `@Valid`) retorna `400` com a lista de campos inválidos; `IllegalStateException` (regras de negócio, ex: vagas esgotadas) retorna `400` com a mensagem do erro.
- [x] **Regras de negócio de vagas**: `vagasDisponiveisEvento` é decrementado automaticamente ao criar uma inscrição, e a criação é bloqueada (`400`) se não houver vagas disponíveis.
- [x] **Validações cruzadas**: bloqueada inscrição duplicada do mesmo participante ativo no mesmo evento, e bloqueada emissão de ticket para inscrição que não esteja `ATIVO`.
- [x] **Segurança/autenticação**: autenticação via JWT implementada com Spring Security (`Usuario implements UserDetails`, `SecurityFilter`, `SecurityConfigurations`). Endpoints `/login` e `/usuario/cadastrar` são públicos; todo o resto exige token válido. Senhas armazenadas com hash BCrypt.
  - ⚠️ Ainda não há sistema de papéis/permissões — todo usuário cadastrado tem o mesmo nível de acesso (`ROLE_USER`), e o cadastro de usuário é público (qualquer um pode criar uma conta). Vale revisar antes de produção.
- [ ] **Testes automatizados**: apenas o teste padrão gerado pelo Spring Initializr (`GerenciadorEventosApplicationTests`) está presente.
- [ ] **Documentação da API** (ex: Swagger/OpenAPI) ainda não configurada.

## Como rodar

Pré-requisitos: JDK 17, Maven (ou usar o wrapper `./mvnw`) e uma instância MySQL.

1. Copie `src/main/resources/application.properties.example` para `src/main/resources/application.properties` (esse último não é versionado).
2. Crie o banco no MySQL, se ainda não existir:
   ```sql
   CREATE DATABASE gerenciador_eventos;
   ```
3. Defina as variáveis de ambiente com suas credenciais antes de subir a aplicação:
   ```bash
   # Linux/macOS
   export DB_USERNAME=seu_usuario
   export DB_PASSWORD=sua_senha
   export JWT_SECRET=um_segredo_forte_e_aleatorio
   ```
   ```powershell
   # Windows (PowerShell)
   $env:DB_USERNAME="seu_usuario"
   $env:DB_PASSWORD="sua_senha"
   $env:JWT_SECRET="um_segredo_forte_e_aleatorio"
   ```
4. Rode a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
5. A API sobe por padrão em `http://localhost:8080`. O Flyway aplica as migrações `V1__criacao_tabelas.sql` e `V2__criacao_tabela_usuario.sql` automaticamente na primeira execução.
6. Crie um usuário (`POST /usuario/cadastrar`) e faça login (`POST /login`) para obter um token — veja a seção [Autenticação](#autenticação).

## Próximos passos sugeridos

1. ~~Configurar o `application.properties` e escrever a primeira migração Flyway.~~ ✅
2. ~~Implementar os endpoints de detalhamento (`GET /.../detalhar/{id}`) para cada recurso.~~ ✅
3. ~~Implementar a atualização de Evento usando `DadosAtualizarEvento`.~~ ✅
4. ~~Adicionar tratamento global de exceções, incluindo retorno de 404 quando um `id` não existir.~~ ✅
5. Escrever testes de integração para os principais fluxos (cadastro de evento → inscrição → ticket → check-in).
