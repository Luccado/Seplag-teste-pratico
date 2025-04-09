package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.Endereco;
import com.seplag.apiseplag.model.Unidade;
import com.seplag.apiseplag.model.UnidadeEndereco;
import com.seplag.apiseplag.repository.EnderecoRepository;
import com.seplag.apiseplag.repository.UnidadeEnderecoRepository;
import com.seplag.apiseplag.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnidadeEnderecoService {

    @Autowired
    private UnidadeEnderecoRepository unidadeEnderecoRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public List<UnidadeEndereco> listarTodos() {
        return unidadeEnderecoRepository.findAll();
    }

    public List<UnidadeEndereco> buscarPorUnidade(Integer unidadeId) {
        return unidadeEnderecoRepository.findByUnidadeId(unidadeId);
    }

    public List<UnidadeEndereco> buscarPorEndereco(Integer enderecoId) {
        return unidadeEnderecoRepository.findByEnderecoId(enderecoId);
    }

    public List<Endereco> listarEnderecosPorUnidade(Integer unidadeId) {
        return buscarPorUnidade(unidadeId).stream()
                .map(UnidadeEndereco::getEndereco)
                .collect(Collectors.toList());
    }

    public List<Unidade> listarUnidadesPorEndereco(Integer enderecoId) {
        return buscarPorEndereco(enderecoId).stream()
                .map(UnidadeEndereco::getUnidade)
                .collect(Collectors.toList());
    }

    @Transactional
    public UnidadeEndereco associarEnderecoAUnidade(Integer unidadeId, Integer enderecoId) {
        Unidade unidade = unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Unidade não encontrada", 1));

        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Endereço não encontrado", 1));

        if (unidadeEnderecoRepository.existsByUnidadeIdAndEnderecoId(unidadeId, enderecoId)) {
            throw new IllegalStateException("Esta unidade já está associada a este endereço");
        }

        UnidadeEndereco unidadeEndereco = new UnidadeEndereco();
        unidadeEndereco.setUnidade(unidade);
        unidadeEndereco.setEndereco(endereco);
        unidadeEndereco.setUnidadeId(unidadeId);

        return unidadeEnderecoRepository.save(unidadeEndereco);
    }

    @Transactional
    public void desassociarEnderecoDeUnidade(Integer unidadeId, Integer enderecoId) {
        if (!unidadeEnderecoRepository.existsByUnidadeIdAndEnderecoId(unidadeId, enderecoId)) {
            throw new EmptyResultDataAccessException("Associação entre unidade e endereço não encontrada", 1);
        }

        unidadeEnderecoRepository.deleteByUnidadeIdAndEnderecoId(unidadeId, enderecoId);
    }
}