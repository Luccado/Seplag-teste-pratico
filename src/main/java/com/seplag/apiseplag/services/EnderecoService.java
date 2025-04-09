package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.Endereco;
import com.seplag.apiseplag.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public Page<Endereco> listarTodos(int pagina, String ordenarPor, String direcao) {
        Sort.Direction sortDirection = direcao.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, 10, Sort.by(sortDirection, ordenarPor));
        return enderecoRepository.findAll(pageable);
    }

    public Optional<Endereco> buscarPorId(Integer id) {
        return enderecoRepository.findById(id);
    }

    public Endereco salvar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public void excluir(Integer id) {
        enderecoRepository.deleteById(id);
    }

    public Page<Endereco> buscarPorLogradouro(String logradouro, int pagina, String ordenarPor, String direcao) {
        Sort.Direction sortDirection = direcao.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, 10, Sort.by(sortDirection, ordenarPor));
        return enderecoRepository.findByLogradouroContainingIgnoreCase(logradouro, pageable);
    }

    public Page<Endereco> buscarPorBairro(String bairro, int pagina, String ordenarPor, String direcao) {
        Sort.Direction sortDirection = direcao.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, 10, Sort.by(sortDirection, ordenarPor));
        return enderecoRepository.findByBairroContainingIgnoreCase(bairro, pageable);
    }

    public Page<Endereco> buscarPorCidadeId(Integer cidadeId, int pagina, String ordenarPor, String direcao) {
        Sort.Direction sortDirection = direcao.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, 10, Sort.by(sortDirection, ordenarPor));
        return enderecoRepository.findByCidadeId(cidadeId, pageable);
    }
}