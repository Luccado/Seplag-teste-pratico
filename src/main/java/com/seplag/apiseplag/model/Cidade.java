package com.seplag.apiseplag.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cidade")
public class Cidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid_id")
    private Integer id;

    @Column(name = "cid_nome", length = 200, nullable = false, columnDefinition = "VARCHAR(200)")
    private String nome;

    @Column(name = "cid_uf", length = 2, nullable = false, columnDefinition = "CHAR(2)")
    private String uf;

}