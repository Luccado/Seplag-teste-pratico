-- Tabela de cidades
CREATE TABLE IF NOT EXISTS cidade (
    cid_id SERIAL PRIMARY KEY,
    cid_nome VARCHAR(200) NOT NULL,
    cid_uf CHAR(2) NOT NULL
);

-- Tabela de endereços
CREATE TABLE IF NOT EXISTS endereco (
    end_id SERIAL PRIMARY KEY,
    end_tipo_logradouro VARCHAR(50) NOT NULL,
    end_logradouro VARCHAR(200) NOT NULL,
    end_numero INTEGER NOT NULL,
    end_bairro VARCHAR(100) NOT NULL,
    cid_id INTEGER,
    CONSTRAINT fk_cidade_endereco FOREIGN KEY (cid_id) REFERENCES cidade (cid_id)
);

-- Tabela de unidades
CREATE TABLE IF NOT EXISTS unidade (
    unid_id SERIAL PRIMARY KEY,
    unid_nome VARCHAR(200) NOT NULL,
    unid_sigla VARCHAR(20)
);

-- Tabela de pessoas
CREATE TABLE IF NOT EXISTS pessoa (
    pes_id SERIAL PRIMARY KEY,
    pes_nome VARCHAR(200) NOT NULL,
    pes_data_nascimento DATE NOT NULL,
    pes_sexo VARCHAR(9),
    pes_mae VARCHAR(200),
    pes_pai VARCHAR(200),
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50),
    enabled BOOLEAN DEFAULT TRUE
);

-- Tabela de servidores efetivos
CREATE TABLE IF NOT EXISTS servidor_efetivo (
    pes_id INTEGER PRIMARY KEY,
    se_matricula VARCHAR(20),
    CONSTRAINT fk_pessoa_servidor_efetivo FOREIGN KEY (pes_id) REFERENCES pessoa (pes_id)
);

-- Tabela de servidores temporários
CREATE TABLE IF NOT EXISTS servidor_temporario (
    pes_id INTEGER PRIMARY KEY,
    st_data_admissao DATE,
    st_data_demissao DATE,
    CONSTRAINT fk_pessoa_servidor_temporario FOREIGN KEY (pes_id) REFERENCES pessoa (pes_id)
);

-- Tabela de lotações
CREATE TABLE IF NOT EXISTS lotacao (
    lot_id SERIAL PRIMARY KEY,
    pes_id INTEGER NOT NULL,
    unid_id INTEGER NOT NULL,
    lot_data_lotacao DATE,
    lot_data_remocao DATE,
    lot_portaria VARCHAR(100),
    CONSTRAINT fk_pessoa_lotacao FOREIGN KEY (pes_id) REFERENCES pessoa (pes_id),
    CONSTRAINT fk_unidade_lotacao FOREIGN KEY (unid_id) REFERENCES unidade (unid_id)
);

-- Tabela de fotos de pessoas
CREATE TABLE IF NOT EXISTS foto_pessoa (
    fp_id SERIAL PRIMARY KEY,
    pes_id INTEGER NOT NULL,
    fp_data DATE,
    fp_bucket VARCHAR(50) NOT NULL,
    fp_hash VARCHAR(50) NOT NULL,
    CONSTRAINT fk_pessoa_foto_pessoa FOREIGN KEY (pes_id) REFERENCES pessoa (pes_id)
);

-- Tabela de relacionamento entre unidades e endereços
CREATE TABLE IF NOT EXISTS unidade_endereco (
    unid_id INTEGER,
    end_id INTEGER,
    CONSTRAINT fk_unidade_endereco_unidade FOREIGN KEY (unid_id) REFERENCES unidade (unid_id),
    CONSTRAINT fk_unidade_endereco_endereco FOREIGN KEY (end_id) REFERENCES endereco (end_id),
    PRIMARY KEY (unid_id, end_id)
);

-- Tabela de relacionamento entre pessoas e endereços
CREATE TABLE IF NOT EXISTS pessoa_endereco (
    id SERIAL PRIMARY KEY,
    pes_id INTEGER,
    end_id INTEGER,
    relation_key VARCHAR(255) UNIQUE,
    CONSTRAINT fk_pessoa_endereco_pessoa FOREIGN KEY (pes_id) REFERENCES pessoa (pes_id),
    CONSTRAINT fk_pessoa_endereco_endereco FOREIGN KEY (end_id) REFERENCES endereco (end_id)
);

-- Tabela de usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Tabela de roles de usuários
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id INTEGER,
    role VARCHAR(50),
    CONSTRAINT fk_usuario_roles_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    PRIMARY KEY (usuario_id, role)
); 