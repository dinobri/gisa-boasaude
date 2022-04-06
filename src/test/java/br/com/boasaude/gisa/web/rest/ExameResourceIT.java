package br.com.boasaude.gisa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.boasaude.gisa.IntegrationTest;
import br.com.boasaude.gisa.domain.Exame;
import br.com.boasaude.gisa.domain.enumeration.SituacaoAtendimento;
import br.com.boasaude.gisa.repository.ExameRepository;
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
 * Integration tests for the {@link ExameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExameResourceIT {

    private static final SituacaoAtendimento DEFAULT_SITUACAO = SituacaoAtendimento.AGENDADO;
    private static final SituacaoAtendimento UPDATED_SITUACAO = SituacaoAtendimento.CANCELADO;

    private static final String ENTITY_API_URL = "/api/exames";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExameMockMvc;

    private Exame exame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exame createEntity(EntityManager em) {
        Exame exame = new Exame().situacao(DEFAULT_SITUACAO);
        return exame;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exame createUpdatedEntity(EntityManager em) {
        Exame exame = new Exame().situacao(UPDATED_SITUACAO);
        return exame;
    }

    @BeforeEach
    public void initTest() {
        exame = createEntity(em);
    }

    @Test
    @Transactional
    void createExame() throws Exception {
        int databaseSizeBeforeCreate = exameRepository.findAll().size();
        // Create the Exame
        restExameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exame)))
            .andExpect(status().isCreated());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeCreate + 1);
        Exame testExame = exameList.get(exameList.size() - 1);
        assertThat(testExame.getSituacao()).isEqualTo(DEFAULT_SITUACAO);
    }

    @Test
    @Transactional
    void createExameWithExistingId() throws Exception {
        // Create the Exame with an existing ID
        exame.setId(1L);

        int databaseSizeBeforeCreate = exameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exame)))
            .andExpect(status().isBadRequest());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExames() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList
        restExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exame.getId().intValue())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())));
    }

    @Test
    @Transactional
    void getExame() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get the exame
        restExameMockMvc
            .perform(get(ENTITY_API_URL_ID, exame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exame.getId().intValue()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExame() throws Exception {
        // Get the exame
        restExameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExame() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        int databaseSizeBeforeUpdate = exameRepository.findAll().size();

        // Update the exame
        Exame updatedExame = exameRepository.findById(exame.getId()).get();
        // Disconnect from session so that the updates on updatedExame are not directly saved in db
        em.detach(updatedExame);
        updatedExame.situacao(UPDATED_SITUACAO);

        restExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExame.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExame))
            )
            .andExpect(status().isOk());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
        Exame testExame = exameList.get(exameList.size() - 1);
        assertThat(testExame.getSituacao()).isEqualTo(UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void putNonExistingExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exame.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exame))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exame))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exame)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExameWithPatch() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        int databaseSizeBeforeUpdate = exameRepository.findAll().size();

        // Update the exame using partial update
        Exame partialUpdatedExame = new Exame();
        partialUpdatedExame.setId(exame.getId());

        partialUpdatedExame.situacao(UPDATED_SITUACAO);

        restExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExame))
            )
            .andExpect(status().isOk());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
        Exame testExame = exameList.get(exameList.size() - 1);
        assertThat(testExame.getSituacao()).isEqualTo(UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void fullUpdateExameWithPatch() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        int databaseSizeBeforeUpdate = exameRepository.findAll().size();

        // Update the exame using partial update
        Exame partialUpdatedExame = new Exame();
        partialUpdatedExame.setId(exame.getId());

        partialUpdatedExame.situacao(UPDATED_SITUACAO);

        restExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExame))
            )
            .andExpect(status().isOk());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
        Exame testExame = exameList.get(exameList.size() - 1);
        assertThat(testExame.getSituacao()).isEqualTo(UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void patchNonExistingExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exame))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exame))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exame)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExame() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        int databaseSizeBeforeDelete = exameRepository.findAll().size();

        // Delete the exame
        restExameMockMvc
            .perform(delete(ENTITY_API_URL_ID, exame.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
