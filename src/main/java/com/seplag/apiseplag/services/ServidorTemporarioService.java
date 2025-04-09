package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.model.ServidorTemporario;
import com.seplag.apiseplag.repository.PessoaRepository;
import com.seplag.apiseplag.repository.ServidorTemporarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ServidorTemporarioService {

    private final ServidorTemporarioRepository servidorTemporarioRepository;
    private final PessoaRepository pessoaRepository;

    @Autowired
    public ServidorTemporarioService(ServidorTemporarioRepository servidorTemporarioRepository,
                                     PessoaRepository pessoaRepository) {
        this.servidorTemporarioRepository = servidorTemporarioRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public ServidorTemporario salvar(ServidorTemporario servidorTemporario) {
        // Verificar se a pessoa existe
        if (servidorTemporario.getPessoa() != null && servidorTemporario.getPessoa().getId() != null) {
            Optional<Pessoa> pessoaOpt = pessoaRepository.findById(servidorTemporario.getPessoa().getId());
            if (pessoaOpt.isPresent()) {
                servidorTemporario.setPessoa(pessoaOpt.get());
                servidorTemporario.setId(pessoaOpt.get().getId());
                return servidorTemporarioRepository.save(servidorTemporario);
            } else {
                throw new RuntimeException("Pessoa não encontrada com o ID: " + servidorTemporario.getPessoa().getId());
            }
        } else {
            throw new RuntimeException("É necessário informar uma pessoa válida para o servidor temporário");
        }
    }

    public Optional<ServidorTemporario> buscarPorId(Integer id) {
        return servidorTemporarioRepository.findById(id);
    }

    public Page<ServidorTemporario> listarTodos(Pageable pageable) {
        return servidorTemporarioRepository.findAll(pageable);
    }

    public List<ServidorTemporario> buscarPorDataAdmissao(LocalDate dataAdmissao) {
        return servidorTemporarioRepository.findByDataAdmissao(dataAdmissao);
    }

    public List<ServidorTemporario> buscarPorPeriodoAdmissao(LocalDate dataInicio, LocalDate dataFim) {
        return servidorTemporarioRepository.findByDataAdmissaoBetween(dataInicio, dataFim);
    }

    public List<ServidorTemporario> buscarServidoresAtivos() {
        return servidorTemporarioRepository.findByDataDemissaoIsNull();
    }

    public List<ServidorTemporario> buscarServidoresInativos() {
        return servidorTemporarioRepository.findByDataDemissaoIsNotNull();
    }

    @Transactional
    public ServidorTemporario atualizar(Integer id, ServidorTemporario servidorTemporario) {
        if (servidorTemporarioRepository.existsById(id)) {
            servidorTemporario.setId(id);
            return servidorTemporarioRepository.save(servidorTemporario);
        } else {
            throw new RuntimeException("Servidor temporário não encontrado com o ID: " + id);
        }
    }


    @Transactional
    public void excluir(Integer id) {
        if (servidorTemporarioRepository.existsById(id)) {
            servidorTemporarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Servidor temporário não encontrado com o ID: " + id);
        }
    }
}