package com.pizzaria.service;

import com.pizzaria.model.*;
import com.pizzaria.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private PromocaoService promocaoService;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPedidosAtivos() {
        return pedidoRepository.findPedidosAtivos();
    }

    public List<Pedido> listarPedidosHoje() {
        return pedidoRepository.findPedidosHoje();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> buscarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> buscarPorStatus(Pedido.StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }

    public Pedido salvar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido criarPedido(Pedido pedido) {
        // Calcular total do pedido
        pedido.calcularTotal();
        
        // Salvar pedido
        Pedido pedidoSalvo = salvar(pedido);
        
        // Adicionar pontos ao cliente (se não for pizza gratuita)
        if (!pedido.getPizzaGratuita()) {
            pedido.adicionarPontosCliente();
            clienteService.salvar(pedido.getCliente());
        }
        
        return pedidoSalvo;
    }

    public void atualizarStatus(Long pedidoId, Pedido.StatusPedido novoStatus) {
        Optional<Pedido> pedido = buscarPorId(pedidoId);
        if (pedido.isPresent()) {
            pedido.get().setStatus(novoStatus);
            salvar(pedido.get());
        }
    }

    public void cancelarPedido(Long pedidoId, String motivo) {
        Optional<Pedido> pedido = buscarPorId(pedidoId);
        if (pedido.isPresent()) {
            pedido.get().setStatus(Pedido.StatusPedido.CANCELADO);
            pedido.get().setObservacoes(pedido.get().getObservacoes() + " - CANCELADO: " + motivo);
            salvar(pedido.get());
        }
    }

    public BigDecimal calcularTaxaEntrega(String endereco) {
        // Lógica simples para taxa de entrega - pode ser expandida
        return new BigDecimal("5.00");
    }

    public Pedido aplicarPromocao(Long pedidoId, String codigoPromocao) {
        Optional<Pedido> pedidoOpt = buscarPorId(pedidoId);
        Optional<Promocao> promocaoOpt = promocaoService.buscarPorCodigo(codigoPromocao);
        
        if (pedidoOpt.isPresent() && promocaoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            Promocao promocao = promocaoOpt.get();
            
            if (promocao.podeAplicar(pedido.getValorTotal())) {
                BigDecimal desconto = promocao.calcularDesconto(pedido.getValorTotal());
                pedido.setValorTotal(pedido.getValorTotal().subtract(desconto));
                return salvar(pedido);
            }
        }
        
        return pedidoOpt.orElse(null);
    }

    public boolean pedidoComPizzaGratuita(Long pedidoId, Long clienteId) {
        if (clienteService.resgatarPizza(clienteId)) {
            Optional<Pedido> pedido = buscarPorId(pedidoId);
            if (pedido.isPresent()) {
                pedido.get().setPizzaGratuita(true);
                pedido.get().setPontosUtilizados(100);
                salvar(pedido.get());
                return true;
            }
        }
        return false;
    }
}