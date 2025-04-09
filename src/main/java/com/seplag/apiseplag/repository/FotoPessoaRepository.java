package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.FotoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoPessoaRepository extends JpaRepository<FotoPessoa, Integer> {
}