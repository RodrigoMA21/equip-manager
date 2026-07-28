package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.EquipamentoRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Equipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.EquipamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Equipamentos", description = "CRUD de equipamentos (criar, listar, editar, excluir).")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
    }

    @Operation(summary = "Listar todos os equipamentos")
    @GetMapping
    public List<Equipamento> listar() {
        return equipamentoService.listarTodos();
    }

    @Operation(summary = "Buscar equipamento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipamento não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Equipamento> buscar(@PathVariable int id) {
        return equipamentoService.buscarPorId(id);
    }

    @Operation(summary = "Cadastrar novo equipamento")
    @ApiResponse(responseCode = "201", description = "Equipamento criado com sucesso")
    @PostMapping
    public ResponseEntity<Equipamento> salvar(@Valid @RequestBody EquipamentoRequestDTO dto) {
        Equipamento equipamento = new Equipamento();
        equipamento.setEspecificacoes(dto.getEspecificacoes());
        equipamento.setNumeroSerie(dto.getNumeroSerie());
        equipamento.setMarca(dto.getMarca());
        equipamento.setModelo(dto.getModelo());
        equipamento.setDataAquisicao(dto.getDataAquisicao());
        equipamento.setTempoUso(dto.getTempoUso());

        TipoEquipamento tipo = new TipoEquipamento();
        tipo.setId(dto.getIdTipoEquipamento());
        equipamento.setTipoEquipamento(tipo);

        Equipamento salvo = equipamentoService.salvar(equipamento);
        return ResponseEntity.status(201).body(salvo); // 201 Created
    }

    @Operation(summary = "Atualizar equipamento existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Equipamento não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Equipamento> atualizar(@PathVariable int id, @Valid @RequestBody EquipamentoRequestDTO dto) {
        Equipamento equipamento = new Equipamento();
        equipamento.setEspecificacoes(dto.getEspecificacoes());
        equipamento.setNumeroSerie(dto.getNumeroSerie());
        equipamento.setMarca(dto.getMarca());
        equipamento.setModelo(dto.getModelo());
        equipamento.setDataAquisicao(dto.getDataAquisicao());
        equipamento.setTempoUso(dto.getTempoUso());

        TipoEquipamento tipo = new TipoEquipamento();
        tipo.setId(dto.getIdTipoEquipamento());
        equipamento.setTipoEquipamento(tipo);

        return equipamentoService.atualizar(id, equipamento);
    }

    @Operation(summary = "Deletar equipamento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Equipamento não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        return equipamentoService.deletar(id);
    }
}


