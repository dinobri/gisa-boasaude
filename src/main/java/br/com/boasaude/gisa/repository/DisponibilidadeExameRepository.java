package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.DisponibilidadeExame;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DisponibilidadeExame entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisponibilidadeExameRepository extends JpaRepository<DisponibilidadeExame, Long> {}
