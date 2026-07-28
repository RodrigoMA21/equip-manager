package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.service.EstoqueAlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alertas")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/estoque-alerta")
public class EstoqueAlertaController {

    private final EstoqueAlertaService estoqueAlertaService;

    public EstoqueAlertaController(EstoqueAlertaService estoqueAlertaService) {
        this.estoqueAlertaService = estoqueAlertaService;
    }

    @Operation(summary = "Verificar se há itens com estoque crítico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping("/verificar")
    public ResponseEntity<Void> verificarEstoqueCritico() {
        estoqueAlertaService.verificarEstoqueCritico();
        return ResponseEntity.ok().build();
    }
}
