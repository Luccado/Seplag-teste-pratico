package com.seplag.apiseplag.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PessoaTest {

    @Test
    public void testCriacaoPessoa() {
        // 1. Preparação dos dados
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João Silva");
        pessoa.setSexo("Masculino");
        pessoa.setMae("Maria da Silva");

        // 2. Execução do teste
        String nome = pessoa.getNome();
        String sexo = pessoa.getSexo();
        String mae = pessoa.getMae();

        // 3. Verificação dos resultados
        assertEquals("João Silva", nome, "O nome deve ser igual ao que foi definido");
        assertEquals("Masculino", sexo, "O sexo deve ser igual ao que foi definido");
        assertEquals("Maria da Silva", mae, "O nome da mãe deve ser igual ao que foi definido");
    }

    @Test
    public void testEqualsEHashCode() {
        // 1. Preparação dos dados
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1); // Corrigido para Integer
        pessoa1.setNome("Maria Santos");

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(1); // Corrigido para Integer
        pessoa2.setNome("Maria Santos");

        Pessoa pessoa3 = new Pessoa();
        pessoa3.setId(2); // Corrigido para Integer
        pessoa3.setNome("Maria Santos");

        // 2. Execução e verificação
        assertEquals(pessoa1, pessoa2, "Pessoas com mesmo ID devem ser iguais");
        assertNotEquals(pessoa1, pessoa3, "Pessoas com IDs diferentes não devem ser iguais");
        assertEquals(pessoa1.hashCode(), pessoa2.hashCode(), "Hash code deve ser igual para pessoas com mesmo ID");
    }

    @Test
    public void testRelacionamentoPessoaEndereco() {
        // 1. Preparação dos dados
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1); // Modificado para String
        pessoa.setNome("Carlos Oliveira");

        PessoaEndereco pessoaEndereco = new PessoaEndereco();
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua das Flores");
        endereco.setNumero(123); // Alterado para Integer

        pessoaEndereco.setPessoa(pessoa);
        pessoaEndereco.setEndereco(endereco);

        // 2. Execução e verificação
        assertEquals(pessoa, pessoaEndereco.getPessoa(), "A pessoa no relacionamento deve ser a mesma");
        assertEquals(endereco, pessoaEndereco.getEndereco(), "O endereço no relacionamento deve ser o mesmo");
    }

    // O teste relacionado à FotoPessoa foi removido conforme solicitado
}