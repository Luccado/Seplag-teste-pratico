package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.Lotacao;
import com.seplag.apiseplag.repository.LotacaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LotacaoService {

    @Autowired
    private LotacaoRepository lotacaoRepository;

    public Lotacao salvar(Lotacao lotacao) {
        return lotacaoRepository.save(lotacao);
    }

    public Lotacao atualizar(Integer id, Lotacao lotacao) {
        Lotacao lotacaoSalvar = buscarLotacaoPeloId(id);
        BeanUtils.copyProperties(lotacao, lotacaoSalvar, "id");
        return lotacaoRepository.save(lotacaoSalvar);
    }

    public Lotacao buscarLotacaoPeloId(Integer id) {
        return lotacaoRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public Page<Lotacao> listarTodas(Pageable pageable) {
        return lotacaoRepository.findAll(pageable);
    }

    public void excluir(Integer id) {
        lotacaoRepository.deleteById(id);
    }
}