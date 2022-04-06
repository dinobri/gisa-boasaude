package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.Conveniado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Conveniado entity.
 */
@Repository
public interface ConveniadoRepository extends ConveniadoRepositoryWithBagRelationships, JpaRepository<Conveniado, Long> {
    default Optional<Conveniado> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Conveniado> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Conveniado> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
