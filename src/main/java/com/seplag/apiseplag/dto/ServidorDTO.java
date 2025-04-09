package com.seplag.apiseplag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServidorDTO {
    private String nome;
    private Integer idade;
    private String unidadeLotacao;
    private String fotografia;
}