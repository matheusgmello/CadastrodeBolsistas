package dev.matheus.CadastroDeBolsistas.Bolsistas.Mappers;

import dev.matheus.CadastroDeBolsistas.Bolsistas.DTOs.BolsistaDTO;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Models.BolsistaModel;
import org.springframework.stereotype.Component;

@Component
public class BolsistaMapper {

    public BolsistaModel map(BolsistaDTO bolsistaDTO) {
        BolsistaModel bolsistaModel = new BolsistaModel();
        bolsistaModel.setId(bolsistaDTO.getId());
        bolsistaModel.setNome(bolsistaDTO.getNome());
        bolsistaModel.setEmail(bolsistaDTO.getEmail());
        bolsistaModel.setIdade(bolsistaDTO.getIdade());
        bolsistaModel.setNivelBolsa(bolsistaDTO.getNivelBolsa());
        bolsistaModel.setLaboratorio(bolsistaDTO.getLaboratorio());

        return bolsistaModel;
    }

    public BolsistaDTO map(BolsistaModel bolsistaModel) {
        BolsistaDTO bolsistaDTO = new BolsistaDTO();
        bolsistaDTO.setId(bolsistaModel.getId());
        bolsistaDTO.setNome(bolsistaModel.getNome());
        bolsistaDTO.setEmail(bolsistaModel.getEmail());
        bolsistaDTO.setIdade(bolsistaModel.getIdade());
        bolsistaDTO.setNivelBolsa(bolsistaModel.getNivelBolsa());
        bolsistaDTO.setLaboratorio(bolsistaModel.getLaboratorio());

        return bolsistaDTO;
    }

}
