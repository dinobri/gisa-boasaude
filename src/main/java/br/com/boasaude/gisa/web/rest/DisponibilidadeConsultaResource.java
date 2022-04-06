package br.com.boasaude.gisa.web.rest;

import br.com.boasaude.gisa.domain.DisponibilidadeConsulta;
import br.com.boasaude.gisa.repository.DisponibilidadeConsultaRepository;
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
 * REST controller for managing {@link br.com.boasaude.gisa.domain.DisponibilidadeConsulta}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DisponibilidadeConsultaResource {

    private final Logger log = LoggerFactory.getLogger(DisponibilidadeConsultaResource.class);

    private static final String ENTITY_NAME = "disponibilidadeConsulta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisponibilidadeConsultaRepository disponibilidadeConsultaRepository;

    public DisponibilidadeConsultaResource(DisponibilidadeConsultaRepository disponibilidadeConsultaRepository) {
        this.disponibilidadeConsultaRepository = disponibilidadeConsultaRepository;
    }

    /**
     * {@code POST  /disponibilidade-consultas} : Create a new disponibilidadeConsulta.
     *
     * @param disponibilidadeConsulta the disponibilidadeConsulta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disponibilidadeConsulta, or with status {@code 400 (Bad Request)} if the disponibilidadeConsulta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/disponibilidade-consultas")
    public ResponseEntity<DisponibilidadeConsulta> createDisponibilidadeConsulta(
        @RequestBody DisponibilidadeConsulta disponibilidadeConsulta
    ) throws URISyntaxException {
        log.debug("REST request to save DisponibilidadeConsulta : {}", disponibilidadeConsulta);
        if (disponibilidadeConsulta.getId() != null) {
            throw new BadRequestAlertException("A new disponibilidadeConsulta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DisponibilidadeConsulta result = disponibilidadeConsultaRepository.save(disponibilidadeConsulta);
        return ResponseEntity
            .created(new URI("/api/disponibilidade-consultas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /disponibilidade-consultas/:id} : Updates an existing disponibilidadeConsulta.
     *
     * @param id the id of the disponibilidadeConsulta to save.
     * @param disponibilidadeConsulta the disponibilidadeConsulta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disponibilidadeConsulta,
     * or with status {@code 400 (Bad Request)} if the disponibilidadeConsulta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disponibilidadeConsulta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/disponibilidade-consultas/{id}")
    public ResponseEntity<DisponibilidadeConsulta> updateDisponibilidadeConsulta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DisponibilidadeConsulta disponibilidadeConsulta
    ) throws URISyntaxException {
        log.debug("REST request to update DisponibilidadeConsulta : {}, {}", id, disponibilidadeConsulta);
        if (disponibilidadeConsulta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disponibilidadeConsulta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disponibilidadeConsultaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DisponibilidadeConsulta result = disponibilidadeConsultaRepository.save(disponibilidadeConsulta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disponibilidadeConsulta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /disponibilidade-consultas/:id} : Partial updates given fields of an existing disponibilidadeConsulta, field will ignore if it is null
     *
     * @param id the id of the disponibilidadeConsulta to save.
     * @param disponibilidadeConsulta the disponibilidadeConsulta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disponibilidadeConsulta,
     * or with status {@code 400 (Bad Request)} if the disponibilidadeConsulta is not valid,
     * or with status {@code 404 (Not Found)} if the disponibilidadeConsulta is not found,
     * or with status {@code 500 (Internal Server Error)} if the disponibilidadeConsulta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/disponibilidade-consultas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DisponibilidadeConsulta> partialUpdateDisponibilidadeConsulta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DisponibilidadeConsulta disponibilidadeConsulta
    ) throws URISyntaxException {
        log.debug("REST request to partial update DisponibilidadeConsulta partially : {}, {}", id, disponibilidadeConsulta);
        if (disponibilidadeConsulta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disponibilidadeConsulta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disponibilidadeConsultaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DisponibilidadeConsulta> result = disponibilidadeConsultaRepository
            .findById(disponibilidadeConsulta.getId())
            .map(existingDisponibilidadeConsulta -> {
                if (disponibilidadeConsulta.getHoraInicio() != null) {
                    existingDisponibilidadeConsulta.setHoraInicio(disponibilidadeConsulta.getHoraInicio());
                }
                if (disponibilidadeConsulta.getHoraFim() != null) {
                    existingDisponibilidadeConsulta.setHoraFim(disponibilidadeConsulta.getHoraFim());
                }

                return existingDisponibilidadeConsulta;
            })
            .map(disponibilidadeConsultaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disponibilidadeConsulta.getId().toString())
        );
    }

    /**
     * {@code GET  /disponibilidade-consultas} : get all the disponibilidadeConsultas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of disponibilidadeConsultas in body.
     */
    @GetMapping("/disponibilidade-consultas")
    public List<DisponibilidadeConsulta> getAllDisponibilidadeConsultas() {
        log.debug("REST request to get all DisponibilidadeConsultas");
        return disponibilidadeConsultaRepository.findAll();
    }

    /**
     * {@code GET  /disponibilidade-consultas/:id} : get the "id" disponibilidadeConsulta.
     *
     * @param id the id of the disponibilidadeConsulta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disponibilidadeConsulta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/disponibilidade-consultas/{id}")
    public ResponseEntity<DisponibilidadeConsulta> getDisponibilidadeConsulta(@PathVariable Long id) {
        log.debug("REST request to get DisponibilidadeConsulta : {}", id);
        Optional<DisponibilidadeConsulta> disponibilidadeConsulta = disponibilidadeConsultaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(disponibilidadeConsulta);
    }

    /**
     * {@code DELETE  /disponibilidade-consultas/:id} : delete the "id" disponibilidadeConsulta.
     *
     * @param id the id of the disponibilidadeConsulta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/disponibilidade-consultas/{id}")
    public ResponseEntity<Void> deleteDisponibilidadeConsulta(@PathVariable Long id) {
        log.debug("REST request to delete DisponibilidadeConsulta : {}", id);
        disponibilidadeConsultaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
