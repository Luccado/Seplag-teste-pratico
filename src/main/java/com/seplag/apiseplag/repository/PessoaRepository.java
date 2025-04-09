package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {
    Page<Pessoa> findBySexo(String sexo, Pageable pageable);
    Page<Pessoa> findByNomeContaining(String texto, Pageable pageable);
    Optional<Pessoa> findByUsername(String username);
    boolean existsByUsername(String username);

}