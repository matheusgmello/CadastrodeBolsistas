package dev.matheus.CadastroDeBolsistas.Bolsistas.Services;

import dev.matheus.CadastroDeBolsistas.Bolsistas.DTOs.BolsistaDTO;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Mappers.BolsistaMapper;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Models.BolsistaModel;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Repositories.BolsistaRepository;
import dev.matheus.CadastroDeBolsistas.Exceptions.DataConflictException;
import dev.matheus.CadastroDeBolsistas.Exceptions.ResourceNotFoundException;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Models.LaboratorioModel;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Repositories.LaboratorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BolsistaService {

    private final BolsistaRepository bolsistaRepository;
    private final BolsistaMapper bolsistaMapper;
    private final LaboratorioRepository laboratorioRepository;


    public BolsistaService(BolsistaRepository bolsistaRepository, BolsistaMapper bolsistaMapper, LaboratorioRepository laboratorioRepository) {
        this.bolsistaRepository = bolsistaRepository;
        this.bolsistaMapper = bolsistaMapper;
        this.laboratorioRepository = laboratorioRepository;
    }

    // Listar todos os bolsistas
    public List<BolsistaDTO> listaBolsistas() {
        return bolsistaRepository.findAll()
                .stream()
                .map(bolsistaMapper::map)
                .toList();
    }

    // Lista bolsista por ID
    public BolsistaDTO listaBolsistaPorId(Long id) {
        return bolsistaRepository.findById(id)
                .map(bolsistaMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Bolsista com ID " + id + " não encontrado."));
    }

    // Criar um novo bolsista
    public BolsistaDTO criarBolsista(BolsistaDTO bolsistaDTO) {
        if (bolsistaRepository.existsByEmail(bolsistaDTO.getEmail())) {
            throw new DataConflictException("Já existe um bolsista cadastrado com o e-mail: " + bolsistaDTO.getEmail());
        }

        BolsistaModel bolsista = bolsistaMapper.map(bolsistaDTO);

        if (bolsista.getLaboratorio() != null && bolsista.getLaboratorio().getId() != null) {
            LaboratorioModel lab = laboratorioRepository.findById(bolsista.getLaboratorio().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Laboratório com ID " + bolsista.getLaboratorio().getId() + " não existe."));
            bolsista.setLaboratorio(lab);
        }

        return bolsistaMapper.map(bolsistaRepository.save(bolsista));
    }

    // Alterar um bolsista atualizado
    public BolsistaDTO alterarBolsista(Long id, BolsistaDTO bolsistaDTO) {
        return bolsistaRepository.findById(id)
                .map(bolsistaExistente -> {
                    if (bolsistaDTO.getNome() != null) bolsistaExistente.setNome(bolsistaDTO.getNome());
                    if (bolsistaDTO.getIdade() != null) bolsistaExistente.setIdade(bolsistaDTO.getIdade());
                    if (bolsistaDTO.getEmail() != null) bolsistaExistente.setEmail(bolsistaDTO.getEmail());
                    if (bolsistaDTO.getNivelBolsa() != null)
                        bolsistaExistente.setNivelBolsa(bolsistaDTO.getNivelBolsa());
                    if (bolsistaDTO.getLaboratorio() != null && bolsistaDTO.getLaboratorio().getId() != null) {
                        LaboratorioModel novoLab = laboratorioRepository.findById(bolsistaDTO.getLaboratorio().getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Laboratório não encontrado"));
                        bolsistaExistente.setLaboratorio(novoLab);
                    }
                    return bolsistaMapper.map(bolsistaRepository.save(bolsistaExistente));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Bolsista com ID " + id + " não encontrado."));
    }


    // Deletar um bolsista
    public void deletarBolsista(Long id) {
        if (!bolsistaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossível deletar: Bolsista com ID " + id + " não encontrado.");
        }
        bolsistaRepository.deleteById(id);
    }
}
