package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ParametroSistemaRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ParametroSistemaResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.PrevisaoFaltaResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Estoque;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.ParametroSistema;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EstoqueRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.ParametroSistemaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParametroSistemaServiceTest {

    @Mock
    private ParametroSistemaRepository parametroSistemaRepository;

    @Mock
    private EstoqueRepository estoqueRepository;

    @InjectMocks
    private ParametroSistemaService parametroSistemaService;

    private ParametroSistema parametroSistema;
    private ParametroSistemaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        parametroSistema = new ParametroSistema();
        parametroSistema.setIdParametro(1);
        parametroSistema.setTempoMedioReposicao(5);
        parametroSistema.setTempoMedioConsumoEstoque(30);
        parametroSistema.setTempoMedioEnvio(3);
        parametroSistema.setTaxaMediaEquipamentosDefeituosos(0.05);
        parametroSistema.setEstoqueMinimoSeguranca(100);

        requestDTO = new ParametroSistemaRequestDTO();
        requestDTO.setTempoMedioReposicao(5);
        requestDTO.setTempoMedioConsumoEstoque(30);
        requestDTO.setTempoMedioEnvio(3);
        requestDTO.setTaxaMediaEquipamentosDefeituosos(0.05);
        requestDTO.setEstoqueMinimoSeguranca(100);
    }

    @Test
    void testCriarParametro_Success() {
        when(parametroSistemaRepository.save(any(ParametroSistema.class))).thenReturn(parametroSistema);

        ParametroSistemaResponseDTO result = parametroSistemaService.criarParametro(requestDTO);

        assertNotNull(result);
        assertEquals(parametroSistema.getIdParametro(), result.getIdParametro());
        assertEquals(parametroSistema.getTempoMedioReposicao(), result.getTempoMedioReposicao());
        assertEquals(parametroSistema.getTempoMedioConsumoEstoque(), result.getTempoMedioConsumoEstoque());
        assertEquals(parametroSistema.getTempoMedioEnvio(), result.getTempoMedioEnvio());
        assertEquals(parametroSistema.getTaxaMediaEquipamentosDefeituosos(), result.getTaxaMediaEquipamentosDefeituosos());
        assertEquals(parametroSistema.getEstoqueMinimoSeguranca(), result.getEstoqueMinimoSeguranca());

        verify(parametroSistemaRepository, times(1)).save(any(ParametroSistema.class));
    }

    @Test
    void testBuscarParametroPorId_Found() {
        when(parametroSistemaRepository.findById(1)).thenReturn(Optional.of(parametroSistema));

        Optional<ParametroSistemaResponseDTO> result = parametroSistemaService.buscarParametroPorId(1);

        assertTrue(result.isPresent());
        assertEquals(parametroSistema.getIdParametro(), result.get().getIdParametro());
        verify(parametroSistemaRepository, times(1)).findById(1);
    }

    @Test
    void testBuscarParametroPorId_NotFound() {
        when(parametroSistemaRepository.findById(99)).thenReturn(Optional.empty());

        Optional<ParametroSistemaResponseDTO> result = parametroSistemaService.buscarParametroPorId(99);

        assertFalse(result.isPresent());
        verify(parametroSistemaRepository, times(1)).findById(99);
    }

    @Test
    void testListarTodosParametros() {
        ParametroSistema parametro2 = new ParametroSistema();
        parametro2.setIdParametro(2);
        parametro2.setTempoMedioReposicao(7);
        parametro2.setTempoMedioConsumoEstoque(25);
        parametro2.setTempoMedioEnvio(4);
        parametro2.setTaxaMediaEquipamentosDefeituosos(0.03);
        parametro2.setEstoqueMinimoSeguranca(150);

        List<ParametroSistema> parametros = Arrays.asList(parametroSistema, parametro2);
        when(parametroSistemaRepository.findAll()).thenReturn(parametros);

        List<ParametroSistemaResponseDTO> result = parametroSistemaService.listarTodosParametros();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(parametroSistema.getIdParametro(), result.get(0).getIdParametro());
        assertEquals(parametro2.getIdParametro(), result.get(1).getIdParametro());
        verify(parametroSistemaRepository, times(1)).findAll();
    }

    @Test
    void testAtualizarParametro_Success() {
        when(parametroSistemaRepository.findById(1)).thenReturn(Optional.of(parametroSistema));
        when(parametroSistemaRepository.save(any(ParametroSistema.class))).thenReturn(parametroSistema);

        ParametroSistemaResponseDTO result = parametroSistemaService.atualizarParametro(1, requestDTO);

        assertNotNull(result);
        assertEquals(parametroSistema.getIdParametro(), result.getIdParametro());
        verify(parametroSistemaRepository, times(1)).findById(1);
        verify(parametroSistemaRepository, times(1)).save(any(ParametroSistema.class));
    }

    @Test
    void testAtualizarParametro_NotFound() {
        when(parametroSistemaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> parametroSistemaService.atualizarParametro(99, requestDTO));

        assertEquals("Parâmetro de estoque não encontrado com o ID: 99", exception.getMessage());
        verify(parametroSistemaRepository, times(1)).findById(99);
        verify(parametroSistemaRepository, never()).save(any(ParametroSistema.class));
    }

    @Test
    void testDeletarParametro_Success() {
        when(parametroSistemaRepository.existsById(1)).thenReturn(true);
        parametroSistemaService.deletarParametro(1);

        verify(parametroSistemaRepository, times(1)).existsById(1);
        verify(parametroSistemaRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeletarParametro_NotFound() {
        when(parametroSistemaRepository.existsById(99)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> parametroSistemaService.deletarParametro(99));

        assertEquals("Parâmetro de estoque não encontrado com o ID: 99", exception.getMessage());
        verify(parametroSistemaRepository, times(1)).existsById(99);
        verify(parametroSistemaRepository, never()).deleteById(anyInt());
    }

    @Test
    void testGetLatestParametroSistema_Found() {
        ParametroSistema p1 = new ParametroSistema();
        p1.setIdParametro(1);
        ParametroSistema p2 = new ParametroSistema();
        p2.setIdParametro(2);
        ParametroSistema p3 = new ParametroSistema();
        p3.setIdParametro(3);

        List<ParametroSistema> parametros = Arrays.asList(p1, p3, p2);
        when(parametroSistemaRepository.findAll()).thenReturn(parametros);

        Optional<ParametroSistema> result = parametroSistemaService.getLatestParametroSistema();

        assertTrue(result.isPresent());
        assertEquals(3, result.get().getIdParametro());
        verify(parametroSistemaRepository, times(1)).findAll();
    }

    @Test
    void testGetLatestParametroSistema_EmptyList() {
        when(parametroSistemaRepository.findAll()).thenReturn(Collections.emptyList());

        Optional<ParametroSistema> result = parametroSistemaService.getLatestParametroSistema();

        assertFalse(result.isPresent());
        verify(parametroSistemaRepository, times(1)).findAll();
    }

    @Test
    void testGerarRelatorioPrevisaoFalta_Success() {
        ParametroSistema parametro = ParametroSistema.builder()
                .tempoMedioConsumoEstoque(10)
                .tempoMedioEnvio(2)
                .tempoMedioReposicao(3)
                .taxaMediaEquipamentosDefeituosos(0.1)
                .estoqueMinimoSeguranca(20)
                .build();

        when(parametroSistemaRepository.getLatestParametroSistema()).thenReturn(Optional.of(parametro));

        TipoEquipamento tipo = new TipoEquipamento();
        tipo.setId(1);
        tipo.setNomeTipo("Notebook");

        Estoque estoque1 = new Estoque();
        estoque1.setTipoEquipamento(tipo);
        estoque1.setQuantidadeDisponivel(50);
        estoque1.setQuantidadeEmUso(150);
        estoque1.setQuantidadeDefeituosa(5);

        Estoque estoque2 = new Estoque();
        estoque2.setTipoEquipamento(tipo);
        estoque2.setQuantidadeDisponivel(200);
        estoque2.setQuantidadeEmUso(100);
        estoque2.setQuantidadeDefeituosa(10);

        List<Estoque> estoques = Arrays.asList(estoque1, estoque2);
        when(estoqueRepository.findAll()).thenReturn(estoques);

        List<PrevisaoFaltaResponseDTO> result = parametroSistemaService.gerarRelatorioPrevisaoFalta();

        assertNotNull(result);
        assertEquals(2, result.size());

        PrevisaoFaltaResponseDTO previsao1 = result.get(0);
        assertEquals(tipo.getId(), previsao1.getIdTipoEquipamento());
        assertEquals("Notebook", previsao1.getNomeTipoEquipamento());
        assertEquals(50, previsao1.getQuantidadeDisponivelAtual());
        assertEquals(150, previsao1.getQuantidadeEmUsoAtual());
        assertEquals(5, previsao1.getQuantidadeDefeituosaAtual());
        assertTrue(previsao1.isEmRiscoDeFalta());
        assertEquals("ATENÇÃO: Em risco de falta!", previsao1.getMensagemAlerta());

        PrevisaoFaltaResponseDTO previsao2 = result.get(1);
        assertEquals(tipo.getId(), previsao2.getIdTipoEquipamento());
        assertFalse(previsao2.isEmRiscoDeFalta());
        assertEquals("Estoque suficiente.", previsao2.getMensagemAlerta());

        verify(parametroSistemaRepository, times(1)).getLatestParametroSistema();
        verify(estoqueRepository, times(1)).findAll();
    }

    @Test
    void testGerarRelatorioPrevisaoFalta_SemParametros() {
        when(parametroSistemaRepository.getLatestParametroSistema()).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> parametroSistemaService.gerarRelatorioPrevisaoFalta());

        assertEquals("Parâmetros de estoque não configurados.", exception.getMessage());
        verify(parametroSistemaRepository, times(1)).getLatestParametroSistema();
        verify(estoqueRepository, never()).findAll();
    }

    @Test
    void testGerarRelatorioPrevisaoFalta_ComEquipamentoSemTipo() {
        ParametroSistema parametro = ParametroSistema.builder()
                .tempoMedioReposicao(5)
                .tempoMedioConsumoEstoque(10)
                .tempoMedioEnvio(3)
                .taxaMediaEquipamentosDefeituosos(0.05)
                .estoqueMinimoSeguranca(100)
                .build();

        when(parametroSistemaRepository.getLatestParametroSistema())
                .thenReturn(Optional.of(parametro));

        Estoque estoque = new Estoque();
        estoque.setTipoEquipamento(null);
        estoque.setQuantidadeDisponivel(80);
        estoque.setQuantidadeEmUso(40);
        estoque.setQuantidadeDefeituosa(1);

        when(estoqueRepository.findAll())
                .thenReturn(Collections.singletonList(estoque));

        List<PrevisaoFaltaResponseDTO> result = parametroSistemaService.gerarRelatorioPrevisaoFalta();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(parametroSistemaRepository, times(1)).getLatestParametroSistema();
        verify(estoqueRepository, times(1)).findAll();
    }

    @Test
    void testGerarRelatorioPrevisaoFalta_ComConsumoZero() {
        ParametroSistema parametroComConsumoZero = ParametroSistema.builder()
                .idParametro(1)
                .tempoMedioReposicao(5)
                .tempoMedioConsumoEstoque(0)
                .tempoMedioEnvio(3)
                .taxaMediaEquipamentosDefeituosos(0.05)
                .estoqueMinimoSeguranca(100)
                .build();

        when(parametroSistemaRepository.getLatestParametroSistema())
                .thenReturn(Optional.of(parametroComConsumoZero));

        TipoEquipamento tipo = new TipoEquipamento();
        tipo.setId(1);
        tipo.setNomeTipo("Monitor");

        Estoque estoque = new Estoque();
        estoque.setTipoEquipamento(tipo);
        estoque.setQuantidadeDisponivel(100);
        estoque.setQuantidadeEmUso(50);
        estoque.setQuantidadeDefeituosa(2);

        when(estoqueRepository.findAll())
                .thenReturn(Collections.singletonList(estoque));

        List<PrevisaoFaltaResponseDTO> result = parametroSistemaService.gerarRelatorioPrevisaoFalta();

        assertNotNull(result);
        assertEquals(1, result.size());

        PrevisaoFaltaResponseDTO dto = result.get(0);
        assertEquals(0.0, dto.getConsumoDiarioEstimado());
        assertEquals(1, dto.getIdTipoEquipamento());
        assertEquals("Monitor", dto.getNomeTipoEquipamento());
        assertEquals(100, dto.getQuantidadeDisponivelAtual());
        assertEquals(50, dto.getQuantidadeEmUsoAtual());
        assertEquals(2, dto.getQuantidadeDefeituosaAtual());
        assertEquals(100.0, dto.getPontoDePedido());
        assertFalse(dto.isEmRiscoDeFalta());
        assertEquals("Estoque suficiente.", dto.getMensagemAlerta());

        verify(parametroSistemaRepository, times(1)).getLatestParametroSistema();
        verify(estoqueRepository, times(1)).findAll();
    }
}
