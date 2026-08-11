# Gerenciador de Eventos

API REST desenvolvida em **Java + Spring Boot** para gerenciamento de eventos, participantes, inscrições, tickets e check-ins.

> ⚠️ **Projeto em desenvolvimento.** Várias partes ainda não foram implementadas ou configuradas — veja a seção [Status do projeto](#status-do-projeto) antes de rodar.

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
├── dto/              # Records de entrada/saída (cadastro, listagem, detalhamento)
├── entity/            # Entidades JPA (Evento, Participante, Inscricao, Ticket, CheckIn)
│   └── enums/          # StatusGeral, StatusCheckIn
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
- `DELETE /evento/inativar/{id}`
- `PUT /evento/reativar/{id}`
- `GET /evento/listar`
- `GET /evento/listar/filtro`

### `/participante`
- `POST /participante/cadastrar`
- `DELETE /participante/inativar/{id}`
- `PUT /participante/reativar/{id}`
- `GET /participante/listar`
- `GET /participante/listar/filtro`

### `/inscricao`
- `POST /inscricao/cadastrar`
- `DELETE /inscricao/inativar/{id}`
- `PUT /inscricao/reativar/{id}`
- `GET /inscricao/listar`
- `GET /inscricao/listar/filtro`

### `/ticket`
- `POST /ticket/cadastrar`
- `DELETE /ticket/inativar/{id}`
- `PUT /ticket/reativar/{id}`
- `GET /ticket/listar`
- `GET /ticket/listar/filtro`

### `/checkin`
- `POST /checkin/cadastrar`
- `PUT /checkin/realizarcheckin/{id}`
- `GET /checkin/listar`
- `GET /checkin/listar/filtro`

## Status do projeto

Este projeto **ainda não está pronto para rodar/produção**. Itens pendentes conhecidos:

- [ ] **Configuração do banco de dados**: `application.properties` só define `spring.application.name`; faltam as propriedades de conexão com o MySQL (`spring.datasource.*`).
- [ ] **Migrações Flyway**: a pasta `src/main/resources/db/migration` existe mas está vazia — nenhum script SQL de criação das tabelas foi versionado ainda.
- [ ] **Endpoints de detalhamento (`GET /.../detalhar/{id}`)**: os controllers montam URIs apontando para rotas `/detalhar/{id}` (usadas no header `Location` das respostas de criação), mas esses endpoints ainda não foram implementados.
- [ ] **Atualização de Evento**: existe o DTO `DadosAtualizarEvento` e o método `Evento.atualizarInformações(...)`, mas não há endpoint/serviço que os utilize ainda.
- [ ] **Regras de negócio de vagas**: `vagasDisponiveisEvento` não é decrementado automaticamente ao criar uma inscrição.
- [ ] **Validações cruzadas**: por exemplo, impedir inscrição duplicada do mesmo participante no mesmo evento, ou emissão de ticket para inscrição inativa.
- [ ] **Tratamento de erros**: não há `@ControllerAdvice`/`ExceptionHandler` para respostas de erro padronizadas (ex: entidade não encontrada).
- [ ] **Segurança/autenticação**: nenhuma dependência ou configuração de segurança foi adicionada até o momento.
- [ ] **Testes automatizados**: apenas o teste padrão gerado pelo Spring Initializr (`GerenciadorEventosApplicationTests`) está presente.
- [ ] **Documentação da API** (ex: Swagger/OpenAPI) ainda não configurada.

## Como rodar (quando configurado)

Pré-requisitos: JDK 17, Maven (ou usar o wrapper `./mvnw`) e uma instância MySQL.

1. Adicione ao `application.properties` (ou `application.yml`) as credenciais do banco:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/gerenciador_eventos
   spring.datasource.username=SEU_USUARIO
   spring.datasource.password=SUA_SENHA
   ```
2. Crie os scripts de migração Flyway em `src/main/resources/db/migration` (ex: `V1__criacao_tabelas.sql`) refletindo as entidades acima.
3. Rode a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
4. A API sobe por padrão em `http://localhost:8080`.

## Próximos passos sugeridos

1. Configurar o `application.properties` e escrever a primeira migração Flyway.
2. Implementar os endpoints de detalhamento (`GET /.../detalhar/{id}`) para cada recurso.
3. Implementar a atualização de Evento usando `DadosAtualizarEvento`.
4. Adicionar tratamento global de exceções.
5. Escrever testes de integração para os principais fluxos (cadastro de evento → inscrição → ticket → check-in).
