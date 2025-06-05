package com.pizzaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @NotNull(message = "Cliente é obrigatório")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedido> itens;

    @Column(name = "valor_total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private TipoPedido tipo = TipoPedido.RETIRADA;

    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.PENDENTE;

    @Column(name = "data_pedido")
    private LocalDateTime dataPedido = LocalDateTime.now();

    private String observacoes;

    @Column(name = "endereco_entrega")
    private String enderecoEntrega;

    @Column(name = "taxa_entrega")
    private BigDecimal taxaEntrega = BigDecimal.ZERO;

    @Column(name = "pontos_utilizados")
    private Integer pontosUtilizados = 0;

    @Column(name = "pizza_gratuita")
    private Boolean pizzaGratuita = false;

    public enum TipoPedido {
        RETIRADA, ENTREGA
    }

    public enum StatusPedido {
        PENDENTE, CONFIRMADO, PREPARANDO, PRONTO, SAIU_ENTREGA, ENTREGUE, CANCELADO
    }

    // Construtores
    public Pedido() {}

    public Pedido(Cliente cliente, TipoPedido tipo) {
        this.cliente = cliente;
        this.tipo = tipo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public TipoPedido getTipo() { return tipo; }
    public void setTipo(TipoPedido tipo) { this.tipo = tipo; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(String enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public BigDecimal getTaxaEntrega() { return taxaEntrega; }
    public void setTaxaEntrega(BigDecimal taxaEntrega) { this.taxaEntrega = taxaEntrega; }

    public Integer getPontosUtilizados() { return pontosUtilizados; }
    public void setPontosUtilizados(Integer pontosUtilizados) { this.pontosUtilizados = pontosUtilizados; }

    public Boolean getPizzaGratuita() { return pizzaGratuita; }
    public void setPizzaGratuita(Boolean pizzaGratuita) { this.pizzaGratuita = pizzaGratuita; }

    // Métodos de negócio
    public void calcularTotal() {
        this.valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(taxaEntrega);
    }

    public void adicionarPontosCliente() {
        int pontos = valorTotal.intValue() / 10; // 1 ponto a cada R$ 10,00
        cliente.adicionarPontos(pontos);
    }
}