package com.pizzaria.repository;

import com.pizzaria.model.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromocaoRepository extends JpaRepository<Promocao, Long> {
    @Query("SELECT p FROM Promocao p WHERE p.ativa = true AND p.dataInicio <= :agora AND p.dataFim >= :agora")
    List<Promocao> findPromocoesValidas(LocalDateTime agora);
    
    Optional<Promocao> findByCodigoPromocionalAndAtivaTrue(String codigo);
    
    List<Promocao> findByAtivaTrue();
}