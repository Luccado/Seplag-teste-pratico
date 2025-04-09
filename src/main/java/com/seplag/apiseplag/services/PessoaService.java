package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    @Autowired
    public PessoaService(PessoaRepository pessoaRepository){
        this.pessoaRepository = pessoaRepository;
    }

    public Pessoa Salvar(Pessoa pessoa){
        return pessoaRepository.save(pessoa);
    }

    public Optional<Pessoa> buscarPorId(Integer id){
        return pessoaRepository.findById(id);
    }

    public Page<Pessoa> buscarTodas(Pageable pageable){
        return pessoaRepository.findAll(pageable);
    }


    public Page<Pessoa> buscarPorSexo(String sexo, Pageable pageable){
        return pessoaRepository.findBySexo(sexo, pageable);
    }

    public Page<Pessoa> buscarPorNomeContaining(String texto, Pageable pageable){
        return pessoaRepository.findByNomeContaining(texto, pageable);
    }

    public void deletar(Integer id){
        pessoaRepository.deleteById(id);
    }

}
