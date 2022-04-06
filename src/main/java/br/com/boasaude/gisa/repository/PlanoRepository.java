package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.Plano;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Plano entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {}
