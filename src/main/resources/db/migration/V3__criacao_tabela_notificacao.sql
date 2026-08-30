-- V3__criacao_tabela_notificacao.sql
-- Notificações internas do app (ex: organizador é avisado quando alguém se inscreve
-- no evento dele). Sem envio por e-mail/push por enquanto — o app consulta essa
-- tabela periodicamente (polling) pra saber se tem notificação nova.

CREATE TABLE notificacao (
    id_notificacao BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    mensagem VARCHAR(500) NOT NULL,
    lida BOOLEAN NOT NULL DEFAULT FALSE,
    data_hora DATETIME NOT NULL,
    CONSTRAINT fk_notificacao_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
);
