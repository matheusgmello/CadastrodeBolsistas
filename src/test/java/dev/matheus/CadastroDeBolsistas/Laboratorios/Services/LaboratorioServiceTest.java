package dev.matheus.CadastroDeBolsistas.Laboratorios.Services;

import dev.matheus.CadastroDeBolsistas.Exceptions.DataConflictException;
import dev.matheus.CadastroDeBolsistas.Exceptions.ResourceNotFoundException;
import dev.matheus.CadastroDeBolsistas.Laboratorios.DTOs.LaboratorioDTO;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Mappers.LaboratorioMapper;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Models.LaboratorioModel;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Repositories.LaboratorioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LaboratorioServiceTest {

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @Mock
    private LaboratorioMapper laboratorioMapper;

    @InjectMocks
    private LaboratorioService laboratorioService;

    private LaboratorioModel laboratorioModel;
    private LaboratorioDTO laboratorioDTO;

    @BeforeEach
    void setUp() {
        laboratorioModel = new LaboratorioModel();
        laboratorioModel.setId(1L);
        laboratorioModel.setNome("Lab 1");
        laboratorioModel.setAreaDeAtuacao("Tecnologia");
        laboratorioModel.setBolsistas(new ArrayList<>());

        laboratorioDTO = new LaboratorioDTO();
        laboratorioDTO.setId(1L);
        laboratorioDTO.setNome("Lab 1");
        laboratorioDTO.setAreaDeAtuacao("Tecnologia");
        laboratorioDTO.setBolsistas(new ArrayList<>());
    }

    @Test
    void listarLaboratorios_DeveRetornarListaDeDTOs() {
        when(laboratorioRepository.findAll()).thenReturn(List.of(laboratorioModel));
        when(laboratorioMapper.map(any(LaboratorioModel.class))).thenReturn(laboratorioDTO);

        List<LaboratorioDTO> resultado = laboratorioService.listarLaboratorios();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Lab 1");
        verify(laboratorioRepository, times(1)).findAll();
    }

    @Test
    void listarLaboratorioPorId_QuandoIdExiste_DeveRetornarDTO() {
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorioModel));
        when(laboratorioMapper.map(laboratorioModel)).thenReturn(laboratorioDTO);

        LaboratorioDTO resultado = laboratorioService.listarLaboratorioPorId(1L);

        assertThat(resultado.getNome()).isEqualTo("Lab 1");
        verify(laboratorioRepository, times(1)).findById(1L);
    }

    @Test
    void listarLaboratorioPorId_QuandoIdNaoExiste_DeveLancarExcecao() {
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> laboratorioService.listarLaboratorioPorId(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void criarLaboratorio_QuandoNomeNaoExiste_DeveRetornarDTO() {
        when(laboratorioRepository.existsByNome(laboratorioDTO.getNome())).thenReturn(false);
        when(laboratorioMapper.map(laboratorioDTO)).thenReturn(laboratorioModel);
        when(laboratorioRepository.save(any(LaboratorioModel.class))).thenReturn(laboratorioModel);
        when(laboratorioMapper.map(laboratorioModel)).thenReturn(laboratorioDTO);

        LaboratorioDTO resultado = laboratorioService.criarLaboratorio(laboratorioDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Lab 1");
        verify(laboratorioRepository, times(1)).save(any(LaboratorioModel.class));
    }

    @Test
    void criarLaboratorio_QuandoNomeJaExiste_DeveLancarExcecao() {
        when(laboratorioRepository.existsByNome(laboratorioDTO.getNome())).thenReturn(true);

        assertThatThrownBy(() -> laboratorioService.criarLaboratorio(laboratorioDTO))
                .isInstanceOf(DataConflictException.class);
    }

    @Test
    void alterarLaboratorio_QuandoIdExiste_DeveRetornarDTOAtualizado() {
        LaboratorioDTO dtoAtualizado = new LaboratorioDTO();
        dtoAtualizado.setNome("Lab Atualizado");

        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorioModel));
        when(laboratorioRepository.save(any(LaboratorioModel.class))).thenReturn(laboratorioModel);
        when(laboratorioMapper.map(laboratorioModel)).thenReturn(dtoAtualizado);

        LaboratorioDTO resultado = laboratorioService.alterarLaboratorio(1L, dtoAtualizado);

        assertThat(resultado.getNome()).isEqualTo("Lab Atualizado");
        verify(laboratorioRepository, times(1)).save(any(LaboratorioModel.class));
    }

    @Test
    void alterarLaboratorio_QuandoIdNaoExiste_DeveLancarExcecao() {
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> laboratorioService.alterarLaboratorio(1L, laboratorioDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deletarLaboratorio_QuandoIdExisteESemBolsistas_DeveDeletar() {
        laboratorioModel.setBolsistas(Collections.emptyList());
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorioModel));

        laboratorioService.deletarLaboratorio(1L);

        verify(laboratorioRepository, times(1)).delete(laboratorioModel);
    }

    @Test
    void deletarLaboratorio_QuandoIdNaoExiste_DeveLancarExcecao() {
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> laboratorioService.deletarLaboratorio(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deletarLaboratorio_QuandoPossuiBolsistas_DeveLancarExcecao() {
        laboratorioModel.setBolsistas(List.of(new dev.matheus.CadastroDeBolsistas.Bolsistas.Models.BolsistaModel()));
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorioModel));

        assertThatThrownBy(() -> laboratorioService.deletarLaboratorio(1L))
                .isInstanceOf(DataConflictException.class);
    }
}
