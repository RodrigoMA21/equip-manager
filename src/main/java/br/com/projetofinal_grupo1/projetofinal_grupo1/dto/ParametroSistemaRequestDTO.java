package br.com.projetofinal_grupo1.projetofinal_grupo1.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParametroSistemaRequestDTO {

    @NotNull(message = "Tempo médio de reposição é obrigatório")
    @PositiveOrZero(message = "Tempo médio de reposição não pode ser negativo")
    private int tempoMedioReposicao;

    @NotNull(message = "Tempo médio de consumo de estoque é obrigatório")
    @PositiveOrZero(message = "Tempo médio de consumo de estoque não pode ser negativo")
    private int tempoMedioConsumoEstoque;

    @NotNull(message = "Tempo médio de envio é obrigatório")
    @PositiveOrZero(message = "Tempo médio de envio não pode ser negativo")
    private int tempoMedioEnvio;

    @NotNull(message = "Taxa média de equipamentos defeituosos é obrigatória")
    @DecimalMin(value = "0.0", message = "Taxa não pode ser negativa")
    @DecimalMax(value = "1.0", message = "Taxa não pode ser maior que 1 (100%)")
    private double taxaMediaEquipamentosDefeituosos;

    @NotNull(message = "Estoque mínimo de segurança é obrigatório")
    @PositiveOrZero(message = "Estoque mínimo de segurança não pode ser negativo")
    private int estoqueMinimoSeguranca;
}
