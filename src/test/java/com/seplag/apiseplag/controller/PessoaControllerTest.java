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

    @Test
    public void testBuscarPorId() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);
        pessoa.setNome("Eduardo Costa");
        pessoa.setDataNascimento(LocalDate.of(1980, 1, 1));
        pessoa.setSexo("M");

        when(pessoaService.buscarPorId(1)).thenReturn(Optional.of(pessoa));

        mockMvc.perform(get("/pessoa/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pessoa/detalhes"))
                .andExpect(model().attributeExists("pessoa"))
                .andExpect(model().attribute("pessoa", pessoa));
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() throws Exception {
        when(pessoaService.buscarPorId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/pessoa/999"))
                .andExpect(status().isOk())
                .andExpect(view().name("pessoa/detalhes"))
                .andExpect(model().attributeExists("erro"))
                .andExpect(model().attribute("erro", "Pessoa não encontrada"));
    }

    @Test
    public void testExibirFormulario() throws Exception {
        mockMvc.perform(get("/pessoa/nova"))
                .andExpect(status().isOk())
                .andExpect(view().name("pessoa/formulario"))
                .andExpect(model().attributeExists("pessoa"));
    }

    @Test
    public void testSalvar() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Eduardo Costa");
        pessoa.setDataNascimento(LocalDate.of(1980, 1, 1));
        pessoa.setSexo("M");

        when(pessoaService.salvar(any(Pessoa.class))).thenReturn(pessoa);

        mockMvc.perform(post("/pessoa/salvar")
                        .param("nome", "Eduardo Costa")
                        .param("dataNascimento", "1980-01-01")
                        .param("sexo", "M"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pessoa"));
    }

    @Test
    public void testBuscar() throws Exception {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1);
        pessoa1.setNome("Eduardo Costa");
        pessoa1.setSexo("M");

        List<Pessoa> pessoas = Arrays.asList(pessoa1);
        Page<Pessoa> paginaPessoas = new PageImpl<>(pessoas);

        when(pessoaService.buscarPorNome(anyString(), any(PageRequest.class))).thenReturn(paginaPessoas);

        mockMvc.perform(get("/pessoa/buscar")
                        .param("nome", "Eduardo")
                        .param("sexo", "M")
                        .param("pagina", "0")
                        .param("ordenarPor", "nome")
                        .param("direcao", "asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("pessoa/lista"))
                .andExpect(model().attributeExists("pessoas"));
    }

    @Test
    public void testDeletar() throws Exception {
        doNothing().when(pessoaService).deletar(1);

        mockMvc.perform(get("/pessoa/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pessoa"));
    }

    @Test
    public void testEditar() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);
        pessoa.setNome("Eduardo Costa");
        pessoa.setDataNascimento(LocalDate.of(1980, 1, 1));
        pessoa.setSexo("M");

        when(pessoaService.buscarPorId(1)).thenReturn(Optional.of(pessoa));

        mockMvc.perform(get("/pessoa/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pessoa/formulario"))
                .andExpect(model().attributeExists("pessoa"))
                .andExpect(model().attribute("pessoa", pessoa));
    }
}