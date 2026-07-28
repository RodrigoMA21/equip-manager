package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Alerta;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Estoque;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.ParametroSistema;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.AlertaRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EstoqueRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.TipoEquipamentoRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.EstoqueAlertaService;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.EmailService;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.ParametroSistemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class EstoqueAlertaServiceTest {

    private TipoEquipamentoRepository tipoEquipamentoRepository;
    private EstoqueRepository estoqueRepository;
    private ParametroSistemaService parametroSistemaService;
    private AlertaRepository alertaRepository;
    private EmailService emailService;
    private EstoqueAlertaService estoqueAlertaService;

    @BeforeEach
    public void setup() {
        tipoEquipamentoRepository = mock(TipoEquipamentoRepository.class);
        estoqueRepository = mock(EstoqueRepository.class);
        parametroSistemaService = mock(ParametroSistemaService.class);
        alertaRepository = mock(AlertaRepository.class);
        emailService = mock(EmailService.class);

        estoqueAlertaService = new EstoqueAlertaService();
        estoqueAlertaService.tipoEquipamentoRepository = tipoEquipamentoRepository;
        estoqueAlertaService.estoqueRepository = estoqueRepository;
        estoqueAlertaService.parametroSistemaService = parametroSistemaService;
        estoqueAlertaService.alertaRepository = alertaRepository;
        estoqueAlertaService.emailService = emailService;
    }

    @Test
    void verificarEstoqueCritico_deveCriarAlertaEEnviarEmail_quandoEstoqueAbaixoDoMinimo() {
        // Arrange
        TipoEquipamento tipoEquipamento = new TipoEquipamento();
        tipoEquipamento.setNomeTipo("Notebook");

        Estoque estoque = new Estoque();
        estoque.setQuantidadeDisponivel(3); // abaixo do mínimo

        ParametroSistema parametroSistema = new ParametroSistema();
        parametroSistema.setEstoqueMinimoSeguranca(5);

        when(parametroSistemaService.getLatestParametroSistema()).thenReturn(Optional.of(parametroSistema));
        when(tipoEquipamentoRepository.findAll()).thenReturn(Collections.singletonList(tipoEquipamento));
        when(estoqueRepository.findByTipoEquipamento(tipoEquipamento)).thenReturn(Collections.singletonList(estoque));
        when(alertaRepository.findTopByTipoEquipamentoAndStatusInOrderByIdDesc(eq(tipoEquipamento), anyList()))
                .thenReturn(Optional.empty());

        // Act
        estoqueAlertaService.verificarEstoqueCritico();

        // Assert
        verify(alertaRepository, times(1)).save(any(Alerta.class));
        verify(emailService, times(1)).enviarAlertaEstoqueBaixo(
                eq("admin@example.com"),
                eq("Notebook"),
                eq(3),
                eq(5)
        );
    }
}
