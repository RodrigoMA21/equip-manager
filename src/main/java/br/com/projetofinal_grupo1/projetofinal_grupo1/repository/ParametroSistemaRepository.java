package br.com.projetofinal_grupo1.projetofinal_grupo1.repository;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.ParametroSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ParametroSistemaRepository extends JpaRepository<ParametroSistema, Integer> {
    @Query("SELECT p FROM ParametroSistema p ORDER BY p.id DESC LIMIT 1")
    Optional<ParametroSistema> getLatestParametroSistema();
}