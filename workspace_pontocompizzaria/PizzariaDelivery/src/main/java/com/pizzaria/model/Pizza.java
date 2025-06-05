package com.pizzaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "pizzas")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    @Column(nullable = false)
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    private TamanhoPizza tamanho = TamanhoPizza.MEDIA;

    private String categoria;
    private Boolean ativa = true;
    private String urlImagem;

    public enum TamanhoPizza {
        PEQUENA, MEDIA, GRANDE, FAMILIA
    }

    // Construtores
    public Pizza() {}

    public Pizza(String nome, String descricao, BigDecimal preco, TamanhoPizza tamanho, String categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.tamanho = tamanho;
        this.categoria = categoria;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public TamanhoPizza getTamanho() { return tamanho; }
    public void setTamanho(TamanhoPizza tamanho) { this.tamanho = tamanho; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }

    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }
}