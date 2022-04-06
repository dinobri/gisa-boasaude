package br.com.boasaude.gisa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.boasaude.gisa.IntegrationTest;
import br.com.boasaude.gisa.domain.Prestador;
import br.com.boasaude.gisa.domain.enumeration.Sexo;
import br.com.boasaude.gisa.repository.PrestadorRepository;
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
 * Integration tests for the {@link PrestadorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrestadorResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SOBRENOME = "AAAAAAAAAA";
    private static final String UPDATED_SOBRENOME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_NASCIMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_NASCIMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Sexo DEFAULT_SEXO = Sexo.MASCULINO;
    private static final Sexo UPDATED_SEXO = Sexo.FEMININO;

    private static final String DEFAULT_NUMERO_REGISTRO_PROFISSIONAL = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_REGISTRO_PROFISSIONAL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prestadors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrestadorMockMvc;

    private Prestador prestador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestador createEntity(EntityManager em) {
        Prestador prestador = new Prestador()
            .nome(DEFAULT_NOME)
            .sobrenome(DEFAULT_SOBRENOME)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .sexo(DEFAULT_SEXO)
            .numeroRegistroProfissional(DEFAULT_NUMERO_REGISTRO_PROFISSIONAL)
            .telefone(DEFAULT_TELEFONE);
        return prestador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestador createUpdatedEntity(EntityManager em) {
        Prestador prestador = new Prestador()
            .nome(UPDATED_NOME)
            .sobrenome(UPDATED_SOBRENOME)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .sexo(UPDATED_SEXO)
            .numeroRegistroProfissional(UPDATED_NUMERO_REGISTRO_PROFISSIONAL)
            .telefone(UPDATED_TELEFONE);
        return prestador;
    }

    @BeforeEach
    public void initTest() {
        prestador = createEntity(em);
    }

    @Test
    @Transactional
    void createPrestador() throws Exception {
        int databaseSizeBeforeCreate = prestadorRepository.findAll().size();
        // Create the Prestador
        restPrestadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestador)))
            .andExpect(status().isCreated());

        // Validate the Prestador in the database
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeCreate + 1);
        Prestador testPrestador = prestadorList.get(prestadorList.size() - 1);
        assertThat(testPrestador.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPrestador.getSobrenome()).isEqualTo(DEFAULT_SOBRENOME);
        assertThat(testPrestador.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testPrestador.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testPrestador.getNumeroRegistroProfissional()).isEqualTo(DEFAULT_NUMERO_REGISTRO_PROFISSIONAL);
        assertThat(testPrestador.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void createPrestadorWithExistingId() throws Exception {
        // Create the Prestador with an existing ID
        prestador.setId(1L);

        int databaseSizeBeforeCreate = prestadorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrestadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestador)))
            .andExpect(status().isBadRequest());

        // Validate the Prestador in the database
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrestadors() throws Exception {
        // Initialize the database
        prestadorRepository.saveAndFlush(prestador);

        // Get all the prestadorList
        restPrestadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sobrenome").value(hasItem(DEFAULT_SOBRENOME)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
            .andExpect(jsonPath("$.[*].numeroRegistroProfissional").value(hasItem(DEFAULT_NUMERO_REGISTRO_PROFISSIONAL)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)));
    }

    @Test
    @Transactional
    void getPrestador() throws Exception {
        // Initialize the database
        prestadorRepository.saveAndFlush(prestador);

        // Get the prestador
        restPrestadorMockMvc
            .perform(get(ENTITY_API_URL_ID, prestador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prestador.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sobrenome").value(DEFAULT_SOBRENOME))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO.toString()))
            .andExpect(jsonPath("$.numeroRegistroProfissional").value(DEFAULT_NUMERO_REGISTRO_PROFISSIONAL))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE));
    }

    @Test
    @Transactional
    void getNonExistingPrestador() throws Exception {
        // Get the prestador
        restPrestadorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrestador() throws Exception {
        // Initialize the database
        prestadorRepository.saveAndFlush(prestador);

        int databaseSizeBeforeUpdate = prestadorRepository.findAll().size();

        // Update the prestador
        Prestador updatedPrestador = prestadorRepository.findById(prestador.getId()).get();
        // Disconnect from session so that the updates on updatedPrestador are not directly saved in db
        em.detach(updatedPrestador);
        updatedPrestador
            .nome(UPDATED_NOME)
            .sobrenome(UPDATED_SOBRENOME)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .sexo(UPDATED_SEXO)
            .numeroRegistroProfissional(UPDATED_NUMERO_REGISTRO_PROFISSIONAL)
            .telefone(UPDATED_TELEFONE);

        restPrestadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrestador.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrestador))
            )
            .andExpect(status().isOk());

        // Validate the Prestador in the database
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeUpdate);
        Prestador testPrestador = prestadorList.get(prestadorList.size() - 1);
        assertThat(testPrestador.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPrestador.getSobrenome()).isEqualTo(UPDATED_SOBRENOME);
        assertThat(testPrestador.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testPrestador.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testPrestador.getNumeroRegistroProfissional()).isEqualTo(UPDATED_NUMERO_REGISTRO_PROFISSIONAL);
        assertThat(testPrestador.getTelefone()).isEqualTo(UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void putNonExistingPrestador() throws Exception {
        int databaseSizeBeforeUpdate = prestadorRepository.findAll().size();
        prestador.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestador.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prestador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestador in the database
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrestador() throws Exception {
        int databaseSizeBeforeUpdate = prestadorRepository.findAll().size();
        prestador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prestador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestador in the database
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrestador() throws Exception {
        int databaseSizeBeforeUpdate = prestadorRepository.findAll().size();
        prestador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestadorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestador)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestador in the database
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrestadorWithPatch() throws Exception {
        // Initialize the database
        prestadorRepository.saveAndFlush(prestador);

        int databaseSizeBeforeUpdate = prestadorRepository.findAll().size();

        // Update the prestador using partial update
        Prestador partialUpdatedPrestador = new Prestador();
        partialUpdatedPrestador.setId(prestador.getId());

        partialUpdatedPrestador.nome(UPDATED_NOME).sobrenome(UPDATED_SOBRENOME).dataNascimento(UPDATED_DATA_NASCIMENTO);

        restPrestadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrestador))
            )
            .andExpect(status().isOk());

        // Validate the Prestador in the database
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeUpdate);
        Prestador testPrestador = prestadorList.get(prestadorList.size() - 1);
        assertThat(testPrestador.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPrestador.getSobrenome()).isEqualTo(UPDATED_SOBRENOME);
        assertThat(testPrestador.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testPrestador.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testPrestador.getNumeroRegistroProfissional()).isEqualTo(DEFAULT_NUMERO_REGISTRO_PROFISSIONAL);
        assertThat(testPrestador.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void fullUpdatePrestadorWithPatch() throws Exception {
        // Initialize the database
        prestadorRepository.saveAndFlush(prestador);

        int databaseSizeBeforeUpdate = prestadorRepository.findAll().size();

        // Update the prestador using partial update
        Prestador partialUpdatedPrestador = new Prestador();
        partialUpdatedPrestador.setId(prestador.getId());

        partialUpdatedPrestador
            .nome(UPDATED_NOME)
            .sobrenome(UPDATED_SOBRENOME)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .sexo(UPDATED_SEXO)
            .numeroRegistroProfissional(UPDATED_NUMERO_REGISTRO_PROFISSIONAL)
            .telefone(UPDATED_TELEFONE);

        restPrestadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrestador))
            )
            .andExpect(status().isOk());

        // Validate the Prestador in the database
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeUpdate);
        Prestador testPrestador = prestadorList.get(prestadorList.size() - 1);
        assertThat(testPrestador.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPrestador.getSobrenome()).isEqualTo(UPDATED_SOBRENOME);
        assertThat(testPrestador.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testPrestador.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testPrestador.getNumeroRegistroProfissional()).isEqualTo(UPDATED_NUMERO_REGISTRO_PROFISSIONAL);
        assertThat(testPrestador.getTelefone()).isEqualTo(UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void patchNonExistingPrestador() throws Exception {
        int databaseSizeBeforeUpdate = prestadorRepository.findAll().size();
        prestador.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prestador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prestador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestador in the database
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrestador() throws Exception {
        int databaseSizeBeforeUpdate = prestadorRepository.findAll().size();
        prestador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prestador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestador in the database
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrestador() throws Exception {
        int databaseSizeBeforeUpdate = prestadorRepository.findAll().size();
        prestador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestadorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prestador))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestador in the database
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrestador() throws Exception {
        // Initialize the database
        prestadorRepository.saveAndFlush(prestador);

        int databaseSizeBeforeDelete = prestadorRepository.findAll().size();

        // Delete the prestador
        restPrestadorMockMvc
            .perform(delete(ENTITY_API_URL_ID, prestador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prestador> prestadorList = prestadorRepository.findAll();
        assertThat(prestadorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
