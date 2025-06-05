package com.pizzaria.service;

import com.pizzaria.model.Pizza;
import com.pizzaria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PizzaService {
    
    @Autowired
    private PizzaRepository pizzaRepository;

    public List<Pizza> listarTodas() {
        return pizzaRepository.findAll();
    }

    public List<Pizza> listarAtivas() {
        return pizzaRepository.findByAtivaTrue();
    }

    public Optional<Pizza> buscarPorId(Long id) {
        return pizzaRepository.findById(id);
    }

    public List<Pizza> buscarPorCategoria(String categoria) {
        return pizzaRepository.findByCategoriaAndAtivaTrue(categoria);
    }

    public List<Pizza> buscarPorNome(String nome) {
        return pizzaRepository.findByNomeContainingIgnoreCaseAndAtivaTrue(nome);
    }

    public List<String> listarCategorias() {
        return pizzaRepository.findCategorias();
    }

    public Pizza salvar(Pizza pizza) {
        return pizzaRepository.save(pizza);
    }

    public void deletar(Long id) {
        pizzaRepository.deleteById(id);
    }

    public void ativarDesativar(Long id) {
        Optional<Pizza> pizza = buscarPorId(id);
        if (pizza.isPresent()) {
            pizza.get().setAtiva(!pizza.get().getAtiva());
            salvar(pizza.get());
        }
    }
}
