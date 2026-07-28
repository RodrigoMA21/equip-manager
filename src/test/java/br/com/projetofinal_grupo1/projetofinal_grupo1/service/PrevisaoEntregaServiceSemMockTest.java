package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.enums.RegiaoEntrega;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PrevisaoEntregaServiceSemMockTest {

    @Test
    void calcularPrevisaoEntrega_ComUfValida_DeveRetornarDataCorreta() {

        RegiaoService regiaoService = new RegiaoService();
        PrevisaoEntregaService service = new PrevisaoEntregaService(regiaoService);
        String uf = "BA";
        int diasEstimados = RegiaoEntrega.NORDESTE.getDiasEstimados();


        LocalDate resultado = service.calcularPrevisaoEntrega(uf);


        assertEquals(LocalDate.now().plusDays(diasEstimados), resultado);
    }

    @Test
    void calcularPrevisaoEntrega_ComUfInvalida_DeveLancarExcecao() {
        RegiaoService regiaoService = new RegiaoService();
        PrevisaoEntregaService service = new PrevisaoEntregaService(regiaoService);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> service.calcularPrevisaoEntrega("ZZ")
        );

        assertEquals("UF desconhecida: ZZ", excecao.getMessage());
    }

    @Test
    void calcularPrevisaoEntrega_ComUfVazia_DeveLancarExcecao() {
        RegiaoService regiaoService = new RegiaoService();
        PrevisaoEntregaService service = new PrevisaoEntregaService(regiaoService);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> service.calcularPrevisaoEntrega(" ")
        );

        assertEquals("UF não pode ser nula ou vazia", excecao.getMessage());
    }
}