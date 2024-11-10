-- V3__create_message_table.sql

CREATE TABLE IF NOT EXISTS tb_message (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    content TEXT NOT NULL,
    isRead BOOLEAN NOT NULL,
    sender_id BIGINT,
    receiver_id BIGINT,
    anuncio_id BIGINT,
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES tb_users(id),
    CONSTRAINT fk_receiver FOREIGN KEY (receiver_id) REFERENCES tb_users(id),
    CONSTRAINT fk_anuncio FOREIGN KEY (anuncio_id) REFERENCES tb_anuncio(id)
);
