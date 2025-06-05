package com.pizzaria.controller;

import com.pizzaria.model.*;
import com.pizzaria.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private PizzaService pizzaService;
    
    @Autowired
    private PromocaoService promocaoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", pedidoService.listarPedidosAtivos());
        return "pedidos/lista";
    }

    @GetMapping("/hoje")
    public String pedidosHoje(Model model) {
        model.addAttribute("pedidos", pedidoService.listarPedidosHoje());
        return "pedidos/hoje";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("pizzas", pizzaService.listarAtivas());
        return "pedidos/form";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Optional<Pedido> pedido = pedidoService.buscarPorId(id);
        if (pedido.isPresent()) {
            model.addAttribute("pedido", pedido.get());
            return "pedidos/detalhes";
        }
        return "redirect:/pedidos";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Pedido pedido, BindingResult result,
                        @RequestParam(required = false) String aplicarPromocao,
                        @RequestParam(required = false) String codigoPromocao,
                        RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "pedidos/form";
        }
        
        // Calcular taxa de entrega se for delivery
        if (pedido.getTipo() == Pedido.TipoPedido.ENTREGA) {
            BigDecimal taxa = pedidoService.calcularTaxaEntrega(pedido.getEnderecoEntrega());
            pedido.setTaxaEntrega(taxa);
        }
        
        Pedido pedidoSalvo = pedidoService.criarPedido(pedido);
        
        // Aplicar promoção se informada
        if (aplicarPromocao != null && codigoPromocao != null && !codigoPromocao.isEmpty()) {
            pedidoService.aplicarPromocao(pedidoSalvo.getId(), codigoPromocao);
        }
        
        redirectAttributes.addFlashAttribute("mensagem", "Pedido criado com sucesso!");
        return "redirect:/pedidos/detalhes/" + pedidoSalvo.getId();
    }

    @PostMapping("/atualizar-status/{id}")
    public String atualizarStatus(@PathVariable Long id, 
                                 @RequestParam Pedido.StatusPedido status,
                                 RedirectAttributes redirectAttributes) {
        pedidoService.atualizarStatus(id, status);
        redirectAttributes.addFlashAttribute("mensagem", "Status atualizado com sucesso!");
        return "redirect:/pedidos";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id, 
                          @RequestParam String motivo,
                          RedirectAttributes redirectAttributes) {
        pedidoService.cancelarPedido(id, motivo);
        redirectAttributes.addFlashAttribute("mensagem", "Pedido cancelado com sucesso!");
        return "redirect:/pedidos";
    }

    @GetMapping("/cliente/{clienteId}")
    public String pedidosCliente(@PathVariable Long clienteId, Model model) {
        Optional<Cliente> cliente = clienteService.buscarPorId(clienteId);
        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            model.addAttribute("pedidos", pedidoService.buscarPorCliente(clienteId));
            return "pedidos/cliente";
        }
        return "redirect:/clientes";
    }
}