package br.com.projetofinal_grupo1.projetofinal_grupo1.security;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.UsuarioRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.security.JwtFilter;
import br.com.projetofinal_grupo1.projetofinal_grupo1.security.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtFilterTest {

    private JwtUtil jwtUtil;
    private UsuarioRepository usuarioRepository;
    private JwtFilter jwtFilter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        jwtUtil = mock(JwtUtil.class);
        usuarioRepository = mock(UsuarioRepository.class);
        jwtFilter = new JwtFilter(jwtUtil, usuarioRepository);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        // Limpa contexto de segurança antes de cada teste
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_deveAutenticarUsuario_quandoTokenValido() throws Exception {
        String token = "tokenFake";
        String email = "usuario@exemplo.com";

        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.getAuthorities()).thenReturn(java.util.Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.getEmailDoToken(token)).thenReturn(email);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioMock));

        jwtFilter.doFilterInternal(request, response, filterChain);

        // Verifica se a autenticação foi configurada no SecurityContext
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(usuarioMock, SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // Verifica se o filtro continuou a cadeia
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_naoDeveAutenticar_quandoTokenInvalido() throws Exception {
        String token = "tokenInvalido";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.getEmailDoToken(token)).thenThrow(new JWTVerificationException("Token inválido"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        // Como o token é inválido, não deve setar autenticação
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verifica que a cadeia do filtro continua
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_deveContinuar_semAutenticacao_quandoHeaderAuthorizationForNulo() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_deveContinuar_semAutenticacao_quandoHeaderNaoComecaComBearer() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("OutroPrefixo token");

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
