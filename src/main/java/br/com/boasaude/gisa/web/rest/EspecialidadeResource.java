package br.com.boasaude.gisa.web.rest;

import br.com.boasaude.gisa.domain.Especialidade;
import br.com.boasaude.gisa.repository.EspecialidadeRepository;
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
 * REST controller for managing {@link br.com.boasaude.gisa.domain.Especialidade}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EspecialidadeResource {

    private final Logger log = LoggerFactory.getLogger(EspecialidadeResource.class);

    private static final String ENTITY_NAME = "especialidade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EspecialidadeRepository especialidadeRepository;

    public EspecialidadeResource(EspecialidadeRepository especialidadeRepository) {
        this.especialidadeRepository = especialidadeRepository;
    }

    /**
     * {@code POST  /especialidades} : Create a new especialidade.
     *
     * @param especialidade the especialidade to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new especialidade, or with status {@code 400 (Bad Request)} if the especialidade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/especialidades")
    public ResponseEntity<Especialidade> createEspecialidade(@RequestBody Especialidade especialidade) throws URISyntaxException {
        log.debug("REST request to save Especialidade : {}", especialidade);
        if (especialidade.getId() != null) {
            throw new BadRequestAlertException("A new especialidade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Especialidade result = especialidadeRepository.save(especialidade);
        return ResponseEntity
            .created(new URI("/api/especialidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /especialidades/:id} : Updates an existing especialidade.
     *
     * @param id the id of the especialidade to save.
     * @param especialidade the especialidade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especialidade,
     * or with status {@code 400 (Bad Request)} if the especialidade is not valid,
     * or with status {@code 500 (Internal Server Error)} if the especialidade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/especialidades/{id}")
    public ResponseEntity<Especialidade> updateEspecialidade(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Especialidade especialidade
    ) throws URISyntaxException {
        log.debug("REST request to update Especialidade : {}, {}", id, especialidade);
        if (especialidade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, especialidade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!especialidadeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Especialidade result = especialidadeRepository.save(especialidade);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, especialidade.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /especialidades/:id} : Partial updates given fields of an existing especialidade, field will ignore if it is null
     *
     * @param id the id of the especialidade to save.
     * @param especialidade the especialidade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especialidade,
     * or with status {@code 400 (Bad Request)} if the especialidade is not valid,
     * or with status {@code 404 (Not Found)} if the especialidade is not found,
     * or with status {@code 500 (Internal Server Error)} if the especialidade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/especialidades/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Especialidade> partialUpdateEspecialidade(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Especialidade especialidade
    ) throws URISyntaxException {
        log.debug("REST request to partial update Especialidade partially : {}, {}", id, especialidade);
        if (especialidade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, especialidade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!especialidadeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Especialidade> result = especialidadeRepository
            .findById(especialidade.getId())
            .map(existingEspecialidade -> {
                if (especialidade.getNome() != null) {
                    existingEspecialidade.setNome(especialidade.getNome());
                }

                return existingEspecialidade;
            })
            .map(especialidadeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, especialidade.getId().toString())
        );
    }

    /**
     * {@code GET  /especialidades} : get all the especialidades.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of especialidades in body.
     */
    @GetMapping("/especialidades")
    public List<Especialidade> getAllEspecialidades() {
        log.debug("REST request to get all Especialidades");
        return especialidadeRepository.findAll();
    }

    /**
     * {@code GET  /especialidades/:id} : get the "id" especialidade.
     *
     * @param id the id of the especialidade to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the especialidade, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/especialidades/{id}")
    public ResponseEntity<Especialidade> getEspecialidade(@PathVariable Long id) {
        log.debug("REST request to get Especialidade : {}", id);
        Optional<Especialidade> especialidade = especialidadeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(especialidade);
    }

    /**
     * {@code DELETE  /especialidades/:id} : delete the "id" especialidade.
     *
     * @param id the id of the especialidade to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/especialidades/{id}")
    public ResponseEntity<Void> deleteEspecialidade(@PathVariable Long id) {
        log.debug("REST request to delete Especialidade : {}", id);
        especialidadeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
