package br.com.projetofinal_grupo1.projetofinal_grupo1.model;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorUpdateDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "colaborador")
public class Colaborador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colaborador")
    private int id;
    @Column(name = "cpf")
    private String cpf;
    @Column(name = "nome")
    private String nome;
    @Column(name = "email_colaborador")
    private String email;
    @Column(name = "cep")
    private String cep;
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    @Column(name = "data_inicio_contrato")
    private LocalDate dataContratacaoInicio;
    @Column(name = "data_rescisao_contrato")
    private LocalDate dataContratacaoRecisao;
    @Column(name = "especificacoes_equipamentos_necessarios")
    private String equipamentoEspecificacao;

    @Column(name = "ativo")
    private boolean ativo = true;

    @JsonManagedReference
    @OneToOne(mappedBy = "colaborador", cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private Endereco endereco;



    public static Colaborador converterParaColaborador(ColaboradorRequestDTO colaboradorRequestDTO){
        Colaborador colaborador = new Colaborador();
        colaborador.setNome(colaboradorRequestDTO.getNome());
        colaborador.setCpf(colaboradorRequestDTO.getCpf());
        colaborador.setEmail(colaboradorRequestDTO.getEmail());
        colaborador.setCep(colaboradorRequestDTO.getCep());
        colaborador.setDataNascimento(colaboradorRequestDTO.getData_aniversario());
        colaborador.setDataContratacaoInicio(colaboradorRequestDTO.getData_contratacao_inicio());
        colaborador.setDataContratacaoRecisao(colaboradorRequestDTO.getData_contratacao_recisao());
        colaborador.setEquipamentoEspecificacao(colaboradorRequestDTO.getEspecificacao_equipamento());

        return colaborador;
    }

    public void atualizarColaborador(ColaboradorUpdateDTO colaboradorUpdateDTO){
        setNome(colaboradorUpdateDTO.getNome());
        setCep(colaboradorUpdateDTO.getCep());
        setEmail(colaboradorUpdateDTO.getEmail());
        setDataContratacaoInicio(colaboradorUpdateDTO.getDataContratacaoInicio());
        setDataContratacaoRecisao(colaboradorUpdateDTO.getDataContratacaoRecisao());
        setEquipamentoEspecificacao(colaboradorUpdateDTO.getEquipamentoEspecificacao());

    }
}
