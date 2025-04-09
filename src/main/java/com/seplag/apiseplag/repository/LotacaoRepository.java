package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.Lotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotacaoRepository extends JpaRepository<Lotacao, Integer> {
}