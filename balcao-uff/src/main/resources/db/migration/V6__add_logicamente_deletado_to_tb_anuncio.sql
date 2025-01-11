-- V6__add_logicamente_deletado_to_tb_anuncio.sql

-- Altera a coluna logicamente_deletado para ser do tipo TIMESTAMP
ALTER TABLE tb_anuncio
ADD COLUMN dt_delete TIMESTAMP DEFAULT NULL;

-- Atualiza as colunas existentes com um valor de NULL para registros não deletados
UPDATE tb_anuncio SET dt_delete = NULL WHERE dt_delete IS NULL;
