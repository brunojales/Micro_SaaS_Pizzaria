package com.pizzaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promocoes")
public class Promocao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descricao;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @Column(name = "desconto_percentual")
    private BigDecimal descontoPercentual;

    @Column(name = "desconto_valor")
    private BigDecimal descontoValor;

    @Column(name = "valor_minimo")
    private BigDecimal valorMinimo;

    @Column(name = "codigo_promocional")
    private String codigoPromocional;

    private Boolean ativa = true;

    @Column(name = "url_imagem")
    private String urlImagem;

    @Enumerated(EnumType.STRING)
    private TipoPromocao tipo = TipoPromocao.DESCONTO_PERCENTUAL;

    public enum TipoPromocao {
        DESCONTO_PERCENTUAL, DESCONTO_VALOR, PIZZA_GRATUITA, FRETE_GRATIS
    }

    // Construtores
    public Promocao() {}

    public Promocao(String titulo, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, TipoPromocao tipo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.tipo = tipo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }

    public BigDecimal getDescontoPercentual() { return descontoPercentual; }
    public void setDescontoPercentual(BigDecimal descontoPercentual) { this.descontoPercentual = descontoPercentual; }

    public BigDecimal getDescontoValor() { return descontoValor; }
    public void setDescontoValor(BigDecimal descontoValor) { this.descontoValor = descontoValor; }

    public BigDecimal getValorMinimo() { return valorMinimo; }
    public void setValorMinimo(BigDecimal valorMinimo) { this.valorMinimo = valorMinimo; }

    public String getCodigoPromocional() { return codigoPromocional; }
    public void setCodigoPromocional(String codigoPromocional) { this.codigoPromocional = codigoPromocional; }

    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }

    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }

    public TipoPromocao getTipo() { return tipo; }
    public void setTipo(TipoPromocao tipo) { this.tipo = tipo; }

    // Métodos de negócio
    public boolean isValida() {
        LocalDateTime agora = LocalDateTime.now();
        return ativa && agora.isAfter(dataInicio) && agora.isBefore(dataFim);
    }

    public boolean podeAplicar(BigDecimal valorPedido) {
        return isValida() && (valorMinimo == null || valorPedido.compareTo(valorMinimo) >= 0);
    }

    public BigDecimal calcularDesconto(BigDecimal valorPedido) {
        if (!podeAplicar(valorPedido)) {
            return BigDecimal.ZERO;
        }

        switch (tipo) {
            case DESCONTO_PERCENTUAL:
                return valorPedido.multiply(descontoPercentual).divide(BigDecimal.valueOf(100));
            case DESCONTO_VALOR:
                return descontoValor;
            default:
                return BigDecimal.ZERO;
        }
    }
}