package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorUpdateDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.ColaboradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Colaboradores", description = "CRUD de colaboradores (criar, listar, editar, excluir).")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @Operation(summary = "Cadastrar colaborador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colaborador cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping
    public ResponseEntity<Void> cadastrarColaborador(@RequestBody @Valid ColaboradorRequestDTO colaboradorRequestDTO) {
        colaboradorService.registrarColaborador(colaboradorRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar todos colaboradores cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de colaboradores retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping
    public ResponseEntity<List<ColaboradorResponseDTO>> imprimir() {
        List<ColaboradorResponseDTO> listaDeColaborador = colaboradorService.devolverTodosOsColaborador();
        return ResponseEntity.status(HttpStatus.OK).body(listaDeColaborador);
    }

    @Operation(summary = "Busca um colaborador por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colaborador encontrado"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorResponseDTO> buscarColaboradorPorId(@PathVariable Long id) {
        ColaboradorResponseDTO colaboradorResponse = colaboradorService.devolverColaboradorPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(colaboradorResponse);
    }

    @Operation(summary = "Atualizar dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Colaborador atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> editarColaborador(@PathVariable int id, @RequestBody @Valid ColaboradorUpdateDTO colaboradorUpdateDTO) {
        colaboradorService.editarNomeEmailDataContratacaoDataRecisaoEspecificacaoEquipamentoDoColaborador(id, colaboradorUpdateDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deletar colaborador pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Colaborador deletado com sucesso (soft delete)"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarColaborador(@PathVariable int id) {
        colaboradorService.deletarColaboradorPorId(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar colaboradores inativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colaboradores inativos retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/inativos")
    public ResponseEntity<List<ColaboradorResponseDTO>> listarInativos() {
        List<ColaboradorResponseDTO> inativos = colaboradorService.devolverColaboradoresInativos();
        return ResponseEntity.ok(inativos);
    }

    @Operation(summary = "Reativar colaborador pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Colaborador reativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativarColaborador(@PathVariable int id) {
        colaboradorService.reativarColaborador(id);
        return ResponseEntity.noContent().build();
    }
}