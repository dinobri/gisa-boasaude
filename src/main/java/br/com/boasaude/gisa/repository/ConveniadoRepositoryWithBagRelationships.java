package br.com.boasaude.gisa.repository;

import br.com.boasaude.gisa.domain.Conveniado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ConveniadoRepositoryWithBagRelationships {
    Optional<Conveniado> fetchBagRelationships(Optional<Conveniado> conveniado);

    List<Conveniado> fetchBagRelationships(List<Conveniado> conveniados);

    Page<Conveniado> fetchBagRelationships(Page<Conveniado> conveniados);
}
