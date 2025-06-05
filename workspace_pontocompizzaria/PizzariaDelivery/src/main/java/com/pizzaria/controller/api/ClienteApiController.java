package com.pizzaria.controller.api;

import com.pizzaria.model.Cliente;
import com.pizzaria.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteApiController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.buscarPorId(id);
        return cliente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/telefone/{telefone}")
    public ResponseEntity<Cliente> buscarPorTelefone(@PathVariable String telefone) {
        Optional<Cliente> cliente = clienteService.buscarPorTelefone(telefone);
        return cliente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> salvar(@RequestBody Cliente cliente) {
        if (clienteService.telefoneJaExiste(cliente.getTelefone(), cliente.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Cliente clienteSalvo = clienteService.salvar(cliente);
        return ResponseEntity.ok(clienteSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        if (!clienteService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        cliente.setId(id);
        Cliente clienteAtualizado = clienteService.salvar(cliente);
        return ResponseEntity.ok(clienteAtualizado);
    }
}