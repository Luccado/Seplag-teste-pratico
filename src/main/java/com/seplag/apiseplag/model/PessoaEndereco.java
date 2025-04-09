package com.seplag.apiseplag.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pessoa_endereco")
public class PessoaEndereco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pes_id", foreignKey = @ForeignKey(name = "fk_pessoa_endereco_pessoa"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_id", foreignKey = @ForeignKey(name = "fk_pessoa_endereco_endereco"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Endereco endereco;

    @Column(unique = true)
    private String relationKey;

    @PrePersist
    @PreUpdate
    private void generateRelationKey() {
        this.relationKey = pessoa.getId() + "-" + endereco.getId();
    }
}