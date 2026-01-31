package dev.matheus.CadastroDeBolsistas.Laboratorios.DTOs;

import dev.matheus.CadastroDeBolsistas.Bolsistas.Models.BolsistaModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaboratorioDTO {

    private Long id;
    private String nome;
    private String areaDeAtuacao;

    private List<BolsistaModel> bolsistas;

}
