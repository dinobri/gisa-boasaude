package br.com.boasaude.gisa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.boasaude.gisa.IntegrationTest;
import br.com.boasaude.gisa.domain.DisponibilidadeExame;
import br.com.boasaude.gisa.repository.DisponibilidadeExameRepository;
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
 * Integration tests for the {@link DisponibilidadeExameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisponibilidadeExameResourceIT {

    private static final Instant DEFAULT_HORA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_HORA_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/disponibilidade-exames";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DisponibilidadeExameRepository disponibilidadeExameRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisponibilidadeExameMockMvc;

    private DisponibilidadeExame disponibilidadeExame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisponibilidadeExame createEntity(EntityManager em) {
        DisponibilidadeExame disponibilidadeExame = new DisponibilidadeExame().horaInicio(DEFAULT_HORA_INICIO).horaFim(DEFAULT_HORA_FIM);
        return disponibilidadeExame;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisponibilidadeExame createUpdatedEntity(EntityManager em) {
        DisponibilidadeExame disponibilidadeExame = new DisponibilidadeExame().horaInicio(UPDATED_HORA_INICIO).horaFim(UPDATED_HORA_FIM);
        return disponibilidadeExame;
    }

    @BeforeEach
    public void initTest() {
        disponibilidadeExame = createEntity(em);
    }

    @Test
    @Transactional
    void createDisponibilidadeExame() throws Exception {
        int databaseSizeBeforeCreate = disponibilidadeExameRepository.findAll().size();
        // Create the DisponibilidadeExame
        restDisponibilidadeExameMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeExame))
            )
            .andExpect(status().isCreated());

        // Validate the DisponibilidadeExame in the database
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeCreate + 1);
        DisponibilidadeExame testDisponibilidadeExame = disponibilidadeExameList.get(disponibilidadeExameList.size() - 1);
        assertThat(testDisponibilidadeExame.getHoraInicio()).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testDisponibilidadeExame.getHoraFim()).isEqualTo(DEFAULT_HORA_FIM);
    }

    @Test
    @Transactional
    void createDisponibilidadeExameWithExistingId() throws Exception {
        // Create the DisponibilidadeExame with an existing ID
        disponibilidadeExame.setId(1L);

        int databaseSizeBeforeCreate = disponibilidadeExameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisponibilidadeExameMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeExame))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisponibilidadeExame in the database
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDisponibilidadeExames() throws Exception {
        // Initialize the database
        disponibilidadeExameRepository.saveAndFlush(disponibilidadeExame);

        // Get all the disponibilidadeExameList
        restDisponibilidadeExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilidadeExame.getId().intValue())))
            .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].horaFim").value(hasItem(DEFAULT_HORA_FIM.toString())));
    }

    @Test
    @Transactional
    void getDisponibilidadeExame() throws Exception {
        // Initialize the database
        disponibilidadeExameRepository.saveAndFlush(disponibilidadeExame);

        // Get the disponibilidadeExame
        restDisponibilidadeExameMockMvc
            .perform(get(ENTITY_API_URL_ID, disponibilidadeExame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disponibilidadeExame.getId().intValue()))
            .andExpect(jsonPath("$.horaInicio").value(DEFAULT_HORA_INICIO.toString()))
            .andExpect(jsonPath("$.horaFim").value(DEFAULT_HORA_FIM.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDisponibilidadeExame() throws Exception {
        // Get the disponibilidadeExame
        restDisponibilidadeExameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDisponibilidadeExame() throws Exception {
        // Initialize the database
        disponibilidadeExameRepository.saveAndFlush(disponibilidadeExame);

        int databaseSizeBeforeUpdate = disponibilidadeExameRepository.findAll().size();

        // Update the disponibilidadeExame
        DisponibilidadeExame updatedDisponibilidadeExame = disponibilidadeExameRepository.findById(disponibilidadeExame.getId()).get();
        // Disconnect from session so that the updates on updatedDisponibilidadeExame are not directly saved in db
        em.detach(updatedDisponibilidadeExame);
        updatedDisponibilidadeExame.horaInicio(UPDATED_HORA_INICIO).horaFim(UPDATED_HORA_FIM);

        restDisponibilidadeExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDisponibilidadeExame.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDisponibilidadeExame))
            )
            .andExpect(status().isOk());

        // Validate the DisponibilidadeExame in the database
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeUpdate);
        DisponibilidadeExame testDisponibilidadeExame = disponibilidadeExameList.get(disponibilidadeExameList.size() - 1);
        assertThat(testDisponibilidadeExame.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testDisponibilidadeExame.getHoraFim()).isEqualTo(UPDATED_HORA_FIM);
    }

    @Test
    @Transactional
    void putNonExistingDisponibilidadeExame() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeExameRepository.findAll().size();
        disponibilidadeExame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibilidadeExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disponibilidadeExame.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeExame))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisponibilidadeExame in the database
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisponibilidadeExame() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeExameRepository.findAll().size();
        disponibilidadeExame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadeExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeExame))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisponibilidadeExame in the database
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisponibilidadeExame() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeExameRepository.findAll().size();
        disponibilidadeExame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadeExameMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disponibilidadeExame))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisponibilidadeExame in the database
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisponibilidadeExameWithPatch() throws Exception {
        // Initialize the database
        disponibilidadeExameRepository.saveAndFlush(disponibilidadeExame);

        int databaseSizeBeforeUpdate = disponibilidadeExameRepository.findAll().size();

        // Update the disponibilidadeExame using partial update
        DisponibilidadeExame partialUpdatedDisponibilidadeExame = new DisponibilidadeExame();
        partialUpdatedDisponibilidadeExame.setId(disponibilidadeExame.getId());

        partialUpdatedDisponibilidadeExame.horaFim(UPDATED_HORA_FIM);

        restDisponibilidadeExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilidadeExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisponibilidadeExame))
            )
            .andExpect(status().isOk());

        // Validate the DisponibilidadeExame in the database
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeUpdate);
        DisponibilidadeExame testDisponibilidadeExame = disponibilidadeExameList.get(disponibilidadeExameList.size() - 1);
        assertThat(testDisponibilidadeExame.getHoraInicio()).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testDisponibilidadeExame.getHoraFim()).isEqualTo(UPDATED_HORA_FIM);
    }

    @Test
    @Transactional
    void fullUpdateDisponibilidadeExameWithPatch() throws Exception {
        // Initialize the database
        disponibilidadeExameRepository.saveAndFlush(disponibilidadeExame);

        int databaseSizeBeforeUpdate = disponibilidadeExameRepository.findAll().size();

        // Update the disponibilidadeExame using partial update
        DisponibilidadeExame partialUpdatedDisponibilidadeExame = new DisponibilidadeExame();
        partialUpdatedDisponibilidadeExame.setId(disponibilidadeExame.getId());

        partialUpdatedDisponibilidadeExame.horaInicio(UPDATED_HORA_INICIO).horaFim(UPDATED_HORA_FIM);

        restDisponibilidadeExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilidadeExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisponibilidadeExame))
            )
            .andExpect(status().isOk());

        // Validate the DisponibilidadeExame in the database
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeUpdate);
        DisponibilidadeExame testDisponibilidadeExame = disponibilidadeExameList.get(disponibilidadeExameList.size() - 1);
        assertThat(testDisponibilidadeExame.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testDisponibilidadeExame.getHoraFim()).isEqualTo(UPDATED_HORA_FIM);
    }

    @Test
    @Transactional
    void patchNonExistingDisponibilidadeExame() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeExameRepository.findAll().size();
        disponibilidadeExame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibilidadeExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disponibilidadeExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeExame))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisponibilidadeExame in the database
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisponibilidadeExame() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeExameRepository.findAll().size();
        disponibilidadeExame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadeExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeExame))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisponibilidadeExame in the database
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisponibilidadeExame() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeExameRepository.findAll().size();
        disponibilidadeExame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadeExameMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disponibilidadeExame))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisponibilidadeExame in the database
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisponibilidadeExame() throws Exception {
        // Initialize the database
        disponibilidadeExameRepository.saveAndFlush(disponibilidadeExame);

        int databaseSizeBeforeDelete = disponibilidadeExameRepository.findAll().size();

        // Delete the disponibilidadeExame
        restDisponibilidadeExameMockMvc
            .perform(delete(ENTITY_API_URL_ID, disponibilidadeExame.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DisponibilidadeExame> disponibilidadeExameList = disponibilidadeExameRepository.findAll();
        assertThat(disponibilidadeExameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
