-- Criar a tabela tb_users
CREATE TABLE IF NOT EXISTS tb_users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    role VARCHAR(255) NOT NULL
);

-- Criar a tabela tb_anuncio
CREATE TABLE IF NOT EXISTS tb_anuncio (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(255),
    price DECIMAL,
    contact_info VARCHAR(255),
    location VARCHAR(255),
    user_id BIGINT,
    dt_criacao TIMESTAMP DEFAULT NOW() NOT NULL,
    CONSTRAINT fk_tb_users FOREIGN KEY (user_id) REFERENCES tb_users(id)
);

-- Criar a tabela tb_conversa
CREATE TABLE IF NOT EXISTS tb_conversa (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    anuncio_id BIGINT NOT NULL,
    interessado_id BIGINT NOT NULL,
    anunciante_id BIGINT NOT NULL,
    dt_inicio TIMESTAMP DEFAULT NOW() NOT NULL,
    data_criacao TIMESTAMP DEFAULT NOW() NOT NULL,
    CONSTRAINT fk_tb_anuncio FOREIGN KEY (anuncio_id) REFERENCES tb_anuncio(id) ON DELETE CASCADE,
    CONSTRAINT fk_tb_interessado FOREIGN KEY (interessado_id) REFERENCES tb_users(id) ON DELETE CASCADE,
    CONSTRAINT fk_tb_anunciante FOREIGN KEY (anunciante_id) REFERENCES tb_users(id) ON DELETE CASCADE,
    CONSTRAINT unique_conversa UNIQUE (anuncio_id, interessado_id, anunciante_id)
);

-- Criar a tabela tb_message
CREATE TABLE IF NOT EXISTS tb_message (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    content TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    sender_id BIGINT NOT NULL,
    conversa_id BIGINT NOT NULL,
    dt_envio TIMESTAMP DEFAULT NOW() NOT NULL,
    CONSTRAINT fk_tb_sender FOREIGN KEY (sender_id) REFERENCES tb_users(id),
    CONSTRAINT fk_tb_conversa FOREIGN KEY (conversa_id) REFERENCES tb_conversa(id) ON DELETE CASCADE
);

-- Criar a tabela tb_anuncio_images
CREATE TABLE IF NOT EXISTS tb_anuncio_images (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    anuncio_id BIGINT NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    CONSTRAINT fk_tb_anuncio_images FOREIGN KEY (anuncio_id) REFERENCES tb_anuncio(id) ON DELETE CASCADE
);

-- Inserir usuários com senhas criptografadas
-- senha: "123"
INSERT INTO tb_users (name, email, password, cpf, role)
VALUES 
('ADMIN', 'jhonatansilva@id.uff.r', '$2a$10$pMQkvFo6Sl8FRQvyGdkzyePn8KZ24vQHyW/aekz1oIDtmxFl9x71O', '15759848708', 'ADMIN');

-- senha: "senhaSegura123"
INSERT INTO tb_users (name, email, password, cpf, role)
VALUES 
('USER', 'rafael@id.uff.r', '$2b$12$ZF5ZCF6T57R3SlYPVjvP9OX1WYTpN.mee.ANVY1Srxnw35MfhyZMS', '15759858708', 'USER');
