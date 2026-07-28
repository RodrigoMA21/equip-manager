package br.com.projetofinal_grupo1.projetofinal_grupo1.dto;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.EquipamentoToColaborador;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.IdEquipamentoToColaborador;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EquipamentoToColaboradorRequestDTO {

    @NotNull
    private IdEquipamentoToColaborador chaveCompostaEquipamentoColaborador;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataEntrega;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataDevolucao;

    public EquipamentoToColaborador toEntity() {
        EquipamentoToColaborador ec = new EquipamentoToColaborador();
        ec.setId(this.getChaveCompostaEquipamentoColaborador());
        ec.setDataEntrega(this.dataEntrega != null ? this.dataEntrega : LocalDate.now());
        ec.setDataDevolucao(this.dataDevolucao);
        return ec;
    }

}
