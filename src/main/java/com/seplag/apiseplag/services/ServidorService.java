package com.seplag.apiseplag.services;

import com.seplag.apiseplag.dto.EnderecoFuncionalDTO;
import com.seplag.apiseplag.dto.ServidorDTO;
import com.seplag.apiseplag.model.Endereco;
import com.seplag.apiseplag.model.Lotacao;
import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.model.Unidade;
import com.seplag.apiseplag.repository.ServidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServidorService {

    private final ServidorRepository servidorRepository;

    @Autowired
    public ServidorService(ServidorRepository servidorRepository) {
        this.servidorRepository = servidorRepository;
    }

    public List<ServidorDTO> buscarServidoresPorUnidade(Integer unidadeId) {
        List<Pessoa> servidores = servidorRepository.findServidoresEfetivosPorUnidade(unidadeId);

        return servidores.stream()
                .map(this::converterParaServidorDTO)
                .collect(Collectors.toList());
    }

    public List<EnderecoFuncionalDTO> buscarEnderecoFuncionalPorNome(String nomeParcial) {
        List<Pessoa> servidores = servidorRepository.findServidoresPorNomeParcial(nomeParcial);

        return servidores.stream()
                .map(this::converterParaEnderecoFuncionalDTO)
                .collect(Collectors.toList());
    }
    public List<ServidorDTO> listarTodos() {
        // Busca todas as pessoas que são servidores (temporários ou efetivos)
        List<Pessoa> servidores = servidorRepository.findAllServidores();

        // Converte cada pessoa para um ServidorDTO
        return servidores.stream()
                .map(this::converterParaServidorDTO)
                .collect(Collectors.toList());
    }


    private ServidorDTO converterParaServidorDTO(Pessoa pessoa) {
        Lotacao lotacaoAtiva = pessoa.getLotacoes().stream()
                .filter(lotacao -> lotacao.getDataRemocao() == null)
                .findFirst()
                .orElse(null);

        int idade = 0;
        if (pessoa.getDataNascimento() != null) {
            idade = Period.between(pessoa.getDataNascimento(), LocalDate.now()).getYears();
        }

        return ServidorDTO.builder()
                .nome(pessoa.getNome())
                .idade(idade)
                .unidadeLotacao(lotacaoAtiva != null ? lotacaoAtiva.getUnidade().getNome() : "Sem lotação")
                .fotografia("URL_PADRAO_FOTO") // Implementar integração com Min.IO posteriormente
                .build();
    }

    private EnderecoFuncionalDTO converterParaEnderecoFuncionalDTO(Pessoa pessoa) {
        Lotacao lotacaoAtiva = pessoa.getLotacoes().stream()
                .filter(lotacao -> lotacao.getDataRemocao() == null)
                .findFirst()
                .orElse(null);

        if (lotacaoAtiva == null) {
            return EnderecoFuncionalDTO.builder()
                    .nomeServidor(pessoa.getNome())
                    .unidadeLotacao("Sem lotação")
                    .build();
        }

        Unidade unidade = lotacaoAtiva.getUnidade();

        Endereco endereco = unidade.getEnderecos() != null && !unidade.getEnderecos().isEmpty()
                ? unidade.getEnderecos().get(0)
                : null;

        if (endereco == null) {
            return EnderecoFuncionalDTO.builder()
                    .nomeServidor(pessoa.getNome())
                    .unidadeLotacao(unidade.getNome())
                    .build();
        }

        return EnderecoFuncionalDTO.builder()
                .nomeServidor(pessoa.getNome())
                .unidadeLotacao(unidade.getNome())
                .tipoLogradouro(endereco.getTipoLogradouro())
                .logradouro(endereco.getLogradouro())
                .numero(endereco.getNumero())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade() != null ? endereco.getCidade().getNome() : "")
                .estado(endereco.getCidade() != null && endereco.getCidade().getUf() != null ?
                        endereco.getCidade().getUf() : "")
                .build();
    }
}