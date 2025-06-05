package com.pizzaria.repository;

import com.pizzaria.model.Pedido;
import com.pizzaria.model.Pedido.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByStatus(StatusPedido status);
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);
    
    @Query("SELECT p FROM Pedido p WHERE p.status IN ('PENDENTE', 'CONFIRMADO', 'PREPARANDO', 'PRONTO', 'SAIU_ENTREGA') ORDER BY p.dataPedido ASC")
    List<Pedido> findPedidosAtivos();
    
    @Query("SELECT p FROM Pedido p WHERE DATE(p.dataPedido) = CURRENT_DATE ORDER BY p.dataPedido DESC")
    List<Pedido> findPedidosHoje();
}