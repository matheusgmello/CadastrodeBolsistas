package dev.matheus.CadastroDeBolsistas.Bolsistas.Controllers;

import dev.matheus.CadastroDeBolsistas.Bolsistas.DTOs.BolsistaDTO;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Services.BolsistaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bolsistas")
public class BolsistaController {

    private final BolsistaService bolsistaService;

    public BolsistaController(BolsistaService bolsistaService) {
        this.bolsistaService = bolsistaService;
    }

    @GetMapping()
    public ResponseEntity<List<BolsistaDTO>> listarBolsistas() {
        List<BolsistaDTO> bolsistas = bolsistaService.listaBolsistas();
        return ResponseEntity.ok(bolsistas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarBolsistaPorId(@PathVariable Long id) {
        BolsistaDTO bolsistaDTO = bolsistaService.listaBolsistaPorId(id);
        if (bolsistaDTO != null) {
            return ResponseEntity.ok(bolsistaDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("O Bolsista com ID " + id + " não foi encontrado.");
        }
    }

    @PostMapping()
    public ResponseEntity<BolsistaDTO> criarBolsista(@RequestBody BolsistaDTO bolsistaDTO) {
        BolsistaDTO novoBolsista = bolsistaService.criarBolsista(bolsistaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoBolsista);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> alterarBolsista(@PathVariable Long id, @RequestBody BolsistaDTO bolsistaDTO) {
        BolsistaDTO bolsistaAtualizado = bolsistaService.alterarBolsista(id, bolsistaDTO);
        if (bolsistaAtualizado != null) {
            return ResponseEntity.ok(bolsistaAtualizado);
        } else {
            return ResponseEntity.status(404)
                    .body("O Bolsista com ID " + id + " não foi encontrado.");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarBolsista(@PathVariable Long id) {
        if (bolsistaService.listaBolsistaPorId(id) != null) {
            bolsistaService.deletarBolsista(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("O Bolsista com ID " + id + " foi deletado com sucesso.");
        } else {
            return ResponseEntity.status(404)
                    .body("O Bolsista com ID " + id + " não foi encontrado.");
        }
    }

}
