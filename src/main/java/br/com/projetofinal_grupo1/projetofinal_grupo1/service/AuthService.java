package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.LoginRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.UsuarioRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {

    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            JwtUtil jwtUtil
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    public Usuario login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        if (!usuario.isEmailConfirmado()) {
            throw new RuntimeException("E-mail não confirmado");
        }

        return usuario;
    }

    public String gerarToken(Usuario usuario) {
        return jwtUtil.gerarToken(usuario);
    }

    public String gerarRefreshToken(Usuario usuario) {
        return jwtUtil.gerarRefreshToken(usuario);
    }

    public String validarEAtualizarRefreshToken(String refreshToken) {
        if (!jwtUtil.validarToken(refreshToken)) {
            throw new RuntimeException("Refresh token inválido ou expirado");
        }

        String email = jwtUtil.getEmailDoToken(refreshToken);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return jwtUtil.gerarToken(usuario);
    }

    public String getExpiracao(String token) {
        Date expiracao = jwtUtil.getExpiracao(token);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(expiracao);
    }

    public void confirmarEmail(String token) {
        Usuario usuario = usuarioRepository.findByTokenRecuperacao(token)
                .orElseThrow(() -> new RuntimeException("Token inválido ou usuário não encontrado"));

        if (usuario.getTokenRecuperacaoValidade().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        usuario.setEmailConfirmado(true);
        usuario.setTokenRecuperacao(null);
        usuario.setTokenRecuperacaoValidade(null);

        usuarioRepository.save(usuario);
    }

    public void solicitarRecuperacaoSenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado."));

        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacao(token);
        usuario.setTokenRecuperacaoValidade(LocalDateTime.now().plusHours(2));
        usuarioRepository.save(usuario);

        emailService.enviarEmailSimples(
                usuario.getEmail(),
                "Recuperação de Senha",
                "Use este token para redefinir sua senha: " + token
        );
    }

    public void alterarSenha(String email, String senhaAtual, String novaSenha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }
    public void redefinirSenha(String token, String novaSenha) {
        Usuario usuario = usuarioRepository.findByTokenRecuperacao(token)
                .orElseThrow(() -> new RuntimeException("Token inválido."));

        if (usuario.getTokenRecuperacaoValidade().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado.");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setTokenRecuperacao(null);
        usuario.setTokenRecuperacaoValidade(null);

        usuarioRepository.save(usuario);
    }


}