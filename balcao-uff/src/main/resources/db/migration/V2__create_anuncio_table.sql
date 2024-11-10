-- V2__create_anuncio_table.sql

CREATE TABLE IF NOT EXISTS tb_anuncio (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(255),
    price DECIMAL,
    contactInfo VARCHAR(255),
    location VARCHAR(255),
    user_id BIGINT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tb_users(id)
);
