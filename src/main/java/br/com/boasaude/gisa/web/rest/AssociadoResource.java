package br.com.boasaude.gisa.web.rest;

import br.com.boasaude.gisa.domain.Associado;
import br.com.boasaude.gisa.repository.AssociadoRepository;
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
 * REST controller for managing {@link br.com.boasaude.gisa.domain.Associado}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AssociadoResource {

    private final Logger log = LoggerFactory.getLogger(AssociadoResource.class);

    private static final String ENTITY_NAME = "associado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssociadoRepository associadoRepository;

    public AssociadoResource(AssociadoRepository associadoRepository) {
        this.associadoRepository = associadoRepository;
    }

    /**
     * {@code POST  /associados} : Create a new associado.
     *
     * @param associado the associado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new associado, or with status {@code 400 (Bad Request)} if the associado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/associados")
    public ResponseEntity<Associado> createAssociado(@RequestBody Associado associado) throws URISyntaxException {
        log.debug("REST request to save Associado : {}", associado);
        if (associado.getId() != null) {
            throw new BadRequestAlertException("A new associado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Associado result = associadoRepository.save(associado);
        return ResponseEntity
            .created(new URI("/api/associados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /associados/:id} : Updates an existing associado.
     *
     * @param id the id of the associado to save.
     * @param associado the associado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated associado,
     * or with status {@code 400 (Bad Request)} if the associado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the associado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/associados/{id}")
    public ResponseEntity<Associado> updateAssociado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Associado associado
    ) throws URISyntaxException {
        log.debug("REST request to update Associado : {}, {}", id, associado);
        if (associado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, associado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!associadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Associado result = associadoRepository.save(associado);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, associado.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /associados/:id} : Partial updates given fields of an existing associado, field will ignore if it is null
     *
     * @param id the id of the associado to save.
     * @param associado the associado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated associado,
     * or with status {@code 400 (Bad Request)} if the associado is not valid,
     * or with status {@code 404 (Not Found)} if the associado is not found,
     * or with status {@code 500 (Internal Server Error)} if the associado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/associados/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Associado> partialUpdateAssociado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Associado associado
    ) throws URISyntaxException {
        log.debug("REST request to partial update Associado partially : {}, {}", id, associado);
        if (associado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, associado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!associadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Associado> result = associadoRepository
            .findById(associado.getId())
            .map(existingAssociado -> {
                if (associado.getNome() != null) {
                    existingAssociado.setNome(associado.getNome());
                }
                if (associado.getSobrenome() != null) {
                    existingAssociado.setSobrenome(associado.getSobrenome());
                }
                if (associado.getDataNascimento() != null) {
                    existingAssociado.setDataNascimento(associado.getDataNascimento());
                }
                if (associado.getEstadoCivil() != null) {
                    existingAssociado.setEstadoCivil(associado.getEstadoCivil());
                }
                if (associado.getSexo() != null) {
                    existingAssociado.setSexo(associado.getSexo());
                }
                if (associado.getNaturalidadeUf() != null) {
                    existingAssociado.setNaturalidadeUf(associado.getNaturalidadeUf());
                }
                if (associado.getNaturalidadeCidade() != null) {
                    existingAssociado.setNaturalidadeCidade(associado.getNaturalidadeCidade());
                }
                if (associado.getNumeroDocumento() != null) {
                    existingAssociado.setNumeroDocumento(associado.getNumeroDocumento());
                }
                if (associado.getUfDocumento() != null) {
                    existingAssociado.setUfDocumento(associado.getUfDocumento());
                }
                if (associado.getOrgaoDocumento() != null) {
                    existingAssociado.setOrgaoDocumento(associado.getOrgaoDocumento());
                }
                if (associado.getDataDocumento() != null) {
                    existingAssociado.setDataDocumento(associado.getDataDocumento());
                }
                if (associado.getNomeMae() != null) {
                    existingAssociado.setNomeMae(associado.getNomeMae());
                }
                if (associado.getNomePai() != null) {
                    existingAssociado.setNomePai(associado.getNomePai());
                }
                if (associado.getEmail() != null) {
                    existingAssociado.setEmail(associado.getEmail());
                }
                if (associado.getTelefone() != null) {
                    existingAssociado.setTelefone(associado.getTelefone());
                }
                if (associado.getSitaucao() != null) {
                    existingAssociado.setSitaucao(associado.getSitaucao());
                }

                return existingAssociado;
            })
            .map(associadoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, associado.getId().toString())
        );
    }

    /**
     * {@code GET  /associados} : get all the associados.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of associados in body.
     */
    @GetMapping("/associados")
    public List<Associado> getAllAssociados() {
        log.debug("REST request to get all Associados");
        return associadoRepository.findAll();
    }

    /**
     * {@code GET  /associados/:id} : get the "id" associado.
     *
     * @param id the id of the associado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the associado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/associados/{id}")
    public ResponseEntity<Associado> getAssociado(@PathVariable Long id) {
        log.debug("REST request to get Associado : {}", id);
        Optional<Associado> associado = associadoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(associado);
    }

    /**
     * {@code DELETE  /associados/:id} : delete the "id" associado.
     *
     * @param id the id of the associado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/associados/{id}")
    public ResponseEntity<Void> deleteAssociado(@PathVariable Long id) {
        log.debug("REST request to delete Associado : {}", id);
        associadoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
