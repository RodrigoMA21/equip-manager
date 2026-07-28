package br.com.projetofinal_grupo1.projetofinal_grupo1.repository;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Integer> {
    Optional<Equipamento> findByNumeroSerie(String numeroSerie);
}
