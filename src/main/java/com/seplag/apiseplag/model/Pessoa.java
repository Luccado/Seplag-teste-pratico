package com.seplag.apiseplag.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pessoa")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pes_id")
    private Integer id;

    //@NotNull(message = "O nome não pode ser nulo")
    //@Size(min = 3, max = 200, message = "O nome deve ter entre 3 à 200 caracteres")
    @Column(name = "pes_nome", length = 200, nullable = false, columnDefinition = "VARCHAR(200)")
    private String nome;

    //@NotNull(message = "A data de nascimento não pode ser nulo")
    //@Past(message = "Coloque uma data de nascimento válida")
    @Temporal(TemporalType.DATE)
    @Column(name = "pes_data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    //@NotNull(message = "Insira um sexo válido (masculino, feminino)")
    @Column(name = "pes_sexo", length = 9, columnDefinition = "VARCHAR(9)")
    private String sexo;

    //NotNull
    @Column(name = "pes_mae", length = 200, columnDefinition = "VARCHAR(200)")
    private String mae;

    //@NotNull
    @Column(name = "pes_pai", length = 200, columnDefinition = "VARCHAR(200)")
    private String pai;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lotacao> lotacoes;

    @Column(unique = true)
    private String username;

    private String password;

    private String role;

    private boolean enabled = true;


}

