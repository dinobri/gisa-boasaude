package br.com.boasaude.gisa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.boasaude.gisa.IntegrationTest;
import br.com.boasaude.gisa.domain.Conveniado;
import br.com.boasaude.gisa.repository.ConveniadoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConveniadoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ConveniadoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/conveniados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConveniadoRepository conveniadoRepository;

    @Mock
    private ConveniadoRepository conveniadoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConveniadoMockMvc;

    private Conveniado conveniado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conveniado createEntity(EntityManager em) {
        Conveniado conveniado = new Conveniado().nome(DEFAULT_NOME).telefone(DEFAULT_TELEFONE);
        return conveniado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conveniado createUpdatedEntity(EntityManager em) {
        Conveniado conveniado = new Conveniado().nome(UPDATED_NOME).telefone(UPDATED_TELEFONE);
        return conveniado;
    }

    @BeforeEach
    public void initTest() {
        conveniado = createEntity(em);
    }

    @Test
    @Transactional
    void createConveniado() throws Exception {
        int databaseSizeBeforeCreate = conveniadoRepository.findAll().size();
        // Create the Conveniado
        restConveniadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conveniado)))
            .andExpect(status().isCreated());

        // Validate the Conveniado in the database
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeCreate + 1);
        Conveniado testConveniado = conveniadoList.get(conveniadoList.size() - 1);
        assertThat(testConveniado.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testConveniado.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void createConveniadoWithExistingId() throws Exception {
        // Create the Conveniado with an existing ID
        conveniado.setId(1L);

        int databaseSizeBeforeCreate = conveniadoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConveniadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conveniado)))
            .andExpect(status().isBadRequest());

        // Validate the Conveniado in the database
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConveniados() throws Exception {
        // Initialize the database
        conveniadoRepository.saveAndFlush(conveniado);

        // Get all the conveniadoList
        restConveniadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conveniado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConveniadosWithEagerRelationshipsIsEnabled() throws Exception {
        when(conveniadoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConveniadoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(conveniadoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConveniadosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(conveniadoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConveniadoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(conveniadoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getConveniado() throws Exception {
        // Initialize the database
        conveniadoRepository.saveAndFlush(conveniado);

        // Get the conveniado
        restConveniadoMockMvc
            .perform(get(ENTITY_API_URL_ID, conveniado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conveniado.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE));
    }

    @Test
    @Transactional
    void getNonExistingConveniado() throws Exception {
        // Get the conveniado
        restConveniadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConveniado() throws Exception {
        // Initialize the database
        conveniadoRepository.saveAndFlush(conveniado);

        int databaseSizeBeforeUpdate = conveniadoRepository.findAll().size();

        // Update the conveniado
        Conveniado updatedConveniado = conveniadoRepository.findById(conveniado.getId()).get();
        // Disconnect from session so that the updates on updatedConveniado are not directly saved in db
        em.detach(updatedConveniado);
        updatedConveniado.nome(UPDATED_NOME).telefone(UPDATED_TELEFONE);

        restConveniadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConveniado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConveniado))
            )
            .andExpect(status().isOk());

        // Validate the Conveniado in the database
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeUpdate);
        Conveniado testConveniado = conveniadoList.get(conveniadoList.size() - 1);
        assertThat(testConveniado.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testConveniado.getTelefone()).isEqualTo(UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void putNonExistingConveniado() throws Exception {
        int databaseSizeBeforeUpdate = conveniadoRepository.findAll().size();
        conveniado.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConveniadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conveniado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conveniado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conveniado in the database
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConveniado() throws Exception {
        int databaseSizeBeforeUpdate = conveniadoRepository.findAll().size();
        conveniado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConveniadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conveniado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conveniado in the database
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConveniado() throws Exception {
        int databaseSizeBeforeUpdate = conveniadoRepository.findAll().size();
        conveniado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConveniadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conveniado)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conveniado in the database
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConveniadoWithPatch() throws Exception {
        // Initialize the database
        conveniadoRepository.saveAndFlush(conveniado);

        int databaseSizeBeforeUpdate = conveniadoRepository.findAll().size();

        // Update the conveniado using partial update
        Conveniado partialUpdatedConveniado = new Conveniado();
        partialUpdatedConveniado.setId(conveniado.getId());

        restConveniadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConveniado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConveniado))
            )
            .andExpect(status().isOk());

        // Validate the Conveniado in the database
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeUpdate);
        Conveniado testConveniado = conveniadoList.get(conveniadoList.size() - 1);
        assertThat(testConveniado.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testConveniado.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void fullUpdateConveniadoWithPatch() throws Exception {
        // Initialize the database
        conveniadoRepository.saveAndFlush(conveniado);

        int databaseSizeBeforeUpdate = conveniadoRepository.findAll().size();

        // Update the conveniado using partial update
        Conveniado partialUpdatedConveniado = new Conveniado();
        partialUpdatedConveniado.setId(conveniado.getId());

        partialUpdatedConveniado.nome(UPDATED_NOME).telefone(UPDATED_TELEFONE);

        restConveniadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConveniado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConveniado))
            )
            .andExpect(status().isOk());

        // Validate the Conveniado in the database
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeUpdate);
        Conveniado testConveniado = conveniadoList.get(conveniadoList.size() - 1);
        assertThat(testConveniado.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testConveniado.getTelefone()).isEqualTo(UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void patchNonExistingConveniado() throws Exception {
        int databaseSizeBeforeUpdate = conveniadoRepository.findAll().size();
        conveniado.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConveniadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conveniado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conveniado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conveniado in the database
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConveniado() throws Exception {
        int databaseSizeBeforeUpdate = conveniadoRepository.findAll().size();
        conveniado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConveniadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conveniado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conveniado in the database
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConveniado() throws Exception {
        int databaseSizeBeforeUpdate = conveniadoRepository.findAll().size();
        conveniado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConveniadoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(conveniado))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conveniado in the database
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConveniado() throws Exception {
        // Initialize the database
        conveniadoRepository.saveAndFlush(conveniado);

        int databaseSizeBeforeDelete = conveniadoRepository.findAll().size();

        // Delete the conveniado
        restConveniadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, conveniado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conveniado> conveniadoList = conveniadoRepository.findAll();
        assertThat(conveniadoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
