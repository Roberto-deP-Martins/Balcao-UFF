-- V9__alter_contact_info_column.sql

ALTER TABLE tb_anuncio
    RENAME COLUMN contactInfo TO contact_info;
