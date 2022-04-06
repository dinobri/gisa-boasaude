package br.com.boasaude.gisa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.boasaude.gisa.IntegrationTest;
import br.com.boasaude.gisa.domain.Plano;
import br.com.boasaude.gisa.domain.enumeration.CategoriaPlano;
import br.com.boasaude.gisa.domain.enumeration.TipoPlano;
import br.com.boasaude.gisa.repository.PlanoRepository;
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
 * Integration tests for the {@link PlanoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlanoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_ANS = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_ANS = "BBBBBBBBBB";

    private static final CategoriaPlano DEFAULT_CATEGORIA = CategoriaPlano.INDIVIDUAL;
    private static final CategoriaPlano UPDATED_CATEGORIA = CategoriaPlano.EMPRESARIAL;

    private static final TipoPlano DEFAULT_TIPO = TipoPlano.ENFERMARIA;
    private static final TipoPlano UPDATED_TIPO = TipoPlano.APARTAMENTO;

    private static final Boolean DEFAULT_ODONTO = false;
    private static final Boolean UPDATED_ODONTO = true;

    private static final Integer DEFAULT_IDADE_MIN = 1;
    private static final Integer UPDATED_IDADE_MIN = 2;

    private static final Integer DEFAULT_IDADE_MAX = 1;
    private static final Integer UPDATED_IDADE_MAX = 2;

    private static final Integer DEFAULT_QUANTIDADE_CONSULTAS_ANO = 1;
    private static final Integer UPDATED_QUANTIDADE_CONSULTAS_ANO = 2;

    private static final Integer DEFAULT_QUANATIDADE_EXAMES_ANO = 1;
    private static final Integer UPDATED_QUANATIDADE_EXAMES_ANO = 2;

    private static final Double DEFAULT_VALOR_MENSALIDADE = 1D;
    private static final Double UPDATED_VALOR_MENSALIDADE = 2D;

    private static final String ENTITY_API_URL = "/api/planos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlanoRepository planoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanoMockMvc;

    private Plano plano;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plano createEntity(EntityManager em) {
        Plano plano = new Plano()
            .nome(DEFAULT_NOME)
            .codigoANS(DEFAULT_CODIGO_ANS)
            .categoria(DEFAULT_CATEGORIA)
            .tipo(DEFAULT_TIPO)
            .odonto(DEFAULT_ODONTO)
            .idadeMin(DEFAULT_IDADE_MIN)
            .idadeMax(DEFAULT_IDADE_MAX)
            .quantidadeConsultasAno(DEFAULT_QUANTIDADE_CONSULTAS_ANO)
            .quanatidadeExamesAno(DEFAULT_QUANATIDADE_EXAMES_ANO)
            .valorMensalidade(DEFAULT_VALOR_MENSALIDADE);
        return plano;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plano createUpdatedEntity(EntityManager em) {
        Plano plano = new Plano()
            .nome(UPDATED_NOME)
            .codigoANS(UPDATED_CODIGO_ANS)
            .categoria(UPDATED_CATEGORIA)
            .tipo(UPDATED_TIPO)
            .odonto(UPDATED_ODONTO)
            .idadeMin(UPDATED_IDADE_MIN)
            .idadeMax(UPDATED_IDADE_MAX)
            .quantidadeConsultasAno(UPDATED_QUANTIDADE_CONSULTAS_ANO)
            .quanatidadeExamesAno(UPDATED_QUANATIDADE_EXAMES_ANO)
            .valorMensalidade(UPDATED_VALOR_MENSALIDADE);
        return plano;
    }

    @BeforeEach
    public void initTest() {
        plano = createEntity(em);
    }

    @Test
    @Transactional
    void createPlano() throws Exception {
        int databaseSizeBeforeCreate = planoRepository.findAll().size();
        // Create the Plano
        restPlanoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plano)))
            .andExpect(status().isCreated());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeCreate + 1);
        Plano testPlano = planoList.get(planoList.size() - 1);
        assertThat(testPlano.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPlano.getCodigoANS()).isEqualTo(DEFAULT_CODIGO_ANS);
        assertThat(testPlano.getCategoria()).isEqualTo(DEFAULT_CATEGORIA);
        assertThat(testPlano.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testPlano.getOdonto()).isEqualTo(DEFAULT_ODONTO);
        assertThat(testPlano.getIdadeMin()).isEqualTo(DEFAULT_IDADE_MIN);
        assertThat(testPlano.getIdadeMax()).isEqualTo(DEFAULT_IDADE_MAX);
        assertThat(testPlano.getQuantidadeConsultasAno()).isEqualTo(DEFAULT_QUANTIDADE_CONSULTAS_ANO);
        assertThat(testPlano.getQuanatidadeExamesAno()).isEqualTo(DEFAULT_QUANATIDADE_EXAMES_ANO);
        assertThat(testPlano.getValorMensalidade()).isEqualTo(DEFAULT_VALOR_MENSALIDADE);
    }

    @Test
    @Transactional
    void createPlanoWithExistingId() throws Exception {
        // Create the Plano with an existing ID
        plano.setId(1L);

        int databaseSizeBeforeCreate = planoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plano)))
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlanos() throws Exception {
        // Initialize the database
        planoRepository.saveAndFlush(plano);

        // Get all the planoList
        restPlanoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plano.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codigoANS").value(hasItem(DEFAULT_CODIGO_ANS)))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].odonto").value(hasItem(DEFAULT_ODONTO.booleanValue())))
            .andExpect(jsonPath("$.[*].idadeMin").value(hasItem(DEFAULT_IDADE_MIN)))
            .andExpect(jsonPath("$.[*].idadeMax").value(hasItem(DEFAULT_IDADE_MAX)))
            .andExpect(jsonPath("$.[*].quantidadeConsultasAno").value(hasItem(DEFAULT_QUANTIDADE_CONSULTAS_ANO)))
            .andExpect(jsonPath("$.[*].quanatidadeExamesAno").value(hasItem(DEFAULT_QUANATIDADE_EXAMES_ANO)))
            .andExpect(jsonPath("$.[*].valorMensalidade").value(hasItem(DEFAULT_VALOR_MENSALIDADE.doubleValue())));
    }

    @Test
    @Transactional
    void getPlano() throws Exception {
        // Initialize the database
        planoRepository.saveAndFlush(plano);

        // Get the plano
        restPlanoMockMvc
            .perform(get(ENTITY_API_URL_ID, plano.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plano.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.codigoANS").value(DEFAULT_CODIGO_ANS))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.odonto").value(DEFAULT_ODONTO.booleanValue()))
            .andExpect(jsonPath("$.idadeMin").value(DEFAULT_IDADE_MIN))
            .andExpect(jsonPath("$.idadeMax").value(DEFAULT_IDADE_MAX))
            .andExpect(jsonPath("$.quantidadeConsultasAno").value(DEFAULT_QUANTIDADE_CONSULTAS_ANO))
            .andExpect(jsonPath("$.quanatidadeExamesAno").value(DEFAULT_QUANATIDADE_EXAMES_ANO))
            .andExpect(jsonPath("$.valorMensalidade").value(DEFAULT_VALOR_MENSALIDADE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPlano() throws Exception {
        // Get the plano
        restPlanoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlano() throws Exception {
        // Initialize the database
        planoRepository.saveAndFlush(plano);

        int databaseSizeBeforeUpdate = planoRepository.findAll().size();

        // Update the plano
        Plano updatedPlano = planoRepository.findById(plano.getId()).get();
        // Disconnect from session so that the updates on updatedPlano are not directly saved in db
        em.detach(updatedPlano);
        updatedPlano
            .nome(UPDATED_NOME)
            .codigoANS(UPDATED_CODIGO_ANS)
            .categoria(UPDATED_CATEGORIA)
            .tipo(UPDATED_TIPO)
            .odonto(UPDATED_ODONTO)
            .idadeMin(UPDATED_IDADE_MIN)
            .idadeMax(UPDATED_IDADE_MAX)
            .quantidadeConsultasAno(UPDATED_QUANTIDADE_CONSULTAS_ANO)
            .quanatidadeExamesAno(UPDATED_QUANATIDADE_EXAMES_ANO)
            .valorMensalidade(UPDATED_VALOR_MENSALIDADE);

        restPlanoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlano.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlano))
            )
            .andExpect(status().isOk());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
        Plano testPlano = planoList.get(planoList.size() - 1);
        assertThat(testPlano.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPlano.getCodigoANS()).isEqualTo(UPDATED_CODIGO_ANS);
        assertThat(testPlano.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testPlano.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testPlano.getOdonto()).isEqualTo(UPDATED_ODONTO);
        assertThat(testPlano.getIdadeMin()).isEqualTo(UPDATED_IDADE_MIN);
        assertThat(testPlano.getIdadeMax()).isEqualTo(UPDATED_IDADE_MAX);
        assertThat(testPlano.getQuantidadeConsultasAno()).isEqualTo(UPDATED_QUANTIDADE_CONSULTAS_ANO);
        assertThat(testPlano.getQuanatidadeExamesAno()).isEqualTo(UPDATED_QUANATIDADE_EXAMES_ANO);
        assertThat(testPlano.getValorMensalidade()).isEqualTo(UPDATED_VALOR_MENSALIDADE);
    }

    @Test
    @Transactional
    void putNonExistingPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().size();
        plano.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plano.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plano))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().size();
        plano.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plano))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().size();
        plano.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plano)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanoWithPatch() throws Exception {
        // Initialize the database
        planoRepository.saveAndFlush(plano);

        int databaseSizeBeforeUpdate = planoRepository.findAll().size();

        // Update the plano using partial update
        Plano partialUpdatedPlano = new Plano();
        partialUpdatedPlano.setId(plano.getId());

        partialUpdatedPlano
            .nome(UPDATED_NOME)
            .codigoANS(UPDATED_CODIGO_ANS)
            .categoria(UPDATED_CATEGORIA)
            .idadeMax(UPDATED_IDADE_MAX)
            .quantidadeConsultasAno(UPDATED_QUANTIDADE_CONSULTAS_ANO)
            .quanatidadeExamesAno(UPDATED_QUANATIDADE_EXAMES_ANO)
            .valorMensalidade(UPDATED_VALOR_MENSALIDADE);

        restPlanoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlano.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlano))
            )
            .andExpect(status().isOk());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
        Plano testPlano = planoList.get(planoList.size() - 1);
        assertThat(testPlano.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPlano.getCodigoANS()).isEqualTo(UPDATED_CODIGO_ANS);
        assertThat(testPlano.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testPlano.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testPlano.getOdonto()).isEqualTo(DEFAULT_ODONTO);
        assertThat(testPlano.getIdadeMin()).isEqualTo(DEFAULT_IDADE_MIN);
        assertThat(testPlano.getIdadeMax()).isEqualTo(UPDATED_IDADE_MAX);
        assertThat(testPlano.getQuantidadeConsultasAno()).isEqualTo(UPDATED_QUANTIDADE_CONSULTAS_ANO);
        assertThat(testPlano.getQuanatidadeExamesAno()).isEqualTo(UPDATED_QUANATIDADE_EXAMES_ANO);
        assertThat(testPlano.getValorMensalidade()).isEqualTo(UPDATED_VALOR_MENSALIDADE);
    }

    @Test
    @Transactional
    void fullUpdatePlanoWithPatch() throws Exception {
        // Initialize the database
        planoRepository.saveAndFlush(plano);

        int databaseSizeBeforeUpdate = planoRepository.findAll().size();

        // Update the plano using partial update
        Plano partialUpdatedPlano = new Plano();
        partialUpdatedPlano.setId(plano.getId());

        partialUpdatedPlano
            .nome(UPDATED_NOME)
            .codigoANS(UPDATED_CODIGO_ANS)
            .categoria(UPDATED_CATEGORIA)
            .tipo(UPDATED_TIPO)
            .odonto(UPDATED_ODONTO)
            .idadeMin(UPDATED_IDADE_MIN)
            .idadeMax(UPDATED_IDADE_MAX)
            .quantidadeConsultasAno(UPDATED_QUANTIDADE_CONSULTAS_ANO)
            .quanatidadeExamesAno(UPDATED_QUANATIDADE_EXAMES_ANO)
            .valorMensalidade(UPDATED_VALOR_MENSALIDADE);

        restPlanoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlano.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlano))
            )
            .andExpect(status().isOk());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
        Plano testPlano = planoList.get(planoList.size() - 1);
        assertThat(testPlano.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPlano.getCodigoANS()).isEqualTo(UPDATED_CODIGO_ANS);
        assertThat(testPlano.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testPlano.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testPlano.getOdonto()).isEqualTo(UPDATED_ODONTO);
        assertThat(testPlano.getIdadeMin()).isEqualTo(UPDATED_IDADE_MIN);
        assertThat(testPlano.getIdadeMax()).isEqualTo(UPDATED_IDADE_MAX);
        assertThat(testPlano.getQuantidadeConsultasAno()).isEqualTo(UPDATED_QUANTIDADE_CONSULTAS_ANO);
        assertThat(testPlano.getQuanatidadeExamesAno()).isEqualTo(UPDATED_QUANATIDADE_EXAMES_ANO);
        assertThat(testPlano.getValorMensalidade()).isEqualTo(UPDATED_VALOR_MENSALIDADE);
    }

    @Test
    @Transactional
    void patchNonExistingPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().size();
        plano.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, plano.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plano))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().size();
        plano.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plano))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().size();
        plano.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(plano)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlano() throws Exception {
        // Initialize the database
        planoRepository.saveAndFlush(plano);

        int databaseSizeBeforeDelete = planoRepository.findAll().size();

        // Delete the plano
        restPlanoMockMvc
            .perform(delete(ENTITY_API_URL_ID, plano.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
