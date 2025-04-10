FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar arquivos de dependências do Maven
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd

# Baixar dependências (camada em cache se pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copiar código-fonte
COPY src/ src/

# Empacotar o aplicativo, ignorando os testes
RUN mvn package -DskipTests -Dmaven.test.skip=true

# Imagem de execução
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar apenas o JAR da fase de build
COPY --from=build /app/target/*.jar app.jar

# Expor a porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação com o perfil 'docker'
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"] 