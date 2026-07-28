package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorUpdateDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.ColaboradorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ColaboradorControllerTest {

    @Mock
    private ColaboradorService colaboradorService;

    @InjectMocks
    private ColaboradorController colaboradorController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarColaborador_deveChamarService() {
        ColaboradorRequestDTO dto = new ColaboradorRequestDTO();
        // configure dto conforme necessário

        doNothing().when(colaboradorService).registrarColaborador(dto);

        var response = colaboradorController.cadastrarColaborador(dto);
        assertEquals(200, response.getStatusCodeValue());

        verify(colaboradorService, times(1)).registrarColaborador(dto);
    }

    @Test
    void imprimir_deveRetornarLista() {
        ColaboradorResponseDTO responseDTO = new ColaboradorResponseDTO();
        // configure responseDTO

        List<ColaboradorResponseDTO> lista = Collections.singletonList(responseDTO);

        when(colaboradorService.devolverTodosOsColaborador()).thenReturn(lista);

        var response = colaboradorController.imprimir();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(lista, response.getBody());

        verify(colaboradorService, times(1)).devolverTodosOsColaborador();
    }

    @Test
    void editarColaborador_deveChamarService() {
        int id = Math.toIntExact(1L);
        ColaboradorUpdateDTO updateDTO = new ColaboradorUpdateDTO();
        // configure updateDTO

        doNothing().when(colaboradorService).editarNomeEmailDataContratacaoDataRecisaoEspecificacaoEquipamentoDoColaborador(id, updateDTO);

        var response = colaboradorController.editarColaborador(id, updateDTO);
        assertEquals(204, response.getStatusCodeValue());

        verify(colaboradorService, times(1)).editarNomeEmailDataContratacaoDataRecisaoEspecificacaoEquipamentoDoColaborador(id, updateDTO);
    }

    @Test
    void deletarColaborador_deveChamarService() {
        int id = Math.toIntExact(1L);

        doNothing().when(colaboradorService).deletarColaboradorPorId(id);

        var response = colaboradorController.deletarColaborador(id);
        assertEquals(204, response.getStatusCodeValue());

        verify(colaboradorService, times(1)).deletarColaboradorPorId(id);
    }
}
