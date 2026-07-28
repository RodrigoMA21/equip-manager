package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.enums.RegiaoEntrega;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PrevisaoEntregaService {

    private final RegiaoService regiaoService;

    public PrevisaoEntregaService(RegiaoService regiaoService) {
        this.regiaoService = regiaoService;
    }

    public LocalDate calcularPrevisaoEntrega(String uf) {
        RegiaoEntrega regiao = regiaoService.obterRegiaoPorUf(uf);
        return LocalDate.now().plusDays(regiao.getDiasEstimados());
    }
}