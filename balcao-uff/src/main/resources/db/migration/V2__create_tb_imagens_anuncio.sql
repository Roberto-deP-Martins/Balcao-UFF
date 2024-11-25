-- Criar a tabela tb_anuncio_images
CREATE TABLE IF NOT EXISTS tb_anuncio_images (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    anuncio_id BIGINT NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    CONSTRAINT fk_anuncio FOREIGN KEY (anuncio_id) REFERENCES tb_anuncio(id) ON DELETE CASCADE
);
