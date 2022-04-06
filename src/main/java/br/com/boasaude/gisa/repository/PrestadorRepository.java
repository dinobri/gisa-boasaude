package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.Prestador;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Prestador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {}
