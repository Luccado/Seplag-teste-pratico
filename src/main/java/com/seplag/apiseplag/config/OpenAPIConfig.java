package com.seplag.apiseplag.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Seplag")
                        .description("API para gerenciamento de pessoas e servidores da Seplag")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Seu Nome")
                                .email("seu.email@exemplo.com")));
    }
}