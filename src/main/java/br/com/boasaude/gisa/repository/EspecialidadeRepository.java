package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.Especialidade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Especialidade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {}
