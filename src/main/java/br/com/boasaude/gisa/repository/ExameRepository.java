package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.Exame;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Exame entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {}
