package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.service.EmailService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/teste")
    public String enviarEmail(@RequestParam String destino) {
        emailService.enviarEmailSimples(
                destino,
                "Teste de E-mail via Mailtrap",
                "Este é um teste de envio de e-mail via Spring Boot e Mailtrap!"
        );
        return "E-mail enviado com sucesso!";
    }
}
