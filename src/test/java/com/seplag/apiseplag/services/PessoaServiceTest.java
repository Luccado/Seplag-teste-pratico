package com.seplag.apiseplag.services;  // Corrigido para o pacote correto (plural)

import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.springframework.data.domain.Sort.Direction.ASC;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaService pessoaService;

    private Pessoa pessoa;

    @BeforeEach
    public void setup() {
        pessoa = new Pessoa();
        pessoa.setId(1);
        pessoa.setNome("João Pereira");
        pessoa.setDataNascimento(LocalDate.of(1988, 3, 25));
        pessoa.setSexo("Masculino");
        pessoa.setMae("Teresa Pereira");
        pessoa.setPai("Antonio Pereira");
    }

    @Test
    public void testSalvarPessoa() {
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        // Corrigido para Salvar com S maiúsculo
        Pessoa resultado = pessoaService.Salvar(new Pessoa());

        assertNotNull(resultado);
        assertEquals("João Pereira", resultado.getNome());
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }

    @Test
    public void testBuscarPorId() {
        when(pessoaRepository.findById(1)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.findById(2)).thenReturn(Optional.empty());

        // Agora tratando o Optional corretamente
        Optional<Pessoa> encontrada = pessoaService.buscarPorId(1);

        assertTrue(encontrada.isPresent());
        assertEquals(1, encontrada.get().getId());
        assertEquals("João Pereira", encontrada.get().getNome());

        // Testando o caso de não encontrar
        Optional<Pessoa> naoEncontrada = pessoaService.buscarPorId(2);
        assertFalse(naoEncontrada.isPresent());
    }


}