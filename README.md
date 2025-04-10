# API SEPLAG

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.x-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.4-blue.svg)](https://www.postgresql.org/)
[![MinIO](https://img.shields.io/badge/MinIO-Object%20Storage-orange.svg)](https://min.io/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-green.svg)](https://swagger.io/)
[![JUnit 5](https://img.shields.io/badge/JUnit%205-Testing-red.svg)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.md)

## 📑 Sumário

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
  - [Justificativa das Escolhas Técnicas](#-justificativa-das-escolhas-técnicas)
- [Executando o Projeto](#-executando-o-projeto)
  - [Pré-requisitos](#-pré-requisitos)
- [Documentação da API](#-documentação-da-api)
- [Endpoints Principais](#-endpoints-principais)
- [Exemplos de Uso](#-exemplos-de-uso)
- [Testes](#-testes)

## 🏢 Sobre o Projeto

A API SEPLAG é um sistema de gerenciamento de recursos humanos desenvolvido para a Secretaria de Estado do Planejamento, Gestão e Patrimônio. O sistema permite o gerenciamento de servidores (efetivos e temporários), suas lotações, unidades administrativas e documentação pessoal, incluindo fotos.

Projeto prático para Processo seletivo Seplag 2025
- Nome do candidato: LUCCA SOUZA DI OLIVEIRA 
- Número de inscrição: 9686 - DESENVOLVEDOR JAVA (BACK-END) - JÚNIOR 
- Número de inscrição: 9695 - DESENVOLVEDOR JAVA (BACK-END) - PLENO 

## ✨ Funcionalidades Principais

- 📋 Gerenciamento de Servidores (Efetivos e Temporários)
- 🏢 Controle de Lotações e Unidades Administrativas
- 📍 Gestão de Endereços Funcionais
- 📸 Upload e Gerenciamento de Fotos de Servidores
- 🔐 Sistema de Autenticação e Autorização

## 🛠️ Tecnologias Utilizadas

- **Spring Boot**: Framework principal para desenvolvimento da API REST
- **PostgreSQL**: Banco de dados relacional para persistência dos dados
- **MinIO**: Sistema de armazenamento de objetos para gerenciamento de arquivos (fotos)
- **Swagger/OpenAPI**: Documentação da API
- **JUnit 5 + Mockito**: Framework de testes

### 💡 Justificativa das Escolhas Técnicas

1. **Spring Boot**: 
   - Facilita o desenvolvimento com sua abordagem "convention over configuration"
   - Amplo suporte a recursos empresariais
   - Excelente integração com outras ferramentas
   - Grande comunidade e documentação robusta

2. **PostgreSQL**:
   - Banco de dados robusto e confiável
   - Suporte nativo a JSON e outros tipos complexos
   - Excelente desempenho em operações transacionais
   - Recursos avançados de indexação e busca

3. **MinIO**:
   - Solução escalável para armazenamento de objetos
   - Compatível com API S3
   - Fácil implantação e manutenção
   - Suporte a alta disponibilidade

## 🚀 Executando o Projeto

### 📋 Pré-requisitos

- Docker
- Docker Compose
- Git

### ⚙️ Configuração do Ambiente

1. Clone o repositório:
```bash
git clone https://github.com/Luccado/Seplag-teste-pratico.git
```

```bash
cd Seplag-teste-pratico
```

2. Crie um arquivo `.env` na raiz do projeto:
```properties
# Banco de dados
POSTGRES_DB=seplag
POSTGRES_USER=seplag
POSTGRES_PASSWORD=seplag123

# MinIO
MINIO_ROOT_USER=ROOTUSER
MINIO_ROOT_PASSWORD=CHANGEME123
MINIO_BUCKET_NAME=fotos-pessoa-seplag

# Java
JAVA_OPTS=-Xmx512m -Xms256m
```



3. Execute o projeto:
```bash
docker-compose up -d
```

4. Aguarde alguns segundos para que todos os serviços iniciem e acesse:
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - MinIO Console: http://localhost:9001 (login com as credenciais do .env)


## 📚 Documentação da API

A documentação completa da API está disponível através do Swagger UI:

- **URL do Swagger**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🔌 Endpoints Principais

### 🔐 Autenticação
- `POST /api/auth/login`: Autenticação de usuário
- `POST /api/auth/refresh`: Renovação do token

### 👥 Servidores
- `GET /api/servidores/efetivos`: Lista servidores efetivos
- `POST /api/servidores/efetivos`: Cadastra novo servidor efetivo
- `GET /api/servidores/temporarios`: Lista servidores temporários
- `POST /api/servidores/temporarios`: Cadastra novo servidor temporário

### 🏢 Unidades
- `GET /api/unidades`: Lista unidades administrativas
- `POST /api/unidades`: Cadastra nova unidade
- `GET /api/unidades/{id}/servidores/efetivos`: Lista servidores efetivos por unidade

### 📸 Fotos
- `GET /api/fotos`: Lista todas as fotos
- `POST /api/fotos/upload`: Upload de foto de servidor

## 💻 Exemplos de Uso

### 📤 Upload de Foto
```bash
curl -X POST http://localhost:8080/api/fotos/upload \
  -H "Content-Type: multipart/form-data" \
  -F "arquivo=@foto.jpg" \
  -F "pessoaId=1"
```

### 📋 Listagem de Servidores por Unidade
```bash
curl http://localhost:8080/api/unidades/1/servidores/efetivos \
  -H "Authorization: Bearer seu_token"
```

## 🧪 Testes

Para executar os testes dentro do container:
```bash
docker-compose exec app ./mvnw test
```


## ⚠️ Aviso para Produção

**IMPORTANTE**: As configurações fornecidas neste README são apenas para desenvolvimento e testes. Em um ambiente de produção:

1. **Altere todas as senhas e credenciais** para valores seguros e únicos
2. **Configure conexões SSL/TLS** para o banco de dados e MinIO
3. **Utilize variáveis de ambiente** em vez de valores hardcoded
4. **Implemente políticas de backup** para o banco de dados e arquivos
5. **Configure firewalls e regras de segurança** adequadas
6. **Utilize um proxy reverso** como Nginx para a API
7. **Configure monitoramento e alertas** para o sistema
8. **Utilize secrets do Docker** para gerenciar credenciais
9. **Configure volumes persistentes** adequadamente
10. **Implemente estratégias de backup e recuperação** 
