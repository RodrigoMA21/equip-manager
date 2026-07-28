package br.com.projetofinal_grupo1.projetofinal_grupo1.model;

import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estoque")
    private int id;

    @Column(name = "quantidade_disponivel")
    private int quantidadeDisponivel;

    @Column(name = "quantidade_em_uso")
    private int quantidadeEmUso;

    @Column(name = "quantidade_defeituosa")
    private int quantidadeDefeituosa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tipo_equipamento")
    private TipoEquipamento tipoEquipamento;
}