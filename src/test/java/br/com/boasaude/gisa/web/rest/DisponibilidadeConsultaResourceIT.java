package br.com.boasaude.gisa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.boasaude.gisa.IntegrationTest;
import br.com.boasaude.gisa.domain.DisponibilidadeConsulta;
import br.com.boasaude.gisa.repository.DisponibilidadeConsultaRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DisponibilidadeConsultaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisponibilidadeConsultaResourceIT {

    private static final Instant DEFAULT_HORA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_HORA_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/disponibilidade-consultas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DisponibilidadeConsultaRepository disponibilidadeConsultaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisponibilidadeConsultaMockMvc;

    private DisponibilidadeConsulta disponibilidadeConsulta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisponibilidadeConsulta createEntity(EntityManager em) {
        DisponibilidadeConsulta disponibilidadeConsulta = new DisponibilidadeConsulta()
            .horaInicio(DEFAULT_HORA_INICIO)
            .horaFim(DEFAULT_HORA_FIM);
        return disponibilidadeConsulta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisponibilidadeConsulta createUpdatedEntity(EntityManager em) {
        DisponibilidadeConsulta disponibilidadeConsulta = new DisponibilidadeConsulta()
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFim(UPDATED_HORA_FIM);
        return disponibilidadeConsulta;
    }

    @BeforeEach
    public void initTest() {
        disponibilidadeConsulta = createEntity(em);
    }

    @Test
    @Transactional
    void createDisponibilidadeConsulta() throws Exception {
        int databaseSizeBeforeCreate = disponibilidadeConsultaRepository.findAll().size();
        // Create the DisponibilidadeConsulta
        restDisponibilidadeConsultaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeConsulta))
            )
            .andExpect(status().isCreated());

        // Validate the DisponibilidadeConsulta in the database
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeCreate + 1);
        DisponibilidadeConsulta testDisponibilidadeConsulta = disponibilidadeConsultaList.get(disponibilidadeConsultaList.size() - 1);
        assertThat(testDisponibilidadeConsulta.getHoraInicio()).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testDisponibilidadeConsulta.getHoraFim()).isEqualTo(DEFAULT_HORA_FIM);
    }

    @Test
    @Transactional
    void createDisponibilidadeConsultaWithExistingId() throws Exception {
        // Create the DisponibilidadeConsulta with an existing ID
        disponibilidadeConsulta.setId(1L);

        int databaseSizeBeforeCreate = disponibilidadeConsultaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisponibilidadeConsultaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeConsulta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisponibilidadeConsulta in the database
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDisponibilidadeConsultas() throws Exception {
        // Initialize the database
        disponibilidadeConsultaRepository.saveAndFlush(disponibilidadeConsulta);

        // Get all the disponibilidadeConsultaList
        restDisponibilidadeConsultaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilidadeConsulta.getId().intValue())))
            .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].horaFim").value(hasItem(DEFAULT_HORA_FIM.toString())));
    }

    @Test
    @Transactional
    void getDisponibilidadeConsulta() throws Exception {
        // Initialize the database
        disponibilidadeConsultaRepository.saveAndFlush(disponibilidadeConsulta);

        // Get the disponibilidadeConsulta
        restDisponibilidadeConsultaMockMvc
            .perform(get(ENTITY_API_URL_ID, disponibilidadeConsulta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disponibilidadeConsulta.getId().intValue()))
            .andExpect(jsonPath("$.horaInicio").value(DEFAULT_HORA_INICIO.toString()))
            .andExpect(jsonPath("$.horaFim").value(DEFAULT_HORA_FIM.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDisponibilidadeConsulta() throws Exception {
        // Get the disponibilidadeConsulta
        restDisponibilidadeConsultaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDisponibilidadeConsulta() throws Exception {
        // Initialize the database
        disponibilidadeConsultaRepository.saveAndFlush(disponibilidadeConsulta);

        int databaseSizeBeforeUpdate = disponibilidadeConsultaRepository.findAll().size();

        // Update the disponibilidadeConsulta
        DisponibilidadeConsulta updatedDisponibilidadeConsulta = disponibilidadeConsultaRepository
            .findById(disponibilidadeConsulta.getId())
            .get();
        // Disconnect from session so that the updates on updatedDisponibilidadeConsulta are not directly saved in db
        em.detach(updatedDisponibilidadeConsulta);
        updatedDisponibilidadeConsulta.horaInicio(UPDATED_HORA_INICIO).horaFim(UPDATED_HORA_FIM);

        restDisponibilidadeConsultaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDisponibilidadeConsulta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDisponibilidadeConsulta))
            )
            .andExpect(status().isOk());

        // Validate the DisponibilidadeConsulta in the database
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeUpdate);
        DisponibilidadeConsulta testDisponibilidadeConsulta = disponibilidadeConsultaList.get(disponibilidadeConsultaList.size() - 1);
        assertThat(testDisponibilidadeConsulta.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testDisponibilidadeConsulta.getHoraFim()).isEqualTo(UPDATED_HORA_FIM);
    }

    @Test
    @Transactional
    void putNonExistingDisponibilidadeConsulta() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeConsultaRepository.findAll().size();
        disponibilidadeConsulta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibilidadeConsultaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disponibilidadeConsulta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeConsulta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisponibilidadeConsulta in the database
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisponibilidadeConsulta() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeConsultaRepository.findAll().size();
        disponibilidadeConsulta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadeConsultaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeConsulta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisponibilidadeConsulta in the database
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisponibilidadeConsulta() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeConsultaRepository.findAll().size();
        disponibilidadeConsulta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadeConsultaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeConsulta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisponibilidadeConsulta in the database
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisponibilidadeConsultaWithPatch() throws Exception {
        // Initialize the database
        disponibilidadeConsultaRepository.saveAndFlush(disponibilidadeConsulta);

        int databaseSizeBeforeUpdate = disponibilidadeConsultaRepository.findAll().size();

        // Update the disponibilidadeConsulta using partial update
        DisponibilidadeConsulta partialUpdatedDisponibilidadeConsulta = new DisponibilidadeConsulta();
        partialUpdatedDisponibilidadeConsulta.setId(disponibilidadeConsulta.getId());

        restDisponibilidadeConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilidadeConsulta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisponibilidadeConsulta))
            )
            .andExpect(status().isOk());

        // Validate the DisponibilidadeConsulta in the database
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeUpdate);
        DisponibilidadeConsulta testDisponibilidadeConsulta = disponibilidadeConsultaList.get(disponibilidadeConsultaList.size() - 1);
        assertThat(testDisponibilidadeConsulta.getHoraInicio()).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testDisponibilidadeConsulta.getHoraFim()).isEqualTo(DEFAULT_HORA_FIM);
    }

    @Test
    @Transactional
    void fullUpdateDisponibilidadeConsultaWithPatch() throws Exception {
        // Initialize the database
        disponibilidadeConsultaRepository.saveAndFlush(disponibilidadeConsulta);

        int databaseSizeBeforeUpdate = disponibilidadeConsultaRepository.findAll().size();

        // Update the disponibilidadeConsulta using partial update
        DisponibilidadeConsulta partialUpdatedDisponibilidadeConsulta = new DisponibilidadeConsulta();
        partialUpdatedDisponibilidadeConsulta.setId(disponibilidadeConsulta.getId());

        partialUpdatedDisponibilidadeConsulta.horaInicio(UPDATED_HORA_INICIO).horaFim(UPDATED_HORA_FIM);

        restDisponibilidadeConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilidadeConsulta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisponibilidadeConsulta))
            )
            .andExpect(status().isOk());

        // Validate the DisponibilidadeConsulta in the database
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeUpdate);
        DisponibilidadeConsulta testDisponibilidadeConsulta = disponibilidadeConsultaList.get(disponibilidadeConsultaList.size() - 1);
        assertThat(testDisponibilidadeConsulta.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testDisponibilidadeConsulta.getHoraFim()).isEqualTo(UPDATED_HORA_FIM);
    }

    @Test
    @Transactional
    void patchNonExistingDisponibilidadeConsulta() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeConsultaRepository.findAll().size();
        disponibilidadeConsulta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibilidadeConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disponibilidadeConsulta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeConsulta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisponibilidadeConsulta in the database
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisponibilidadeConsulta() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeConsultaRepository.findAll().size();
        disponibilidadeConsulta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadeConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeConsulta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisponibilidadeConsulta in the database
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisponibilidadeConsulta() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeConsultaRepository.findAll().size();
        disponibilidadeConsulta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadeConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeConsulta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisponibilidadeConsulta in the database
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisponibilidadeConsulta() throws Exception {
        // Initialize the database
        disponibilidadeConsultaRepository.saveAndFlush(disponibilidadeConsulta);

        int databaseSizeBeforeDelete = disponibilidadeConsultaRepository.findAll().size();

        // Delete the disponibilidadeConsulta
        restDisponibilidadeConsultaMockMvc
            .perform(delete(ENTITY_API_URL_ID, disponibilidadeConsulta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DisponibilidadeConsulta> disponibilidadeConsultaList = disponibilidadeConsultaRepository.findAll();
        assertThat(disponibilidadeConsultaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
