package br.com.boasaude.gisa.web.rest;

import br.com.boasaude.gisa.domain.TipoExame;
import br.com.boasaude.gisa.repository.TipoExameRepository;
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
 * REST controller for managing {@link br.com.boasaude.gisa.domain.TipoExame}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TipoExameResource {

    private final Logger log = LoggerFactory.getLogger(TipoExameResource.class);

    private static final String ENTITY_NAME = "tipoExame";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoExameRepository tipoExameRepository;

    public TipoExameResource(TipoExameRepository tipoExameRepository) {
        this.tipoExameRepository = tipoExameRepository;
    }

    /**
     * {@code POST  /tipo-exames} : Create a new tipoExame.
     *
     * @param tipoExame the tipoExame to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoExame, or with status {@code 400 (Bad Request)} if the tipoExame has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-exames")
    public ResponseEntity<TipoExame> createTipoExame(@RequestBody TipoExame tipoExame) throws URISyntaxException {
        log.debug("REST request to save TipoExame : {}", tipoExame);
        if (tipoExame.getId() != null) {
            throw new BadRequestAlertException("A new tipoExame cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoExame result = tipoExameRepository.save(tipoExame);
        return ResponseEntity
            .created(new URI("/api/tipo-exames/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-exames/:id} : Updates an existing tipoExame.
     *
     * @param id the id of the tipoExame to save.
     * @param tipoExame the tipoExame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoExame,
     * or with status {@code 400 (Bad Request)} if the tipoExame is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoExame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-exames/{id}")
    public ResponseEntity<TipoExame> updateTipoExame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoExame tipoExame
    ) throws URISyntaxException {
        log.debug("REST request to update TipoExame : {}, {}", id, tipoExame);
        if (tipoExame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoExame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoExameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoExame result = tipoExameRepository.save(tipoExame);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoExame.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-exames/:id} : Partial updates given fields of an existing tipoExame, field will ignore if it is null
     *
     * @param id the id of the tipoExame to save.
     * @param tipoExame the tipoExame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoExame,
     * or with status {@code 400 (Bad Request)} if the tipoExame is not valid,
     * or with status {@code 404 (Not Found)} if the tipoExame is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoExame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-exames/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoExame> partialUpdateTipoExame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoExame tipoExame
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoExame partially : {}, {}", id, tipoExame);
        if (tipoExame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoExame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoExameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoExame> result = tipoExameRepository
            .findById(tipoExame.getId())
            .map(existingTipoExame -> {
                if (tipoExame.getNome() != null) {
                    existingTipoExame.setNome(tipoExame.getNome());
                }

                return existingTipoExame;
            })
            .map(tipoExameRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoExame.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-exames} : get all the tipoExames.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoExames in body.
     */
    @GetMapping("/tipo-exames")
    public List<TipoExame> getAllTipoExames() {
        log.debug("REST request to get all TipoExames");
        return tipoExameRepository.findAll();
    }

    /**
     * {@code GET  /tipo-exames/:id} : get the "id" tipoExame.
     *
     * @param id the id of the tipoExame to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoExame, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-exames/{id}")
    public ResponseEntity<TipoExame> getTipoExame(@PathVariable Long id) {
        log.debug("REST request to get TipoExame : {}", id);
        Optional<TipoExame> tipoExame = tipoExameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tipoExame);
    }

    /**
     * {@code DELETE  /tipo-exames/:id} : delete the "id" tipoExame.
     *
     * @param id the id of the tipoExame to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-exames/{id}")
    public ResponseEntity<Void> deleteTipoExame(@PathVariable Long id) {
        log.debug("REST request to delete TipoExame : {}", id);
        tipoExameRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
