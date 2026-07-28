package br.com.projetofinal_grupo1.projetofinal_grupo1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParametroSistemaResponseDTO {
    private int idParametro;
    private int tempoMedioReposicao;
    private int tempoMedioConsumoEstoque;
    private int tempoMedioEnvio;
    private double taxaMediaEquipamentosDefeituosos;
    private int estoqueMinimoSeguranca;
}