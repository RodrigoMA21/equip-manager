package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.enums.RegiaoEntrega;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PrevisaoEntregaServiceTest {

    private RegiaoService regiaoService;
    private PrevisaoEntregaService previsaoEntregaService;

    @BeforeEach
    void setUp() {
        regiaoService = mock(RegiaoService.class);
        previsaoEntregaService = new PrevisaoEntregaService(regiaoService);
    }

    @Test
    void calcularPrevisaoEntrega_DeveRetornarDataCorreta_ParaUFDoSudeste() {
        // Arrange
        String uf = "SP";
        RegiaoEntrega regiaoSudeste = RegiaoEntrega.SUDESTE;
        when(regiaoService.obterRegiaoPorUf(uf)).thenReturn(regiaoSudeste);


        LocalDate dataEsperada = LocalDate.now().plusDays(regiaoSudeste.getDiasEstimados());
        LocalDate resultado = previsaoEntregaService.calcularPrevisaoEntrega(uf);


        assertEquals(dataEsperada, resultado);
        verify(regiaoService, times(1)).obterRegiaoPorUf(uf);
    }

    @Test
    void calcularPrevisaoEntrega_DeveRetornarDataCorreta_ParaUFDoNordeste() {

        String uf = "BA"; // Bahia
        RegiaoEntrega regiaoNordeste = RegiaoEntrega.NORDESTE;
        when(regiaoService.obterRegiaoPorUf(uf)).thenReturn(regiaoNordeste);


        LocalDate dataEsperada = LocalDate.now().plusDays(regiaoNordeste.getDiasEstimados());
        LocalDate resultado = previsaoEntregaService.calcularPrevisaoEntrega(uf);


        assertEquals(dataEsperada, resultado);
        verify(regiaoService).obterRegiaoPorUf(uf);
    }
}
