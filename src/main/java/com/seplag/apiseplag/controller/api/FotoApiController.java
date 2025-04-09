package com.seplag.apiseplag.controller.api;

import com.seplag.apiseplag.services.FotoService;
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
public class FotoApiController {

    private final FotoService fotoService;

    @Autowired
    public FotoApiController(FotoService fotoService) {
        this.fotoService = fotoService;
    }

    @PostMapping("/upload/{pessoaId}")
    public ResponseEntity<String> uploadFoto(@RequestParam("file") MultipartFile file,
                                             @PathVariable Integer pessoaId) {
        try {
            String url = fotoService.uploadFoto(file, pessoaId);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao fazer upload: " + e.getMessage());
        }
    }

    @GetMapping("/{objectName}")
    public ResponseEntity<InputStreamResource> getFoto(@PathVariable String objectName) {
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