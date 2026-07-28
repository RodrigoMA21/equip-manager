package br.com.projetofinal_grupo1.projetofinal_grupo1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EquipamentoResponseDTO {

    private int id;

    @JsonProperty("especificacoes")
    private String especificacoes;

    @JsonProperty("numeroSerie")
    private String numeroSerie;

    @JsonProperty("marca")
    private String marca;

    @JsonProperty("modelo")
    private String modelo;

    @JsonProperty("dataAquisicao")
    private LocalDate dataAquisicao;

    @JsonProperty("tempoUso")
    private int tempoUso;

    @JsonProperty("id_tipo_equipamento")
    private int idTipoEquipamento;

    // Novo campo opcional, para evitar referência cíclica
    private EstoqueResumoDTO estoque;

    private LocalDate previsaoEntrega;
}