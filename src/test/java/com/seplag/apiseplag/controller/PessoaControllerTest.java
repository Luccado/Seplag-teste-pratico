package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.services.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PessoaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PessoaService pessoaService;

    @InjectMocks
    private PessoaController pessoaController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(pessoaController).build();
    }

    @Test
    public void testListarTodas() throws Exception {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1);
        pessoa1.setNome("Eduardo Costa");

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(2);
        pessoa2.setNome("Fernanda Lima");

        List<Pessoa> pessoas = Arrays.asList(pessoa1, pessoa2);
        Page<Pessoa> paginaPessoas = new PageImpl<>(pessoas);

        when(pessoaService.buscarTodas(any(PageRequest.class))).thenReturn(paginaPessoas);

        mockMvc.perform(get("/pessoa")
                        .param("pagina", "0")
                        .param("ordenarPor", "nome")
                        .param("direcao", "asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("pessoa/lista"));

    }
}