-- V4__preco_evento_opcional.sql
-- O projeto não envolve transação entre usuários (decisão de produto), então o
-- preço do evento nunca é preenchido pelo frontend. Antes disso quebrava a criação
-- de evento (coluna era NOT NULL). Mantém a coluna (não removi, só relaxei a
-- obrigatoriedade) — se um dia quiserem reativar preço, o dado já está lá.

ALTER TABLE evento MODIFY COLUMN preco_evento DECIMAL(10,2) NULL;
