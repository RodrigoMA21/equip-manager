package br.com.projetofinal_grupo1.projetofinal_grupo1.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "equipamento")
@ToString
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipamento")
    private int id;

    @Column(name = "especificacoes")
    private String especificacoes;

    @Column(name = "numero_serie", unique = true, nullable = false)
    private String numeroSerie;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;

    @Column(name = "tempo_uso")
    private int tempoUso;

    @ManyToOne
    @JoinColumn(name = "id_tipo_equipamento", nullable = false)
    private TipoEquipamento tipoEquipamento;


    @Column(nullable = false)
    private Boolean disponivel;
}