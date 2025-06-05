package com.pizzaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(nullable = false)
    private String telefone;

    @Email(message = "Email deve ser válido")
    private String email;

    private String endereco;
    private String bairro;
    private String cidade;
    private String cep;

    @Column(name = "pontos_fidelidade")
    private Integer pontosFidelidade = 0;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    // Construtores
    public Cliente() {}

    public Cliente(String nome, String telefone, String email, String endereco, String bairro, String cidade, String cep) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public Integer getPontosFidelidade() { return pontosFidelidade; }
    public void setPontosFidelidade(Integer pontosFidelidade) { this.pontosFidelidade = pontosFidelidade; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }

    // Métodos de negócio
    public void adicionarPontos(Integer pontos) {
        this.pontosFidelidade += pontos;
    }

    public boolean podeResgatar() {
        return this.pontosFidelidade >= 100; // 100 pontos = 1 pizza grátis
    }

    public void resgatarPizza() {
        if (podeResgatar()) {
            this.pontosFidelidade -= 100;
        }
    }
}
