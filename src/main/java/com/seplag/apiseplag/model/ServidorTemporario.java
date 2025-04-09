package com.seplag.apiseplag.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "servidor_temporario")
public class ServidorTemporario extends Servidor {

    @Temporal(TemporalType.DATE)
    @Column(name = "st_data_admissao")
    private LocalDate dataAdmissao;

    @Temporal(TemporalType.DATE)
    @Column(name = "st_data_demissao")
    private LocalDate dataDemissao;
}