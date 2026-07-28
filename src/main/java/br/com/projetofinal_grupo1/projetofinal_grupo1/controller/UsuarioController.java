package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.UsuarioAtualizacaoDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.UsuarioCriacaoDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.UsuarioResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Usuario;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.UsuarioRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Usuário", description = "CRUD de usuários (criar, listar, editar, excluir).")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder,
                             UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
    }
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponseDTO> dtos = usuarios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @Parameter(description = "ID do usuário a ser buscado", required = true) @PathVariable int id) {
        return usuarioRepository.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private UsuarioResponseDTO convertToDto(Usuario usuario) {
        List<String> authorities = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.isEmailConfirmado(),
                authorities
        );
    }

    @Operation(summary = "Criar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })

    @PostMapping
    public ResponseEntity<String> criar(@Valid @RequestBody UsuarioCriacaoDTO usuarioCriacaoDTO) {
        if (usuarioRepository.existsByEmail(usuarioCriacaoDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado.");
        }

        usuarioService.cadastrarUsuarioComConfirmacao(usuarioCriacaoDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuário criado com sucesso. Verifique seu e-mail para confirmação.");
    }
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar um usuário existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @Parameter(description = "ID do usuário a ser atualizado", required = true) @PathVariable int id,
            @Valid @RequestBody UsuarioAtualizacaoDTO usuarioAtualizacaoDTO) {
        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    usuarioExistente.setEmail(usuarioAtualizacaoDTO.getEmail());

                    if (usuarioAtualizacaoDTO.getSenha() != null && !usuarioAtualizacaoDTO.getSenha().isEmpty()) {
                        usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizacaoDTO.getSenha()));
                    }

                    Usuario salvo = usuarioRepository.save(usuarioExistente);
                    return ResponseEntity.ok(convertToDto(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Deletar um usuário pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(
            @Parameter(description = "ID do usuário a ser deletado", required = true) @PathVariable int id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuarioRepository.delete(usuario);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}