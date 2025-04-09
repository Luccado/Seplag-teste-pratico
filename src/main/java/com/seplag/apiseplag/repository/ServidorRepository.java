package com.seplag.apiseplag.repository;

import com.seplag.apiseplag.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServidorRepository extends JpaRepository<Pessoa, Integer> {

    @Query("SELECT p FROM Pessoa p JOIN p.lotacoes l WHERE l.unidade.id = :unidadeId AND l.dataRemocao IS NULL")
    List<Pessoa> findServidoresEfetivosPorUnidade(@Param("unidadeId") Integer unidadeId);

    @Query("SELECT p FROM Pessoa p JOIN p.lotacoes l WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nomeParcial, '%')) AND l.dataRemocao IS NULL")
    List<Pessoa> findServidoresPorNomeParcial(@Param("nomeParcial") String nomeParcial);

    @Query("SELECT p FROM Pessoa p WHERE EXISTS (SELECT st FROM ServidorTemporario st WHERE st.pessoa = p) OR EXISTS (SELECT se FROM ServidorEfetivo se WHERE se.pessoa = p)")
    List<Pessoa> findAllServidores();

}