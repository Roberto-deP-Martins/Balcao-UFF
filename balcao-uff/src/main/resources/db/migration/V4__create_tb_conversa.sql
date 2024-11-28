-- Criar a tabela tb_conversa
CREATE TABLE IF NOT EXISTS tb_conversa (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    anuncio_id BIGINT NOT NULL,
    interessado_id BIGINT NOT NULL,
    anunciante_id BIGINT NOT NULL,
    dt_inicio TIMESTAMP DEFAULT NOW() NOT NULL,
    CONSTRAINT fk_anuncio FOREIGN KEY (anuncio_id) REFERENCES tb_anuncio(id) ON DELETE CASCADE,
    CONSTRAINT fk_interessado FOREIGN KEY (interessado_id) REFERENCES tb_users(id) ON DELETE CASCADE,
    CONSTRAINT fk_anunciante FOREIGN KEY (anunciante_id) REFERENCES tb_users(id) ON DELETE CASCADE,
    CONSTRAINT unique_conversa UNIQUE (anuncio_id, interessado_id, anunciante_id)
);

-- Atualizar tb_message para usar tb_conversa
ALTER TABLE tb_message
ADD COLUMN conversa_id BIGINT NOT NULL,
ADD CONSTRAINT fk_conversa FOREIGN KEY (conversa_id) REFERENCES tb_conversa(id) ON DELETE CASCADE;

-- Remover colunas que agora são redundantes em tb_message
ALTER TABLE tb_message
DROP COLUMN receiver_id,
DROP COLUMN anuncio_id;
