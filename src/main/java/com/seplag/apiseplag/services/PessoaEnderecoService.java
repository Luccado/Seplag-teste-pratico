package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.Endereco;
import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.model.PessoaEndereco;
import com.seplag.apiseplag.repository.EnderecoRepository;
import com.seplag.apiseplag.repository.PessoaEnderecoRepository;
import com.seplag.apiseplag.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PessoaEnderecoService {

    private final PessoaEnderecoRepository pessoaEnderecoRepository;
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;

    @Autowired
    public PessoaEnderecoService(
            PessoaEnderecoRepository pessoaEnderecoRepository,
            PessoaRepository pessoaRepository,
            EnderecoRepository enderecoRepository) {
        this.pessoaEnderecoRepository = pessoaEnderecoRepository;
        this.pessoaRepository = pessoaRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public Page<PessoaEndereco> listarPaginado(int pagina, int tamanho, String ordenarPor, String direcao) {
        Sort.Direction sortDirection = direcao.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(sortDirection, ordenarPor));
        return pessoaEnderecoRepository.findAll(pageable);
    }

    public Optional<PessoaEndereco> buscarPorId(Integer id) {
        return pessoaEnderecoRepository.findById(id);
    }

    public Page<PessoaEndereco> buscarPorPessoaIdPaginado(Integer pessoaId, int pagina, String ordenarPor, String direcao) {
        Sort.Direction sortDirection = direcao.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, 10, Sort.by(sortDirection, ordenarPor));
        return pessoaEnderecoRepository.findByPessoaId(pessoaId, pageable);
    }

    @Transactional
    public PessoaEndereco associarPessoaEndereco(Integer pessoaId, Integer enderecoId) {
        String relationKey = pessoaId + "-" + enderecoId;
        Optional<PessoaEndereco> existingAssociation = pessoaEnderecoRepository.findByRelationKey(relationKey);

        if (existingAssociation.isPresent()) {
            return existingAssociation.get();
        }

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada com ID: " + pessoaId));

        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado com ID: " + enderecoId));

        PessoaEndereco pessoaEndereco = new PessoaEndereco();
        pessoaEndereco.setPessoa(pessoa);
        pessoaEndereco.setEndereco(endereco);

        return pessoaEnderecoRepository.save(pessoaEndereco);
    }

    @Transactional
    public void removerAssociacao(Integer id) {
        pessoaEnderecoRepository.deleteById(id);
    }

    @Transactional
    public void removerAssociacaoPorPessoaEEndereco(Integer pessoaId, Integer enderecoId) {
        String relationKey = pessoaId + "-" + enderecoId;
        Optional<PessoaEndereco> association = pessoaEnderecoRepository.findByRelationKey(relationKey);

        association.ifPresent(pessoaEndereco ->
                pessoaEnderecoRepository.deleteById(pessoaEndereco.getId())
        );
    }
}