package dev.matheus.CadastroDeBolsistas.Laboratorios.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Models.BolsistaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tb_laboratorios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LaboratorioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "area_atuacao")
    private String areaDeAtuacao;

    @OneToMany(mappedBy = "laboratorio", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BolsistaModel> bolsistas;
}
