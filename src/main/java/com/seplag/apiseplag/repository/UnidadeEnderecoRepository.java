package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.UnidadeEndereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadeEnderecoRepository extends JpaRepository<UnidadeEndereco, Integer> {

    List<UnidadeEndereco> findByUnidadeId(Integer unidadeId);

    List<UnidadeEndereco> findByEnderecoId(Integer enderecoId);

    boolean existsByUnidadeIdAndEnderecoId(Integer unidadeId, Integer enderecoId);

    void deleteByUnidadeIdAndEnderecoId(Integer unidadeId, Integer enderecoId);
}