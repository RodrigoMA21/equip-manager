package br.com.projetofinal_grupo1.projetofinal_grupo1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrevisaoFaltaResponseDTO {

    private int idTipoEquipamento;
    private String nomeTipoEquipamento;
    private String numeroSerie;

    private int quantidadeDisponivelAtual;
    private int quantidadeEmUsoAtual;
    private int quantidadeDefeituosaAtual;
    private double consumoDiarioEstimado;
    private int leadTimeTotalDias;
    private int estoqueMinimoSeguranca;
    private double pontoDePedido;
    private boolean emRiscoDeFalta;
    private String mensagemAlerta;
}