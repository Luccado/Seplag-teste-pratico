package com.seplag.apiseplag.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "foto_pessoa")
public class FotoPessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fp_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pes_id", nullable = false)
    private Pessoa pessoa;

    @Column(name = "fp_data")
    private LocalDate data;

    @Column(name = "fp_bucket", length = 50, nullable = false, columnDefinition = "VARCHAR(50)")
    private String bucket;

    @Column(name = "fp_hash", length = 50, nullable = false, columnDefinition = "VARCHAR(50)")
    private String hash;

}
