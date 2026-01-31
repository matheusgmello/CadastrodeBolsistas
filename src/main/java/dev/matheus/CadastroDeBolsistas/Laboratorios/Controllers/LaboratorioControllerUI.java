package dev.matheus.CadastroDeBolsistas.Laboratorios.Controllers;

import dev.matheus.CadastroDeBolsistas.Laboratorios.DTOs.LaboratorioDTO;
import dev.matheus.CadastroDeBolsistas.Laboratorios.Services.LaboratorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/laboratorios/ui")
@RequiredArgsConstructor
public class LaboratorioControllerUI {

    private final LaboratorioService laboratorioService;

    @GetMapping("/listar")
    public String listarLaboratorios(Model model) {
        model.addAttribute("laboratorios", laboratorioService.listarLaboratorios());
        return "listarLaboratorios";
    }

    @GetMapping("/adicionar")
    public String mostrarFormulario(Model model) {
        model.addAttribute("laboratorio", new LaboratorioDTO());
        return "adicionarLaboratorio";
    }

    @PostMapping("/salvar")
    public String salvarLaboratorio(LaboratorioDTO laboratorio, RedirectAttributes redirectAttributes) {
        try {
            laboratorioService.criarLaboratorio(laboratorio);
            redirectAttributes.addFlashAttribute("mensagem", "Laboratório cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar: " + e.getMessage());
            return "redirect:/laboratorios/ui/adicionar";
        }
        return "redirect:/laboratorios/ui/listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("laboratorio", laboratorioService.listarLaboratorioPorId(id));
        return "editarLaboratorio";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, LaboratorioDTO laboratorio, RedirectAttributes redirectAttributes) {
        try {
            laboratorio.setId(id);
            laboratorioService.alterarLaboratorio(id, laboratorio);
            redirectAttributes.addFlashAttribute("mensagem", "Laboratório atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar: " + e.getMessage());
            return "redirect:/laboratorios/ui/editar/" + id;
        }
        return "redirect:/laboratorios/ui/listar";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("laboratorio", laboratorioService.listarLaboratorioPorId(id));
        return "visualizarLaboratorio";
    }

    @GetMapping("/excluir/{id}")
    public String confirmarExcluir(@PathVariable Long id, Model model) {
        model.addAttribute("laboratorio", laboratorioService.listarLaboratorioPorId(id));
        return "confirmarExcluirLaboratorio";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            laboratorioService.deletarLaboratorio(id);
            redirectAttributes.addFlashAttribute("mensagem", "Laboratório excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/laboratorios/ui/listar";
    }
}
