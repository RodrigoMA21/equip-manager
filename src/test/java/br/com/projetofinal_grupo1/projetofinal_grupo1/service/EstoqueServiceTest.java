package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Estoque;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EquipamentoRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EstoqueRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.TipoEquipamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstoqueServiceTest {

    private EstoqueRepository estoqueRepository;
    private TipoEquipamentoRepository tipoEquipamentoRepository;
    private EquipamentoRepository equipamentoRepository;
    private EstoqueService estoqueService;

    @BeforeEach
    void setUp() {
        estoqueRepository = mock(EstoqueRepository.class);
        tipoEquipamentoRepository = mock(TipoEquipamentoRepository.class);
        equipamentoRepository = mock(EquipamentoRepository.class);
        estoqueService = new EstoqueService(estoqueRepository, tipoEquipamentoRepository, equipamentoRepository);
    }

    @Test
    void deveAtualizarEstoqueComTipoEquipamentoDiferente() {
        Estoque estoqueExistente = new Estoque();
        estoqueExistente.setId(1);
        TipoEquipamento tipoAntigo = new TipoEquipamento();
        tipoAntigo.setId(1);
        estoqueExistente.setTipoEquipamento(tipoAntigo);

        Estoque estoqueAtualizado = new Estoque();
        estoqueAtualizado.setQuantidadeDisponivel(10);
        estoqueAtualizado.setQuantidadeEmUso(5);
        estoqueAtualizado.setQuantidadeDefeituosa(2);

        TipoEquipamento novoTipo = new TipoEquipamento();
        novoTipo.setId(2);

        when(estoqueRepository.findById(1)).thenReturn(Optional.of(estoqueExistente));
        when(tipoEquipamentoRepository.findById(2)).thenReturn(Optional.of(novoTipo));
        when(estoqueRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Estoque resultado = estoqueService.atualizarEstoque(1, estoqueAtualizado, 2);

        assertEquals(10, resultado.getQuantidadeDisponivel());
        assertEquals(5, resultado.getQuantidadeEmUso());
        assertEquals(2, resultado.getQuantidadeDefeituosa());
        assertEquals(2, resultado.getTipoEquipamento().getId());
    }

    @Test
    void deveAtualizarEstoqueSemTrocarTipoEquipamento() {
        TipoEquipamento tipo = new TipoEquipamento();
        tipo.setId(1);

        Estoque estoqueExistente = new Estoque();
        estoqueExistente.setId(1);
        estoqueExistente.setTipoEquipamento(tipo);

        Estoque estoqueAtualizado = new Estoque();
        estoqueAtualizado.setQuantidadeDisponivel(8);
        estoqueAtualizado.setQuantidadeEmUso(3);
        estoqueAtualizado.setQuantidadeDefeituosa(1);

        when(estoqueRepository.findById(1)).thenReturn(Optional.of(estoqueExistente));
        when(estoqueRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Estoque resultado = estoqueService.atualizarEstoque(1, estoqueAtualizado, 1);

        assertEquals(8, resultado.getQuantidadeDisponivel());
        assertEquals(3, resultado.getQuantidadeEmUso());
        assertEquals(1, resultado.getQuantidadeDefeituosa());
        assertEquals(1, resultado.getTipoEquipamento().getId());
        verify(tipoEquipamentoRepository, never()).findById(anyInt());
    }

    @Test
    void deveLancarErroSeEstoqueNaoEncontrado() {
        when(estoqueRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> estoqueService.atualizarEstoque(1, new Estoque(), 1));

        assertEquals("Estoque não encontrado", ex.getMessage());
    }

    @Test
    void deveLancarErroSeTipoEquipamentoNaoEncontrado() {
        TipoEquipamento tipoAtual = new TipoEquipamento();
        tipoAtual.setId(1);

        Estoque estoqueExistente = new Estoque();
        estoqueExistente.setId(1);
        estoqueExistente.setTipoEquipamento(tipoAtual);

        when(estoqueRepository.findById(1)).thenReturn(Optional.of(estoqueExistente));
        when(tipoEquipamentoRepository.findById(2)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> estoqueService.atualizarEstoque(1, new Estoque(), 2));

        assertEquals("TipoEquipamento não encontrado", ex.getMessage());
    }
}
