package com.seplag.apiseplag.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Servidor {

    @Id
    @Column(name = "pes_id")
    private Integer id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "pes_id")
    private Pessoa pessoa;
}