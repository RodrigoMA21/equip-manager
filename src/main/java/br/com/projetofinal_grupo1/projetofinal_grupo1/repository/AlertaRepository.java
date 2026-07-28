package br.com.projetofinal_grupo1.projetofinal_grupo1.repository;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Alerta;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    // Busca o alerta mais recente para o tipoEquipamento com status na lista
    Optional<Alerta> findTopByTipoEquipamentoAndStatusInOrderByIdDesc(TipoEquipamento tipoEquipamento, List<String> statuses);

    List<Alerta> findByTipoEquipamentoAndStatusIn(TipoEquipamento tipoEquipamento, List<String> statuses);
}