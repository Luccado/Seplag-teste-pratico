package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.model.ServidorEfetivo;
import com.seplag.apiseplag.repository.PessoaRepository;
import com.seplag.apiseplag.repository.ServidorEfetivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ServidorEfetivoService {

    private final ServidorEfetivoRepository servidorEfetivoRepository;
    private final PessoaRepository pessoaRepository;

    @Autowired
    public ServidorEfetivoService(ServidorEfetivoRepository servidorEfetivoRepository,
                                  PessoaRepository pessoaRepository) {
        this.servidorEfetivoRepository = servidorEfetivoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public ServidorEfetivo salvar(ServidorEfetivo servidorEfetivo) {
        if (servidorEfetivo.getPessoa() != null && servidorEfetivo.getPessoa().getId() != null) {
            Optional<Pessoa> pessoaOpt = pessoaRepository.findById(servidorEfetivo.getPessoa().getId());
            if (pessoaOpt.isPresent()) {
                servidorEfetivo.setPessoa(pessoaOpt.get());
                servidorEfetivo.setId(pessoaOpt.get().getId());
                return servidorEfetivoRepository.save(servidorEfetivo);
            } else {
                throw new RuntimeException("Pessoa não encontrada com o ID: " + servidorEfetivo.getPessoa().getId());
            }
        } else {
            throw new RuntimeException("É necessário informar uma pessoa válida para o servidor efetivo");
        }
    }

    public Optional<ServidorEfetivo> buscarPorId(Integer id) {
        return servidorEfetivoRepository.findById(id);
    }

    public Page<ServidorEfetivo> listarTodos(Pageable pageable) {
        return servidorEfetivoRepository.findAll(pageable);
    }

    @Transactional
    public ServidorEfetivo atualizar(Integer id, ServidorEfetivo servidorEfetivo) {
        if (servidorEfetivoRepository.existsById(id)) {
            servidorEfetivo.setId(id);
            return servidorEfetivoRepository.save(servidorEfetivo);
        } else {
            throw new RuntimeException("Servidor efetivo não encontrado com o ID: " + id);
        }
    }

    @Transactional
    public void excluir(Integer id) {
        if (servidorEfetivoRepository.existsById(id)) {
            servidorEfetivoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Servidor efetivo não encontrado com o ID: " + id);
        }
    }
}