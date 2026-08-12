# Gerenciador de Eventos

API REST desenvolvida em **Java + Spring Boot** para gerenciamento de eventos, participantes, inscrições, tickets e check-ins.

> ⚠️ **Projeto em desenvolvimento.** Banco de dados, endpoints de detalhamento/atualização, regras de negócio de vagas/inscrição e tratamento de erros já funcionam, mas ainda faltam pontos importantes — veja a seção [Status do projeto](#status-do-projeto).

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
  - Bean Validation
  - Flyway (migração de banco)
- **MySQL** (via `mysql-connector-j`)
- **Lombok**
- **Maven**

## Estrutura do projeto

```
src/main/java/Projeto/Gerenciador_Eventos/
├── controllers/     # Endpoints REST
├── dto/              # Records de entrada/saída (cadastro, listagem, detalhamento, erro)
├── entity/            # Entidades JPA (Evento, Participante, Inscricao, Ticket, CheckIn)
│   └── enums/          # StatusGeral, StatusCheckIn
├── handler/           # Tratamento global de exceções (TratadorDeErros)
├── repository/       # Interfaces Spring Data JPA
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

- `404 Not Found`: id informado não existe.
- `400 Bad Request`: falha de validação (`@Valid`, retorna lista de `{campo, mensagem}`) ou violação de regra de negócio (retorna a mensagem do erro), como:
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
- [ ] **Segurança/autenticação**: nenhuma dependência ou configuração de segurança foi adicionada até o momento.
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
   ```
   ```powershell
   # Windows (PowerShell)
   $env:DB_USERNAME="seu_usuario"
   $env:DB_PASSWORD="sua_senha"
   ```
4. Rode a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
5. A API sobe por padrão em `http://localhost:8080`. O Flyway aplica a migração `V1__criacao_tabelas.sql` automaticamente na primeira execução.

## Próximos passos sugeridos

1. ~~Configurar o `application.properties` e escrever a primeira migração Flyway.~~ ✅
2. ~~Implementar os endpoints de detalhamento (`GET /.../detalhar/{id}`) para cada recurso.~~ ✅
3. ~~Implementar a atualização de Evento usando `DadosAtualizarEvento`.~~ ✅
4. ~~Adicionar tratamento global de exceções, incluindo retorno de 404 quando um `id` não existir.~~ ✅
5. Escrever testes de integração para os principais fluxos (cadastro de evento → inscrição → ticket → check-in).
