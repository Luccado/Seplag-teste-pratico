package com.seplag.apiseplag.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "servidor_efetivo")
public class ServidorEfetivo extends Servidor {

    @Column(name = "se_matricula", length = 20, columnDefinition = "VARCHAR(20)")
    private String matricula;
}