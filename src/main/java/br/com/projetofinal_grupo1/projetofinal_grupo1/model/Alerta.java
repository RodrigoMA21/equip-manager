package br.com.projetofinal_grupo1.projetofinal_grupo1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alerta")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private int id;

    @Column(name = "tipo_alerta", nullable = false)
    private String tipoAlerta;

    @Column(name = "data_hora_geracao", nullable = false)
    private LocalDateTime dataHoraGeracao;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;


    @ManyToOne
    @JoinColumn(name = "id_tipo_equipamento")
    private TipoEquipamento tipoEquipamento;


    public Alerta(String tipoAlerta, String descricao, String status, Equipamento equipamento, TipoEquipamento tipoEquipamento) {
        this.tipoAlerta = tipoAlerta;
        this.descricao = descricao;
        this.status = status;
        this.dataHoraGeracao = LocalDateTime.now();
        this.tipoEquipamento = tipoEquipamento;
    }
}