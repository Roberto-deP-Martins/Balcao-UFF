-- V4 -- Criar a tabela tb_transacoes
CREATE TABLE IF NOT EXISTS tb_transacoes (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    anuncio_id BIGINT NOT NULL,
    vendedor_id BIGINT NOT NULL, 
    comprador_id BIGINT NOT NULL,
    data_conclusao TIMESTAMP DEFAULT NOW() NOT NULL,
    vendedor_avaliou BOOLEAN DEFAULT FALSE NOT NULL,
    comprador_avaliou BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT fk_tb_transacao_anuncio FOREIGN KEY (anuncio_id) REFERENCES tb_anuncio(id) ON DELETE CASCADE,
    CONSTRAINT fk_tb_transacao_vendedor FOREIGN KEY (vendedor_id) REFERENCES tb_users(id) ON DELETE CASCADE,
    CONSTRAINT fk_tb_transacao_comprador FOREIGN KEY (comprador_id) REFERENCES tb_users(id) ON DELETE CASCADE,
    CONSTRAINT unique_transacao UNIQUE (anuncio_id, comprador_id) 
);

