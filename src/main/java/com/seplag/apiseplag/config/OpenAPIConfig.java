package com.seplag.apiseplag.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("API Seplag")
                        .description("API para gerenciamento de pessoas e servidores da Secretaria de Estado da Administração (SEPLAG)")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipe SEPLAG")
                                .email("suporte.seplag@alagoas.al.gov.br")
                                .url("https://www.seplag.al.gov.br"))
                        .license(new License()
                                .name("Licença SEPLAG")
                                .url("https://www.seplag.al.gov.br/licenca")))
                .addTagsItem(new Tag().name("Pessoas").description("Operações relacionadas a pessoas"))
                .addTagsItem(new Tag().name("Servidores").description("Operações relacionadas a servidores"))
                .addTagsItem(new Tag().name("Unidades").description("Operações relacionadas a unidades"))
                .addTagsItem(new Tag().name("Endereços").description("Operações relacionadas a endereços"))
                .addTagsItem(new Tag().name("Cidades").description("Operações relacionadas a cidades"))
                .addTagsItem(new Tag().name("Lotações").description("Operações relacionadas a lotações"))
                .addTagsItem(new Tag().name("Fotos").description("Operações relacionadas a fotos de pessoas"))
                .addServersItem(new Server().url("/").description("Servidor de Produção"))
                .addServersItem(new Server().url("http://localhost:8080").description("Servidor Local"))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}