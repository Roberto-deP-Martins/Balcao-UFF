-- Adicionar a coluna dt_criacao na tabela tb_anuncio
ALTER TABLE tb_anuncio
ADD COLUMN dt_criacao TIMESTAMP;

-- Atualizar a tabela tb_anuncio para definir a data de criação
UPDATE tb_anuncio
SET dt_criacao = NOW()
WHERE dt_criacao IS NULL;
