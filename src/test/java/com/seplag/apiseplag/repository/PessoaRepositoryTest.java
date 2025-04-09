package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.Pessoa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PessoaRepositoryTest {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    public void testSalvarPessoa() {
        // 1. Preparação dos dados
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Ana Silva");
        pessoa.setDataNascimento(LocalDate.of(1990, 5, 15));
        pessoa.setSexo("Feminino");
        pessoa.setMae("Maria Silva");
        pessoa.setPai("José Silva");

        // 2. Execução
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        // 3. Verificação
        assertNotNull(pessoaSalva.getId());
        assertEquals("Ana Silva", pessoaSalva.getNome());
    }

    @Test
    public void testBuscarPorId() {
        // 1. Preparação
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Carlos Santos");
        pessoa.setDataNascimento(LocalDate.of(1985, 8, 20));
        pessoa.setSexo("Masculino");
        pessoa.setMae("Sandra Santos");
        pessoa.setPai("Roberto Santos");

        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        // 2. Execução
        Pessoa pessoaEncontrada = pessoaRepository.findById(pessoaSalva.getId()).orElse(null);

        // 3. Verificação
        assertNotNull(pessoaEncontrada);
        assertEquals("Carlos Santos", pessoaEncontrada.getNome());
    }

    @Test
    public void testListarPaginado() {
        // 1. Preparação
        for (int i = 0; i < 20; i++) {
            Pessoa pessoa = new Pessoa();
            pessoa.setNome("Pessoa " + i);
            pessoa.setDataNascimento(LocalDate.of(1990 + i % 10, 1 + i % 12, 1 + i % 28));
            pessoa.setSexo(i % 2 == 0 ? "Masculino" : "Feminino");
            pessoa.setMae("Mãe da Pessoa " + i);
            pessoa.setPai("Pai da Pessoa " + i);
            pessoaRepository.save(pessoa);
        }

        // 2. Execução
        Page<Pessoa> resultado = pessoaRepository.findAll(
                PageRequest.of(0, 10, Sort.by("nome").ascending())
        );

        // 3. Verificação
        assertEquals(10, resultado.getContent().size());
        assertEquals(20, resultado.getTotalElements());
        assertEquals(2, resultado.getTotalPages());
    }
}