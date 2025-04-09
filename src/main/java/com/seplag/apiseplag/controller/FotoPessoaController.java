package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.FotoPessoa;
import com.seplag.apiseplag.services.FotoPessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/fotos")
@RequiredArgsConstructor
public class FotoPessoaController {

    private final FotoPessoaService fotoPessoaService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFoto(
            @RequestParam("arquivo") MultipartFile arquivo,
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