package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.ServidorEfetivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServidorEfetivoRepository extends JpaRepository<ServidorEfetivo, Integer> {
    Page<ServidorEfetivo> findAll(Pageable pageable);
}