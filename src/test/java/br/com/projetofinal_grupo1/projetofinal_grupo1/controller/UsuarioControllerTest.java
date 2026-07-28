package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.controller.UsuarioController;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.UsuarioAtualizacaoDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.UsuarioCriacaoDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.UsuarioResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.UsuarioRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_deveRetornarListaDeUsuariosDTO() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("teste@exemplo.com");
        usuario.setEmailConfirmado(true);
        usuario.setSenha("123456");

        when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(usuario));

        ResponseEntity<List<UsuarioResponseDTO>> response = usuarioController.listarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("teste@exemplo.com", response.getBody().get(0).getEmail());
        assertEquals(1, response.getBody().get(0).getAuthorities().size());
        assertEquals("ROLE_USER", response.getBody().get(0).getAuthorities().get(0));
    }

    @Test
    void buscarPorId_usuarioExistente_deveRetornarDTO() {
        int id = 1;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEmail("user@teste.com");
        usuario.setEmailConfirmado(false);
        usuario.setSenha("123456");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        ResponseEntity<UsuarioResponseDTO> response = usuarioController.buscarPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody().getId());
        assertEquals("user@teste.com", response.getBody().getEmail());
        assertEquals(1, response.getBody().getAuthorities().size());
        assertEquals("ROLE_USER", response.getBody().getAuthorities().get(0));
    }

    @Test
    void buscarPorId_usuarioNaoEncontrado_deveRetornar404() {
        int id = 99;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioResponseDTO> response = usuarioController.buscarPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void criar_emailNaoExistente_deveCriarUsuarioERetornar201() {
        UsuarioCriacaoDTO dto = new UsuarioCriacaoDTO();
        dto.setEmail("novo@exemplo.com");
        dto.setSenha("123456");

        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        doNothing().when(usuarioService).cadastrarUsuarioComConfirmacao(dto);

        ResponseEntity<String> response = usuarioController.criar(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().contains("Usuário criado com sucesso"));
        verify(usuarioService, times(1)).cadastrarUsuarioComConfirmacao(dto);
    }

    @Test
    void criar_emailExistente_deveRetornar409() {
        UsuarioCriacaoDTO dto = new UsuarioCriacaoDTO();
        dto.setEmail("existente@exemplo.com");

        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        ResponseEntity<String> response = usuarioController.criar(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email já cadastrado.", response.getBody());
        verify(usuarioService, never()).cadastrarUsuarioComConfirmacao(any());
    }

    @Test
    void atualizar_usuarioExistente_deveAtualizarEretornarDTO() {
        int id = 1;
        UsuarioAtualizacaoDTO dto = new UsuarioAtualizacaoDTO();
        dto.setEmail("novoemail@exemplo.com");
        dto.setSenha("novaSenha");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setEmail("antigo@exemplo.com");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("senhaCodificada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<UsuarioResponseDTO> response = usuarioController.atualizar(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto.getEmail(), response.getBody().getEmail());
        verify(usuarioRepository, times(1)).save(usuarioExistente);
        assertEquals("senhaCodificada", usuarioExistente.getSenha());
    }

    @Test
    void atualizar_usuarioNaoEncontrado_deveRetornar404() {
        int id = 99;
        UsuarioAtualizacaoDTO dto = new UsuarioAtualizacaoDTO();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioResponseDTO> response = usuarioController.atualizar(id, dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletar_usuarioExistente_deveChamarDeleteERetornar204() {
        int id = 1;
        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        ResponseEntity<?> response = usuarioController.deletar(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void deletar_usuarioNaoExistente_deveRetornar404() {
        int id = 99;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = usuarioController.deletar(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuarioRepository, never()).delete(any());
    }
}
