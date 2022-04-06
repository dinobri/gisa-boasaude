package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.TipoExame;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoExame entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoExameRepository extends JpaRepository<TipoExame, Long> {}
