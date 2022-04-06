package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.Consulta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Consulta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {}
