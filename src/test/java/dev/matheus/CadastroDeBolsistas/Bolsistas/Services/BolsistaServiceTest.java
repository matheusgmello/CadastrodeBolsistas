package dev.matheus.CadastroDeBolsistas.Bolsistas.Services;

import dev.matheus.CadastroDeBolsistas.Bolsistas.DTOs.BolsistaDTO;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Enums.NivelBolsa;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Mappers.BolsistaMapper;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Models.BolsistaModel;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Repositories.BolsistaRepository;
import dev.matheus.CadastroDeBolsistas.Exceptions.DataConflictException;
import dev.matheus.CadastroDeBolsistas.Exceptions.ResourceNotFoundException;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Models.LaboratorioModel;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Repositories.LaboratorioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BolsistaServiceTest {

    @Mock
    private BolsistaRepository bolsistaRepository;

    @Mock
    private BolsistaMapper bolsistaMapper;

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @InjectMocks
    private BolsistaService bolsistaService;

    private BolsistaModel bolsistaModel;
    private BolsistaDTO bolsistaDTO;
    private LaboratorioModel laboratorioModel;

    @BeforeEach
    void setUp() {
        laboratorioModel = new LaboratorioModel();
        laboratorioModel.setId(1L);
        laboratorioModel.setNome("Lab 1");

        bolsistaModel = new BolsistaModel();
        bolsistaModel.setId(1L);
        bolsistaModel.setNome("Bolsista 1");
        bolsistaModel.setEmail("bolsista1@email.com");
        bolsistaModel.setIdade(20);
        bolsistaModel.setNivelBolsa(NivelBolsa.INICIACAO_CIENTIFICA);
        bolsistaModel.setLaboratorio(laboratorioModel);

        bolsistaDTO = new BolsistaDTO();
        bolsistaDTO.setId(1L);
        bolsistaDTO.setNome("Bolsista 1");
        bolsistaDTO.setEmail("bolsista1@email.com");
        bolsistaDTO.setIdade(20);
        bolsistaDTO.setNivelBolsa(NivelBolsa.INICIACAO_CIENTIFICA);
        bolsistaDTO.setLaboratorio(laboratorioModel);
    }

    @Test
    void listaBolsistas_DeveRetornarListaDeDTOs() {
        when(bolsistaRepository.findAll()).thenReturn(List.of(bolsistaModel));
        when(bolsistaMapper.map(any(BolsistaModel.class))).thenReturn(bolsistaDTO);

        List<BolsistaDTO> resultado = bolsistaService.listaBolsistas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Bolsista 1");
        verify(bolsistaRepository, times(1)).findAll();
    }

    @Test
    void listaBolsistaPorId_QuandoIdExiste_DeveRetornarDTO() {
        when(bolsistaRepository.findById(1L)).thenReturn(Optional.of(bolsistaModel));
        when(bolsistaMapper.map(bolsistaModel)).thenReturn(bolsistaDTO);

        BolsistaDTO resultado = bolsistaService.listaBolsistaPorId(1L);

        assertThat(resultado.getNome()).isEqualTo("Bolsista 1");
        verify(bolsistaRepository, times(1)).findById(1L);
    }

    @Test
    void listaBolsistaPorId_QuandoIdNaoExiste_DeveLancarExcecao() {
        when(bolsistaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bolsistaService.listaBolsistaPorId(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void criarBolsista_QuandoEmailNaoExiste_DeveRetornarDTO() {
        when(bolsistaRepository.existsByEmail(bolsistaDTO.getEmail())).thenReturn(false);
        when(bolsistaMapper.map(bolsistaDTO)).thenReturn(bolsistaModel);
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorioModel));
        when(bolsistaRepository.save(any(BolsistaModel.class))).thenReturn(bolsistaModel);
        when(bolsistaMapper.map(bolsistaModel)).thenReturn(bolsistaDTO);

        BolsistaDTO resultado = bolsistaService.criarBolsista(bolsistaDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Bolsista 1");
        verify(bolsistaRepository, times(1)).save(any(BolsistaModel.class));
    }

    @Test
    void criarBolsista_QuandoEmailJaExiste_DeveLancarExcecao() {
        when(bolsistaRepository.existsByEmail(bolsistaDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> bolsistaService.criarBolsista(bolsistaDTO))
                .isInstanceOf(DataConflictException.class);
    }

    @Test
    void criarBolsista_QuandoLaboratorioNaoExiste_DeveLancarExcecao() {
        when(bolsistaRepository.existsByEmail(bolsistaDTO.getEmail())).thenReturn(false);
        when(bolsistaMapper.map(bolsistaDTO)).thenReturn(bolsistaModel);
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bolsistaService.criarBolsista(bolsistaDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void alterarBolsista_QuandoIdExiste_DeveRetornarDTOAtualizado() {
        BolsistaDTO dtoAtualizado = new BolsistaDTO();
        dtoAtualizado.setNome("Bolsista Atualizado");

        when(bolsistaRepository.findById(1L)).thenReturn(Optional.of(bolsistaModel));
        when(bolsistaRepository.save(any(BolsistaModel.class))).thenReturn(bolsistaModel);
        when(bolsistaMapper.map(bolsistaModel)).thenReturn(dtoAtualizado);

        BolsistaDTO resultado = bolsistaService.alterarBolsista(1L, dtoAtualizado);

        assertThat(resultado.getNome()).isEqualTo("Bolsista Atualizado");
        verify(bolsistaRepository, times(1)).save(any(BolsistaModel.class));
    }

    @Test
    void alterarBolsista_QuandoIdNaoExiste_DeveLancarExcecao() {
        when(bolsistaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bolsistaService.alterarBolsista(1L, bolsistaDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deletarBolsista_QuandoIdExiste_DeveDeletar() {
        when(bolsistaRepository.existsById(1L)).thenReturn(true);

        bolsistaService.deletarBolsista(1L);

        verify(bolsistaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletarBolsista_QuandoIdNaoExiste_DeveLancarExcecao() {
        when(bolsistaRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> bolsistaService.deletarBolsista(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
