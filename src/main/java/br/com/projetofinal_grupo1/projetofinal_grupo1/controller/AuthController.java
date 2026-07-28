package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.*;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autorizações", description = "autenticação/autorização (login, geração de token JWT, confirmação de email, recuperação de senha).")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Fazer login do usuário e gerar um token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou e-mail não confirmado")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        Usuario usuario = authService.login(dto);

        String accessToken = authService.gerarToken(usuario);
        String refreshToken = authService.gerarRefreshToken(usuario);
        String expiracao = authService.getExpiracao(accessToken);

        TokenResponseDTO tokenResponse = new TokenResponseDTO(accessToken, refreshToken, expiracao);
        return ResponseEntity.ok(tokenResponse);
    }
    @Operation(summary = "Confirmar e-mail via token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "E-mail confirmado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Token inválido ou expirado")
    })
    @Hidden
    @GetMapping("/confirmar")
    public ResponseEntity<String> confirmarEmail(
            @Parameter(description = "Token de confirmação enviado por e-mail")
            @RequestParam String token
    ) {
        authService.confirmarEmail(token);
        return ResponseEntity.ok("E-mail confirmado com sucesso! Agora você pode fazer login.");
    }

    @Operation(summary = "Solicitar redefinição de senha")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Link de redefinição enviado para o e-mail"),
            @ApiResponse(responseCode = "404", description = "E-mail não encontrado")
    })
    @PostMapping("/esqueci minha senha")
    public ResponseEntity<String> esqueciSenha(@RequestParam String email) {
        authService.solicitarRecuperacaoSenha(email);
        return ResponseEntity.ok("Um link para redefinir sua senha foi enviado para o e-mail.");
    }

    @Operation(summary = "Cadastrar nova senha")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Token inválido ou expirado")
    })
    @PostMapping("/alterar senha")
    public ResponseEntity<String> alterarSenha(@Valid @RequestBody AlterarSenhaDTO dto) {
        authService.alterarSenha(dto.getEmail(), dto.getSenhaAtual(), dto.getNovaSenha());
        return ResponseEntity.ok("Senha alterada com sucesso.");
    }
    @PostMapping("/redefinir senha")
    @Operation(summary = "Redefinir senha com token de recuperação recebido no email após solicitação (esqueci minha senha)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Token inválido ou expirado")
    })
    public ResponseEntity<String> resetarSenha(@Valid @RequestBody NovaSenhaDTO dto) {
        authService.redefinirSenha(dto.getToken(), dto.getNovaSenha());
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }
    @PostMapping("/refresh-token")
    @Operation(summary = "Renovar access token usando refresh token (Tokens usados no authorize do swagger)")
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO dto) {
        String novoAccessToken = authService.validarEAtualizarRefreshToken(dto.getRefreshToken());
        String expiracao = authService.getExpiracao(novoAccessToken);

        TokenResponseDTO response = new TokenResponseDTO(novoAccessToken, dto.getRefreshToken(), expiracao);
        return ResponseEntity.ok(response);
    }

}

