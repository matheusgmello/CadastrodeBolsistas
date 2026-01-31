package dev.matheus.CadastroDeBolsistas.Bolsistas.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Enums.NivelBolsa;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Models.LaboratorioModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_bolsistas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BolsistaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Integer idade;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_bolsa")
    private NivelBolsa nivelBolsa;

    @ManyToOne
    @JoinColumn(name = "laboratorio_id")
    @JsonIgnore
    private LaboratorioModel laboratorio;

}
