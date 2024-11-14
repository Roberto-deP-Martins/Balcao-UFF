-- Criar o tipo ENUM para role (removido, pois alteramos para VARCHAR posteriormente)

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
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tb_users(id)
);

-- Criar a tabela tb_message
CREATE TABLE IF NOT EXISTS tb_message (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    content TEXT NOT NULL,
    is_read BOOLEAN NOT NULL,
    sender_id BIGINT,
    receiver_id BIGINT,
    anuncio_id BIGINT,
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES tb_users(id),
    CONSTRAINT fk_receiver FOREIGN KEY (receiver_id) REFERENCES tb_users(id),
    CONSTRAINT fk_anuncio FOREIGN KEY (anuncio_id) REFERENCES tb_anuncio(id)
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
