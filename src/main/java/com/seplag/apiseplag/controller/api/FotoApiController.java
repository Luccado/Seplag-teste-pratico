package com.seplag.apiseplag.controller.api;

import com.seplag.apiseplag.services.FotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Profile("minio")
@RestController
@RequestMapping("/api/fotos")
@Tag(name = "Fotos", description = "API para gerenciamento de fotos de pessoas")
public class FotoApiController {

    private final FotoService fotoService;

    @Autowired
    public FotoApiController(FotoService fotoService) {
        this.fotoService = fotoService;
    }

    @Operation(
        summary = "Upload de foto",
        description = "Faz o upload de uma foto para uma pessoa específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto enviada com sucesso",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/upload/{pessoaId}")
    public ResponseEntity<String> uploadFoto(
            @Parameter(description = "Arquivo da foto", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "ID da pessoa", required = true)
            @PathVariable Integer pessoaId) {
        try {
            String url = fotoService.uploadFoto(file, pessoaId);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao fazer upload: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Recuperar foto",
        description = "Recupera uma foto pelo nome do objeto"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto recuperada com sucesso",
                    content = @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary"))),
        @ApiResponse(responseCode = "404", description = "Foto não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{objectName}")
    public ResponseEntity<InputStreamResource> getFoto(
            @Parameter(description = "Nome do objeto da foto", required = true)
            @PathVariable String objectName) {
        try {
            InputStream is = fotoService.getFoto("fotos/" + objectName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(is));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}