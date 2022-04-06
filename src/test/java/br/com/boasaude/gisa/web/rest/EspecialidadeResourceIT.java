package br.com.boasaude.gisa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.boasaude.gisa.IntegrationTest;
import br.com.boasaude.gisa.domain.Especialidade;
import br.com.boasaude.gisa.repository.EspecialidadeRepository;
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
 * Integration tests for the {@link EspecialidadeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EspecialidadeResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/especialidades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEspecialidadeMockMvc;

    private Especialidade especialidade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialidade createEntity(EntityManager em) {
        Especialidade especialidade = new Especialidade().nome(DEFAULT_NOME);
        return especialidade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialidade createUpdatedEntity(EntityManager em) {
        Especialidade especialidade = new Especialidade().nome(UPDATED_NOME);
        return especialidade;
    }

    @BeforeEach
    public void initTest() {
        especialidade = createEntity(em);
    }

    @Test
    @Transactional
    void createEspecialidade() throws Exception {
        int databaseSizeBeforeCreate = especialidadeRepository.findAll().size();
        // Create the Especialidade
        restEspecialidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(especialidade)))
            .andExpect(status().isCreated());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeCreate + 1);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void createEspecialidadeWithExistingId() throws Exception {
        // Create the Especialidade with an existing ID
        especialidade.setId(1L);

        int databaseSizeBeforeCreate = especialidadeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEspecialidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(especialidade)))
            .andExpect(status().isBadRequest());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEspecialidades() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        // Get all the especialidadeList
        restEspecialidadeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getEspecialidade() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        // Get the especialidade
        restEspecialidadeMockMvc
            .perform(get(ENTITY_API_URL_ID, especialidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(especialidade.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingEspecialidade() throws Exception {
        // Get the especialidade
        restEspecialidadeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEspecialidade() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();

        // Update the especialidade
        Especialidade updatedEspecialidade = especialidadeRepository.findById(especialidade.getId()).get();
        // Disconnect from session so that the updates on updatedEspecialidade are not directly saved in db
        em.detach(updatedEspecialidade);
        updatedEspecialidade.nome(UPDATED_NOME);

        restEspecialidadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEspecialidade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEspecialidade))
            )
            .andExpect(status().isOk());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void putNonExistingEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();
        especialidade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecialidadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, especialidade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(especialidade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();
        especialidade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(especialidade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();
        especialidade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(especialidade)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEspecialidadeWithPatch() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();

        // Update the especialidade using partial update
        Especialidade partialUpdatedEspecialidade = new Especialidade();
        partialUpdatedEspecialidade.setId(especialidade.getId());

        restEspecialidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspecialidade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEspecialidade))
            )
            .andExpect(status().isOk());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void fullUpdateEspecialidadeWithPatch() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();

        // Update the especialidade using partial update
        Especialidade partialUpdatedEspecialidade = new Especialidade();
        partialUpdatedEspecialidade.setId(especialidade.getId());

        partialUpdatedEspecialidade.nome(UPDATED_NOME);

        restEspecialidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspecialidade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEspecialidade))
            )
            .andExpect(status().isOk());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void patchNonExistingEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();
        especialidade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecialidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, especialidade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(especialidade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();
        especialidade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(especialidade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();
        especialidade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(especialidade))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEspecialidade() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        int databaseSizeBeforeDelete = especialidadeRepository.findAll().size();

        // Delete the especialidade
        restEspecialidadeMockMvc
            .perform(delete(ENTITY_API_URL_ID, especialidade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
