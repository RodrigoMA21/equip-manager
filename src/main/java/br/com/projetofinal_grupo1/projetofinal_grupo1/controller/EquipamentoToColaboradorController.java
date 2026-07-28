package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.*;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.IdEquipamentoToColaborador;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.EquipamentoToColaboradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Equipamento-To-Colaborador", description = "CRUD de envio de equipamentos para colaboradores (criar, listar, editar, excluir).")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/emprestimos")
public class EquipamentoToColaboradorController {

    @Autowired
    private EquipamentoToColaboradorService equipamentoToColaboradorService;

    @Operation(summary = "Listar todos os envios")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de envios retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping
    public List<EquipamentoToColaboradorResponseDTO> listar() {
        return equipamentoToColaboradorService.listarTodos();
    }

    @Operation(summary = "Buscar envios por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envio encontrado"),
            @ApiResponse(responseCode = "404", description = "Envio não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoToColaboradorResponseDTO> buscarPorId(@PathVariable IdEquipamentoToColaborador id) {
        return equipamentoToColaboradorService.buscarPorId(id);
    }

    @Operation(summary = "Cadastrar novo envio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Envio criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping
    public ResponseEntity<EquipamentoToColaboradorResponseDTO> criar(
            @RequestBody @Valid EquipamentoToColaboradorRequestDTO dto) {
        return equipamentoToColaboradorService.salvar(dto);
    }

    @Operation(summary = "Atualizar envio existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Envio não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoToColaboradorResponseDTO> atualizar(
            @PathVariable IdEquipamentoToColaborador id,
            @RequestBody @Valid EquipamentoToColaboradorRequestDTO dto) {
        return equipamentoToColaboradorService.atualizar(id, dto);
    }

    @Operation(summary = "Deletar envio por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Envio não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable IdEquipamentoToColaborador id) {
        return equipamentoToColaboradorService.deletar(id);
    }

    @Operation(summary = "Registrar devolução do equipamento pelo ID do empréstimo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devolução registrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Equipamento já devolvido"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PutMapping("/{id}/devolver")
    public ResponseEntity<Void> registrarDevolucao(@PathVariable IdEquipamentoToColaborador id) {
        return equipamentoToColaboradorService.registrarDevolucao(id);
    }
}
