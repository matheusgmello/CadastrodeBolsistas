package dev.matheus.CadastroDeBolsistas.Laboratorios.Services;

import dev.matheus.CadastroDeBolsistas.Laboratorios.DTOs.LaboratorioDTO;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Mappers.LaboratorioMapper;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Models.LaboratorioModel;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Repositories.LaboratorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;
    private final LaboratorioMapper laboratorioMapper;

    public LaboratorioService(LaboratorioRepository laboratorioRepository, LaboratorioMapper laboratorioMapper) {
        this.laboratorioRepository = laboratorioRepository;
        this.laboratorioMapper = laboratorioMapper;
    }

    // Listar todos os laboratórios
    public List<LaboratorioDTO> listarLaboratorios() {
        return laboratorioRepository.findAll()
                .stream()
                .map(laboratorioMapper::map)
                .toList();
    }


    // Listar laboratório por ID
    public LaboratorioDTO listarLaboratorioPorId(Long id) {
        Optional<LaboratorioModel> labPorId = laboratorioRepository.findById(id);
        return labPorId.map(laboratorioMapper::map).orElse(null);
    }

    // Criar um novo laboratório
    public LaboratorioDTO criarLaboratorio(LaboratorioDTO laboratorioDTO) {
        LaboratorioModel lab = laboratorioMapper.map(laboratorioDTO);
        lab = laboratorioRepository.save(lab);
        return laboratorioMapper.map(lab);
    }

    // Alterar um laboratório existente
    public LaboratorioDTO alterarLaboratorio(Long id, LaboratorioDTO laboratorioDTO) {
        return laboratorioRepository.findById(id)
                .map(labExistente -> {
                    if (laboratorioDTO.getNome() != null) {
                        labExistente.setNome(laboratorioDTO.getNome());
                    }
                    if (laboratorioDTO.getAreaDeAtuacao() != null) {
                        labExistente.setAreaDeAtuacao(laboratorioDTO.getAreaDeAtuacao());
                    }
                    if (laboratorioDTO.getBolsistas() != null) {
                        labExistente.setBolsistas(laboratorioDTO.getBolsistas());
                    }

                    LaboratorioModel labAtualizado = laboratorioRepository.save(labExistente);
                    return laboratorioMapper.map(labAtualizado);
                })
                .orElse(null);
    }

    // Deletar um laboratório
    public void deletarLaboratorio(Long id) {
        Optional<LaboratorioModel> labPorId = laboratorioRepository.findById(id);
        labPorId.ifPresent(laboratorioRepository::delete);
    }

}
