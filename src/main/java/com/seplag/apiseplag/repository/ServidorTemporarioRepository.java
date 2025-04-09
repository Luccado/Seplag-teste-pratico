package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.ServidorTemporario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServidorTemporarioRepository extends JpaRepository<ServidorTemporario, Integer> {


    List<ServidorTemporario> findByDataAdmissao(LocalDate dataAdmissao);

    List<ServidorTemporario> findByDataAdmissaoBetween(LocalDate dataInicio, LocalDate dataFim);

    List<ServidorTemporario> findByDataDemissaoIsNull();

    List<ServidorTemporario> findByDataDemissaoIsNotNull();

    Page<ServidorTemporario> findAll(Pageable pageable);

    boolean existsById(Integer id);
}