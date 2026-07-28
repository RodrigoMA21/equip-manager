package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.UsuarioCriacaoDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void cadastrarUsuarioComConfirmacao(UsuarioCriacaoDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setEmailConfirmado(false); // impede login antes da confirmação

        String tokenConfirmacao = UUID.randomUUID().toString();
        usuario.setTokenRecuperacao(tokenConfirmacao);
        usuario.setTokenRecuperacaoValidade(LocalDateTime.now().plusHours(2));

        usuarioRepository.save(usuario);

        String linkConfirmacao = "http://localhost:8080/auth/confirmar?token=" + tokenConfirmacao;
        emailService.enviarEmailSimples(
                usuario.getEmail(),
                "Confirme seu e-mail",
                "Clique no link para ativar sua conta: " + linkConfirmacao
        );
    }
}
