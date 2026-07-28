-- TABELA: tipo_equipamento
CREATE TABLE tipo_equipamento (
    id_tipo_equipamento SERIAL PRIMARY KEY,
    nome_tipo VARCHAR(100) NOT NULL UNIQUE
);

-- TABELA: parametro_sistema
CREATE TABLE parametro_sistema (
    id_parametro SERIAL PRIMARY KEY,
    tempo_medio_reposicao INTEGER NOT NULL,
    tempo_medio_consumo_estoque INTEGER NOT NULL,
    tempo_medio_envio INTEGER NOT NULL,
    taxa_media_equipamentos_defeituosos DOUBLE PRECISION NOT NULL,
    estoque_minimo_seguranca INTEGER NOT NULL
);

-- TABELA: equipamento
CREATE TABLE equipamento (
    id_equipamento SERIAL PRIMARY KEY,
    especificacoes TEXT,
    numero_serie VARCHAR(100) NOT NULL UNIQUE,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    data_aquisicao DATE,
    tempo_uso INTEGER,
    disponivel BOOLEAN NOT NULL,
    id_tipo_equipamento INTEGER NOT NULL REFERENCES tipo_equipamento(id_tipo_equipamento)
);

-- TABELA: estoque
CREATE TABLE estoque (
    id_estoque SERIAL PRIMARY KEY,
    quantidade_disponivel INTEGER,
    quantidade_em_uso INTEGER,
    quantidade_defeituosa INTEGER,
    id_tipo_equipamento INTEGER NOT NULL REFERENCES tipo_equipamento(id_tipo_equipamento)
);

-- TABELA: colaborador
CREATE TABLE colaborador (
    id_colaborador SERIAL PRIMARY KEY,
    cpf VARCHAR(20),
    nome VARCHAR(100),
    email_colaborador VARCHAR(100),
    cep VARCHAR(20),
    data_nascimento DATE,
    data_inicio_contrato DATE,
    data_rescisao_contrato DATE,
    especificacoes_equipamentos_necessarios TEXT,
    ativo BOOLEAN DEFAULT TRUE
);

-- TABELA: endereco
CREATE TABLE endereco (
    id BIGINT PRIMARY KEY,
    cep VARCHAR(20),
    logradouro VARCHAR(255),
    numero VARCHAR(20),
    complemento VARCHAR(255),
    unidade VARCHAR(100),
    bairro VARCHAR(100),
    localidade VARCHAR(100),
    uf VARCHAR(10),
    estado VARCHAR(100),
    regiao VARCHAR(100),
    ibge VARCHAR(20),
    gia VARCHAR(20),
    ddd VARCHAR(10),
    siafi VARCHAR(20),
    colaborador_id_fk BIGINT UNIQUE REFERENCES colaborador(id_colaborador)
);

CREATE TABLE equipamento_to_colaborador (
    id_equipamento INTEGER NOT NULL,
    id_colaborador INTEGER NOT NULL,
    data_entrega DATE,
    data_devolucao DATE,
    previsao_entrega DATE,
    PRIMARY KEY (id_equipamento, id_colaborador),
    FOREIGN KEY (id_equipamento) REFERENCES equipamento(id_equipamento),
    FOREIGN KEY (id_colaborador) REFERENCES colaborador(id_colaborador)
);

-- TABELA: alerta
CREATE TABLE alerta (
    id_alerta SERIAL PRIMARY KEY,
    tipo_alerta VARCHAR(100) NOT NULL,
    data_hora_geracao TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    descricao TEXT,
    id_tipo_equipamento INTEGER REFERENCES tipo_equipamento(id_tipo_equipamento)
);

-- TABELA: usuario
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    email_confirmado BOOLEAN,
    token_recuperacao VARCHAR(255),
    token_recuperacao_validade TIMESTAMP,
    token_confirmacao VARCHAR(255)
);
