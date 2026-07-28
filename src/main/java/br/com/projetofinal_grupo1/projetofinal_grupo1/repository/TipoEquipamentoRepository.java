package br.com.projetofinal_grupo1.projetofinal_grupo1.repository;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoEquipamentoRepository extends JpaRepository<TipoEquipamento, Integer> {
    Optional<TipoEquipamento> findByNomeTipo(String nomeTipo);

    boolean existsByNomeTipoIgnoreCase(String nomeTipo);
}
