package br.com.boasaude.gisa.web.rest;

import br.com.boasaude.gisa.domain.DisponibilidadeExame;
import br.com.boasaude.gisa.repository.DisponibilidadeExameRepository;
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
 * REST controller for managing {@link br.com.boasaude.gisa.domain.DisponibilidadeExame}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DisponibilidadeExameResource {

    private final Logger log = LoggerFactory.getLogger(DisponibilidadeExameResource.class);

    private static final String ENTITY_NAME = "disponibilidadeExame";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisponibilidadeExameRepository disponibilidadeExameRepository;

    public DisponibilidadeExameResource(DisponibilidadeExameRepository disponibilidadeExameRepository) {
        this.disponibilidadeExameRepository = disponibilidadeExameRepository;
    }

    /**
     * {@code POST  /disponibilidade-exames} : Create a new disponibilidadeExame.
     *
     * @param disponibilidadeExame the disponibilidadeExame to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disponibilidadeExame, or with status {@code 400 (Bad Request)} if the disponibilidadeExame has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/disponibilidade-exames")
    public ResponseEntity<DisponibilidadeExame> createDisponibilidadeExame(@RequestBody DisponibilidadeExame disponibilidadeExame)
        throws URISyntaxException {
        log.debug("REST request to save DisponibilidadeExame : {}", disponibilidadeExame);
        if (disponibilidadeExame.getId() != null) {
            throw new BadRequestAlertException("A new disponibilidadeExame cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DisponibilidadeExame result = disponibilidadeExameRepository.save(disponibilidadeExame);
        return ResponseEntity
            .created(new URI("/api/disponibilidade-exames/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /disponibilidade-exames/:id} : Updates an existing disponibilidadeExame.
     *
     * @param id the id of the disponibilidadeExame to save.
     * @param disponibilidadeExame the disponibilidadeExame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disponibilidadeExame,
     * or with status {@code 400 (Bad Request)} if the disponibilidadeExame is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disponibilidadeExame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/disponibilidade-exames/{id}")
    public ResponseEntity<DisponibilidadeExame> updateDisponibilidadeExame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DisponibilidadeExame disponibilidadeExame
    ) throws URISyntaxException {
        log.debug("REST request to update DisponibilidadeExame : {}, {}", id, disponibilidadeExame);
        if (disponibilidadeExame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disponibilidadeExame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disponibilidadeExameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DisponibilidadeExame result = disponibilidadeExameRepository.save(disponibilidadeExame);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disponibilidadeExame.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /disponibilidade-exames/:id} : Partial updates given fields of an existing disponibilidadeExame, field will ignore if it is null
     *
     * @param id the id of the disponibilidadeExame to save.
     * @param disponibilidadeExame the disponibilidadeExame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disponibilidadeExame,
     * or with status {@code 400 (Bad Request)} if the disponibilidadeExame is not valid,
     * or with status {@code 404 (Not Found)} if the disponibilidadeExame is not found,
     * or with status {@code 500 (Internal Server Error)} if the disponibilidadeExame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/disponibilidade-exames/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DisponibilidadeExame> partialUpdateDisponibilidadeExame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DisponibilidadeExame disponibilidadeExame
    ) throws URISyntaxException {
        log.debug("REST request to partial update DisponibilidadeExame partially : {}, {}", id, disponibilidadeExame);
        if (disponibilidadeExame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disponibilidadeExame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disponibilidadeExameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DisponibilidadeExame> result = disponibilidadeExameRepository
            .findById(disponibilidadeExame.getId())
            .map(existingDisponibilidadeExame -> {
                if (disponibilidadeExame.getHoraInicio() != null) {
                    existingDisponibilidadeExame.setHoraInicio(disponibilidadeExame.getHoraInicio());
                }
                if (disponibilidadeExame.getHoraFim() != null) {
                    existingDisponibilidadeExame.setHoraFim(disponibilidadeExame.getHoraFim());
                }

                return existingDisponibilidadeExame;
            })
            .map(disponibilidadeExameRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disponibilidadeExame.getId().toString())
        );
    }

    /**
     * {@code GET  /disponibilidade-exames} : get all the disponibilidadeExames.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of disponibilidadeExames in body.
     */
    @GetMapping("/disponibilidade-exames")
    public List<DisponibilidadeExame> getAllDisponibilidadeExames() {
        log.debug("REST request to get all DisponibilidadeExames");
        return disponibilidadeExameRepository.findAll();
    }

    /**
     * {@code GET  /disponibilidade-exames/:id} : get the "id" disponibilidadeExame.
     *
     * @param id the id of the disponibilidadeExame to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disponibilidadeExame, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/disponibilidade-exames/{id}")
    public ResponseEntity<DisponibilidadeExame> getDisponibilidadeExame(@PathVariable Long id) {
        log.debug("REST request to get DisponibilidadeExame : {}", id);
        Optional<DisponibilidadeExame> disponibilidadeExame = disponibilidadeExameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(disponibilidadeExame);
    }

    /**
     * {@code DELETE  /disponibilidade-exames/:id} : delete the "id" disponibilidadeExame.
     *
     * @param id the id of the disponibilidadeExame to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/disponibilidade-exames/{id}")
    public ResponseEntity<Void> deleteDisponibilidadeExame(@PathVariable Long id) {
        log.debug("REST request to delete DisponibilidadeExame : {}", id);
        disponibilidadeExameRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
