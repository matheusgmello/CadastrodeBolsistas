package dev.matheus.CadastroDeBolsistas.Bolsistas.Controllers;

import dev.matheus.CadastroDeBolsistas.Bolsistas.DTOs.BolsistaDTO;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Services.BolsistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bolsistas")
@RequiredArgsConstructor
@Tag(name = "Bolsistas", description = "Endpoints para gerenciamento de bolsistas acadêmicos")
public class BolsistaController {

    private final BolsistaService bolsistaService;

    @Operation(summary = "Listar bolsistas", description = "Retorna a lista completa de bolsistas e seus respectivos laboratórios")
    @ApiResponse(responseCode = "200", description = "Lista de bolsistas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<BolsistaDTO>> listaBolsistas() {
        return ResponseEntity.ok(bolsistaService.listaBolsistas());
    }

    @Operation(summary = "Obter bolsista por ID", description = "Retorna os detalhes de um bolsista específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bolsista encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Bolsista não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BolsistaDTO> listaBolsistaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bolsistaService.listaBolsistaPorId(id));
    }

    @Operation(summary = "Cadastrar bolsista", description = "Cria um novo bolsista e o vincula a um laboratório")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bolsista cadastrado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Conflito: Email já cadastrado")
    })
    @PostMapping
    public ResponseEntity<BolsistaDTO> criarBolsista(@RequestBody BolsistaDTO bolsistaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bolsistaService.criarBolsista(bolsistaDTO));
    }

    @Operation(summary = "Atualizar bolsista", description = "Altera os dados de um bolsista ou troca seu laboratório")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bolsista atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Bolsista ou Laboratório não encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<BolsistaDTO> alterarBolsista(@PathVariable Long id, @RequestBody BolsistaDTO bolsistaDTO) {
        return ResponseEntity.ok(bolsistaService.alterarBolsista(id, bolsistaDTO));
    }

    @Operation(summary = "Deletar bolsista", description = "Remove o registro de um bolsista do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bolsista removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Bolsista não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBolsista(@PathVariable Long id) {
        bolsistaService.deletarBolsista(id);
        return ResponseEntity.noContent().build();
    }
}