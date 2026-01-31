package dev.matheus.CadastroDeBolsistas.Bolsistas.Services;

import dev.matheus.CadastroDeBolsistas.Bolsistas.DTOs.BolsistaDTO;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Mappers.BolsistaMapper;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Models.BolsistaModel;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Repositories.BolsistaRepository;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Models.LaboratorioModel;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Repositories.LaboratorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<BolsistaModel> bolsistaPorId = bolsistaRepository.findById(id);
        return bolsistaPorId.map(bolsistaMapper::map).orElse(null);
    }

    // Criar um novo bolsista atualizado
    public BolsistaDTO criarBolsista(BolsistaDTO bolsistaDTO) {
        BolsistaModel bolsista = bolsistaMapper.map(bolsistaDTO);

        // Buscamos o laboratório completo para que o JSON de resposta venha preenchido
        if (bolsista.getLaboratorio() != null && bolsista.getLaboratorio().getId() != null) {
            LaboratorioModel lab = laboratorioRepository.findById(bolsista.getLaboratorio().getId())
                    .orElseThrow(() -> new RuntimeException("Laboratório não encontrado"));
            bolsista.setLaboratorio(lab);
        }

        bolsista = bolsistaRepository.save(bolsista);
        return bolsistaMapper.map(bolsista);
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

                    // Se o laboratório for alterado, buscamos o novo completo
                    if (bolsistaDTO.getLaboratorio() != null && bolsistaDTO.getLaboratorio().getId() != null) {
                        LaboratorioModel novoLab = laboratorioRepository.findById(bolsistaDTO.getLaboratorio().getId())
                                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado"));
                        bolsistaExistente.setLaboratorio(novoLab);
                    }

                    BolsistaModel bolsistaAtualizado = bolsistaRepository.save(bolsistaExistente);
                    return bolsistaMapper.map(bolsistaAtualizado);
                })
                .orElse(null);
    }

    // Deletar um bolsista
    public void deletarBolsista(Long id) {
        Optional<BolsistaModel> bolsistaPorId = bolsistaRepository.findById(id);
        bolsistaPorId.ifPresent(bolsistaRepository::delete);
    }
}
