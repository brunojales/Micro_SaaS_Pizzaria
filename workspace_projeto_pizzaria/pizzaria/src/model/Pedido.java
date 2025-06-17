import java.util.List;

public class Pedido {
    private List<Pizza> pizzas;
    private String cliente;
    private String status;

    public Pedido(String cliente, List<Pizza> pizzas) {
        this.cliente = cliente;
        this.pizzas = pizzas;
        this.status = "Recebido";
    }

    public double calcularTotal() {
        return pizzas.stream().mapToDouble(Pizza::getPreco).sum();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getCliente() {
        return cliente;
    }
}