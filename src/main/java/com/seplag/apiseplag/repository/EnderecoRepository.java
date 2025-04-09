package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.Endereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

    Page<Endereco> findAll(Pageable pageable);

    Page<Endereco> findByLogradouroContainingIgnoreCase(String logradouro, Pageable pageable);

    Page<Endereco> findByBairroContainingIgnoreCase(String bairro, Pageable pageable);

    Page<Endereco> findByCidadeId(Integer cidadeId, Pageable pageable);
}