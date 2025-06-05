package com.pizzaria.repository;

import com.pizzaria.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    List<Pizza> findByAtivaTrue();
    List<Pizza> findByCategoriaAndAtivaTrue(String categoria);
    List<Pizza> findByNomeContainingIgnoreCaseAndAtivaTrue(String nome);
    
    @Query("SELECT DISTINCT p.categoria FROM Pizza p WHERE p.ativa = true")
    List<String> findCategorias();
}