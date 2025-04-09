package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.Cidade;
import com.seplag.apiseplag.model.Endereco;
import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.model.PessoaEndereco;
import com.seplag.apiseplag.repository.EnderecoRepository;
import com.seplag.apiseplag.repository.PessoaEnderecoRepository;
import com.seplag.apiseplag.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PessoaEnderecoServiceTest {

    @Mock
    private PessoaEnderecoRepository pessoaEnderecoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private PessoaEnderecoService pessoaEnderecoService;

    private Pessoa pessoa;
    private Endereco endereco;
    private PessoaEndereco pessoaEndereco;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pessoa = new Pessoa();
        pessoa.setId(1);
        pessoa.setNome("João Silva");

        endereco = new Endereco();
        endereco.setId(1);
        endereco.setLogradouro("Rua Exemplo");
        endereco.setNumero(123);
        endereco.setBairro("Centro");


        pessoaEndereco = new PessoaEndereco();
        pessoaEndereco.setId(1);
        pessoaEndereco.setPessoa(pessoa);
        pessoaEndereco.setEndereco(endereco);
    }

    @Test
    void deveListarPaginado() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        List<PessoaEndereco> pessoaEnderecos = Arrays.asList(pessoaEndereco);
        Page<PessoaEndereco> page = new PageImpl<>(pessoaEnderecos, pageable, pessoaEnderecos.size());

        when(pessoaEnderecoRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        Page<PessoaEndereco> resultado = pessoaEnderecoService.listarPaginado(0, 10, "id", "asc");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(pessoaEndereco.getId(), resultado.getContent().get(0).getId());
        verify(pessoaEnderecoRepository).findAll(any(Pageable.class));
    }

    @Test
    void deveBuscarPorId() {
        // Arrange
        when(pessoaEnderecoRepository.findById(anyInt())).thenReturn(Optional.of(pessoaEndereco));

        // Act
        Optional<PessoaEndereco> resultado = pessoaEnderecoService.buscarPorId(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(pessoaEndereco.getId(), resultado.get().getId());
        verify(pessoaEnderecoRepository).findById(1);
    }

    @Test
    void deveBuscarPorPessoaIdPaginado() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        List<PessoaEndereco> pessoaEnderecos = Arrays.asList(pessoaEndereco);
        Page<PessoaEndereco> page = new PageImpl<>(pessoaEnderecos, pageable, pessoaEnderecos.size());

        when(pessoaEnderecoRepository.findByPessoaId(anyInt(), any(Pageable.class))).thenReturn(page);

        // Act
        Page<PessoaEndereco> resultado = pessoaEnderecoService.buscarPorPessoaIdPaginado(1, 0, "id", "asc");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(pessoaEndereco.getId(), resultado.getContent().get(0).getId());
        verify(pessoaEnderecoRepository).findByPessoaId(eq(1), any(Pageable.class));
    }

    @Test
    void deveAssociarPessoaEndereco() {
        // Arrange
        String relationKey = "1-1";

        when(pessoaEnderecoRepository.findByRelationKey(anyString())).thenReturn(Optional.empty());
        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));
        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(endereco));
        when(pessoaEnderecoRepository.save(any(PessoaEndereco.class))).thenReturn(pessoaEndereco);

        // Act
        PessoaEndereco resultado = pessoaEnderecoService.associarPessoaEndereco(1, 1);

        // Assert
        assertNotNull(resultado);
        assertEquals(pessoa, resultado.getPessoa());
        assertEquals(endereco, resultado.getEndereco());
        verify(pessoaRepository).findById(1);
        verify(enderecoRepository).findById(1);
        verify(pessoaEnderecoRepository).save(any(PessoaEndereco.class));
    }

    @Test
    void deveRetornarAssociacaoExistente() {
        // Arrange
        String relationKey = "1-1";

        when(pessoaEnderecoRepository.findByRelationKey(anyString())).thenReturn(Optional.of(pessoaEndereco));

        // Act
        PessoaEndereco resultado = pessoaEnderecoService.associarPessoaEndereco(1, 1);

        // Assert
        assertNotNull(resultado);
        assertEquals(pessoaEndereco, resultado);
        verify(pessoaEnderecoRepository).findByRelationKey(anyString());
        verify(pessoaRepository, never()).findById(anyInt());
        verify(enderecoRepository, never()).findById(anyInt());
    }

    @Test
    void deveRemoverAssociacao() {
        // Arrange
        doNothing().when(pessoaEnderecoRepository).deleteById(anyInt());

        // Act & Assert
        assertDoesNotThrow(() -> pessoaEnderecoService.removerAssociacao(1));
        verify(pessoaEnderecoRepository).deleteById(1);
    }

    @Test
    void deveRemoverAssociacaoPorPessoaEEndereco() {
        // Arrange
        String relationKey = "1-1";

        when(pessoaEnderecoRepository.findByRelationKey(anyString())).thenReturn(Optional.of(pessoaEndereco));
        doNothing().when(pessoaEnderecoRepository).deleteById(anyInt());

        // Act
        pessoaEnderecoService.removerAssociacaoPorPessoaEEndereco(1, 1);

        // Assert
        verify(pessoaEnderecoRepository).findByRelationKey(anyString());
        verify(pessoaEnderecoRepository).deleteById(1);
    }
}