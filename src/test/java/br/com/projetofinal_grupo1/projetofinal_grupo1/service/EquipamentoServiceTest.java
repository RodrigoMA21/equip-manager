package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Equipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Estoque;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EquipamentoRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EstoqueRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.TipoEquipamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipamentoServiceTest {

    @Mock
    private EquipamentoRepository equipamentoRepository;

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private TipoEquipamentoRepository tipoEquipamentoRepository;

    @InjectMocks
    private EquipamentoService equipamentoService;

    @Test
    void deveListarTodosEquipamentos() {
        Equipamento eq1 = new Equipamento();
        Equipamento eq2 = new Equipamento();
        when(equipamentoRepository.findAll()).thenReturn(Arrays.asList(eq1, eq2));

        List<Equipamento> resultado = equipamentoService.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveBuscarEquipamentoPorIdExistente() {
        Equipamento equipamento = new Equipamento();
        when(equipamentoRepository.findById(1)).thenReturn(Optional.of(equipamento));

        ResponseEntity<Equipamento> resposta = equipamentoService.buscarPorId(1);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(equipamento, resposta.getBody());
    }

    @Test
    void deveRetornarNotFoundAoBuscarPorIdInexistente() {
        when(equipamentoRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Equipamento> resposta = equipamentoService.buscarPorId(1);

        assertEquals(404, resposta.getStatusCodeValue());
    }

    @Test
    void deveSalvarEquipamentoEAtualizarEstoqueExistente() {
        TipoEquipamento tipo = new TipoEquipamento();
        tipo.setId(1);
        when(tipoEquipamentoRepository.findById(1)).thenReturn(Optional.of(tipo));

        Equipamento equipamento = new Equipamento();
        equipamento.setTipoEquipamento(tipo);

        when(equipamentoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Estoque estoque = new Estoque();
        estoque.setQuantidadeDisponivel(2);
        estoque.setTipoEquipamento(tipo);
        when(estoqueRepository.findByTipoEquipamentoId(1)).thenReturn(Optional.of(estoque));

        Equipamento salvo = equipamentoService.salvar(equipamento);

        assertTrue(salvo.getDisponivel());
        verify(estoqueRepository).save(any(Estoque.class));
    }

    @Test
    void deveSalvarEquipamentoEInserirNovoEstoque() {
        TipoEquipamento tipo = new TipoEquipamento();
        tipo.setId(2);
        when(tipoEquipamentoRepository.findById(2)).thenReturn(Optional.of(tipo));

        Equipamento equipamento = new Equipamento();
        equipamento.setTipoEquipamento(tipo);

        when(equipamentoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        when(estoqueRepository.findByTipoEquipamentoId(2)).thenReturn(Optional.empty());

        Equipamento salvo = equipamentoService.salvar(equipamento);

        assertTrue(salvo.getDisponivel());
        verify(estoqueRepository).save(any(Estoque.class));
    }

    @Test
    void deveAtualizarEquipamentoExistente() {
        Equipamento equipamentoExistente = new Equipamento();
        equipamentoExistente.setId(1);
        equipamentoExistente.setDisponivel(true);

        when(equipamentoRepository.findById(1)).thenReturn(Optional.of(equipamentoExistente));
        when(equipamentoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Equipamento equipamentoAtualizado = new Equipamento();
        equipamentoAtualizado.setId(1);
        equipamentoAtualizado.setDisponivel(false);

        ResponseEntity<Equipamento> resposta = equipamentoService.atualizar(1, equipamentoAtualizado);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertFalse(resposta.getBody().getDisponivel());
        verify(equipamentoRepository).save(any(Equipamento.class));
    }

    @Test
    void deveLancarErroAoAtualizarEquipamentoInexistente() {
        when(equipamentoRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Equipamento> resposta = equipamentoService.atualizar(1, new Equipamento());

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        assertNull(resposta.getBody());
    }

    @Test
    void deveDeletarEquipamentoExistente() {
        when(equipamentoRepository.existsById(1)).thenReturn(true);

        ResponseEntity<Void> resposta = equipamentoService.deletar(1);

        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        verify(equipamentoRepository).deleteById(1);
    }

    @Test
    void deveLancarErroAoDeletarEquipamentoInexistente() {
        when(equipamentoRepository.existsById(1)).thenReturn(false);

        ResponseEntity<Void> resposta = equipamentoService.deletar(1);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(equipamentoRepository, never()).deleteById(anyInt());
    }
}
