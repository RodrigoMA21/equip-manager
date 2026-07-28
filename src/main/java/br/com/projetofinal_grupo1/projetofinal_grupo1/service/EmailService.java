package br.com.projetofinal_grupo1.projetofinal_grupo1.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailSimples(String destino, String assunto, String corpo) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom("noreply@rarolabs.com");
        mensagem.setTo(destino);
        mensagem.setSubject(assunto);
        mensagem.setText(corpo);

        System.out.println("Destino: " + destino);
        System.out.println("Assunto: " + assunto);
        System.out.println("Corpo: " + corpo);

        mailSender.send(mensagem);
    }
    public void enviarAlertaEstoqueBaixo(String destinatario, String tipoEquipamento, int estoqueAtual, int estoqueMinimo) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom("noreply@rarolabs.com");
        mensagem.setTo(destinatario);
        mensagem.setSubject("ALERTA DE ESTOQUE BAIXO: " + tipoEquipamento);
        mensagem.setText(String.format(
                "Prezado(a),\n\n" +
                        "O equipamento do tipo '%s' atingiu um nível de estoque crítico.\n" +
                        "Estoque Atual: %d unidades\n" +
                        "Estoque Mínimo de Segurança: %d unidades\n\n" +
                        "Por favor, tome as medidas necessárias para reposição.\n\n" +
                        "Atenciosamente,\n" +
                        "Sistema de Gerenciamento de Estoque",
                tipoEquipamento, estoqueAtual, estoqueMinimo
        ));
        mailSender.send(mensagem);
    }


}