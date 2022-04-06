package br.com.boasaude.gisa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.boasaude.gisa.IntegrationTest;
import br.com.boasaude.gisa.domain.TipoExame;
import br.com.boasaude.gisa.repository.TipoExameRepository;
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
 * Integration tests for the {@link TipoExameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoExameResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-exames";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoExameRepository tipoExameRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoExameMockMvc;

    private TipoExame tipoExame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoExame createEntity(EntityManager em) {
        TipoExame tipoExame = new TipoExame().nome(DEFAULT_NOME);
        return tipoExame;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoExame createUpdatedEntity(EntityManager em) {
        TipoExame tipoExame = new TipoExame().nome(UPDATED_NOME);
        return tipoExame;
    }

    @BeforeEach
    public void initTest() {
        tipoExame = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoExame() throws Exception {
        int databaseSizeBeforeCreate = tipoExameRepository.findAll().size();
        // Create the TipoExame
        restTipoExameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoExame)))
            .andExpect(status().isCreated());

        // Validate the TipoExame in the database
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeCreate + 1);
        TipoExame testTipoExame = tipoExameList.get(tipoExameList.size() - 1);
        assertThat(testTipoExame.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void createTipoExameWithExistingId() throws Exception {
        // Create the TipoExame with an existing ID
        tipoExame.setId(1L);

        int databaseSizeBeforeCreate = tipoExameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoExameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoExame)))
            .andExpect(status().isBadRequest());

        // Validate the TipoExame in the database
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTipoExames() throws Exception {
        // Initialize the database
        tipoExameRepository.saveAndFlush(tipoExame);

        // Get all the tipoExameList
        restTipoExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoExame.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getTipoExame() throws Exception {
        // Initialize the database
        tipoExameRepository.saveAndFlush(tipoExame);

        // Get the tipoExame
        restTipoExameMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoExame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoExame.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingTipoExame() throws Exception {
        // Get the tipoExame
        restTipoExameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoExame() throws Exception {
        // Initialize the database
        tipoExameRepository.saveAndFlush(tipoExame);

        int databaseSizeBeforeUpdate = tipoExameRepository.findAll().size();

        // Update the tipoExame
        TipoExame updatedTipoExame = tipoExameRepository.findById(tipoExame.getId()).get();
        // Disconnect from session so that the updates on updatedTipoExame are not directly saved in db
        em.detach(updatedTipoExame);
        updatedTipoExame.nome(UPDATED_NOME);

        restTipoExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTipoExame.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTipoExame))
            )
            .andExpect(status().isOk());

        // Validate the TipoExame in the database
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeUpdate);
        TipoExame testTipoExame = tipoExameList.get(tipoExameList.size() - 1);
        assertThat(testTipoExame.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void putNonExistingTipoExame() throws Exception {
        int databaseSizeBeforeUpdate = tipoExameRepository.findAll().size();
        tipoExame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoExame.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoExame))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoExame in the database
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoExame() throws Exception {
        int databaseSizeBeforeUpdate = tipoExameRepository.findAll().size();
        tipoExame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoExame))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoExame in the database
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoExame() throws Exception {
        int databaseSizeBeforeUpdate = tipoExameRepository.findAll().size();
        tipoExame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoExameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoExame)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoExame in the database
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoExameWithPatch() throws Exception {
        // Initialize the database
        tipoExameRepository.saveAndFlush(tipoExame);

        int databaseSizeBeforeUpdate = tipoExameRepository.findAll().size();

        // Update the tipoExame using partial update
        TipoExame partialUpdatedTipoExame = new TipoExame();
        partialUpdatedTipoExame.setId(tipoExame.getId());

        partialUpdatedTipoExame.nome(UPDATED_NOME);

        restTipoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoExame))
            )
            .andExpect(status().isOk());

        // Validate the TipoExame in the database
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeUpdate);
        TipoExame testTipoExame = tipoExameList.get(tipoExameList.size() - 1);
        assertThat(testTipoExame.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void fullUpdateTipoExameWithPatch() throws Exception {
        // Initialize the database
        tipoExameRepository.saveAndFlush(tipoExame);

        int databaseSizeBeforeUpdate = tipoExameRepository.findAll().size();

        // Update the tipoExame using partial update
        TipoExame partialUpdatedTipoExame = new TipoExame();
        partialUpdatedTipoExame.setId(tipoExame.getId());

        partialUpdatedTipoExame.nome(UPDATED_NOME);

        restTipoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoExame))
            )
            .andExpect(status().isOk());

        // Validate the TipoExame in the database
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeUpdate);
        TipoExame testTipoExame = tipoExameList.get(tipoExameList.size() - 1);
        assertThat(testTipoExame.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void patchNonExistingTipoExame() throws Exception {
        int databaseSizeBeforeUpdate = tipoExameRepository.findAll().size();
        tipoExame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoExame))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoExame in the database
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoExame() throws Exception {
        int databaseSizeBeforeUpdate = tipoExameRepository.findAll().size();
        tipoExame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoExame))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoExame in the database
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoExame() throws Exception {
        int databaseSizeBeforeUpdate = tipoExameRepository.findAll().size();
        tipoExame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoExameMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoExame))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoExame in the database
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoExame() throws Exception {
        // Initialize the database
        tipoExameRepository.saveAndFlush(tipoExame);

        int databaseSizeBeforeDelete = tipoExameRepository.findAll().size();

        // Delete the tipoExame
        restTipoExameMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoExame.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoExame> tipoExameList = tipoExameRepository.findAll();
        assertThat(tipoExameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
