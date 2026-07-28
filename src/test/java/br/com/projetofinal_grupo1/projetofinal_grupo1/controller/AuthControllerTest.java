package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.*;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    void login_DeveRetornarTokenResponseDTO_QuandoCredenciaisValidas() {
        LoginRequestDTO dto = new LoginRequestDTO("email@email.com", "senha123");
        Usuario usuario = new Usuario();

        when(authService.login(dto)).thenReturn(usuario);
        when(authService.gerarToken(usuario)).thenReturn("access-token");
        when(authService.gerarRefreshToken(usuario)).thenReturn("refresh-token");
        when(authService.getExpiracao("access-token")).thenReturn("2030-12-31T23:59:59");

        ResponseEntity<TokenResponseDTO> response = authController.login(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("access-token", response.getBody().getAccessToken());
        assertEquals("refresh-token", response.getBody().getRefreshToken());
        assertEquals("2030-12-31T23:59:59", response.getBody().getExpiracao());

        verify(authService).login(dto);
    }

    @Test
    void confirmarEmail_DeveRetornarMensagemSucesso_QuandoTokenValido() {
        String token = "valid-token";
        doNothing().when(authService).confirmarEmail(token);

        ResponseEntity<String> response = authController.confirmarEmail(token);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("E-mail confirmado com sucesso! Agora você pode fazer login.", response.getBody());

        verify(authService).confirmarEmail(token);
    }

    @Test
    void esqueciSenha_DeveRetornarMensagemSucesso_QuandoEmailExistente() {
        String email = "usuario@email.com";
        doNothing().when(authService).solicitarRecuperacaoSenha(email);

        ResponseEntity<String> response = authController.esqueciSenha(email);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Um link para redefinir sua senha foi enviado para o e-mail.", response.getBody());

        verify(authService).solicitarRecuperacaoSenha(email);
    }

    @Test
    void alterarSenha_DeveRetornarMensagemSucesso_QuandoDadosValidos() {
        AlterarSenhaDTO dto = new AlterarSenhaDTO("email@email.com", "senhaAtual", "novaSenha");

        doNothing().when(authService).alterarSenha(dto.getEmail(), dto.getSenhaAtual(), dto.getNovaSenha());

        ResponseEntity<String> response = authController.alterarSenha(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Senha alterada com sucesso.", response.getBody());

        verify(authService).alterarSenha(dto.getEmail(), dto.getSenhaAtual(), dto.getNovaSenha());
    }

    @Test
    void resetarSenha_DeveRetornarMensagemSucesso_QuandoTokenValido() {
        NovaSenhaDTO dto = new NovaSenhaDTO("valid-token", "novaSenha123");

        doNothing().when(authService).redefinirSenha(dto.getToken(), dto.getNovaSenha());

        ResponseEntity<String> response = authController.resetarSenha(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Senha redefinida com sucesso.", response.getBody());

        verify(authService).redefinirSenha(dto.getToken(), dto.getNovaSenha());
    }

    @Test
    void refreshToken_DeveRetornarNovoAccessToken_QuandoRefreshTokenValido() {
        String refreshToken = "valid-refresh";
        String novoAccessToken = "novo-access";
        String expiracao = "2099-01-01T00:00:00";

        RefreshTokenRequestDTO dto = new RefreshTokenRequestDTO(refreshToken);

        when(authService.validarEAtualizarRefreshToken(refreshToken)).thenReturn(novoAccessToken);
        when(authService.getExpiracao(novoAccessToken)).thenReturn(expiracao);

        ResponseEntity<TokenResponseDTO> response = authController.refreshToken(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(novoAccessToken, response.getBody().getAccessToken());
        assertEquals(refreshToken, response.getBody().getRefreshToken());
        assertEquals(expiracao, response.getBody().getExpiracao());

        verify(authService).validarEAtualizarRefreshToken(refreshToken);
        verify(authService).getExpiracao(novoAccessToken);
    }
}
