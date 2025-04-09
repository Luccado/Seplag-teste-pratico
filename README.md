# API SEPLAG

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.x-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue.svg)](https://www.postgresql.org/)
[![MinIO](https://img.shields.io/badge/MinIO-Object%20Storage-orange.svg)](https://min.io/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-green.svg)](https://swagger.io/)
[![JUnit 5](https://img.shields.io/badge/JUnit%205-Testing-red.svg)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.md)

## 📑 Sumário

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
  - [Justificativa das Escolhas Técnicas](#-justificativa-das-escolhas-técnicas)
- [Configuração do Ambiente](#-configuração-do-ambiente)
  - [Pré-requisitos](#-pré-requisitos)
  - [Configuração do Banco de Dados](#-configuração-do-banco-de-dados)
  - [Configuração do MinIO](#-configuração-do-minio)
  - [Configuração com Docker](#-configuração-com-docker)
- [Executando o Projeto](#-executando-o-projeto)
- [Documentação da API](#-documentação-da-api)
- [Endpoints Principais](#-endpoints-principais)
- [Exemplos de Uso](#-exemplos-de-uso)
- [Testes](#-testes)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)
- [Suporte](#-suporte)

## 🏢 Sobre o Projeto

A API SEPLAG é um sistema de gerenciamento de recursos humanos desenvolvido para a Secretaria de Estado do Planejamento, Gestão e Patrimônio. O sistema permite o gerenciamento de servidores (efetivos e temporários), suas lotações, unidades administrativas e documentação pessoal, incluindo fotos.

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

## ⚙️ Configuração do Ambiente

### 📋 Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+
- PostgreSQL 12+
- MinIO Server
- Docker e Docker Compose (opcional)

### 🗄️ Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL:
```sql
CREATE DATABASE seplag;
```

2. Configure as credenciais no arquivo `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/seplag
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 📦 Configuração do MinIO

1. Configure as credenciais do MinIO no `application.properties`:
```properties
minio.endpoint=http://localhost:9000
minio.accessKey=seu_access_key
minio.secretKey=seu_secret_key
minio.bucketName=fotos-pessoas
```

### 🐳 Configuração com Docker

Para facilitar a configuração do ambiente, você pode usar Docker Compose para executar o PostgreSQL e o MinIO:

1. Crie um arquivo `docker-compose.yml` na raiz do projeto:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:12
    container_name: seplag-postgres
    environment:
      POSTGRES_DB: seplag
      POSTGRES_USER: seplag
      POSTGRES_PASSWORD: seplag123
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - seplag-network

  minio:
    image: minio/minio
    container_name: seplag-minio
    command: server /data --console-address ":9001"
    environment:
      MINIO_ROOT_USER: minioadmin
      MINIO_ROOT_PASSWORD: minioadmin
    ports:
      - "9000:9000"
      - "9001:9001"
    volumes:
      - minio_data:/data
    networks:
      - seplag-network

volumes:
  postgres_data:
  minio_data:

networks:
  seplag-network:
    driver: bridge
```

2. Execute o Docker Compose:

```bash
docker-compose up -d
```

3. Acesse o console do MinIO em `http://localhost:9001` e crie um bucket chamado `fotos-pessoas`.

4. Atualize o `application.properties` para usar as credenciais do Docker:

```properties
# Banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/seplag
spring.datasource.username=seplag
spring.datasource.password=seplag123

# MinIO
minio.endpoint=http://localhost:9000
minio.accessKey=minioadmin
minio.secretKey=minioadmin
minio.bucketName=fotos-pessoas
```

## 🚀 Executando o Projeto

1. Clone o repositório:
```bash
git clone git@github.com:Luccado/Seplag-teste-pratico.git
cd api-seplag
```

2. Compile o projeto:
```bash
mvn clean install
```

3. Execute a aplicação:
```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

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

Execute os testes automatizados com:
```bash
mvn test
```


## ⚠️ Aviso para Produção

**IMPORTANTE**: As configurações de banco de dados e MinIO fornecidas neste README são apenas para desenvolvimento e testes. Em um ambiente de produção:

1. **Altere todas as senhas e credenciais** para valores seguros e únicos
2. **Configure conexões SSL/TLS** para o banco de dados e MinIO
3. **Utilize variáveis de ambiente** em vez de valores hardcoded no `application.properties`
4. **Implemente políticas de backup** para o banco de dados e arquivos
5. **Configure firewalls e regras de segurança** adequadas
6. **Utilize um proxy reverso** como Nginx para a API
7. **Configure monitoramento e alertas** para o sistema

Exemplo de uso de variáveis de ambiente em produção:

```properties
# Banco de dados
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# MinIO
minio.endpoint=${MINIO_ENDPOINT}
minio.accessKey=${MINIO_ACCESS_KEY}
minio.secretKey=${MINIO_SECRET_KEY}
minio.bucketName=${MINIO_BUCKET_NAME}
```



