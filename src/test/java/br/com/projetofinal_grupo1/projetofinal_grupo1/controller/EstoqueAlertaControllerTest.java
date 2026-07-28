package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.controller.EstoqueAlertaController;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.EstoqueAlertaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EstoqueAlertaControllerTest {

    @Mock
    private EstoqueAlertaService estoqueAlertaService;

    @InjectMocks
    private EstoqueAlertaController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void verificarEstoqueCritico_deveChamarServicoERetornarOk() {
        // Arrange
        doNothing().when(estoqueAlertaService).verificarEstoqueCritico();

        // Act
        ResponseEntity<Void> response = controller.verificarEstoqueCritico();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(estoqueAlertaService, times(1)).verificarEstoqueCritico();
    }
}
