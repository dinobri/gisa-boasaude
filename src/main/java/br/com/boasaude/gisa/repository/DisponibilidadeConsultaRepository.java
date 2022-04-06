package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.DisponibilidadeConsulta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DisponibilidadeConsulta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisponibilidadeConsultaRepository extends JpaRepository<DisponibilidadeConsulta, Long> {}
