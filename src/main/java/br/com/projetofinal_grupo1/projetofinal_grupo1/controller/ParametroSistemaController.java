package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ParametroSistemaRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ParametroSistemaResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.PrevisaoFaltaResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.ParametroSistemaService;
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

@Tag(name = "Parâmetros do sistema", description = "Define os parametros de sistema (tempo de reposição, consumo, envio, taxa de equipamentos defeituosos e estoque minimo de segurança).")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/parametros-sistema")
public class ParametroSistemaController {

    private final ParametroSistemaService parametrosSistemaService;

    public ParametroSistemaController(ParametroSistemaService parametrosSistemaService) {
        this.parametrosSistemaService = parametrosSistemaService;
    }

    @Operation(summary = "Lista todos os parâmetros de sistema cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parâmetros listados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping
    public ResponseEntity<List<ParametroSistemaResponseDTO>> listarParametros() {
        List<ParametroSistemaResponseDTO> parametros = parametrosSistemaService.listarTodosParametros();
        return ResponseEntity.ok(parametros);
    }

    @Operation(summary = "Busca um parâmetro de sistema por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parâmetro encontrado"),
            @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ParametroSistemaResponseDTO> buscarParametroPorId(@PathVariable int id) {
        return parametrosSistemaService.buscarParametroPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo parâmetro de sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parâmetro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping
    public ResponseEntity<ParametroSistemaResponseDTO> criarParametro(@Valid @RequestBody ParametroSistemaRequestDTO dto) {
        try {
            ParametroSistemaResponseDTO novoParametro = parametrosSistemaService.criarParametro(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoParametro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Atualiza um parâmetro de sistema existente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parâmetro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ParametroSistemaResponseDTO> atualizarParametro(@PathVariable int id, @Valid @RequestBody ParametroSistemaRequestDTO dto) {
        try {
            ParametroSistemaResponseDTO parametroAtualizado = parametrosSistemaService.atualizarParametro(id, dto);
            return ResponseEntity.ok(parametroAtualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Deleta um parâmetro de sistema por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parâmetro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarParametro(@PathVariable int id) {
        try {
            parametrosSistemaService.deletarParametro(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Gera um relatório de previsão de falta de equipamentos com base nos parâmetros de sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros de estoque não configurados ou erro nos dados de estoque"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/relatorio-previsao-falta")
    public ResponseEntity<List<PrevisaoFaltaResponseDTO>> gerarRelatorioPrevisaoFalta() {
        try {
            List<PrevisaoFaltaResponseDTO> relatorio = parametrosSistemaService.gerarRelatorioPrevisaoFalta();
            return ResponseEntity.ok(relatorio);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}