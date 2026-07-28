package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

class UsuarioDetailsServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioDetailsService usuarioDetailsService;

    @BeforeEach
    void setup() {
        usuarioRepository = mock(UsuarioRepository.class);
        usuarioDetailsService = new UsuarioDetailsService(usuarioRepository);
    }

    @Test
    void loadUserByUsername_deveRetornarUsuarioQuandoEncontrado() {
        // Arrange
        String email = "teste@email.com";
        Usuario usuario = mock(Usuario.class); // Ou crie um objeto real de Usuario
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        String email = "inexistente@email.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            usuarioDetailsService.loadUserByUsername(email);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioRepository).findByEmail(email);
    }
}
