-- V1__create_users_table.sql

-- Criar o tipo ENUM para role
CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');

-- Criar a tabela tb_users
CREATE TABLE IF NOT EXISTS tb_users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    role user_role NOT NULL
);
