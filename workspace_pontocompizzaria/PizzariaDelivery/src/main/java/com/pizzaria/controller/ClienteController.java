package com.pizzaria.controller;

import com.pizzaria.model.Cliente;
import com.pizzaria.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Cliente> cliente = clienteService.buscarPorId(id);
        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            return "clientes/form";
        }
        return "redirect:/clientes";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Cliente cliente, BindingResult result, 
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "clientes/form";
        }
        
        // Verificar se telefone já existe
        if (clienteService.telefoneJaExiste(cliente.getTelefone(), cliente.getId())) {
            result.rejectValue("telefone", "telefone.exists", "Telefone já cadastrado");
            return "clientes/form";
        }
        
        clienteService.salvar(cliente);
        redirectAttributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
        return "redirect:/clientes";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clienteService.deletar(id);
        redirectAttributes.addFlashAttribute("mensagem", "Cliente deletado com sucesso!");
        return "redirect:/clientes";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam("termo") String termo, Model model) {
        model.addAttribute("clientes", clienteService.buscarPorNome(termo));
        model.addAttribute("termo", termo);
        return "clientes/lista";
    }

    @PostMapping("/resgatar-pizza/{id}")
    @ResponseBody
    public String resgatarPizza(@PathVariable Long id) {
        if (clienteService.resgatarPizza(id)) {
            return "success";
        }
        return "error";
    }
}