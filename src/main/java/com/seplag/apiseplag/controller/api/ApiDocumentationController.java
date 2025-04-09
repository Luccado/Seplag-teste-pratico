package com.seplag.apiseplag.controller.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OpenAPIDefinition(
    info = @Info(
        title = "API Seplag",
        version = "1.0",
        description = "API para gerenciamento de pessoas e servidores da Secretaria de Estado da Administração (SEPLAG)"
    ),
    servers = {
        @Server(url = "/", description = "Servidor de Produção"),
        @Server(url = "http://localhost:8080", description = "Servidor Local")
    },
    tags = {
        @Tag(name = "Pessoas", description = "Operações relacionadas a pessoas"),
        @Tag(name = "Servidores", description = "Operações relacionadas a servidores"),
        @Tag(name = "Unidades", description = "Operações relacionadas a unidades"),
        @Tag(name = "Endereços", description = "Operações relacionadas a endereços"),
        @Tag(name = "Cidades", description = "Operações relacionadas a cidades"),
        @Tag(name = "Lotações", description = "Operações relacionadas a lotações"),
        @Tag(name = "Fotos", description = "Operações relacionadas a fotos de pessoas")
    }
)
public class ApiDocumentationController {
    // Este controlador serve apenas para documentação da API
} 