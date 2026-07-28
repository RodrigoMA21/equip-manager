package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.LoginRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.UsuarioRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.security.JwtUtil;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.AuthService;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private JwtUtil jwtUtil;
    private AuthService authService;

    @BeforeEach
    public void setup() {
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        emailService = mock(EmailService.class);
        jwtUtil = mock(JwtUtil.class);

        authService = new AuthService(usuarioRepository, passwordEncoder, emailService, jwtUtil);
    }

    @Test
    void login_deveRetornarUsuario_quandoCredenciaisForemValidas() {
        // Arrange
        String email = "teste@exemplo.com";
        String senha = "123456";
        String senhaCodificada = "senhaCodificada";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(senhaCodificada);
        usuario.setEmailConfirmado(true);

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail(email);
        dto.setSenha(senha);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, senhaCodificada)).thenReturn(true);

        // Act
        Usuario resultado = authService.login(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
    }

    @Test
    void login_deveLancarExcecao_quandoEmailNaoExistir() {
        // Arrange
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("naoexiste@exemplo.com");
        dto.setSenha("123");

        when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.login(dto);
        });

        assertEquals("Email ou senha inválidos", ex.getMessage());
    }

    @Test
    void login_deveLancarExcecao_quandoSenhaIncorreta() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@exemplo.com");
        usuario.setSenha("senhaCodificada");
        usuario.setEmailConfirmado(true);

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("teste@exemplo.com");
        dto.setSenha("senhaErrada");

        when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(dto.getSenha(), usuario.getSenha())).thenReturn(false);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.login(dto);
        });

        assertEquals("Email ou senha inválidos", ex.getMessage());
    }

    @Test
    void login_deveLancarExcecao_quandoEmailNaoConfirmado() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@exemplo.com");
        usuario.setSenha("senhaCodificada");
        usuario.setEmailConfirmado(false);

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("teste@exemplo.com");
        dto.setSenha("123");

        when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(dto.getSenha(), usuario.getSenha())).thenReturn(true);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.login(dto);
        });

        assertEquals("E-mail não confirmado", ex.getMessage());
    }
    @Test
    void confirmarEmail_deveConfirmarQuandoTokenValidoENaoExpirado() {
        String token = "valid-token";
        Usuario usuario = new Usuario();
        usuario.setTokenRecuperacao(token);
        usuario.setTokenRecuperacaoValidade(LocalDateTime.now().plusMinutes(30));

        when(usuarioRepository.findByTokenRecuperacao(token)).thenReturn(Optional.of(usuario));

        authService.confirmarEmail(token);

        assertTrue(usuario.isEmailConfirmado());
        assertNull(usuario.getTokenRecuperacao());
        assertNull(usuario.getTokenRecuperacaoValidade());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void confirmarEmail_deveLancarExcecaoQuandoTokenNaoExiste() {
        when(usuarioRepository.findByTokenRecuperacao("invalido")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.confirmarEmail("invalido");
        });

        assertEquals("Token inválido ou usuário não encontrado", ex.getMessage());
    }

    @Test
    void confirmarEmail_deveLancarExcecaoQuandoTokenExpirado() {
        Usuario usuario = new Usuario();
        usuario.setTokenRecuperacaoValidade(LocalDateTime.now().minusHours(1));

        when(usuarioRepository.findByTokenRecuperacao("token-expirado")).thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.confirmarEmail("token-expirado");
        });

        assertEquals("Token expirado", ex.getMessage());
    }
}
