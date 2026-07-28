ALTER TABLE estoque ADD CONSTRAINT estoque_tipo_unique UNIQUE (id_tipo_equipamento);
INSERT INTO estoque (
    quantidade_disponivel,
    quantidade_em_uso,
    quantidade_defeituosa,
    id_tipo_equipamento
)
SELECT
    COUNT(*) AS quantidade_disponivel,
    0,
    0,
    id_tipo_equipamento
FROM equipamento
WHERE disponivel = TRUE
GROUP BY id_tipo_equipamento
ON CONFLICT (id_tipo_equipamento) DO UPDATE
SET
    quantidade_disponivel = EXCLUDED.quantidade_disponivel,
    quantidade_em_uso = EXCLUDED.quantidade_em_uso,
    quantidade_defeituosa = EXCLUDED.quantidade_defeituosa;