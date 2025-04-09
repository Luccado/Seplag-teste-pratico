package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Cidade;
import com.seplag.apiseplag.services.CidadeService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CidadeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CidadeService cidadeService;

    @InjectMocks
    private CidadeController cidadeController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cidadeController).build();
    }

    @Test
    public void testListarTodas() throws Exception {
        Cidade cidade1 = new Cidade();
        cidade1.setId(1);
        cidade1.setNome("Maceió");
        cidade1.setUf("AL");

        Cidade cidade2 = new Cidade();
        cidade2.setId(2);
        cidade2.setNome("Arapiraca");
        cidade2.setUf("AL");

        List<Cidade> cidades = Arrays.asList(cidade1, cidade2);
        Page<Cidade> paginaCidades = new PageImpl<>(cidades);

        when(cidadeService.listarTodas(any(Pageable.class))).thenReturn(paginaCidades);

        mockMvc.perform(get("/cidades"))
                .andExpect(status().isOk())
                .andExpect(view().name("cidades/lista"))
                .andExpect(model().attributeExists("cidades"));
    }

    @Test
    public void testListarTodasComFiltro() throws Exception {
        Cidade cidade1 = new Cidade();
        cidade1.setId(1);
        cidade1.setNome("Maceió");
        cidade1.setUf("AL");

        List<Cidade> cidades = Arrays.asList(cidade1);
        Page<Cidade> paginaCidades = new PageImpl<>(cidades);

        when(cidadeService.buscarPorNomePaginado(eq("Maceió"), any(Pageable.class))).thenReturn(paginaCidades);

        mockMvc.perform(get("/cidades")
                        .param("nome", "Maceió"))
                .andExpect(status().isOk())
                .andExpect(view().name("cidades/lista"))
                .andExpect(model().attributeExists("cidades"))
                .andExpect(model().attributeExists("filtroNome"));
    }

    @Test
    public void testBuscarPorId() throws Exception {
        Cidade cidade = new Cidade();
        cidade.setId(1);
        cidade.setNome("Maceió");
        cidade.setUf("AL");

        when(cidadeService.buscarCidadePeloId(1)).thenReturn(cidade);

        mockMvc.perform(get("/cidades/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cidades/detalhes"))
                .andExpect(model().attributeExists("cidade"))
                .andExpect(model().attribute("cidade", cidade));
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() throws Exception {
        when(cidadeService.buscarCidadePeloId(999)).thenThrow(new EmptyResultDataAccessException(1));

        mockMvc.perform(get("/cidades/999"))
                .andExpect(status().isOk())
                .andExpect(view().name("cidades/detalhes"))
                .andExpect(model().attributeExists("erro"))
                .andExpect(model().attribute("erro", "Cidade não encontrada"));
    }

    @Test
    public void testExibirFormulario() throws Exception {
        mockMvc.perform(get("/cidades/nova"))
                .andExpect(status().isOk())
                .andExpect(view().name("cidades/formulario"))
                .andExpect(model().attributeExists("cidade"));
    }

    @Test
    public void testSalvar() throws Exception {
        Cidade cidade = new Cidade();
        cidade.setNome("Maceió");
        cidade.setUf("AL");

        when(cidadeService.salvar(any(Cidade.class))).thenReturn(cidade);

        mockMvc.perform(post("/cidades/salvar")
                        .param("nome", "Maceió")
                        .param("uf", "AL"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cidades"));
    }

    @Test
    public void testExibirFormularioEdicao() throws Exception {
        Cidade cidade = new Cidade();
        cidade.setId(1);
        cidade.setNome("Maceió");
        cidade.setUf("AL");

        when(cidadeService.buscarCidadePeloId(1)).thenReturn(cidade);

        mockMvc.perform(get("/cidades/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cidades/formulario"))
                .andExpect(model().attributeExists("cidade"))
                .andExpect(model().attribute("cidade", cidade));
    }

    @Test
    public void testAtualizar() throws Exception {
        Cidade cidade = new Cidade();
        cidade.setId(1);
        cidade.setNome("Maceió");
        cidade.setUf("AL");

        when(cidadeService.atualizar(eq(1), any(Cidade.class))).thenReturn(cidade);

        mockMvc.perform(post("/cidades/atualizar/1")
                        .param("nome", "Maceió")
                        .param("uf", "AL"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cidades"));
    }

    @Test
    public void testExcluir() throws Exception {
        doNothing().when(cidadeService).excluir(1);

        mockMvc.perform(get("/cidades/excluir/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cidades"));
    }

    @Test
    public void testListarPorUf() throws Exception {
        Cidade cidade1 = new Cidade();
        cidade1.setId(1);
        cidade1.setNome("Maceió");
        cidade1.setUf("AL");

        Cidade cidade2 = new Cidade();
        cidade2.setId(2);
        cidade2.setNome("Arapiraca");
        cidade2.setUf("AL");

        List<Cidade> cidades = Arrays.asList(cidade1, cidade2);

        when(cidadeService.buscarPorUf('A')).thenReturn(cidades);

        mockMvc.perform(get("/cidades/por-uf")
                        .param("uf", "AL"))
                .andExpect(status().isOk())
                .andExpect(view().name("cidades/lista"))
                .andExpect(model().attributeExists("cidades"));
    }

    @Test
    public void testBuscarPorNome() throws Exception {
        Cidade cidade1 = new Cidade();
        cidade1.setId(1);
        cidade1.setNome("Maceió");
        cidade1.setUf("AL");

        List<Cidade> cidades = Arrays.asList(cidade1);

        when(cidadeService.buscarPorNome("Maceió")).thenReturn(cidades);

        mockMvc.perform(get("/cidades/buscar")
                        .param("nome", "Maceió"))
                .andExpect(status().isOk())
                .andExpect(view().name("cidades/lista"))
                .andExpect(model().attributeExists("cidades"));
    }
} 