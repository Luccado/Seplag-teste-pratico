package com.seplag.apiseplag.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "unidade")
public class Unidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unid_id")
    private Integer id;

    @Column(name = "unid_nome", length = 200, nullable = false, columnDefinition = "VARCHAR(200)")
    private String nome;

    @Column(name = "unid_sigla", length = 20, columnDefinition = "VARCHAR(20)")
    private String sigla;

    @OneToMany(mappedBy = "unidade")
    private List<Lotacao> lotacoes;

    @ManyToMany
    @JoinTable(
            name = "unidade_endereco",
            joinColumns = @JoinColumn(name = "unid_id"),
            inverseJoinColumns = @JoinColumn(name = "end_id")
    )
    private List<Endereco> enderecos;
}