package dev.matheus.CadastroDeBolsistas.Laboratorios.Controllers;

import tools.jackson.databind.ObjectMapper;
import dev.matheus.CadastroDeBolsistas.Laboratorios.DTOs.LaboratorioDTO;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Models.LaboratorioModel;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Repositories.LaboratorioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class LaboratorioControllerIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        laboratorioRepository.deleteAll();
    }

    @Test
    void listarLaboratorios_DeveRetornarListaVazia() throws Exception {
        mockMvc.perform(get("/api/laboratorios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void criarLaboratorio_DeveRetornarStatusCriado() throws Exception {
        LaboratorioDTO dto = new LaboratorioDTO();
        dto.setNome("Novo Laboratório");
        dto.setAreaDeAtuacao("Pesquisa");

        mockMvc.perform(post("/api/laboratorios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Novo Laboratório"));
    }

    @Test
    void listarLaboratorioPorId_QuandoExiste_DeveRetornarLaboratorio() throws Exception {
        LaboratorioModel lab = new LaboratorioModel();
        lab.setNome("Lab Existente");
        lab.setAreaDeAtuacao("Bio");
        lab = laboratorioRepository.save(lab);

        mockMvc.perform(get("/api/laboratorios/" + lab.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Lab Existente"));
    }

    @Test
    void listarLaboratorioPorId_QuandoNaoExiste_DeveRetornar404() throws Exception {
        mockMvc.perform(get("/api/laboratorios/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarLaboratorio_DeveRetornarNoContent() throws Exception {
        LaboratorioModel lab = new LaboratorioModel();
        lab.setNome("Lab para deletar");
        lab = laboratorioRepository.save(lab);

        mockMvc.perform(delete("/api/laboratorios/" + lab.getId()))
                .andExpect(status().isNoContent());
    }
}
