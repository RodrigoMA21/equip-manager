package br.com.projetofinal_grupo1.projetofinal_grupo1.security;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil();
        usuario = new Usuario();
        usuario.setEmail("teste@exemplo.com");
    }

    @Test
    void testGerarToken_E_ValidarToken() {
        String token = jwtUtil.gerarToken(usuario);
        assertNotNull(token);
        assertTrue(jwtUtil.validarToken(token));
    }

    @Test
    void testGerarRefreshToken_E_ValidarToken() {
        String refreshToken = jwtUtil.gerarRefreshToken(usuario);
        assertNotNull(refreshToken);
        assertTrue(jwtUtil.validarToken(refreshToken));
    }

    @Test
    void testValidarToken_Invalido() {
        String tokenInvalido = "token.invalido.falso";
        assertFalse(jwtUtil.validarToken(tokenInvalido));
    }

    @Test
    void testGetEmailDoToken() {
        String token = jwtUtil.gerarToken(usuario);
        String email = jwtUtil.getEmailDoToken(token);
        assertEquals(usuario.getEmail(), email);
    }

    @Test
    void testGetExpiracao() {
        String token = jwtUtil.gerarToken(usuario);
        Date expiracao = jwtUtil.getExpiracao(token);
        assertNotNull(expiracao);
        // A data de expiração deve ser maior que o momento atual
        assertTrue(expiracao.after(new Date()));
    }
}
