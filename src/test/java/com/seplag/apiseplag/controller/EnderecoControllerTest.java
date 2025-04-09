package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Cidade;
import com.seplag.apiseplag.model.Endereco;
import com.seplag.apiseplag.services.CidadeService;
import com.seplag.apiseplag.services.EnderecoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EnderecoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EnderecoService enderecoService;

    @Mock
    private CidadeService cidadeService;

    @InjectMocks
    private EnderecoController enderecoController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(enderecoController).build();
    }

    @Test
    public void testListarTodos() throws Exception {
        Endereco endereco1 = new Endereco();
        endereco1.setId(1);
        endereco1.setTipo("Residencial");
        endereco1.setLogradouro("Rua Teste 1");
        endereco1.setNumero("123");
        endereco1.setBairro("Centro");

        Endereco endereco2 = new Endereco();
        endereco2.setId(2);
        endereco2.setTipo("Comercial");
        endereco2.setLogradouro("Rua Teste 2");
        endereco2.setNumero("456");
        endereco2.setBairro("Benedito Bentes");

        List<Endereco> enderecos = Arrays.asList(endereco1, endereco2);
        Page<Endereco> paginaEnderecos = new PageImpl<>(enderecos);

        when(enderecoService.listarTodos(any(Pageable.class))).thenReturn(paginaEnderecos);

        mockMvc.perform(get("/enderecos"))
                .andExpect(status().isOk())
                .andExpect(view().name("enderecos/lista"))
                .andExpect(model().attributeExists("enderecos"));
    }

    @Test
    public void testListarTodosComFiltro() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setTipo("Residencial");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Centro");

        List<Endereco> enderecos = Arrays.asList(endereco);
        Page<Endereco> paginaEnderecos = new PageImpl<>(enderecos);

        when(enderecoService.buscarPorLogradouroPaginado(eq("Rua Teste"), any(Pageable.class))).thenReturn(paginaEnderecos);

        mockMvc.perform(get("/enderecos")
                        .param("logradouro", "Rua Teste"))
                .andExpect(status().isOk())
                .andExpect(view().name("enderecos/lista"))
                .andExpect(model().attributeExists("enderecos"))
                .andExpect(model().attributeExists("filtroLogradouro"));
    }

    @Test
    public void testBuscarPorId() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setTipo("Residencial");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Centro");

        when(enderecoService.buscarEnderecoPeloId(1)).thenReturn(endereco);

        mockMvc.perform(get("/enderecos/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("enderecos/detalhes"))
                .andExpect(model().attributeExists("endereco"))
                .andExpect(model().attribute("endereco", endereco));
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() throws Exception {
        when(enderecoService.buscarEnderecoPeloId(999)).thenThrow(new EmptyResultDataAccessException(1));

        mockMvc.perform(get("/enderecos/999"))
                .andExpect(status().isOk())
                .andExpect(view().name("enderecos/detalhes"))
                .andExpect(model().attributeExists("erro"))
                .andExpect(model().attribute("erro", "Endereço não encontrado"));
    }

    @Test
    public void testExibirFormulario() throws Exception {
        Cidade cidade = new Cidade();
        cidade.setId(1);
        cidade.setNome("Maceió");
        cidade.setUf("AL");

        List<Cidade> cidades = Arrays.asList(cidade);
        when(cidadeService.listarTodas()).thenReturn(cidades);

        mockMvc.perform(get("/enderecos/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("enderecos/formulario"))
                .andExpect(model().attributeExists("endereco"))
                .andExpect(model().attributeExists("cidades"));
    }

    @Test
    public void testSalvar() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setTipo("Residencial");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Centro");

        when(enderecoService.salvar(any(Endereco.class))).thenReturn(endereco);

        mockMvc.perform(post("/enderecos/salvar")
                        .param("tipo", "Residencial")
                        .param("logradouro", "Rua Teste")
                        .param("numero", "123")
                        .param("bairro", "Centro")
                        .param("cidade.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/enderecos"));
    }

    @Test
    public void testExibirFormularioEdicao() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setTipo("Residencial");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Centro");

        Cidade cidade = new Cidade();
        cidade.setId(1);
        cidade.setNome("Maceió");
        cidade.setUf("AL");

        List<Cidade> cidades = Arrays.asList(cidade);

        when(enderecoService.buscarEnderecoPeloId(1)).thenReturn(endereco);
        when(cidadeService.listarTodas()).thenReturn(cidades);

        mockMvc.perform(get("/enderecos/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("enderecos/formulario"))
                .andExpect(model().attributeExists("endereco"))
                .andExpect(model().attributeExists("cidades"));
    }

    @Test
    public void testAtualizar() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setTipo("Residencial");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Centro");

        when(enderecoService.atualizar(eq(1), any(Endereco.class))).thenReturn(endereco);

        mockMvc.perform(post("/enderecos/atualizar/1")
                        .param("tipo", "Residencial")
                        .param("logradouro", "Rua Teste")
                        .param("numero", "123")
                        .param("bairro", "Centro")
                        .param("cidade.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/enderecos"));
    }

    @Test
    public void testExcluir() throws Exception {
        doNothing().when(enderecoService).excluir(1);

        mockMvc.perform(get("/enderecos/excluir/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/enderecos"));
    }

    @Test
    public void testBuscarPorLogradouro() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setTipo("Residencial");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Centro");

        List<Endereco> enderecos = Arrays.asList(endereco);

        when(enderecoService.buscarPorLogradouro("Rua Teste")).thenReturn(enderecos);

        mockMvc.perform(get("/enderecos/buscar")
                        .param("logradouro", "Rua Teste"))
                .andExpect(status().isOk())
                .andExpect(view().name("enderecos/lista"))
                .andExpect(model().attributeExists("enderecos"));
    }
} 