package br.com.projetofinal_grupo1.projetofinal_grupo1.security;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "raroacademy-2025-backend-supersecreto-token-key!";
    private final long EXPIRATION = 1000L * 60 * 60 * 24 * 30; // 30 dias (aproximadamente 1 mês)
    private final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 30; // 30 dias (aproximadamente 1 mês)
    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET);
    }

    public String gerarToken(Usuario usuario) {
        return JWT.create()
                .withSubject(usuario.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(getAlgorithm());
    }

    public String gerarRefreshToken(Usuario usuario) {
        return JWT.create()
                .withSubject(usuario.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .sign(getAlgorithm());
    }

    public boolean validarToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailDoToken(String token) {
        DecodedJWT decoded = JWT.require(getAlgorithm()).build().verify(token);
        return decoded.getSubject();
    }

    public Date getExpiracao(String token) {
        DecodedJWT decoded = JWT.require(getAlgorithm()).build().verify(token);
        return decoded.getExpiresAt();
    }
}
