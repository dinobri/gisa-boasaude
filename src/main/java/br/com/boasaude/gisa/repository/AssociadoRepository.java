package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.Associado;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Associado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {}
