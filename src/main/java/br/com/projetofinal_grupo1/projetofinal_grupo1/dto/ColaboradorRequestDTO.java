package br.com.projetofinal_grupo1.projetofinal_grupo1.dto;

import br.com.projetofinal_grupo1.projetofinal_grupo1.annotation.CepValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ColaboradorRequestDTO {
    @CPF(message = "Cpf inválido")
    @NotBlank(message = "Cpf não deve ser vazio")
    private String cpf;
    @NotBlank(message = "Nome não deve ser vazio")
    private String nome;
    @NotBlank(message = "Email não deve ser vazio")
    private String email;
    @NotBlank(message = "Cep não deve ser vazio")
    @CepValidation
    private String cep;

    @NotNull(message = "Data de aniversário não deve estar vazia")
    @Past(message = "Data de aniversário inválida")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate data_aniversario;

    @NotNull(message = "Data de contratação não deve estar vazia")
    @PastOrPresent(message = "Data de contratação inválida" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate data_contratacao_inicio;

    @NotNull(message = "Data de recisão não deve estar vazia")
    @PastOrPresent(message = "Data de recisão inválida")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate data_contratacao_recisao;

    @NotBlank(message = "Especificação de equipamento não deve ser vazio")
    private String especificacao_equipamento;

    @NotNull(message = "Número da casa não pode ser vazio")
    @PositiveOrZero(message = "Número da casa deve ser maior ou igual a zero")
    private Integer numeroCasa;
}
