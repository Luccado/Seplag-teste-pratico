package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.Endereco;
import com.seplag.apiseplag.repository.EnderecoRepository;
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

class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    private Endereco endereco;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        endereco = new Endereco();
        endereco.setId(1);
        endereco.setLogradouro("Rua Exemplo");
    }

    @Test
    void deveListarTodosPaginado() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "logradouro"));
        List<Endereco> enderecos = Arrays.asList(endereco);
        Page<Endereco> page = new PageImpl<>(enderecos, pageable, enderecos.size());

        when(enderecoRepository.findAll(any(Pageable.class))).thenReturn(page);



        // Assert
        //assertNotNull(resultado);
        //assertEquals(1, resultado.getTotalElements());
        //assertEquals(endereco.getLogradouro(), resultado.getContent().get(0).getLogradouro());
        //verify(enderecoRepository).findAll(any(Pageable.class));
    }

    @Test
    void deveBuscarPorId() {
        // Arrange
        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(endereco));

        // Act
        Optional<Endereco> resultado = enderecoService.buscarPorId(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(endereco.getId(), resultado.get().getId());
        verify(enderecoRepository).findById(1);
    }

    @Test
    void deveSalvarEndereco() {
        // Arrange
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        // Act
        Endereco resultado = enderecoService.salvar(endereco);

        // Assert
        assertNotNull(resultado);
        assertEquals(endereco.getLogradouro(), resultado.getLogradouro());
        verify(enderecoRepository).save(endereco);
    }

    @Test
    void deveExcluirEndereco() {
        // Arrange
        doNothing().when(enderecoRepository).deleteById(anyInt());
        when(enderecoRepository.existsById(anyInt())).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> enderecoService.excluir(1));
        verify(enderecoRepository).deleteById(1);
    }

    @Test
    void deveBuscarPorLogradouro() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "logradouro"));
        List<Endereco> enderecos = Arrays.asList(endereco);
        Page<Endereco> page = new PageImpl<>(enderecos, pageable, enderecos.size());

        when(enderecoRepository.findByLogradouroContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        // Act
        Page<Endereco> resultado = enderecoService.buscarPorLogradouro("Exemplo", 0, "10", "asc");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(endereco.getLogradouro(), resultado.getContent().get(0).getLogradouro());
        verify(enderecoRepository).findByLogradouroContainingIgnoreCase(eq("Exemplo"), any(Pageable.class));
    }


}