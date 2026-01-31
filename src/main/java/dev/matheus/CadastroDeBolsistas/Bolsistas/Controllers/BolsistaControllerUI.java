package dev.matheus.CadastroDeBolsistas.Bolsistas.Controllers;

import dev.matheus.CadastroDeBolsistas.Bolsistas.DTOs.BolsistaDTO;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Enums.NivelBolsa;
import dev.matheus.CadastroDeBolsistas.Bolsistas.Services.BolsistaService;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Services.LaboratorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bolsistas/ui")
@RequiredArgsConstructor
public class BolsistaControllerUI {

    private final BolsistaService bolsistaService;
    private final LaboratorioService laboratorioService;

    @GetMapping("/listar")
    public String listarBolsistas(Model model) {
        model.addAttribute("bolsistas", bolsistaService.listaBolsistas());
        return "listarBolsistas";
    }

    @GetMapping("/adicionar")
    public String mostrarFormulario(Model model) {
        model.addAttribute("bolsista", new BolsistaDTO());
        model.addAttribute("laboratorios", laboratorioService.listarLaboratorios());
        model.addAttribute("niveis", NivelBolsa.values());
        return "adicionarBolsista";
    }

    @PostMapping("/salvar")
    public String salvarBolsista(@ModelAttribute BolsistaDTO bolsista, RedirectAttributes redirectAttributes) {
        try {
            bolsistaService.criarBolsista(bolsista);
            redirectAttributes.addFlashAttribute("mensagem", "Bolsista cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar: " + e.getMessage());
            return "redirect:/bolsistas/ui/adicionar";
        }
        return "redirect:/bolsistas/ui/listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("bolsista", bolsistaService.listaBolsistaPorId(id));
        model.addAttribute("laboratorios", laboratorioService.listarLaboratorios());
        model.addAttribute("niveis", NivelBolsa.values());
        return "editarBolsista";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute BolsistaDTO bolsista, RedirectAttributes redirectAttributes) {
        try {
            bolsista.setId(id);
            bolsistaService.alterarBolsista(id, bolsista);
            redirectAttributes.addFlashAttribute("mensagem", "Bolsista atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar: " + e.getMessage());
            return "redirect:/bolsistas/ui/editar/" + id;
        }
        return "redirect:/bolsistas/ui/listar";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("bolsista", bolsistaService.listaBolsistaPorId(id));
        return "visualizarBolsista";
    }

    @GetMapping("/excluir/{id}")
    public String confirmarExcluir(@PathVariable Long id, Model model) {
        model.addAttribute("bolsista", bolsistaService.listaBolsistaPorId(id));
        return "confirmarExcluirBolsista";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bolsistaService.deletarBolsista(id);
            redirectAttributes.addFlashAttribute("mensagem", "Bolsista excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/bolsistas/ui/listar";
    }

}