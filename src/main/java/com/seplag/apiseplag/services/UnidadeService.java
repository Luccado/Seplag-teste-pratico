package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.Unidade;
import com.seplag.apiseplag.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;

    @Autowired
    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    @Transactional
    public Unidade salvar(Unidade unidade) {
        return unidadeRepository.save(unidade);
    }

    public Optional<Unidade> buscarPorId(Integer id) {
        return unidadeRepository.findById(id);
    }

    public Page<Unidade> listarTodas(Pageable pageable) {
        return unidadeRepository.findAll(pageable);
    }

    public Page<Unidade> buscarPorNome(String nome, Pageable pageable) {
        return unidadeRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public Page<Unidade> buscarPorSigla(String sigla, Pageable pageable) {
        return unidadeRepository.findBySiglaContainingIgnoreCase(sigla, pageable);
    }

    @Transactional
    public Unidade atualizar(Integer id, Unidade unidade) {
        if (unidadeRepository.existsById(id)) {
            unidade.setId(id);
            return unidadeRepository.save(unidade);
        } else {
            throw new RuntimeException("Unidade não encontrada com o ID: " + id);
        }
    }

    @Transactional
    public void excluir(Integer id) {
        if (unidadeRepository.existsById(id)) {
            unidadeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Unidade não encontrada com o ID: " + id);
        }
    }
}