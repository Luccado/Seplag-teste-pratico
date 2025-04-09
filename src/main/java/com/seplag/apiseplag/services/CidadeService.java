package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.Cidade;
import com.seplag.apiseplag.repository.CidadeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    public Cidade salvar(Cidade cidade) {
        return cidadeRepository.save(cidade);
    }

    public Cidade atualizar(Integer id, Cidade cidade) {
        Cidade cidadeSalvar = buscarCidadePeloId(id);
        BeanUtils.copyProperties(cidade, cidadeSalvar, "id");
        return cidadeRepository.save(cidadeSalvar);
    }

    public Cidade buscarCidadePeloId(Integer id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public List<Cidade> buscarPorNome(String nome) {
        return cidadeRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Cidade> buscarPorUf(Character uf) {
        return cidadeRepository.findByUf(uf);
    }

    public Page<Cidade> listarTodas(Pageable pageable) {
        return cidadeRepository.findAll(pageable);
    }

    public Page<Cidade> buscarPorNomePaginado(String nome, Pageable pageable) {
        return cidadeRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public void excluir(Integer id) {
        cidadeRepository.deleteById(id);
    }
}