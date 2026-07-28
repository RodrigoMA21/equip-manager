package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.controller.EmailController;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmailControllerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enviarEmail_deveChamarEmailServiceEretornarMensagemSucesso() {
        String destino = "teste@exemplo.com";

        // Não precisa configurar o emailService, pois o método é void
        doNothing().when(emailService).enviarEmailSimples(anyString(), anyString(), anyString());

        String resposta = emailController.enviarEmail(destino);

        // Verifica se o serviço foi chamado com os parâmetros corretos
        verify(emailService, times(1)).enviarEmailSimples(
                eq(destino),
                eq("Teste de E-mail via Mailtrap"),
                eq("Este é um teste de envio de e-mail via Spring Boot e Mailtrap!")
        );

        assertEquals("E-mail enviado com sucesso!", resposta);
    }
}
