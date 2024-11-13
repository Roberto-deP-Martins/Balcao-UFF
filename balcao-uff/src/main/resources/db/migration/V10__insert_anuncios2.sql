-- V9__insert_sample_anuncios.sql

-- Inserindo anúncios de exemplo
INSERT INTO tb_anuncio (title, description, category, price, contact_info, location, user_id)
VALUES 
    ('Anúncio 1', 'Descrição do anúncio 1', 'Tecnologia', 1200.00, 'contato1@exemplo.com', 'Rio de Janeiro', 3),
    ('Anúncio 2', 'Descrição do anúncio 2', 'Móveis', 800.00, 'contato2@exemplo.com', 'São Paulo', 3),
    ('Anúncio 3', 'Descrição do anúncio 3', 'Carros', 30000.00, 'contato3@exemplo.com', 'Belo Horizonte', 4),
    ('Anúncio 4', 'Descrição do anúncio 4', 'Eletrônicos', 1500.00, 'contato4@exemplo.com', 'Salvador', 5);
