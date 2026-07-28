package br.com.projetofinal_grupo1.projetofinal_grupo1.dto;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Colaborador;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ColaboradorResponseDTO {
    private int id;
    private String nome;
    private String email;
    private String cep;
    private LocalDate data_aniversario;
    private LocalDate data_contratacao_inicio;
    private LocalDate data_contratacao_recisao;
    private String especificacao_equipamento;

    private EnderecoResponseDTO endereco;  // campo para endereço

    public static ColaboradorResponseDTO converterParaColaboradorResponseDTO(Colaborador colaborador) {
        return new ColaboradorResponseDTO(
                colaborador.getId(),
                colaborador.getNome(),
                colaborador.getEmail(),
                colaborador.getCep(),
                colaborador.getDataNascimento(),
                colaborador.getDataContratacaoInicio(),
                colaborador.getDataContratacaoRecisao(),
                colaborador.getEquipamentoEspecificacao(),
                EnderecoResponseDTO.fromEntity(colaborador.getEndereco())  // seta o endereço
        );
    }
}
