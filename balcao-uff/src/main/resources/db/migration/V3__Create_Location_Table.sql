-- Criar a tabela tb_location
CREATE TABLE IF NOT EXISTS tb_location (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    address VARCHAR(255)
);

-- Adiciona coluna de localização à tabela tb_anuncio
ALTER TABLE tb_anuncio
ADD COLUMN location_id BIGINT,
DROP COLUMN location;

ALTER TABLE tb_anuncio
ADD CONSTRAINT fk_tb_location FOREIGN KEY (location_id) REFERENCES tb_location(id);
