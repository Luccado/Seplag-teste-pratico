package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.Cidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

    List<Cidade> findByNomeContainingIgnoreCase(String nome);

    List<Cidade> findByUf(Character uf);

    Page<Cidade> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}