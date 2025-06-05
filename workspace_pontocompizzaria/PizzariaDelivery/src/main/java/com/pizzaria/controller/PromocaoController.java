package com.pizzaria.controller;

import com.pizzaria.model.Promocao;
import com.pizzaria.service.PromocaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/promocoes")
public class PromocaoController {
    
    @Autowired
    private PromocaoService promocaoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("promocoes", promocaoService.listarTodas());
        return "admin/promocoes/lista";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("promocao", new Promocao());
        return "admin/promocoes/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Promocao> promocao = promocaoService.buscarPorId(id);
        if (promocao.isPresent()) {
            model.addAttribute("promocao", promocao.get());
            return "admin/promocoes/form";
        }
        return "redirect:/admin/promocoes";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Promocao promocao, BindingResult result,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/promocoes/form";
        }
        
        promocaoService.salvar(promocao);
        redirectAttributes.addFlashAttribute("mensagem", "Promoção salva com sucesso!");
        return "redirect:/admin/promocoes";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        promocaoService.deletar(id);
        redirectAttributes.addFlashAttribute("mensagem", "Promoção deletada com sucesso!");
        return "redirect:/admin/promocoes";
    }

    @PostMapping("/ativar-desativar/{id}")
    @ResponseBody
    public String ativarDesativar(@PathVariable Long id) {
        promocaoService.ativarDesativar(id);
        return "success";
    }
}