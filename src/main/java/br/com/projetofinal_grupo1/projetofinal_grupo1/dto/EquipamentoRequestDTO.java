package br.com.projetofinal_grupo1.projetofinal_grupo1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EquipamentoRequestDTO {

    @NotBlank(message = "Especificações são obrigatórias")
    private String especificacoes;

    @NotBlank(message = "Número de série é obrigatório")
    private String numeroSerie;

    @NotBlank(message = "Marca é obrigatória")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    @NotNull(message = "Data de aquisição é obrigatória")
    @PastOrPresent(message = "Data de aquisição não pode ser no futuro")
    private LocalDate dataAquisicao;

    @PositiveOrZero(message = "Tempo de uso não pode ser negativo")
    private int tempoUso;

    @NotNull(message = "ID do tipo de equipamento é obrigatório")
    @JsonProperty("id_tipo_equipamento")
    private Integer idTipoEquipamento;

    private Boolean disponivel;
}