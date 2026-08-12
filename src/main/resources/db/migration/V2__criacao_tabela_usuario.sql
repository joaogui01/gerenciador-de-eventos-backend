-- V2__criacao_tabela_usuario.sql
-- Tabela de usuários para autenticação da API

CREATE TABLE usuario (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    CONSTRAINT uk_usuario_login UNIQUE (login)
);