-- V6__alter_role_to_varchar.sql

ALTER TABLE tb_users
ALTER COLUMN role TYPE VARCHAR(255);
