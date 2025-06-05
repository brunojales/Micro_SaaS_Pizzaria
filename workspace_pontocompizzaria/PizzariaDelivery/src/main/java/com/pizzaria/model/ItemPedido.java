package com.pizzaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @NotNull(message = "Pedido é obrigatório")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "pizza_id")
    @NotNull(message = "Pizza é obrigatória")
    private Pizza pizza;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;

    private BigDecimal subtotal;

    private String observacoes;

    // Construtores
    public ItemPedido() {}

    public ItemPedido(Pedido pedido, Pizza pizza, Integer quantidade) {
        this.pedido = pedido;
        this.pizza = pizza;
        this.quantidade = quantidade;
        this.precoUnitario = pizza.getPreco();
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Pizza getPizza() { return pizza; }
    public void setPizza(Pizza pizza) { this.pizza = pizza; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    // Métodos de negócio
    public void calcularSubtotal() {
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}