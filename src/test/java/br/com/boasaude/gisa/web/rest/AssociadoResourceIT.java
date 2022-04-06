package br.com.boasaude.gisa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.boasaude.gisa.IntegrationTest;
import br.com.boasaude.gisa.domain.Associado;
import br.com.boasaude.gisa.domain.enumeration.EstadoCivil;
import br.com.boasaude.gisa.domain.enumeration.Sexo;
import br.com.boasaude.gisa.domain.enumeration.SituacaoAssociado;
import br.com.boasaude.gisa.domain.enumeration.UF;
import br.com.boasaude.gisa.domain.enumeration.UF;
import br.com.boasaude.gisa.repository.AssociadoRepository;
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
 * Integration tests for the {@link AssociadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssociadoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SOBRENOME = "AAAAAAAAAA";
    private static final String UPDATED_SOBRENOME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_NASCIMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_NASCIMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final EstadoCivil DEFAULT_ESTADO_CIVIL = EstadoCivil.SOLTEIRO;
    private static final EstadoCivil UPDATED_ESTADO_CIVIL = EstadoCivil.CASADO;

    private static final Sexo DEFAULT_SEXO = Sexo.MASCULINO;
    private static final Sexo UPDATED_SEXO = Sexo.FEMININO;

    private static final UF DEFAULT_NATURALIDADE_UF = UF.AC;
    private static final UF UPDATED_NATURALIDADE_UF = UF.AL;

    private static final String DEFAULT_NATURALIDADE_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_NATURALIDADE_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_DOCUMENTO = "BBBBBBBBBB";

    private static final UF DEFAULT_UF_DOCUMENTO = UF.AC;
    private static final UF UPDATED_UF_DOCUMENTO = UF.AL;

    private static final String DEFAULT_ORGAO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_ORGAO_DOCUMENTO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_DOCUMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_DOCUMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOME_MAE = "AAAAAAAAAA";
    private static final String UPDATED_NOME_MAE = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_PAI = "AAAAAAAAAA";
    private static final String UPDATED_NOME_PAI = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final SituacaoAssociado DEFAULT_SITAUCAO = SituacaoAssociado.ATIVO;
    private static final SituacaoAssociado UPDATED_SITAUCAO = SituacaoAssociado.SUSPENSO;

    private static final String ENTITY_API_URL = "/api/associados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssociadoMockMvc;

    private Associado associado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Associado createEntity(EntityManager em) {
        Associado associado = new Associado()
            .nome(DEFAULT_NOME)
            .sobrenome(DEFAULT_SOBRENOME)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .estadoCivil(DEFAULT_ESTADO_CIVIL)
            .sexo(DEFAULT_SEXO)
            .naturalidadeUf(DEFAULT_NATURALIDADE_UF)
            .naturalidadeCidade(DEFAULT_NATURALIDADE_CIDADE)
            .numeroDocumento(DEFAULT_NUMERO_DOCUMENTO)
            .ufDocumento(DEFAULT_UF_DOCUMENTO)
            .orgaoDocumento(DEFAULT_ORGAO_DOCUMENTO)
            .dataDocumento(DEFAULT_DATA_DOCUMENTO)
            .nomeMae(DEFAULT_NOME_MAE)
            .nomePai(DEFAULT_NOME_PAI)
            .email(DEFAULT_EMAIL)
            .telefone(DEFAULT_TELEFONE)
            .sitaucao(DEFAULT_SITAUCAO);
        return associado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Associado createUpdatedEntity(EntityManager em) {
        Associado associado = new Associado()
            .nome(UPDATED_NOME)
            .sobrenome(UPDATED_SOBRENOME)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .sexo(UPDATED_SEXO)
            .naturalidadeUf(UPDATED_NATURALIDADE_UF)
            .naturalidadeCidade(UPDATED_NATURALIDADE_CIDADE)
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .ufDocumento(UPDATED_UF_DOCUMENTO)
            .orgaoDocumento(UPDATED_ORGAO_DOCUMENTO)
            .dataDocumento(UPDATED_DATA_DOCUMENTO)
            .nomeMae(UPDATED_NOME_MAE)
            .nomePai(UPDATED_NOME_PAI)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .sitaucao(UPDATED_SITAUCAO);
        return associado;
    }

    @BeforeEach
    public void initTest() {
        associado = createEntity(em);
    }

    @Test
    @Transactional
    void createAssociado() throws Exception {
        int databaseSizeBeforeCreate = associadoRepository.findAll().size();
        // Create the Associado
        restAssociadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associado)))
            .andExpect(status().isCreated());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeCreate + 1);
        Associado testAssociado = associadoList.get(associadoList.size() - 1);
        assertThat(testAssociado.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAssociado.getSobrenome()).isEqualTo(DEFAULT_SOBRENOME);
        assertThat(testAssociado.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testAssociado.getEstadoCivil()).isEqualTo(DEFAULT_ESTADO_CIVIL);
        assertThat(testAssociado.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testAssociado.getNaturalidadeUf()).isEqualTo(DEFAULT_NATURALIDADE_UF);
        assertThat(testAssociado.getNaturalidadeCidade()).isEqualTo(DEFAULT_NATURALIDADE_CIDADE);
        assertThat(testAssociado.getNumeroDocumento()).isEqualTo(DEFAULT_NUMERO_DOCUMENTO);
        assertThat(testAssociado.getUfDocumento()).isEqualTo(DEFAULT_UF_DOCUMENTO);
        assertThat(testAssociado.getOrgaoDocumento()).isEqualTo(DEFAULT_ORGAO_DOCUMENTO);
        assertThat(testAssociado.getDataDocumento()).isEqualTo(DEFAULT_DATA_DOCUMENTO);
        assertThat(testAssociado.getNomeMae()).isEqualTo(DEFAULT_NOME_MAE);
        assertThat(testAssociado.getNomePai()).isEqualTo(DEFAULT_NOME_PAI);
        assertThat(testAssociado.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAssociado.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testAssociado.getSitaucao()).isEqualTo(DEFAULT_SITAUCAO);
    }

    @Test
    @Transactional
    void createAssociadoWithExistingId() throws Exception {
        // Create the Associado with an existing ID
        associado.setId(1L);

        int databaseSizeBeforeCreate = associadoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssociadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associado)))
            .andExpect(status().isBadRequest());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssociados() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList
        restAssociadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(associado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sobrenome").value(hasItem(DEFAULT_SOBRENOME)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].estadoCivil").value(hasItem(DEFAULT_ESTADO_CIVIL.toString())))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
            .andExpect(jsonPath("$.[*].naturalidadeUf").value(hasItem(DEFAULT_NATURALIDADE_UF.toString())))
            .andExpect(jsonPath("$.[*].naturalidadeCidade").value(hasItem(DEFAULT_NATURALIDADE_CIDADE)))
            .andExpect(jsonPath("$.[*].numeroDocumento").value(hasItem(DEFAULT_NUMERO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].ufDocumento").value(hasItem(DEFAULT_UF_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].orgaoDocumento").value(hasItem(DEFAULT_ORGAO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].dataDocumento").value(hasItem(DEFAULT_DATA_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].nomeMae").value(hasItem(DEFAULT_NOME_MAE)))
            .andExpect(jsonPath("$.[*].nomePai").value(hasItem(DEFAULT_NOME_PAI)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].sitaucao").value(hasItem(DEFAULT_SITAUCAO.toString())));
    }

    @Test
    @Transactional
    void getAssociado() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get the associado
        restAssociadoMockMvc
            .perform(get(ENTITY_API_URL_ID, associado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(associado.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sobrenome").value(DEFAULT_SOBRENOME))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.estadoCivil").value(DEFAULT_ESTADO_CIVIL.toString()))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO.toString()))
            .andExpect(jsonPath("$.naturalidadeUf").value(DEFAULT_NATURALIDADE_UF.toString()))
            .andExpect(jsonPath("$.naturalidadeCidade").value(DEFAULT_NATURALIDADE_CIDADE))
            .andExpect(jsonPath("$.numeroDocumento").value(DEFAULT_NUMERO_DOCUMENTO))
            .andExpect(jsonPath("$.ufDocumento").value(DEFAULT_UF_DOCUMENTO.toString()))
            .andExpect(jsonPath("$.orgaoDocumento").value(DEFAULT_ORGAO_DOCUMENTO))
            .andExpect(jsonPath("$.dataDocumento").value(DEFAULT_DATA_DOCUMENTO.toString()))
            .andExpect(jsonPath("$.nomeMae").value(DEFAULT_NOME_MAE))
            .andExpect(jsonPath("$.nomePai").value(DEFAULT_NOME_PAI))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.sitaucao").value(DEFAULT_SITAUCAO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAssociado() throws Exception {
        // Get the associado
        restAssociadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssociado() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();

        // Update the associado
        Associado updatedAssociado = associadoRepository.findById(associado.getId()).get();
        // Disconnect from session so that the updates on updatedAssociado are not directly saved in db
        em.detach(updatedAssociado);
        updatedAssociado
            .nome(UPDATED_NOME)
            .sobrenome(UPDATED_SOBRENOME)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .sexo(UPDATED_SEXO)
            .naturalidadeUf(UPDATED_NATURALIDADE_UF)
            .naturalidadeCidade(UPDATED_NATURALIDADE_CIDADE)
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .ufDocumento(UPDATED_UF_DOCUMENTO)
            .orgaoDocumento(UPDATED_ORGAO_DOCUMENTO)
            .dataDocumento(UPDATED_DATA_DOCUMENTO)
            .nomeMae(UPDATED_NOME_MAE)
            .nomePai(UPDATED_NOME_PAI)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .sitaucao(UPDATED_SITAUCAO);

        restAssociadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAssociado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAssociado))
            )
            .andExpect(status().isOk());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
        Associado testAssociado = associadoList.get(associadoList.size() - 1);
        assertThat(testAssociado.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAssociado.getSobrenome()).isEqualTo(UPDATED_SOBRENOME);
        assertThat(testAssociado.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testAssociado.getEstadoCivil()).isEqualTo(UPDATED_ESTADO_CIVIL);
        assertThat(testAssociado.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testAssociado.getNaturalidadeUf()).isEqualTo(UPDATED_NATURALIDADE_UF);
        assertThat(testAssociado.getNaturalidadeCidade()).isEqualTo(UPDATED_NATURALIDADE_CIDADE);
        assertThat(testAssociado.getNumeroDocumento()).isEqualTo(UPDATED_NUMERO_DOCUMENTO);
        assertThat(testAssociado.getUfDocumento()).isEqualTo(UPDATED_UF_DOCUMENTO);
        assertThat(testAssociado.getOrgaoDocumento()).isEqualTo(UPDATED_ORGAO_DOCUMENTO);
        assertThat(testAssociado.getDataDocumento()).isEqualTo(UPDATED_DATA_DOCUMENTO);
        assertThat(testAssociado.getNomeMae()).isEqualTo(UPDATED_NOME_MAE);
        assertThat(testAssociado.getNomePai()).isEqualTo(UPDATED_NOME_PAI);
        assertThat(testAssociado.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAssociado.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testAssociado.getSitaucao()).isEqualTo(UPDATED_SITAUCAO);
    }

    @Test
    @Transactional
    void putNonExistingAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, associado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(associado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(associado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associado)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssociadoWithPatch() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();

        // Update the associado using partial update
        Associado partialUpdatedAssociado = new Associado();
        partialUpdatedAssociado.setId(associado.getId());

        partialUpdatedAssociado
            .sexo(UPDATED_SEXO)
            .naturalidadeUf(UPDATED_NATURALIDADE_UF)
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .dataDocumento(UPDATED_DATA_DOCUMENTO)
            .nomeMae(UPDATED_NOME_MAE)
            .nomePai(UPDATED_NOME_PAI)
            .telefone(UPDATED_TELEFONE);

        restAssociadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssociado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssociado))
            )
            .andExpect(status().isOk());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
        Associado testAssociado = associadoList.get(associadoList.size() - 1);
        assertThat(testAssociado.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAssociado.getSobrenome()).isEqualTo(DEFAULT_SOBRENOME);
        assertThat(testAssociado.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testAssociado.getEstadoCivil()).isEqualTo(DEFAULT_ESTADO_CIVIL);
        assertThat(testAssociado.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testAssociado.getNaturalidadeUf()).isEqualTo(UPDATED_NATURALIDADE_UF);
        assertThat(testAssociado.getNaturalidadeCidade()).isEqualTo(DEFAULT_NATURALIDADE_CIDADE);
        assertThat(testAssociado.getNumeroDocumento()).isEqualTo(UPDATED_NUMERO_DOCUMENTO);
        assertThat(testAssociado.getUfDocumento()).isEqualTo(DEFAULT_UF_DOCUMENTO);
        assertThat(testAssociado.getOrgaoDocumento()).isEqualTo(DEFAULT_ORGAO_DOCUMENTO);
        assertThat(testAssociado.getDataDocumento()).isEqualTo(UPDATED_DATA_DOCUMENTO);
        assertThat(testAssociado.getNomeMae()).isEqualTo(UPDATED_NOME_MAE);
        assertThat(testAssociado.getNomePai()).isEqualTo(UPDATED_NOME_PAI);
        assertThat(testAssociado.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAssociado.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testAssociado.getSitaucao()).isEqualTo(DEFAULT_SITAUCAO);
    }

    @Test
    @Transactional
    void fullUpdateAssociadoWithPatch() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();

        // Update the associado using partial update
        Associado partialUpdatedAssociado = new Associado();
        partialUpdatedAssociado.setId(associado.getId());

        partialUpdatedAssociado
            .nome(UPDATED_NOME)
            .sobrenome(UPDATED_SOBRENOME)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .sexo(UPDATED_SEXO)
            .naturalidadeUf(UPDATED_NATURALIDADE_UF)
            .naturalidadeCidade(UPDATED_NATURALIDADE_CIDADE)
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .ufDocumento(UPDATED_UF_DOCUMENTO)
            .orgaoDocumento(UPDATED_ORGAO_DOCUMENTO)
            .dataDocumento(UPDATED_DATA_DOCUMENTO)
            .nomeMae(UPDATED_NOME_MAE)
            .nomePai(UPDATED_NOME_PAI)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .sitaucao(UPDATED_SITAUCAO);

        restAssociadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssociado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssociado))
            )
            .andExpect(status().isOk());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
        Associado testAssociado = associadoList.get(associadoList.size() - 1);
        assertThat(testAssociado.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAssociado.getSobrenome()).isEqualTo(UPDATED_SOBRENOME);
        assertThat(testAssociado.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testAssociado.getEstadoCivil()).isEqualTo(UPDATED_ESTADO_CIVIL);
        assertThat(testAssociado.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testAssociado.getNaturalidadeUf()).isEqualTo(UPDATED_NATURALIDADE_UF);
        assertThat(testAssociado.getNaturalidadeCidade()).isEqualTo(UPDATED_NATURALIDADE_CIDADE);
        assertThat(testAssociado.getNumeroDocumento()).isEqualTo(UPDATED_NUMERO_DOCUMENTO);
        assertThat(testAssociado.getUfDocumento()).isEqualTo(UPDATED_UF_DOCUMENTO);
        assertThat(testAssociado.getOrgaoDocumento()).isEqualTo(UPDATED_ORGAO_DOCUMENTO);
        assertThat(testAssociado.getDataDocumento()).isEqualTo(UPDATED_DATA_DOCUMENTO);
        assertThat(testAssociado.getNomeMae()).isEqualTo(UPDATED_NOME_MAE);
        assertThat(testAssociado.getNomePai()).isEqualTo(UPDATED_NOME_PAI);
        assertThat(testAssociado.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAssociado.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testAssociado.getSitaucao()).isEqualTo(UPDATED_SITAUCAO);
    }

    @Test
    @Transactional
    void patchNonExistingAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, associado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(associado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(associado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(associado))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssociado() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        int databaseSizeBeforeDelete = associadoRepository.findAll().size();

        // Delete the associado
        restAssociadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, associado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
