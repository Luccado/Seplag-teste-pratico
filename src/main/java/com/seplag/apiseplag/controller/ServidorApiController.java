package com.seplag.apiseplag.controller.api;

import com.seplag.apiseplag.services.ServidorService;
import com.seplag.apiseplag.dto.ServidorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Adicione este import
import java.util.List;

@RestController
@RequestMapping("/api/servidores")
@Tag(name = "Servidores", description = "API para gerenciamento de servidores")
public class ServidorApiController {

    private final ServidorService servidorService;

    @Autowired
    public ServidorApiController(ServidorService servidorService) {
        this.servidorService = servidorService;
    }

    @Operation(
        summary = "Listar todos os servidores",
        description = "Retorna uma lista de todos os servidores cadastrados no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de servidores recuperada com sucesso",
                    content = @Content(schema = @Schema(implementation = ServidorDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public List<ServidorDTO> listarTodos() {
        return servidorService.listarTodos();
    }

}