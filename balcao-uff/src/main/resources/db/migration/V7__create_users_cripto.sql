-- V7__create_users_encrip.sql

-- senha 123
INSERT INTO tb_users (name, email, password, cpf, role)
VALUES ('ADMIN', 'jhonatansilva@id.uff.r', '$2a$10$pMQkvFo6Sl8FRQvyGdkzyePn8KZ24vQHyW/aekz1oIDtmxFl9x71O', '15759848708', 'ADMIN');

--senha senhaSegura123
INSERT INTO tb_users (name, email, password, cpf, role)
VALUES ('USER', 'rafael@id.uff.r', '$2b$12$ZF5ZCF6T57R3SlYPVjvP9OX1WYTpN.mee.ANVY1Srxnw35MfhyZMS', '15759858708', 'ADMIN');

-- Adicionar restrição de unicidade ao campo CPF
ALTER TABLE tb_users
ADD CONSTRAINT uq_cpf UNIQUE (cpf);


