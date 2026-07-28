package br.com.projetofinal_grupo1.projetofinal_grupo1.model;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class IdEquipamentoToColaborador {

    private int idEquipamento;
    private int idColaborador;

}
