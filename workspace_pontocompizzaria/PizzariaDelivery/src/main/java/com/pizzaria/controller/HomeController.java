package com.pizzaria.controller;

import com.pizzaria.service.PizzaService;
import com.pizzaria.service.PromocaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Autowired
    private PizzaService pizzaService;
    
    @Autowired
    private PromocaoService promocaoService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pizzas", pizzaService.listarAtivas());
        model.addAttribute("promocoes", promocaoService.listarPromocoesValidas());
        model.addAttribute("categorias", pizzaService.listarCategorias());
        return "index";
    }

    @GetMapping("/cardapio")
    public String cardapio(Model model) {
        model.addAttribute("pizzas", pizzaService.listarAtivas());
        model.addAttribute("categorias", pizzaService.listarCategorias());
        return "cardapio";
    }

    @GetMapping("/promocoes")
    public String promocoes(Model model) {
        model.addAttribute("promocoes", promocaoService.listarPromocoesValidas());
        return "promocoes";
    }
}