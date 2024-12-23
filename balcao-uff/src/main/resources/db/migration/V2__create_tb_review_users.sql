-- Criar a tabela tb_user_reviews
CREATE TABLE IF NOT EXISTS tb_user_reviews (
                                               id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                               reviewer_id BIGINT NOT NULL,
                                               reviewed_id BIGINT NOT NULL,
                                               rating INT NOT NULL,  -- Avaliação de 1 a 5
                                               comment VARCHAR(500),
    review_date TIMESTAMP DEFAULT NOW() NOT NULL,
    CONSTRAINT fk_reviewer FOREIGN KEY (reviewer_id) REFERENCES tb_users(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviewed FOREIGN KEY (reviewed_id) REFERENCES tb_users(id) ON DELETE CASCADE,
    CONSTRAINT unique_review UNIQUE (reviewer_id, reviewed_id) -- Impede avaliações duplicadas entre o mesmo par
    );

-- Inserir avaliações de exemplo

-- O ADMIN faz uma avaliação do USER
INSERT INTO tb_user_reviews (reviewer_id, reviewed_id, rating, comment)
VALUES
    ((SELECT id FROM tb_users WHERE email = 'jhonatansilva@id.uff.r'), -- reviewer_id (ADMIN)
     (SELECT id FROM tb_users WHERE email = 'rafael@id.uff.r'), -- reviewed_id (USER)
     5, -- Avaliação (nota de 1 a 5)
     'Excelente negociação! O usuário foi muito educado e claro nas negociações.'
    );

-- O USER faz uma avaliação do ADMIN
INSERT INTO tb_user_reviews (reviewer_id, reviewed_id, rating, comment)
VALUES
    ((SELECT id FROM tb_users WHERE email = 'rafael@id.uff.r'), -- reviewer_id (USER)
     (SELECT id FROM tb_users WHERE email = 'jhonatansilva@id.uff.r'), -- reviewed_id (ADMIN)
     4, -- Avaliação (nota de 1 a 5)
     'Muito bom, mas poderia melhorar a clareza nas informações fornecidas.'
    );
