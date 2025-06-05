package com.pizzaria.service;

import com.pizzaria.model.Cliente;
import com.pizzaria.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorTelefone(String telefone) {
        return clienteRepository.findByTelefone(telefone);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<Cliente> clientesComPontosParaResgate() {
        return clienteRepository.findClientesComPontosParaResgate();
    }

    public boolean telefoneJaExiste(String telefone, Long excludeId) {
        Optional<Cliente> cliente = clienteRepository.findByTelefone(telefone);
        return cliente.isPresent() && !cliente.get().getId().equals(excludeId);
    }

    public void adicionarPontos(Long clienteId, Integer pontos) {
        Optional<Cliente> cliente = buscarPorId(clienteId);
        if (cliente.isPresent()) {
            cliente.get().adicionarPontos(pontos);
            salvar(cliente.get());
        }
    }

    public boolean resgatarPizza(Long clienteId) {
        Optional<Cliente> cliente = buscarPorId(clienteId);
        if (cliente.isPresent() && cliente.get().podeResgatar()) {
            cliente.get().resgatarPizza();
            salvar(cliente.get());
            return true;
        }
        return false;
    }
}