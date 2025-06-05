package com.pizzaria.controller.api;

import com.pizzaria.model.*;
import com.pizzaria.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoApiController {

    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private PizzaService pizzaService;

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/ativos")
    public List<Pedido> listarAtivos() {
        return pedidoService.listarPedidosAtivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.buscarPorId(id);
        return pedido.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody PedidoRequest request) {
        Optional<Cliente> cliente = clienteService.buscarPorId(request.getClienteId());
        if (cliente.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Pedido pedido = pedidoService.criarPedido(cliente.get(), request.getTipo());
        Pedido pedidoSalvo = pedidoService.salvar(pedido);
        return ResponseEntity.ok(pedidoSalvo);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, 
                                                 @RequestBody StatusRequest request) {
        Optional<Pedido> pedido = pedidoService.buscarPorId(id);
        if (pedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidoService.atualizarStatus(id, request.getStatus());
        return ResponseEntity.ok(pedidoService.buscarPorId(id).get());
    }

    // Classes auxiliares para requests
    public static class PedidoRequest {
        private Long clienteId;
        private Pedido.TipoPedido tipo;
        private String observacoes;

        // Getters e Setters
        public Long getClienteId() { return clienteId; }
        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
        public Pedido.TipoPedido getTipo() { return tipo; }
        public void setTipo(Pedido.TipoPedido tipo) { this.tipo = tipo; }
        public String getObservacoes() { return observacoes; }
        public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    }

    public static class StatusRequest {
        private Pedido.StatusPedido status;

        public Pedido.StatusPedido getStatus() { return status; }
        public void setStatus(Pedido.StatusPedido status) { this.status = status; }
    }
}