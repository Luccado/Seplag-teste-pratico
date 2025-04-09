-- Inserindo cidades
INSERT INTO cidade (cid_id, cid_nome, cid_uf) 
SELECT 1, 'Maceió', 'AL'
WHERE NOT EXISTS (SELECT 1 FROM cidade WHERE cid_id = 1);

-- Inserindo endereços
INSERT INTO endereco (end_id, end_tipo_logradouro, end_logradouro, end_numero, end_bairro, cid_id) 
SELECT 1, 'Rua', 'Doutor Antônio Gomes de Barros', 429, 'Jaraguá', 1
WHERE NOT EXISTS (SELECT 1 FROM endereco WHERE end_id = 1);

-- Inserindo unidades
INSERT INTO unidade (unid_id, unid_nome, unid_sigla) 
SELECT 1, 'Secretaria de Estado da Administração', 'SEPLAG'
WHERE NOT EXISTS (SELECT 1 FROM unidade WHERE unid_id = 1);

-- Inserindo pessoas (incluindo usuários)
INSERT INTO pessoa (pes_id, pes_nome, pes_data_nascimento, pes_sexo, pes_mae, pes_pai, username, password, role, enabled) 
SELECT 1, 'João Silva', '1980-01-15', 'MASCULINO', 'Maria Silva', 'José Silva', 'joao.silva', '$2a$10$X7UrE2J5Q5Q5Q5Q5Q5Q5QO5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q', 'ADMIN', true
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE pes_id = 1);

-- Inserindo servidores efetivos
INSERT INTO servidor_efetivo (pes_id, se_matricula) 
SELECT 1, '12345'
WHERE NOT EXISTS (SELECT 1 FROM servidor_efetivo WHERE pes_id = 1);

-- Inserindo lotações
INSERT INTO lotacao (lot_id, pes_id, unid_id, lot_data_lotacao, lot_data_remocao, lot_portaria) 
SELECT 1, 1, 1, '2020-01-01', NULL, 'Portaria SEPLAG 001/2020'
WHERE NOT EXISTS (SELECT 1 FROM lotacao WHERE lot_id = 1);

-- Inserindo fotos de pessoas
INSERT INTO foto_pessoa (fp_id, pes_id, fp_data, fp_bucket, fp_hash) 
SELECT 1, 1, '2023-01-01', 'fotos-pessoas', 'hash1'
WHERE NOT EXISTS (SELECT 1 FROM foto_pessoa WHERE fp_id = 1);

-- Inserindo relacionamentos entre unidades e endereços
INSERT INTO unidade_endereco (unid_id, end_id) 
SELECT 1, 1
WHERE NOT EXISTS (SELECT 1 FROM unidade_endereco WHERE unid_id = 1 AND end_id = 1);

-- Inserindo relacionamentos entre pessoas e endereços
INSERT INTO pessoa_endereco (id, pes_id, end_id, relation_key) 
SELECT 1, 1, 1, '1-1'
WHERE NOT EXISTS (SELECT 1 FROM pessoa_endereco WHERE id = 1);

-- Inserindo usuários
INSERT INTO usuarios (id, username, password) 
SELECT 1, 'admin', '$2a$10$X7UrE2J5Q5Q5Q5Q5Q5Q5QO5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id = 1);

-- Inserindo roles de usuários
INSERT INTO usuario_roles (usuario_id, role) 
SELECT 1, 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM usuario_roles WHERE usuario_id = 1 AND role = 'ROLE_ADMIN'); 