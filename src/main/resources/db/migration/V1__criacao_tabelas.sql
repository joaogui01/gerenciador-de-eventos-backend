-- V1__criacao_tabelas.sql
-- Criação das tabelas do domínio Gerenciador de Eventos

CREATE TABLE evento (
    id_evento BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_evento VARCHAR(100) NOT NULL,
    descricao_evento TEXT,
    data_evento DATE NOT NULL,
    local_evento VARCHAR(200),
    vagas_totais_evento INT NOT NULL,
    vagas_disponiveis_evento INT NOT NULL,
    preco_evento DECIMAL(10,2) NOT NULL,
    status_evento VARCHAR(20) NOT NULL
);

CREATE TABLE participante (
    id_participante BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_participante VARCHAR(150) NOT NULL,
    email_participante VARCHAR(100) NOT NULL,
    cpf_participante VARCHAR(11) NOT NULL,
    telefone_participante VARCHAR(20),
    status_participante VARCHAR(20) NOT NULL,
    CONSTRAINT uk_participante_email UNIQUE (email_participante),
    CONSTRAINT uk_participante_cpf UNIQUE (cpf_participante)
);

CREATE TABLE inscricao (
    id_inscricao BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_evento BIGINT NOT NULL,
    id_participante BIGINT NOT NULL,
    data_inscricao DATE NOT NULL,
    status_inscricao VARCHAR(20) NOT NULL,
    CONSTRAINT fk_inscricao_evento FOREIGN KEY (id_evento) REFERENCES evento (id_evento),
    CONSTRAINT fk_inscricao_participante FOREIGN KEY (id_participante) REFERENCES participante (id_participante)
);

CREATE TABLE ticket (
    id_ticket BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_inscricao BIGINT NOT NULL,
    codigo_hash_ticket VARCHAR(255) NOT NULL,
    status_ticket VARCHAR(20) NOT NULL,
    CONSTRAINT uk_ticket_inscricao UNIQUE (id_inscricao),
    CONSTRAINT uk_ticket_codigo_hash UNIQUE (codigo_hash_ticket),
    CONSTRAINT fk_ticket_inscricao FOREIGN KEY (id_inscricao) REFERENCES inscricao (id_inscricao)
);

CREATE TABLE checkin (
    id_check_in BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_ticket BIGINT NOT NULL,
    data_checkin DATETIME NOT NULL,
    status_checkin VARCHAR(20) NOT NULL,
    CONSTRAINT uk_checkin_ticket UNIQUE (id_ticket),
    CONSTRAINT fk_checkin_ticket FOREIGN KEY (id_ticket) REFERENCES ticket (id_ticket)
);