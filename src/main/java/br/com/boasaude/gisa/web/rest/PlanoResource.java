package br.com.boasaude.gisa.web.rest;

import br.com.boasaude.gisa.domain.Plano;
import br.com.boasaude.gisa.repository.PlanoRepository;
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
 * REST controller for managing {@link br.com.boasaude.gisa.domain.Plano}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlanoResource {

    private final Logger log = LoggerFactory.getLogger(PlanoResource.class);

    private static final String ENTITY_NAME = "plano";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanoRepository planoRepository;

    public PlanoResource(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    /**
     * {@code POST  /planos} : Create a new plano.
     *
     * @param plano the plano to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plano, or with status {@code 400 (Bad Request)} if the plano has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/planos")
    public ResponseEntity<Plano> createPlano(@RequestBody Plano plano) throws URISyntaxException {
        log.debug("REST request to save Plano : {}", plano);
        if (plano.getId() != null) {
            throw new BadRequestAlertException("A new plano cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Plano result = planoRepository.save(plano);
        return ResponseEntity
            .created(new URI("/api/planos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /planos/:id} : Updates an existing plano.
     *
     * @param id the id of the plano to save.
     * @param plano the plano to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plano,
     * or with status {@code 400 (Bad Request)} if the plano is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plano couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/planos/{id}")
    public ResponseEntity<Plano> updatePlano(@PathVariable(value = "id", required = false) final Long id, @RequestBody Plano plano)
        throws URISyntaxException {
        log.debug("REST request to update Plano : {}, {}", id, plano);
        if (plano.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plano.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Plano result = planoRepository.save(plano);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, plano.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /planos/:id} : Partial updates given fields of an existing plano, field will ignore if it is null
     *
     * @param id the id of the plano to save.
     * @param plano the plano to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plano,
     * or with status {@code 400 (Bad Request)} if the plano is not valid,
     * or with status {@code 404 (Not Found)} if the plano is not found,
     * or with status {@code 500 (Internal Server Error)} if the plano couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/planos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Plano> partialUpdatePlano(@PathVariable(value = "id", required = false) final Long id, @RequestBody Plano plano)
        throws URISyntaxException {
        log.debug("REST request to partial update Plano partially : {}, {}", id, plano);
        if (plano.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plano.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Plano> result = planoRepository
            .findById(plano.getId())
            .map(existingPlano -> {
                if (plano.getNome() != null) {
                    existingPlano.setNome(plano.getNome());
                }
                if (plano.getCodigoANS() != null) {
                    existingPlano.setCodigoANS(plano.getCodigoANS());
                }
                if (plano.getCategoria() != null) {
                    existingPlano.setCategoria(plano.getCategoria());
                }
                if (plano.getTipo() != null) {
                    existingPlano.setTipo(plano.getTipo());
                }
                if (plano.getOdonto() != null) {
                    existingPlano.setOdonto(plano.getOdonto());
                }
                if (plano.getIdadeMin() != null) {
                    existingPlano.setIdadeMin(plano.getIdadeMin());
                }
                if (plano.getIdadeMax() != null) {
                    existingPlano.setIdadeMax(plano.getIdadeMax());
                }
                if (plano.getQuantidadeConsultasAno() != null) {
                    existingPlano.setQuantidadeConsultasAno(plano.getQuantidadeConsultasAno());
                }
                if (plano.getQuanatidadeExamesAno() != null) {
                    existingPlano.setQuanatidadeExamesAno(plano.getQuanatidadeExamesAno());
                }
                if (plano.getValorMensalidade() != null) {
                    existingPlano.setValorMensalidade(plano.getValorMensalidade());
                }

                return existingPlano;
            })
            .map(planoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, plano.getId().toString())
        );
    }

    /**
     * {@code GET  /planos} : get all the planos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planos in body.
     */
    @GetMapping("/planos")
    public List<Plano> getAllPlanos() {
        log.debug("REST request to get all Planos");
        return planoRepository.findAll();
    }

    /**
     * {@code GET  /planos/:id} : get the "id" plano.
     *
     * @param id the id of the plano to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plano, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/planos/{id}")
    public ResponseEntity<Plano> getPlano(@PathVariable Long id) {
        log.debug("REST request to get Plano : {}", id);
        Optional<Plano> plano = planoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(plano);
    }

    /**
     * {@code DELETE  /planos/:id} : delete the "id" plano.
     *
     * @param id the id of the plano to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/planos/{id}")
    public ResponseEntity<Void> deletePlano(@PathVariable Long id) {
        log.debug("REST request to delete Plano : {}", id);
        planoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
