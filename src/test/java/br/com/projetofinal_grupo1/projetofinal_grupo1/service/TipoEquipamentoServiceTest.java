package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.TipoEquipamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TipoEquipamentoServiceTest {

    private TipoEquipamentoRepository tipoEquipamentoRepository;
    private TipoEquipamentoService tipoEquipamentoService;

    @BeforeEach
    void setUp() {
        tipoEquipamentoRepository = mock(TipoEquipamentoRepository.class);
        tipoEquipamentoService = new TipoEquipamentoService(tipoEquipamentoRepository);
    }

    @Test
    void deveCriarTipoEquipamento() {
        String nome = "Notebook";
        when(tipoEquipamentoRepository.existsByNomeTipoIgnoreCase(nome)).thenReturn(false);
        when(tipoEquipamentoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TipoEquipamento criado = tipoEquipamentoService.criarTipoEquipamento(nome);

        assertEquals(nome, criado.getNomeTipo());
        verify(tipoEquipamentoRepository).save(any(TipoEquipamento.class));
    }

    @Test
    void deveLancarErroAoCriarTipoComNomeVazio() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> tipoEquipamentoService.criarTipoEquipamento("  "));
        assertEquals("Nome do tipo de equipamento é obrigatório", ex.getMessage());
    }

    @Test
    void deveLancarErroSeTipoJaExiste() {
        String nome = "Impressora";
        when(tipoEquipamentoRepository.existsByNomeTipoIgnoreCase(nome)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> tipoEquipamentoService.criarTipoEquipamento(nome));
        assertEquals("Tipo de equipamento já existe", ex.getMessage());
    }

    @Test
    void deveListarTodosTiposEquipamento() {
        List<TipoEquipamento> lista = Arrays.asList(new TipoEquipamento(), new TipoEquipamento());
        when(tipoEquipamentoRepository.findAll()).thenReturn(lista);

        List<TipoEquipamento> resultado = tipoEquipamentoService.listarTodosTiposEquipamento();

        assertEquals(2, resultado.size());
        verify(tipoEquipamentoRepository).findAll();
    }

    @Test
    void deveBuscarPorIdExistente() {
        TipoEquipamento tipo = new TipoEquipamento();
        tipo.setId(1);

        when(tipoEquipamentoRepository.findById(1)).thenReturn(Optional.of(tipo));

        Optional<TipoEquipamento> resultado = tipoEquipamentoService.buscarTipoEquipamentoPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
    }

    @Test
    void deveBuscarPorNomeExistente() {
        String nome = "Servidor";
        TipoEquipamento tipo = new TipoEquipamento();
        tipo.setNomeTipo(nome);

        when(tipoEquipamentoRepository.findByNomeTipo(nome)).thenReturn(Optional.of(tipo));

        Optional<TipoEquipamento> resultado = tipoEquipamentoService.buscarTipoEquipamentoPorNome(nome);

        assertTrue(resultado.isPresent());
        assertEquals(nome, resultado.get().getNomeTipo());
    }

    @Test
    void deveAtualizarTipoEquipamentoExistente() {
        TipoEquipamento existente = new TipoEquipamento();
        existente.setId(1);
        existente.setNomeTipo("Antigo");

        TipoEquipamento atualizado = new TipoEquipamento();
        atualizado.setNomeTipo("Novo");

        when(tipoEquipamentoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(tipoEquipamentoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TipoEquipamento resultado = tipoEquipamentoService.atualizarTipoEquipamento(1, atualizado);

        assertEquals("Novo", resultado.getNomeTipo());
        verify(tipoEquipamentoRepository).save(any());
    }

    @Test
    void deveLancarErroAoAtualizarTipoInexistente() {
        when(tipoEquipamentoRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> tipoEquipamentoService.atualizarTipoEquipamento(1, new TipoEquipamento()));

        assertEquals("Tipo de equipamento não encontrado com o ID: 1", ex.getMessage());
    }

    @Test
    void deveDeletarTipoExistente() {
        when(tipoEquipamentoRepository.existsById(1)).thenReturn(true);

        tipoEquipamentoService.deletarTipoEquipamento(1);

        verify(tipoEquipamentoRepository).deleteById(1);
    }

    @Test
    void deveLancarErroAoDeletarTipoInexistente() {
        when(tipoEquipamentoRepository.existsById(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> tipoEquipamentoService.deletarTipoEquipamento(1));

        assertEquals("Tipo de equipamento não encontrado com o ID: 1", ex.getMessage());
    }
}
