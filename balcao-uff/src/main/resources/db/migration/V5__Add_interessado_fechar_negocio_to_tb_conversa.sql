-- V5__Add_interessado_fechar_negocio_to_tb_conversa.sql

-- Adiciona o campo interessado_fechar_negocio à tabela tb_conversa
ALTER TABLE tb_conversa
ADD COLUMN interessado_fechar_negocio BOOLEAN DEFAULT FALSE NOT NULL;

