package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.controller.EquipamentoToColaboradorController;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.EquipamentoToColaboradorRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.EquipamentoToColaboradorResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.IdEquipamentoToColaborador;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.EquipamentoToColaboradorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipamentoToColaboradorControllerTest {

    @Mock
    private EquipamentoToColaboradorService equipamentoToColaboradorService;

    @InjectMocks
    private EquipamentoToColaboradorController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listar_deveRetornarLista() {
        EquipamentoToColaboradorResponseDTO dto = new EquipamentoToColaboradorResponseDTO();
        List<EquipamentoToColaboradorResponseDTO> lista = Collections.singletonList(dto);

        when(equipamentoToColaboradorService.listarTodos()).thenReturn(lista);

        List<EquipamentoToColaboradorResponseDTO> resultado = controller.listar();

        assertEquals(1, resultado.size());
        verify(equipamentoToColaboradorService, times(1)).listarTodos();
    }

    @Test
    void buscarPorId_deveRetornarResponseEntity() {
        IdEquipamentoToColaborador id = mock(IdEquipamentoToColaborador.class);
        EquipamentoToColaboradorResponseDTO dto = new EquipamentoToColaboradorResponseDTO();

        ResponseEntity<EquipamentoToColaboradorResponseDTO> response = ResponseEntity.ok(dto);
        when(equipamentoToColaboradorService.buscarPorId(id)).thenReturn(response);

        ResponseEntity<EquipamentoToColaboradorResponseDTO> resultado = controller.buscarPorId(id);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(dto, resultado.getBody());
        verify(equipamentoToColaboradorService, times(1)).buscarPorId(id);
    }

    @Test
    void criar_deveChamarServiceERetornarResponse() {
        EquipamentoToColaboradorRequestDTO requestDTO = new EquipamentoToColaboradorRequestDTO();
        EquipamentoToColaboradorResponseDTO responseDTO = new EquipamentoToColaboradorResponseDTO();

        ResponseEntity<EquipamentoToColaboradorResponseDTO> response = ResponseEntity.status(201).body(responseDTO);
        when(equipamentoToColaboradorService.salvar(requestDTO)).thenReturn(response);

        ResponseEntity<EquipamentoToColaboradorResponseDTO> resultado = controller.criar(requestDTO);

        assertEquals(201, resultado.getStatusCodeValue());
        assertEquals(responseDTO, resultado.getBody());
        verify(equipamentoToColaboradorService, times(1)).salvar(requestDTO);
    }

    @Test
    void atualizar_deveChamarServiceERetornarResponse() {
        IdEquipamentoToColaborador id = mock(IdEquipamentoToColaborador.class);
        EquipamentoToColaboradorRequestDTO requestDTO = new EquipamentoToColaboradorRequestDTO();
        EquipamentoToColaboradorResponseDTO responseDTO = new EquipamentoToColaboradorResponseDTO();

        ResponseEntity<EquipamentoToColaboradorResponseDTO> response = ResponseEntity.ok(responseDTO);
        when(equipamentoToColaboradorService.atualizar(id, requestDTO)).thenReturn(response);

        ResponseEntity<EquipamentoToColaboradorResponseDTO> resultado = controller.atualizar(id, requestDTO);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(responseDTO, resultado.getBody());
        verify(equipamentoToColaboradorService, times(1)).atualizar(id, requestDTO);
    }

    @Test
    void deletar_deveChamarServiceERetornarNoContent() {
        IdEquipamentoToColaborador id = mock(IdEquipamentoToColaborador.class);
        ResponseEntity<Void> response = ResponseEntity.noContent().build();

        when(equipamentoToColaboradorService.deletar(id)).thenReturn(response);

        ResponseEntity<Void> resultado = controller.deletar(id);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        verify(equipamentoToColaboradorService, times(1)).deletar(id);
    }

    @Test
    void registrarDevolucao_deveChamarServiceERetornarOk() {
        IdEquipamentoToColaborador id = mock(IdEquipamentoToColaborador.class);
        ResponseEntity<Void> response = ResponseEntity.ok().build();

        when(equipamentoToColaboradorService.registrarDevolucao(id)).thenReturn(response);

        ResponseEntity<Void> resultado = controller.registrarDevolucao(id);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        verify(equipamentoToColaboradorService, times(1)).registrarDevolucao(id);
    }
}
