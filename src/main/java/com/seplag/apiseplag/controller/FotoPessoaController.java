package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.FotoPessoa;
import com.seplag.apiseplag.services.FotoPessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fotos")
@RequiredArgsConstructor
@Tag(name = "Fotos de Pessoas", description = "API para gerenciamento de fotos de pessoas")
public class FotoPessoaController {

    private final FotoPessoaService fotoPessoaService;

    @Operation(
        summary = "Listar todas as fotos",
        description = "Retorna todas as fotos armazenadas no MinIO"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fotos listadas com sucesso",
                    content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @GetMapping
    public ResponseEntity<?> listarTodasFotos() {
        try {
            List<FotoPessoa> fotos = fotoPessoaService.listarTodasFotos();
            return new ResponseEntity<>(fotos, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro ao listar fotos: " + e.getMessage());
            return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
        summary = "Upload de foto de pessoa",
        description = "Faz o upload de uma foto para uma pessoa específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Foto enviada com sucesso",
                    content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                    content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFoto(
            @Parameter(description = "Arquivo de imagem a ser enviado", required = true)
            @RequestParam("arquivo") MultipartFile arquivo,
            @Parameter(description = "ID da pessoa à qual a foto será associada", required = true)
            @RequestParam("pessoaId") Integer pessoaId) {

        try {
            FotoPessoa fotoPessoa = fotoPessoaService.uploadFoto(arquivo, pessoaId);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("id", fotoPessoa.getId());
            resposta.put("pessoaId", fotoPessoa.getPessoa().getId());
            resposta.put("data", fotoPessoa.getData());
            resposta.put("hash", fotoPessoa.getHash());
            resposta.put("mensagem", "Foto enviada com sucesso");

            return new ResponseEntity<>(resposta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro ao processar o upload: " + e.getMessage());
            return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}