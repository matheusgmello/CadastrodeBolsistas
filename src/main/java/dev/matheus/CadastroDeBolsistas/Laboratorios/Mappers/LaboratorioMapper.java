package dev.matheus.CadastroDeBolsistas.Laboratorios.Mappers;

import dev.matheus.CadastroDeBolsistas.Laboratorios.DTOs.LaboratorioDTO;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Models.LaboratorioModel;
import org.springframework.stereotype.Component;

@Component
public class LaboratorioMapper {

    public LaboratorioModel map(LaboratorioDTO laboratorioDTO) {
        LaboratorioModel laboratorioModel = new LaboratorioModel();
        laboratorioModel.setId(laboratorioDTO.getId());
        laboratorioModel.setNome(laboratorioDTO.getNome());
        laboratorioModel.setAreaDeAtuacao(laboratorioDTO.getAreaDeAtuacao());
        laboratorioModel.setBolsistas(laboratorioDTO.getBolsistas());

        return laboratorioModel;
    }

    public LaboratorioDTO map(LaboratorioModel laboratorioModel) {
        LaboratorioDTO laboratorioDTO = new LaboratorioDTO();
        laboratorioDTO.setId(laboratorioModel.getId());
        laboratorioDTO.setNome(laboratorioModel.getNome());
        laboratorioDTO.setAreaDeAtuacao(laboratorioModel.getAreaDeAtuacao());
        laboratorioDTO.setBolsistas(laboratorioModel.getBolsistas());

        return laboratorioDTO;
    }
}