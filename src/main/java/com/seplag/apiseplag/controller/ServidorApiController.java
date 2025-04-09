package com.seplag.apiseplag.controller.api;

import com.seplag.apiseplag.services.ServidorService;
import com.seplag.apiseplag.dto.ServidorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Adicione este import
import java.util.List;

@RestController
@RequestMapping("/api/servidores")
public class ServidorApiController {

    private final ServidorService servidorService;

    @Autowired
    public ServidorApiController(ServidorService servidorService) {
        this.servidorService = servidorService;
    }

    @GetMapping
    public List<ServidorDTO> listarTodos() {
        return servidorService.listarTodos();
    }

}