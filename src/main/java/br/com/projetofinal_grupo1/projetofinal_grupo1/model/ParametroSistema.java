package br.com.projetofinal_grupo1.projetofinal_grupo1.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parametro_sistema")
public class ParametroSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parametro")
    private int idParametro;

    @Column(name = "tempo_medio_reposicao", nullable = false)
    private int tempoMedioReposicao; // em dias

    @Column(name = "tempo_medio_consumo_estoque", nullable = false)
    private int tempoMedioConsumoEstoque; // em dias

    @Column(name = "tempo_medio_envio", nullable = false)
    private int tempoMedioEnvio; // em dias

    @Column(name = "taxa_media_equipamentos_defeituosos", nullable = false)
    private double taxaMediaEquipamentosDefeituosos;

    @Column(name = "estoque_minimo_seguranca", nullable = false)
    private int estoqueMinimoSeguranca;
}