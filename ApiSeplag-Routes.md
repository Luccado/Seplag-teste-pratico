# API SEPLAG - Documentação de Rotas

Este documento lista todas as rotas disponíveis no projeto ApiSeplag, organizadas por categoria.

## Índice

1. [Autenticação](#autenticação)
2. [Servidores](#servidores)
3. [Unidades](#unidades)
4. [Lotações](#lotações)
5. [Endereços](#endereços)
6. [Cidades](#cidades)
7. [Pessoas](#pessoas)
8. [Fotos](#fotos)
9. [Relacionamentos](#relacionamentos)

## Autenticação

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/auth/login` | Realiza o login do usuário |
| POST | `/api/auth/refresh` | Atualiza o token de autenticação |

## Servidores

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/servidores` | Lista todos os servidores |
| GET | `/servidores-efetivos` | Lista todos os servidores efetivos (interface) |
| GET | `/servidores-efetivos/{id}` | Exibe detalhes de um servidor efetivo |
| GET | `/servidores-efetivos/novo` | Exibe formulário para criar novo servidor efetivo |
| GET | `/servidores-efetivos/editar/{id}` | Exibe formulário para editar servidor efetivo |
| POST | `/servidores-efetivos/salvar` | Salva um novo servidor efetivo |
| GET | `/servidores-efetivos/excluir/{id}` | Exclui um servidor efetivo |
| GET | `/servidores/temporarios` | Lista todos os servidores temporários (interface) |
| GET | `/servidores/temporarios/{id}` | Exibe detalhes de um servidor temporário |
| GET | `/servidores/temporarios/novo` | Exibe formulário para criar novo servidor temporário |
| GET | `/servidores/temporarios/editar/{id}` | Exibe formulário para editar servidor temporário |
| POST | `/servidores/temporarios/salvar` | Salva um novo servidor temporário |
| GET | `/servidores/temporarios/excluir/{id}` | Exclui um servidor temporário |

## Unidades

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/unidades` | Lista todas as unidades |
| GET | `/unidades/{id}` | Exibe detalhes de uma unidade |
| GET | `/unidades/nova` | Exibe formulário para criar nova unidade |
| GET | `/unidades/editar/{id}` | Exibe formulário para editar unidade |
| POST | `/unidades/salvar` | Salva uma nova unidade |
| GET | `/unidades/excluir/{id}` | Exclui uma unidade |
| POST | `/unidades/atualizar/{id}` | Atualiza uma unidade existente |
| GET | `/api/unidades/endereco-funcional` | Lista unidades com endereço funcional |

## Lotações

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/lotacoes` | Lista todas as lotações |
| GET | `/lotacoes/{id}` | Exibe detalhes de uma lotação |
| GET | `/lotacoes/nova` | Exibe formulário para criar nova lotação |
| POST | `/lotacoes/salvar` | Salva uma nova lotação |
| GET | `/lotacoes/editar/{id}` | Exibe formulário para editar lotação |
| POST | `/lotacoes/atualizar/{id}` | Atualiza uma lotação existente |
| GET | `/lotacoes/excluir/{id}` | Exclui uma lotação |

## Endereços

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/enderecos` | Lista todos os endereços |
| GET | `/enderecos/{id}` | Exibe detalhes de um endereço |
| GET | `/enderecos/novo` | Exibe formulário para criar novo endereço |
| GET | `/enderecos/editar/{id}` | Exibe formulário para editar endereço |
| POST | `/enderecos/salvar` | Salva um novo endereço |
| GET | `/enderecos/excluir/{id}` | Exclui um endereço |
| GET | `/enderecos/buscar` | Busca endereços por critérios |

## Cidades

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/cidades` | Lista todas as cidades |
| GET | `/cidades/{id}` | Exibe detalhes de uma cidade |
| GET | `/cidades/nova` | Exibe formulário para criar nova cidade |
| POST | `/cidades/salvar` | Salva uma nova cidade |
| GET | `/cidades/editar/{id}` | Exibe formulário para editar cidade |
| POST | `/cidades/atualizar/{id}` | Atualiza uma cidade existente |
| GET | `/cidades/excluir/{id}` | Exclui uma cidade |
| GET | `/cidades/por-uf` | Lista cidades por UF |
| GET | `/cidades/buscar` | Busca cidades por nome |

## Pessoas

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/pessoa` | Lista todas as pessoas |
| GET | `/pessoa/{id}` | Exibe detalhes de uma pessoa |
| GET | `/pessoa/nova` | Exibe formulário para criar nova pessoa |
| POST | `/pessoa/salvar` | Salva uma nova pessoa |
| GET | `/pessoa/buscar` | Busca pessoas por critérios |
| GET | `/pessoa/deletar/{id}` | Exclui uma pessoa |
| GET | `/pessoa/editar/{id}` | Exibe formulário para editar pessoa |

## Fotos

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/fotos/upload` | Upload de foto de pessoa |
| POST | `/api/fotos/upload/{pessoaId}` | Upload de foto de pessoa (MinIO) |
| GET | `/api/fotos/{objectName}` | Obtém uma foto pelo nome do objeto |

## Relacionamentos

### Unidade-Endereço

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/unidades-enderecos` | Lista todos os relacionamentos unidade-endereço |
| GET | `/unidades-enderecos/unidade/{unidadeId}` | Lista endereços de uma unidade |
| GET | `/unidades-enderecos/endereco/{enderecoId}` | Lista unidades de um endereço |
| GET | `/unidades-enderecos/enderecos-por-unidade/{unidadeId}` | Lista endereços por unidade |
| GET | `/unidades-enderecos/unidades-por-endereco/{enderecoId}` | Lista unidades por endereço |
| GET | `/unidades-enderecos/associar` | Exibe formulário para associar unidade a endereço |
| POST | `/unidades-enderecos/associar` | Associa um endereço a uma unidade |
| GET | `/unidades-enderecos/desassociar/{unidadeId}/{enderecoId}` | Remove associação entre unidade e endereço |

### Pessoa-Endereço

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/pessoa-endereco` | Lista todos os relacionamentos pessoa-endereço |
| GET | `/pessoa-endereco/pessoa/{pessoaId}` | Lista endereços de uma pessoa |
| GET | `/pessoa-endereco/nova` | Exibe formulário para criar novo relacionamento |
| POST | `/pessoa-endereco/salvar` | Salva um novo relacionamento pessoa-endereço |
| GET | `/pessoa-endereco/remover/{id}` | Remove um relacionamento pessoa-endereço |
| GET | `/pessoa-endereco/remover/pessoa/{pessoaId}/endereco/{enderecoId}` | Remove um relacionamento específico |

---

**Nota:** Todas as rotas estão disponíveis sem autenticação para fins de teste. Em ambiente de produção, recomenda-se reativar a autenticação para garantir a segurança da aplicação. 