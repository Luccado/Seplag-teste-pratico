package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.PessoaEndereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaEnderecoRepository extends JpaRepository<PessoaEndereco, Integer> {

    Page<PessoaEndereco> findByPessoaId(Integer pessoaId, Pageable pageable);

    Optional<PessoaEndereco> findByRelationKey(String relationKey);

}