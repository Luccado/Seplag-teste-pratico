package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.Unidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Integer> {

    Page<Unidade> findAll(Pageable pageable);

    Page<Unidade> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Unidade> findBySiglaContainingIgnoreCase(String sigla, Pageable pageable);
}