package br.com.projetofinal_grupo1.projetofinal_grupo1.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name ="equipamento_to_colaborador")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipamentoToColaborador {

    @EmbeddedId
    private IdEquipamentoToColaborador id;

    @JsonProperty("dataEntrega")
    private LocalDate dataEntrega;

    @JsonProperty("dataDevolucao")
    private LocalDate dataDevolucao;

    @Column(name = "previsao_entrega")
    private LocalDate previsaoEntrega;

}