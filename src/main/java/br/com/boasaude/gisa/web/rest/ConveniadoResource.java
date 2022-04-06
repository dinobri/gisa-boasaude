package br.com.boasaude.gisa.web.rest;

import br.com.boasaude.gisa.domain.Conveniado;
import br.com.boasaude.gisa.repository.ConveniadoRepository;
import br.com.boasaude.gisa.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.boasaude.gisa.domain.Conveniado}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConveniadoResource {

    private final Logger log = LoggerFactory.getLogger(ConveniadoResource.class);

    private static final String ENTITY_NAME = "conveniado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConveniadoRepository conveniadoRepository;

    public ConveniadoResource(ConveniadoRepository conveniadoRepository) {
        this.conveniadoRepository = conveniadoRepository;
    }

    /**
     * {@code POST  /conveniados} : Create a new conveniado.
     *
     * @param conveniado the conveniado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conveniado, or with status {@code 400 (Bad Request)} if the conveniado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conveniados")
    public ResponseEntity<Conveniado> createConveniado(@RequestBody Conveniado conveniado) throws URISyntaxException {
        log.debug("REST request to save Conveniado : {}", conveniado);
        if (conveniado.getId() != null) {
            throw new BadRequestAlertException("A new conveniado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conveniado result = conveniadoRepository.save(conveniado);
        return ResponseEntity
            .created(new URI("/api/conveniados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conveniados/:id} : Updates an existing conveniado.
     *
     * @param id the id of the conveniado to save.
     * @param conveniado the conveniado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conveniado,
     * or with status {@code 400 (Bad Request)} if the conveniado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conveniado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conveniados/{id}")
    public ResponseEntity<Conveniado> updateConveniado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Conveniado conveniado
    ) throws URISyntaxException {
        log.debug("REST request to update Conveniado : {}, {}", id, conveniado);
        if (conveniado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conveniado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conveniadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Conveniado result = conveniadoRepository.save(conveniado);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, conveniado.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conveniados/:id} : Partial updates given fields of an existing conveniado, field will ignore if it is null
     *
     * @param id the id of the conveniado to save.
     * @param conveniado the conveniado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conveniado,
     * or with status {@code 400 (Bad Request)} if the conveniado is not valid,
     * or with status {@code 404 (Not Found)} if the conveniado is not found,
     * or with status {@code 500 (Internal Server Error)} if the conveniado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conveniados/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Conveniado> partialUpdateConveniado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Conveniado conveniado
    ) throws URISyntaxException {
        log.debug("REST request to partial update Conveniado partially : {}, {}", id, conveniado);
        if (conveniado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conveniado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conveniadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Conveniado> result = conveniadoRepository
            .findById(conveniado.getId())
            .map(existingConveniado -> {
                if (conveniado.getNome() != null) {
                    existingConveniado.setNome(conveniado.getNome());
                }
                if (conveniado.getTelefone() != null) {
                    existingConveniado.setTelefone(conveniado.getTelefone());
                }

                return existingConveniado;
            })
            .map(conveniadoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, conveniado.getId().toString())
        );
    }

    /**
     * {@code GET  /conveniados} : get all the conveniados.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conveniados in body.
     */
    @GetMapping("/conveniados")
    public List<Conveniado> getAllConveniados(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Conveniados");
        return conveniadoRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /conveniados/:id} : get the "id" conveniado.
     *
     * @param id the id of the conveniado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conveniado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conveniados/{id}")
    public ResponseEntity<Conveniado> getConveniado(@PathVariable Long id) {
        log.debug("REST request to get Conveniado : {}", id);
        Optional<Conveniado> conveniado = conveniadoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(conveniado);
    }

    /**
     * {@code DELETE  /conveniados/:id} : delete the "id" conveniado.
     *
     * @param id the id of the conveniado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conveniados/{id}")
    public ResponseEntity<Void> deleteConveniado(@PathVariable Long id) {
        log.debug("REST request to delete Conveniado : {}", id);
        conveniadoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
