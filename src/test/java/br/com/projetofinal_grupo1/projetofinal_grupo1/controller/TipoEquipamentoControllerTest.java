package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.controller.TipoEquipamentoController;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.TipoEquipamentoRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.TipoEquipamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TipoEquipamentoControllerTest {

    @Mock
    private TipoEquipamentoService tipoEquipamentoService;

    @InjectMocks
    private TipoEquipamentoController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarTipoEquipamento_sucesso_deveRetornarCreated() {
        TipoEquipamentoRequestDTO dto = new TipoEquipamentoRequestDTO();
        dto.setNomeTipo("Notebook");

        TipoEquipamento criado = new TipoEquipamento();
        criado.setId(1);
        criado.setNomeTipo("Notebook");

        when(tipoEquipamentoService.criarTipoEquipamento("Notebook")).thenReturn(criado);

        ResponseEntity<?> response = controller.criarTipoEquipamento(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(criado, response.getBody());
        verify(tipoEquipamentoService, times(1)).criarTipoEquipamento("Notebook");
    }

    @Test
    void criarTipoEquipamento_jaExiste_deveRetornarConflict() {
        TipoEquipamentoRequestDTO dto = new TipoEquipamentoRequestDTO();
        dto.setNomeTipo("Notebook");

        when(tipoEquipamentoService.criarTipoEquipamento("Notebook"))
                .thenThrow(new RuntimeException("Tipo já existe"));

        ResponseEntity<?> response = controller.criarTipoEquipamento(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Tipo já existe", response.getBody());
    }

    @Test
    void listarTodosTiposEquipamento_deveRetornarListaComOk() {
        TipoEquipamento t1 = new TipoEquipamento(1, "Notebook");
        TipoEquipamento t2 = new TipoEquipamento(2, "Celular");

        when(tipoEquipamentoService.listarTodosTiposEquipamento()).thenReturn(List.of(t1, t2));

        ResponseEntity<List<TipoEquipamento>> response = controller.listarTodosTiposEquipamento();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains(t1));
        assertTrue(response.getBody().contains(t2));
    }

    @Test
    void buscarTipoEquipamentoPorId_encontrado_deveRetornarOk() {
        int id = 1;
        TipoEquipamento t = new TipoEquipamento(id, "Notebook");

        when(tipoEquipamentoService.buscarTipoEquipamentoPorId(id)).thenReturn(Optional.of(t));

        ResponseEntity<TipoEquipamento> response = controller.buscarTipoEquipamentoPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(t, response.getBody());
    }

    @Test
    void buscarTipoEquipamentoPorId_naoEncontrado_deveRetornarNotFound() {
        int id = 99;

        when(tipoEquipamentoService.buscarTipoEquipamentoPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<TipoEquipamento> response = controller.buscarTipoEquipamentoPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void atualizarTipoEquipamento_sucesso_deveRetornarOk() {
        int id = 1;
        TipoEquipamento tipoAtualizado = new TipoEquipamento(id, "Tablet");

        when(tipoEquipamentoService.atualizarTipoEquipamento(eq(id), any(TipoEquipamento.class))).thenReturn(tipoAtualizado);

        ResponseEntity<TipoEquipamento> response = controller.atualizarTipoEquipamento(id, tipoAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tipoAtualizado, response.getBody());
    }

    @Test
    void atualizarTipoEquipamento_naoEncontrado_deveRetornarNotFound() {
        int id = 99;
        TipoEquipamento tipo = new TipoEquipamento(id, "Tablet");

        when(tipoEquipamentoService.atualizarTipoEquipamento(eq(id), any(TipoEquipamento.class))).thenThrow(new RuntimeException());

        ResponseEntity<TipoEquipamento> response = controller.atualizarTipoEquipamento(id, tipo);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deletarTipoEquipamento_sucesso_deveRetornarNoContent() {
        int id = 1;
        doNothing().when(tipoEquipamentoService).deletarTipoEquipamento(id);

        ResponseEntity<Void> response = controller.deletarTipoEquipamento(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tipoEquipamentoService, times(1)).deletarTipoEquipamento(id);
    }

    @Test
    void deletarTipoEquipamento_naoEncontrado_deveRetornarNotFound() {
        int id = 99;
        doThrow(new RuntimeException()).when(tipoEquipamentoService).deletarTipoEquipamento(id);

        ResponseEntity<Void> response = controller.deletarTipoEquipamento(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
