package dev.matheus.CadastroDeBolsistas.Laboratorios.Services;

import dev.matheus.CadastroDeBolsistas.Exceptions.DataConflictException;
import dev.matheus.CadastroDeBolsistas.Exceptions.ResourceNotFoundException;
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

    public List<LaboratorioDTO> listarLaboratorios() {
        return laboratorioRepository.findAll()
                .stream()
                .map(laboratorioMapper::map)
                .toList();
    }

    public LaboratorioDTO listarLaboratorioPorId(Long id) {
        return laboratorioRepository.findById(id)
                .map(laboratorioMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Laboratório com ID " + id + " não encontrado."));
    }

    public LaboratorioDTO criarLaboratorio(LaboratorioDTO laboratorioDTO) {
        if (laboratorioRepository.existsByNome(laboratorioDTO.getNome())) {
            throw new DataConflictException("Já existe um laboratório cadastrado com o nome: " + laboratorioDTO.getNome());
        }

        LaboratorioModel lab = laboratorioMapper.map(laboratorioDTO);
        return laboratorioMapper.map(laboratorioRepository.save(lab));
    }

    public LaboratorioDTO alterarLaboratorio(Long id, LaboratorioDTO laboratorioDTO) {
        return laboratorioRepository.findById(id)
                .map(labExistente -> {
                    if (laboratorioDTO.getNome() != null) labExistente.setNome(laboratorioDTO.getNome());
                    if (laboratorioDTO.getAreaDeAtuacao() != null) labExistente.setAreaDeAtuacao(laboratorioDTO.getAreaDeAtuacao());
                    if (laboratorioDTO.getBolsistas() != null) labExistente.setBolsistas(laboratorioDTO.getBolsistas());

                    return laboratorioMapper.map(laboratorioRepository.save(labExistente));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possível atualizar: Laboratório ID " + id + " não encontrado."));
    }

    public void deletarLaboratorio(Long id) {
        LaboratorioModel lab = laboratorioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possível deletar: Laboratório ID " + id + " não encontrado."));

        if (lab.getBolsistas() != null && !lab.getBolsistas().isEmpty()) {
            throw new DataConflictException("Não é possível deletar um laboratório que possui bolsistas vinculados.");
        }

        laboratorioRepository.delete(lab);
    }
}