package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

class EmailServiceTest {

    private JavaMailSender mailSender;
    private EmailService emailService;

    @BeforeEach
    void setup() {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailService(mailSender);
    }

    @Test
    void enviarEmailSimples_deveEnviarEmailComCamposCorretos() {
        // Arrange
        String destino = "usuario@teste.com";
        String assunto = "Teste Assunto";
        String corpo = "Corpo do email";

        // Act
        emailService.enviarEmailSimples(destino, assunto, corpo);

        // Assert
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage mensagemEnviada = captor.getValue();
        assertEquals("noreply@rarolabs.com", mensagemEnviada.getFrom());
        assertArrayEquals(new String[]{destino}, mensagemEnviada.getTo());
        assertEquals(assunto, mensagemEnviada.getSubject());
        assertEquals(corpo, mensagemEnviada.getText());
    }

    @Test
    void enviarAlertaEstoqueBaixo_deveEnviarEmailComMensagemFormatada() {
        // Arrange
        String destinatario = "admin@empresa.com";
        String tipoEquipamento = "Notebook";
        int estoqueAtual = 3;
        int estoqueMinimo = 5;

        String textoEsperado = String.format(
                "Prezado(a),\n\n" +
                        "O equipamento do tipo '%s' atingiu um nível de estoque crítico.\n" +
                        "Estoque Atual: %d unidades\n" +
                        "Estoque Mínimo de Segurança: %d unidades\n\n" +
                        "Por favor, tome as medidas necessárias para reposição.\n\n" +
                        "Atenciosamente,\n" +
                        "Sistema de Gerenciamento de Estoque",
                tipoEquipamento, estoqueAtual, estoqueMinimo
        );

        // Act
        emailService.enviarAlertaEstoqueBaixo(destinatario, tipoEquipamento, estoqueAtual, estoqueMinimo);

        // Assert
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage mensagemEnviada = captor.getValue();
        assertEquals("noreply@rarolabs.com", mensagemEnviada.getFrom());
        assertArrayEquals(new String[]{destinatario}, mensagemEnviada.getTo());
        assertEquals("ALERTA DE ESTOQUE BAIXO: " + tipoEquipamento, mensagemEnviada.getSubject());
        assertEquals(textoEsperado, mensagemEnviada.getText());
    }
}
