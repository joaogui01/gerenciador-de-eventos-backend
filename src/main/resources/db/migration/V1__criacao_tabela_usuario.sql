-- V1__criacao_tabela_usuario.sql
-- Tabela de usuários. Todo usuário pode ser organizador de eventos e/ou participante
-- (se auto-inscrever em eventos de outras pessoas). O campo "perfil" (ADMIN/USER)
-- controla quem tem acesso total ao sistema.

CREATE TABLE usuario (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    login VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    telefone VARCHAR(20),
    perfil VARCHAR(20) NOT NULL,
    CONSTRAINT uk_usuario_login UNIQUE (login),
    CONSTRAINT uk_usuario_cpf UNIQUE (cpf)
);
