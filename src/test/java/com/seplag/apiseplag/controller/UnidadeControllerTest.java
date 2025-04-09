package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Unidade;
import com.seplag.apiseplag.services.UnidadeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UnidadeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UnidadeService unidadeService;

    @InjectMocks
    private UnidadeController unidadeController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(unidadeController).build();
    }

    @Test
    public void testListarTodas() throws Exception {
        Unidade unidade1 = new Unidade();
        unidade1.setId(1);
        unidade1.setNome("SEPLAG");
        unidade1.setSigla("SEPLAG");

        Unidade unidade2 = new Unidade();
        unidade2.setId(2);
        unidade2.setNome("SEFAZ");
        unidade2.setSigla("SEFAZ");

        List<Unidade> unidades = Arrays.asList(unidade1, unidade2);
        Page<Unidade> paginaUnidades = new PageImpl<>(unidades);

        when(unidadeService.listarTodas(any(Pageable.class))).thenReturn(paginaUnidades);

        mockMvc.perform(get("/unidades"))
                .andExpect(status().isOk())
                .andExpect(view().name("unidades/lista"))
                .andExpect(model().attributeExists("unidades"));
    }

    @Test
    public void testListarTodasComFiltro() throws Exception {
        Unidade unidade1 = new Unidade();
        unidade1.setId(1);
        unidade1.setNome("SEPLAG");
        unidade1.setSigla("SEPLAG");

        List<Unidade> unidades = Arrays.asList(unidade1);
        Page<Unidade> paginaUnidades = new PageImpl<>(unidades);

        when(unidadeService.buscarPorNomePaginado(eq("SEPLAG"), any(Pageable.class))).thenReturn(paginaUnidades);

        mockMvc.perform(get("/unidades")
                        .param("nome", "SEPLAG"))
                .andExpect(status().isOk())
                .andExpect(view().name("unidades/lista"))
                .andExpect(model().attributeExists("unidades"))
                .andExpect(model().attributeExists("filtroNome"));
    }

    @Test
    public void testBuscarPorId() throws Exception {
        Unidade unidade = new Unidade();
        unidade.setId(1);
        unidade.setNome("SEPLAG");
        unidade.setSigla("SEPLAG");

        when(unidadeService.buscarPorId(1)).thenReturn(Optional.of(unidade));

        mockMvc.perform(get("/unidades/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("unidades/detalhes"))
                .andExpect(model().attributeExists("unidade"))
                .andExpect(model().attribute("unidade", unidade));
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() throws Exception {
        when(unidadeService.buscarPorId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/unidades/999"))
                .andExpect(status().isOk())
                .andExpect(view().name("unidades/detalhes"))
                .andExpect(model().attributeExists("erro"))
                .andExpect(model().attribute("erro", "Unidade não encontrada"));
    }

    @Test
    public void testExibirFormulario() throws Exception {
        mockMvc.perform(get("/unidades/nova"))
                .andExpect(status().isOk())
                .andExpect(view().name("unidades/formulario"))
                .andExpect(model().attributeExists("unidade"));
    }

    @Test
    public void testSalvar() throws Exception {
        Unidade unidade = new Unidade();
        unidade.setNome("SEPLAG");
        unidade.setSigla("SEPLAG");

        when(unidadeService.salvar(any(Unidade.class))).thenReturn(unidade);

        mockMvc.perform(post("/unidades/salvar")
                        .param("nome", "SEPLAG")
                        .param("sigla", "SEPLAG"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/unidades"));
    }

    @Test
    public void testExibirFormularioEdicao() throws Exception {
        Unidade unidade = new Unidade();
        unidade.setId(1);
        unidade.setNome("SEPLAG");
        unidade.setSigla("SEPLAG");

        when(unidadeService.buscarPorId(1)).thenReturn(Optional.of(unidade));

        mockMvc.perform(get("/unidades/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("unidades/formulario"))
                .andExpect(model().attributeExists("unidade"))
                .andExpect(model().attribute("unidade", unidade));
    }

    @Test
    public void testAtualizar() throws Exception {
        Unidade unidade = new Unidade();
        unidade.setId(1);
        unidade.setNome("SEPLAG");
        unidade.setSigla("SEPLAG");

        when(unidadeService.atualizar(eq(1), any(Unidade.class))).thenReturn(unidade);

        mockMvc.perform(post("/unidades/atualizar/1")
                        .param("nome", "SEPLAG")
                        .param("sigla", "SEPLAG"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/unidades"));
    }

    @Test
    public void testExcluir() throws Exception {
        doNothing().when(unidadeService).excluir(1);

        mockMvc.perform(get("/unidades/excluir/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/unidades"));
    }

    @Test
    public void testBuscarPorNome() throws Exception {
        Unidade unidade1 = new Unidade();
        unidade1.setId(1);
        unidade1.setNome("SEPLAG");
        unidade1.setSigla("SEPLAG");

        List<Unidade> unidades = Arrays.asList(unidade1);

        when(unidadeService.buscarPorNome("SEPLAG")).thenReturn(unidades);

        mockMvc.perform(get("/unidades/buscar")
                        .param("nome", "SEPLAG"))
                .andExpect(status().isOk())
                .andExpect(view().name("unidades/lista"))
                .andExpect(model().attributeExists("unidades"));
    }
} 