# Sistema de Gestão de Eventos 📅

> 🛠️ **Status do Projeto:** Em desenvolvimento (Work in Progress).

Este é o backend de um **Sistema de Gestão de Eventos** completo, desenvolvido para gerenciar inscrições, programações, palestrantes e usuários de forma robusta e escalável. 

O projeto está sendo construído seguindo as melhores práticas de arquitetura de software, com foco em uma API REST eficiente, persistência de dados segura e organização limpa do código.

---

## 🛠️ Tecnologias e Ferramentas

*   **Linguagem Principal:** Java 
*   **Framework:** Spring Boot
*   **Acesso a Dados:** Spring Data JPA
*   **Banco de Dados:** MySQL
*   **Controle de Versão:** Git & GitHub

---

## 📂 Estrutura do Projeto

O backend está organizado com uma estrutura de pacotes limpa e padronizada para o ecossistema Spring:

*   `config/`: Classes de configuração do sistema.
*   `controller/`: Endpoints da API REST que recebem as requisições.
*   `dto/`: Objetos de Transferência de Dados (Data Transfer Objects) para validação e segurança.
*   `entity/`: Entidades de domínio mapeadas diretamente para o banco de dados MySQL.
*   `repository/`: Interfaces que realizam a comunicação e consultas no banco de dados (Spring Data JPA).
*   `service/`: Camada de regras de negócio da aplicação.

---

## 📋 Status de Desenvolvimento (Roadmap)

Aqui está o progresso atual do desenvolvimento das funcionalidades do sistema:

- [x] Configuração inicial do ambiente e estrutura de pastas do Spring Boot
- [x] Modelagem do banco de dados MySQL e definição dos atributos das entidades
- [/] Implementação das regras de negócio e serviços principais (Em progresso)
- [ ] Criação dos Controllers e liberação das rotas da API REST
- [ ] Validação de dados com DTOs e tratamento global de exceções
- [ ] Testes de integração dos endpoints

---

Como o projeto está em fase de desenvolvimento, você precisará das seguintes ferramentas instaladas:
*   Java JDK 
*   Maven
*   MySQL Server
