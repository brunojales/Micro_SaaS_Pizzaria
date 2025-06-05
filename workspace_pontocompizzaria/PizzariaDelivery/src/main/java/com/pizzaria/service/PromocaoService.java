package com.pizzaria.service;

import com.pizzaria.model.Promocao;
import com.pizzaria.repository.PromocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PromocaoService {
    
    @Autowired
    private PromocaoRepository promocaoRepository;

    public List<Promocao> listarTodas() {
        return promocaoRepository.findAll();
    }

    public List<Promocao> listarAtivas() {
        return promocaoRepository.findByAtivaTrue();
    }

    public List<Promocao> listarPromocoesValidas() {
        return promocaoRepository.findPromocoesValidas(LocalDateTime.now());
    }

    public Optional<Promocao> buscarPorId(Long id) {
        return promocaoRepository.findById(id);
    }

    public Optional<Promocao> buscarPorCodigo(String codigo) {
        return promocaoRepository.findByCodigoPromocionalAndAtivaTrue(codigo);
    }

    public Promocao salvar(Promocao promocao) {
        return promocaoRepository.save(promocao);
    }

    public void deletar(Long id) {
        promocaoRepository.deleteById(id);
    }

    public void ativarDesativar(Long id) {
        Optional<Promocao> promocao = buscarPorId(id);
        if (promocao.isPresent()) {
            promocao.get().setAtiva(!promocao.get().getAtiva());
            salvar(promocao.get());
        }
    }
}