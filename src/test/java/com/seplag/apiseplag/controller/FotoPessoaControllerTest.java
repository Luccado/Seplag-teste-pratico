package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.FotoPessoa;
import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.services.FotoPessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FotoPessoaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FotoPessoaService fotoPessoaService;

    @InjectMocks
    private FotoPessoaController fotoPessoaController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(fotoPessoaController).build();
    }

    @Test
    public void testListarTodasFotos() throws Exception {
        FotoPessoa foto1 = new FotoPessoa();
        foto1.setId(1);
        foto1.setPessoa(new Pessoa());
        foto1.getPessoa().setId(1);
        foto1.setBucket("bucket1");
        foto1.setHash("hash1");
        foto1.setData(LocalDate.now());

        FotoPessoa foto2 = new FotoPessoa();
        foto2.setId(2);
        foto2.setPessoa(new Pessoa());
        foto2.getPessoa().setId(2);
        foto2.setBucket("bucket2");
        foto2.setHash("hash2");
        foto2.setData(LocalDate.now());

        List<FotoPessoa> fotos = Arrays.asList(foto1, foto2);

        when(fotoPessoaService.listarTodasFotos()).thenReturn(fotos);

        mockMvc.perform(get("/api/fotos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].pessoa.id").value(1))
                .andExpect(jsonPath("$[0].bucket").value("bucket1"))
                .andExpect(jsonPath("$[0].hash").value("hash1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].pessoa.id").value(2))
                .andExpect(jsonPath("$[1].bucket").value("bucket2"))
                .andExpect(jsonPath("$[1].hash").value("hash2"));
    }

    @Test
    public void testUploadFoto() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo",
                "foto.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "conteudo da foto".getBytes()
        );

        FotoPessoa fotoPessoa = new FotoPessoa();
        fotoPessoa.setId(1);
        fotoPessoa.setPessoa(new Pessoa());
        fotoPessoa.getPessoa().setId(1);
        fotoPessoa.setBucket("fotos-pessoas");
        fotoPessoa.setHash("foto.jpg_12345678");
        fotoPessoa.setData(LocalDate.now());

        when(fotoPessoaService.uploadFoto(any(), eq(1))).thenReturn(fotoPessoa);

        mockMvc.perform(multipart("/api/fotos/upload")
                .file(arquivo)
                .param("pessoaId", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pessoaId").value(1))
                .andExpect(jsonPath("$.hash").value("foto.jpg_12345678"))
                .andExpect(jsonPath("$.mensagem").value("Foto enviada com sucesso"));
    }

    @Test
    public void testUploadFotoArquivoVazio() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo",
                "foto.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        when(fotoPessoaService.uploadFoto(any(), eq(1)))
                .thenThrow(new IllegalArgumentException("O arquivo está vazio"));

        mockMvc.perform(multipart("/api/fotos/upload")
                .file(arquivo)
                .param("pessoaId", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("O arquivo está vazio"));
    }

    @Test
    public void testUploadFotoPessoaNaoEncontrada() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo",
                "foto.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "conteudo da foto".getBytes()
        );

        when(fotoPessoaService.uploadFoto(any(), eq(999)))
                .thenThrow(new IllegalArgumentException("Pessoa não encontrada com o ID: 999"));

        mockMvc.perform(multipart("/api/fotos/upload")
                .file(arquivo)
                .param("pessoaId", "999")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Pessoa não encontrada com o ID: 999"));
    }
} 