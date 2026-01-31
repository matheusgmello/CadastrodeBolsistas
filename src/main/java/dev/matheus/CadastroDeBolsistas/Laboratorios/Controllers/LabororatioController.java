package dev.matheus.CadastroDeBolsistas.Laboratorios.Controllers;

import dev.matheus.CadastroDeBolsistas.Laboratorios.DTOs.LaboratorioDTO;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Services.LaboratorioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/laboratorios")
public class LabororatioController {

    private final LaboratorioService laboratorioService;

    public LabororatioController(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }

    @GetMapping()
    public ResponseEntity<List<LaboratorioDTO>> listarLaboratorios() {
        List<LaboratorioDTO> laboratorios = laboratorioService.listarLaboratorios();
        return ResponseEntity.ok(laboratorios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarLaboratorioPorId(@PathVariable Long id) {
        LaboratorioDTO laboratorioDTO = laboratorioService.listarLaboratorioPorId(id);
        if (laboratorioDTO != null) {
            return ResponseEntity.ok(laboratorioDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A Missao com ID " + id + " não foi encontrada.");
        }
    }

    @PostMapping()
    public ResponseEntity<LaboratorioDTO> criarLaboratorio(@RequestBody LaboratorioDTO laboratorioDTO) {
        LaboratorioDTO novoLaboratorio = laboratorioService.criarLaboratorio(laboratorioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLaboratorio);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> alterarLaboratorio(@PathVariable Long id, @RequestBody LaboratorioDTO laboratorioDTO) {
        LaboratorioDTO laboratorioAtualizado = laboratorioService.alterarLaboratorio(id, laboratorioDTO);
        if (laboratorioAtualizado != null) {
            return ResponseEntity.ok(laboratorioAtualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("O Laboratório com ID " + id + " não foi encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarLaboratorio(@PathVariable Long id) {
        if (laboratorioService.listarLaboratorioPorId(id) != null) {
            laboratorioService.deletarLaboratorio(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("O Laboratorio com ID " + id + " foi deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A Laboratorio com ID " + id + " não foi encontrado.");
        }
    }

}
