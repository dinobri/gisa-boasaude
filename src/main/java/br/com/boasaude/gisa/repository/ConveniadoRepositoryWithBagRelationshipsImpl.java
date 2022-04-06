package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.Conveniado;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ConveniadoRepositoryWithBagRelationshipsImpl implements ConveniadoRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Conveniado> fetchBagRelationships(Optional<Conveniado> conveniado) {
        return conveniado.map(this::fetchTipoExames);
    }

    @Override
    public Page<Conveniado> fetchBagRelationships(Page<Conveniado> conveniados) {
        return new PageImpl<>(fetchBagRelationships(conveniados.getContent()), conveniados.getPageable(), conveniados.getTotalElements());
    }

    @Override
    public List<Conveniado> fetchBagRelationships(List<Conveniado> conveniados) {
        return Optional.of(conveniados).map(this::fetchTipoExames).orElse(Collections.emptyList());
    }

    Conveniado fetchTipoExames(Conveniado result) {
        return entityManager
            .createQuery(
                "select conveniado from Conveniado conveniado left join fetch conveniado.tipoExames where conveniado is :conveniado",
                Conveniado.class
            )
            .setParameter("conveniado", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Conveniado> fetchTipoExames(List<Conveniado> conveniados) {
        return entityManager
            .createQuery(
                "select distinct conveniado from Conveniado conveniado left join fetch conveniado.tipoExames where conveniado in :conveniados",
                Conveniado.class
            )
            .setParameter("conveniados", conveniados)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
