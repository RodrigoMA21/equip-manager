package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.enums.RegiaoEntrega;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RegiaoService {

    private static final Set<String> SUDESTE = Set.of("MG", "SP", "RJ", "ES");
    private static final Set<String> SUL = Set.of("PR", "SC", "RS");
    private static final Set<String> CENTRO_OESTE = Set.of("GO", "MT", "MS", "DF");
    private static final Set<String> NORDESTE = Set.of("BA", "PE", "CE", "MA", "PB", "RN", "PI", "AL", "SE");
    private static final Set<String> NORTE = Set.of("AM", "PA", "AP", "RO", "RR", "TO", "AC");

    public RegiaoEntrega obterRegiaoPorUf(String uf) {
        if (uf == null || uf.isBlank()) {
            throw new IllegalArgumentException("UF não pode ser nula ou vazia");
        }

        uf = uf.toUpperCase();

        if (SUDESTE.contains(uf)) return RegiaoEntrega.SUDESTE;
        if (SUL.contains(uf)) return RegiaoEntrega.SUL;
        if (CENTRO_OESTE.contains(uf)) return RegiaoEntrega.CENTRO_OESTE;
        if (NORDESTE.contains(uf)) return RegiaoEntrega.NORDESTE;
        if (NORTE.contains(uf)) return RegiaoEntrega.NORTE;

        throw new IllegalArgumentException("UF desconhecida: " + uf);
    }
}