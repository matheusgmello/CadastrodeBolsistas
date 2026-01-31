package dev.matheus.CadastroDeBolsistas.Laboratorios.Controllers;

import dev.matheus.CadastroDeBolsistas.Laboratorios.DTOs.LaboratorioDTO;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Services.LaboratorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/laboratorios")
@RequiredArgsConstructor
@Tag(name = "Laboratórios", description = "Endpoints para gerenciamento de laboratórios acadêmicos")
public class LaboratorioController {

    private final LaboratorioService laboratorioService;

    @Operation(summary = "Listar laboratórios", description = "Retorna uma lista de todos os laboratórios cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de laboratórios retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<LaboratorioDTO>> listarLaboratorios() {
        return ResponseEntity.ok(laboratorioService.listarLaboratorios());
    }

    @Operation(summary = "Obter laboratório por ID", description = "Busca os detalhes de um laboratório específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laboratório encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Laboratório não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LaboratorioDTO> listarLaboratorioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(laboratorioService.listarLaboratorioPorId(id));
    }

    @Operation(summary = "Criar novo laboratório", description = "Cadastra um novo laboratório no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Laboratório criado com sucesso",
                    content = @Content(schema = @Schema(implementation = LaboratorioDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito: Laboratório com este nome já existe")
    })
    @PostMapping
    public ResponseEntity<LaboratorioDTO> criarLaboratorio(@RequestBody LaboratorioDTO laboratorioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(laboratorioService.criarLaboratorio(laboratorioDTO));
    }

    @Operation(summary = "Atualizar laboratório", description = "Atualiza os dados de um laboratório existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laboratório atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Laboratório não encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<LaboratorioDTO> alterarLaboratorio(@PathVariable Long id, @RequestBody LaboratorioDTO laboratorioDTO) {
        return ResponseEntity.ok(laboratorioService.alterarLaboratorio(id, laboratorioDTO));
    }

    @Operation(summary = "Deletar laboratório", description = "Remove um laboratório do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Laboratório deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Laboratório não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito: Laboratório possui bolsistas vinculados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLaboratorio(@PathVariable Long id) {
        laboratorioService.deletarLaboratorio(id);
        return ResponseEntity.noContent().build();
    }
}