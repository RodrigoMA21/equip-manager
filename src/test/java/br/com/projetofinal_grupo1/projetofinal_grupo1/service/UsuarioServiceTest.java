package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.UsuarioCriacaoDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private UsuarioService usuarioService;

    @BeforeEach
    public void setup() {
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        emailService = mock(EmailService.class);

        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder, emailService);
    }

    @Test
    void cadastrarUsuarioComConfirmacao_deveSalvarUsuarioEEnviarEmail() {
        // Arrange
        UsuarioCriacaoDTO dto = new UsuarioCriacaoDTO();
        dto.setEmail("teste@exemplo.com");
        dto.setSenha("123456");

        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("senhaCodificada");

        // Act
        usuarioService.cadastrarUsuarioComConfirmacao(dto);

        // Assert

        // Captura o usuário salvo para verificar campos
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        Usuario usuarioSalvo = usuarioCaptor.getValue();

        assertEquals(dto.getEmail(), usuarioSalvo.getEmail());
        assertEquals("senhaCodificada", usuarioSalvo.getSenha());
        assertFalse(usuarioSalvo.isEmailConfirmado());
        assertNotNull(usuarioSalvo.getTokenRecuperacao());
        assertNotNull(usuarioSalvo.getTokenRecuperacaoValidade());

        // Verifica se o email foi enviado
        verify(emailService).enviarEmailSimples(
                eq(dto.getEmail()),
                eq("Confirme seu e-mail"),
                contains(usuarioSalvo.getTokenRecuperacao())
        );
    }

    @Test
    void cadastrarUsuarioComConfirmacao_quandoEmailJaExistir_deveLancarExcecao() {
        // Arrange
        UsuarioCriacaoDTO dto = new UsuarioCriacaoDTO();
        dto.setEmail("teste@exemplo.com");

        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.cadastrarUsuarioComConfirmacao(dto);
        });

        assertEquals("Email já cadastrado", ex.getMessage());

        // Verifica que não chamou save nem enviou email
        verify(usuarioRepository, never()).save(any());
        verify(emailService, never()).enviarEmailSimples(any(), any(), any());
    }
}
