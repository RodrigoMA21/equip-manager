package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.controller.EquipamentoController;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.EquipamentoRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Equipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.EquipamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipamentoControllerTest {

    @Mock
    private EquipamentoService equipamentoService;

    @InjectMocks
    private EquipamentoController equipamentoController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Testar listar todos equipamentos
    @Test
    void listar_deveRetornarListaEquipamentos() {
        Equipamento eq1 = new Equipamento();
        Equipamento eq2 = new Equipamento();
        List<Equipamento> lista = Arrays.asList(eq1, eq2);

        when(equipamentoService.listarTodos()).thenReturn(lista);

        List<Equipamento> resultado = equipamentoController.listar();

        assertEquals(2, resultado.size());
        verify(equipamentoService, times(1)).listarTodos();
    }

    // Testar buscar por ID (retorno ResponseEntity)
    @Test
    void buscar_deveRetornarEquipamento() {
        int id = 1;
        Equipamento eq = new Equipamento();
        eq.setId(id);

        ResponseEntity<Equipamento> response = ResponseEntity.ok(eq);

        when(equipamentoService.buscarPorId(id)).thenReturn(response);

        ResponseEntity<Equipamento> resultado = equipamentoController.buscar(id);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(id, resultado.getBody().getId());
        verify(equipamentoService, times(1)).buscarPorId(id);
    }

    // Testar salvar equipamento
    @Test
    void salvar_deveCriarEquipamentoERetornar201() {
        EquipamentoRequestDTO dto = new EquipamentoRequestDTO();
        dto.setEspecificacoes("Especificação teste");
        dto.setNumeroSerie("12345");
        dto.setMarca("Marca X");
        dto.setModelo("Modelo Y");
        dto.setDataAquisicao(LocalDate.now());
        dto.setTempoUso(12);
        dto.setIdTipoEquipamento(2);

        Equipamento salvo = new Equipamento();
        salvo.setId(1);
        salvo.setMarca(dto.getMarca());

        when(equipamentoService.salvar(any(Equipamento.class))).thenReturn(salvo);

        ResponseEntity<Equipamento> response = equipamentoController.salvar(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(salvo, response.getBody());

        ArgumentCaptor<Equipamento> captor = ArgumentCaptor.forClass(Equipamento.class);
        verify(equipamentoService, times(1)).salvar(captor.capture());

        Equipamento capturado = captor.getValue();
        assertEquals(dto.getMarca(), capturado.getMarca());
        assertEquals(dto.getNumeroSerie(), capturado.getNumeroSerie());
        assertEquals(dto.getIdTipoEquipamento(), capturado.getTipoEquipamento().getId());
    }

    // Testar atualizar equipamento
    @Test
    void atualizar_deveChamarServiceERetornarResponse() {
        int id = 1;

        EquipamentoRequestDTO dto = new EquipamentoRequestDTO();
        dto.setEspecificacoes("Nova especificação");
        dto.setNumeroSerie("67890");
        dto.setMarca("Marca Nova");
        dto.setModelo("Modelo Novo");
        dto.setDataAquisicao(LocalDate.now());
        dto.setTempoUso(24);
        dto.setIdTipoEquipamento(3);

        Equipamento atualizado = new Equipamento();
        atualizado.setId(id);
        atualizado.setMarca(dto.getMarca());

        ResponseEntity<Equipamento> responseEsperado = ResponseEntity.ok(atualizado);

        when(equipamentoService.atualizar(eq(id), any(Equipamento.class))).thenReturn(responseEsperado);

        ResponseEntity<Equipamento> response = equipamentoController.atualizar(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(atualizado, response.getBody());

        ArgumentCaptor<Equipamento> captor = ArgumentCaptor.forClass(Equipamento.class);
        verify(equipamentoService, times(1)).atualizar(eq(id), captor.capture());

        Equipamento capturado = captor.getValue();
        assertEquals(dto.getMarca(), capturado.getMarca());
        assertEquals(dto.getNumeroSerie(), capturado.getNumeroSerie());
        assertEquals(dto.getIdTipoEquipamento(), capturado.getTipoEquipamento().getId());
    }

    // Testar deletar equipamento
    @Test
    void deletar_deveChamarServiceERetornarResponse() {
        int id = 1;
        ResponseEntity<Void> responseEsperado = ResponseEntity.noContent().build();

        when(equipamentoService.deletar(id)).thenReturn(responseEsperado);

        ResponseEntity<Void> response = equipamentoController.deletar(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(equipamentoService, times(1)).deletar(id);
    }
}
