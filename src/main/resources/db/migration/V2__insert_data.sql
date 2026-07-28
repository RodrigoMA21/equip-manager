-- TIPOS DE EQUIPAMENTO
INSERT INTO tipo_equipamento (nome_tipo) VALUES
('Notebook'),
('Monitor'),
('Teclado'),
('Mouse'),
('Headset');

-- PARÂMETROS DO SISTEMA
INSERT INTO parametro_sistema (
    tempo_medio_reposicao,
    tempo_medio_consumo_estoque,
    tempo_medio_envio,
    taxa_media_equipamentos_defeituosos,
    estoque_minimo_seguranca
) VALUES
(7, 30, 3, 0.05, 10),
(10, 60, 5, 0.02, 5),
(15, 45, 7, 0.08, 20),
(5, 15, 2, 0.03, 15),
(20, 90, 10, 0.1, 25);

-- COLABORADORES
INSERT INTO colaborador (cpf, nome, email_colaborador, cep, data_nascimento, data_inicio_contrato, data_rescisao_contrato, especificacoes_equipamentos_necessarios) VALUES
('12345678900', 'Luis', 'joao@empresa.com', '65970-000', '1990-05-10', '2020-01-01', NULL, 'Notebook, Headset'),
('98765432100', 'Lara', 'maria@empresa.com', '65970-001', '1992-07-15', '2019-06-10', NULL, 'Monitor, Mouse'),
('11122233344', 'Rodrigo', 'carlos@empresa.com', '65970-002', '1988-03-22', '2018-11-20', NULL, 'Notebook'),
('55566677788', 'João', 'ana@empresa.com', '65970-003', '1995-09-30', '2021-02-05', NULL, 'Teclado, Mouse'),
('99988877766', 'Paulo', 'paulo@empresa.com', '65970-004', '1985-12-11', '2022-04-01', NULL, 'Monitor');

-- ENDERECOS (IDs iguais aos IDs dos colaboradores)
INSERT INTO endereco (id, cep, logradouro, numero, complemento, unidade, bairro, localidade, uf, estado, regiao, ibge, gia, ddd, siafi, colaborador_id_fk) VALUES
(1, '65600-160', 'Rua Principal', 1467, 'Casa 2', NULL, 'Centro', 'Caxias', 'MA', 'Maranhão', 'Nordeste', '2103001', NULL, '99', '1234', 1),
(2, '50010-000', 'Avenida Guararapes', 3062, 'Apto 101', NULL, 'Santo Antônio', 'Recife', 'PE', 'Pernambuco', 'Nordeste', '2611606', NULL, '81', '1235', 2),
(3, '95010-000', 'Rua Sinimbu', 1576, 'Sala 202', NULL, 'Centro', 'Caxias do Sul', 'RS', 'Rio Grande do Sul', 'Sul', '4305108', NULL, '54', '1236', 3),
(4, '65970-001', 'Rua da Liberdade', 1500, 'Casa 5', NULL, 'Bela Vista', 'Porto Franco', 'MA', 'Maranhão', 'Nordeste', '2109052', NULL, '99', '1237', 4),
(5, '70000-000', 'Rua Nova', 1789, 'Casa', NULL, 'Centro', 'Brasília', 'DF', 'Distrito Federal', 'Centro-Oeste', '5300108', NULL, '61', '1238', 5);

-- EQUIPAMENTOS
INSERT INTO equipamento (especificacoes, numero_serie, marca, modelo, data_aquisicao, tempo_uso, disponivel, id_tipo_equipamento) VALUES
('Notebook com 8GB RAM', 'NB001', 'Dell', 'Inspiron 14', '2022-01-01', 12, false, 1),
('Monitor 24 polegadas', 'MN001', 'LG', 'UltraWide', '2022-02-15', 10, false, 2),
('Teclado Mecânico', 'TK001', 'Logitech', 'G413', '2022-03-10', 8, true, 3),
('Mouse Óptico', 'MS001', 'Razer', 'Viper', '2022-04-05', 6, true, 4),
('Headset Gamer', 'HS001', 'HyperX', 'Cloud II', '2022-05-20', 4, true, 5),
('Notebook Gamer i7 16GB RAM', 'NB002', 'Asus', 'ROG Strix', '2022-06-10', 3, true, 1),
('Monitor 27 polegadas Full HD', 'MN002', 'Samsung', 'Smart Monitor', '2022-03-15', 8, true, 2),
('Teclado Mecânico RGB', 'TK003', 'HyperX', 'Alloy FPS', '2022-05-20', 5, true, 3),
('Mouse Gamer Wireless', 'MS002', 'Logitech', 'G Pro', '2022-04-01', 7, true, 4),
('Headset com Cancelamento de Ruído', 'HS002', 'Bose', 'QuietComfort 35', '2022-02-28', 9, true, 5),
('Notebook Ultrafino', 'NB003', 'Apple', 'MacBook Air', '2021-12-01', 12, true, 1),
('Monitor 24 polegadas IPS', 'MN003', 'Dell', 'P2421', '2021-11-15', 13, true, 2),
('Teclado Sem Fio', 'TK004', 'Microsoft', 'Wireless Keyboard', '2022-01-10', 10, true, 3),
('Mouse Óptico Clássico', 'MS003', 'Microsoft', 'Basic Optical', '2022-07-01', 2, true, 4),
('Headset Estéreo', 'HS003', 'Sony', 'MDR-ZX110', '2021-10-20', 14, true, 5),
('Notebook 2 em 1', 'NB004', 'Lenovo', 'Yoga 7i', '2022-03-10', 8, true, 1),
('Monitor Curvo 34 polegadas', 'MN004', 'LG', 'UltraWide', '2022-04-05', 6, true, 2),
('Teclado Gamer Mecânico', 'TK005', 'Razer', 'BlackWidow', '2022-06-20', 3, true, 3),
('Mouse Ergonômico', 'MS004', 'Logitech', 'MX Vertical', '2021-12-25', 11, true, 4),
('Headset Bluetooth', 'HS004', 'JBL', 'Quantum 800', '2022-05-15', 5, true, 5);


-- EQUIPAMENTO_TO_COLABORADOR (2 registros apenas)
INSERT INTO equipamento_to_colaborador (id_equipamento, id_colaborador, data_entrega, data_devolucao, previsao_entrega) VALUES
(1, 1, '2023-01-15', NULL, '2023-01-20'),
(2, 2, '2023-02-01', '2024-01-01', NULL);

