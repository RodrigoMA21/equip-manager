package br.com.projetofinal_grupo1.projetofinal_grupo1.repository;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Colaborador;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Integer> {

    // Atualizar dados do colaborador
    @Modifying
    @Transactional
    @Query("UPDATE Colaborador col SET col.nome = :nome, col.email = :email, col.cep = :cep, col.dataContratacaoInicio = :dataContratacaoInicio" +
            ", col.dataContratacaoRecisao = :dataContratacaoRecisao, col.equipamentoEspecificacao = :equipamentoEspecificacao WHERE col.id = :id")
    void editarColaborador(
            @Param("id") int id,
            @Param("nome") String nome,
            @Param("email") String email,
            @Param("cep") String cep,
            @Param("dataContratacaoInicio") LocalDate dataInicio,
            @Param("dataContratacaoRecisao") LocalDate dataRecisao,
            @Param("equipamentoEspecificacao") String equipamentoEspecificacao
    );

    // Soft delete (ativos)
    List<Colaborador> findByAtivoTrue();

    // Inativos
    List<Colaborador> findByAtivoFalse();

    // Reativar colaborador
    @Modifying
    @Transactional
    @Query("UPDATE Colaborador col SET col.ativo = true WHERE col.id = :id")
    void reativarColaborador(@Param("id") int id);
}