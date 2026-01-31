package dev.matheus.CadastroDeBolsistas.Bolsistas.DTOs;

import dev.matheus.CadastroDeBolsistas.Bolsistas.Enums.NivelBolsa;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Models.LaboratorioModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BolsistaDTO {

    private Long id;
    private String nome;
    private Integer idade;
    private String email;
    private NivelBolsa nivelBolsa;

    private LaboratorioModel laboratorio;


}
