package dev.matheus.CadastroDeBolsistas.Bolsistas.Controllers;

import tools.jackson.databind.ObjectMapper;
import dev.matheus.CadastroDeBolsistas.Bolsistas.DTOs.BolsistaDTO;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Enums.NivelBolsa;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Models.BolsistaModel;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Repositories.BolsistaRepository;
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
public class BolsistaControllerIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BolsistaRepository bolsistaRepository;

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private LaboratorioModel lab;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        bolsistaRepository.deleteAll();
        laboratorioRepository.deleteAll();

        lab = new LaboratorioModel();
        lab.setNome("Lab Teste");
        lab.setAreaDeAtuacao("Informatica");
        lab = laboratorioRepository.save(lab);
    }

    @Test
    void listaBolsistas_DeveRetornarListaVazia() throws Exception {
        mockMvc.perform(get("/api/bolsistas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void criarBolsista_DeveRetornarStatusCriado() throws Exception {
        BolsistaDTO dto = new BolsistaDTO();
        dto.setNome("Bolsista Novo");
        dto.setEmail("novo@email.com");
        dto.setIdade(22);
        dto.setNivelBolsa(NivelBolsa.MESTRADO);
        dto.setLaboratorio(lab);

        mockMvc.perform(post("/api/bolsistas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Bolsista Novo"))
                .andExpect(jsonPath("$.email").value("novo@email.com"));
    }

    @Test
    void listaBolsistaPorId_QuandoExiste_DeveRetornarBolsista() throws Exception {
        BolsistaModel bolsista = new BolsistaModel();
        bolsista.setNome("Bolsista Existente");
        bolsista.setEmail("existente@email.com");
        bolsista.setIdade(25);
        bolsista.setNivelBolsa(NivelBolsa.DOUTORADO);
        bolsista.setLaboratorio(lab);
        bolsista = bolsistaRepository.save(bolsista);

        mockMvc.perform(get("/api/bolsistas/" + bolsista.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Bolsista Existente"));
    }

    @Test
    void deletarBolsista_DeveRetornarNoContent() throws Exception {
        BolsistaModel bolsista = new BolsistaModel();
        bolsista.setNome("Para Deletar");
        bolsista.setEmail("deletar@email.com");
        bolsista.setLaboratorio(lab);
        bolsista = bolsistaRepository.save(bolsista);

        mockMvc.perform(delete("/api/bolsistas/" + bolsista.getId()))
                .andExpect(status().isNoContent());
    }
}
