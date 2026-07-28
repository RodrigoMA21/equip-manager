package br.com.projetofinal_grupo1.projetofinal_grupo1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstoqueResumoDTO {
    private int quantidadeDisponivel;
    private int quantidadeEmUso;
    private int quantidadeDefeituosa;
}