package com.forticas.cabinet3.web.rest;

import static com.forticas.cabinet3.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.forticas.cabinet3.IntegrationTest;
import com.forticas.cabinet3.domain.Constante;
import com.forticas.cabinet3.domain.Patient;
import com.forticas.cabinet3.repository.ConstanteRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link ConstanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConstanteResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_POID = 1;
    private static final Integer UPDATED_POID = 2;

    private static final Integer DEFAULT_TAILLE = 1;
    private static final Integer UPDATED_TAILLE = 2;

    private static final Integer DEFAULT_PAS = 1;
    private static final Integer UPDATED_PAS = 2;

    private static final Integer DEFAULT_PAD = 1;
    private static final Integer UPDATED_PAD = 2;

    private static final Integer DEFAULT_POULS = 1;
    private static final Integer UPDATED_POULS = 2;

    private static final Integer DEFAULT_TEMP = 1;
    private static final Integer UPDATED_TEMP = 2;

    private static final Integer DEFAULT_GLYCEMIE = 1;
    private static final Integer UPDATED_GLYCEMIE = 2;

    private static final Integer DEFAULT_CHOLESTEROL = 1;
    private static final Integer UPDATED_CHOLESTEROL = 2;

    private static final String ENTITY_API_URL = "/api/constantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConstanteRepository constanteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConstanteMockMvc;

    private Constante constante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Constante createEntity(EntityManager em) {
        Constante constante = new Constante()
            .date(DEFAULT_DATE)
            .poid(DEFAULT_POID)
            .taille(DEFAULT_TAILLE)
            .pas(DEFAULT_PAS)
            .pad(DEFAULT_PAD)
            .pouls(DEFAULT_POULS)
            .temp(DEFAULT_TEMP)
            .glycemie(DEFAULT_GLYCEMIE)
            .cholesterol(DEFAULT_CHOLESTEROL);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        constante.setPatient(patient);
        return constante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Constante createUpdatedEntity(EntityManager em) {
        Constante constante = new Constante()
            .date(UPDATED_DATE)
            .poid(UPDATED_POID)
            .taille(UPDATED_TAILLE)
            .pas(UPDATED_PAS)
            .pad(UPDATED_PAD)
            .pouls(UPDATED_POULS)
            .temp(UPDATED_TEMP)
            .glycemie(UPDATED_GLYCEMIE)
            .cholesterol(UPDATED_CHOLESTEROL);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        constante.setPatient(patient);
        return constante;
    }

    @BeforeEach
    public void initTest() {
        constante = createEntity(em);
    }

    @Test
    @Transactional
    void createConstante() throws Exception {
        int databaseSizeBeforeCreate = constanteRepository.findAll().size();
        // Create the Constante
        restConstanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(constante)))
            .andExpect(status().isCreated());

        // Validate the Constante in the database
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeCreate + 1);
        Constante testConstante = constanteList.get(constanteList.size() - 1);
        assertThat(testConstante.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testConstante.getPoid()).isEqualTo(DEFAULT_POID);
        assertThat(testConstante.getTaille()).isEqualTo(DEFAULT_TAILLE);
        assertThat(testConstante.getPas()).isEqualTo(DEFAULT_PAS);
        assertThat(testConstante.getPad()).isEqualTo(DEFAULT_PAD);
        assertThat(testConstante.getPouls()).isEqualTo(DEFAULT_POULS);
        assertThat(testConstante.getTemp()).isEqualTo(DEFAULT_TEMP);
        assertThat(testConstante.getGlycemie()).isEqualTo(DEFAULT_GLYCEMIE);
        assertThat(testConstante.getCholesterol()).isEqualTo(DEFAULT_CHOLESTEROL);
    }

    @Test
    @Transactional
    void createConstanteWithExistingId() throws Exception {
        // Create the Constante with an existing ID
        constante.setId(1L);

        int databaseSizeBeforeCreate = constanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConstanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(constante)))
            .andExpect(status().isBadRequest());

        // Validate the Constante in the database
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = constanteRepository.findAll().size();
        // set the field null
        constante.setDate(null);

        // Create the Constante, which fails.

        restConstanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(constante)))
            .andExpect(status().isBadRequest());

        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConstantes() throws Exception {
        // Initialize the database
        constanteRepository.saveAndFlush(constante);

        // Get all the constanteList
        restConstanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(constante.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].poid").value(hasItem(DEFAULT_POID)))
            .andExpect(jsonPath("$.[*].taille").value(hasItem(DEFAULT_TAILLE)))
            .andExpect(jsonPath("$.[*].pas").value(hasItem(DEFAULT_PAS)))
            .andExpect(jsonPath("$.[*].pad").value(hasItem(DEFAULT_PAD)))
            .andExpect(jsonPath("$.[*].pouls").value(hasItem(DEFAULT_POULS)))
            .andExpect(jsonPath("$.[*].temp").value(hasItem(DEFAULT_TEMP)))
            .andExpect(jsonPath("$.[*].glycemie").value(hasItem(DEFAULT_GLYCEMIE)))
            .andExpect(jsonPath("$.[*].cholesterol").value(hasItem(DEFAULT_CHOLESTEROL)));
    }

    @Test
    @Transactional
    void getConstante() throws Exception {
        // Initialize the database
        constanteRepository.saveAndFlush(constante);

        // Get the constante
        restConstanteMockMvc
            .perform(get(ENTITY_API_URL_ID, constante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(constante.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.poid").value(DEFAULT_POID))
            .andExpect(jsonPath("$.taille").value(DEFAULT_TAILLE))
            .andExpect(jsonPath("$.pas").value(DEFAULT_PAS))
            .andExpect(jsonPath("$.pad").value(DEFAULT_PAD))
            .andExpect(jsonPath("$.pouls").value(DEFAULT_POULS))
            .andExpect(jsonPath("$.temp").value(DEFAULT_TEMP))
            .andExpect(jsonPath("$.glycemie").value(DEFAULT_GLYCEMIE))
            .andExpect(jsonPath("$.cholesterol").value(DEFAULT_CHOLESTEROL));
    }

    @Test
    @Transactional
    void getNonExistingConstante() throws Exception {
        // Get the constante
        restConstanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConstante() throws Exception {
        // Initialize the database
        constanteRepository.saveAndFlush(constante);

        int databaseSizeBeforeUpdate = constanteRepository.findAll().size();

        // Update the constante
        Constante updatedConstante = constanteRepository.findById(constante.getId()).get();
        // Disconnect from session so that the updates on updatedConstante are not directly saved in db
        em.detach(updatedConstante);
        updatedConstante
            .date(UPDATED_DATE)
            .poid(UPDATED_POID)
            .taille(UPDATED_TAILLE)
            .pas(UPDATED_PAS)
            .pad(UPDATED_PAD)
            .pouls(UPDATED_POULS)
            .temp(UPDATED_TEMP)
            .glycemie(UPDATED_GLYCEMIE)
            .cholesterol(UPDATED_CHOLESTEROL);

        restConstanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConstante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConstante))
            )
            .andExpect(status().isOk());

        // Validate the Constante in the database
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeUpdate);
        Constante testConstante = constanteList.get(constanteList.size() - 1);
        assertThat(testConstante.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testConstante.getPoid()).isEqualTo(UPDATED_POID);
        assertThat(testConstante.getTaille()).isEqualTo(UPDATED_TAILLE);
        assertThat(testConstante.getPas()).isEqualTo(UPDATED_PAS);
        assertThat(testConstante.getPad()).isEqualTo(UPDATED_PAD);
        assertThat(testConstante.getPouls()).isEqualTo(UPDATED_POULS);
        assertThat(testConstante.getTemp()).isEqualTo(UPDATED_TEMP);
        assertThat(testConstante.getGlycemie()).isEqualTo(UPDATED_GLYCEMIE);
        assertThat(testConstante.getCholesterol()).isEqualTo(UPDATED_CHOLESTEROL);
    }

    @Test
    @Transactional
    void putNonExistingConstante() throws Exception {
        int databaseSizeBeforeUpdate = constanteRepository.findAll().size();
        constante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConstanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, constante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(constante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Constante in the database
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConstante() throws Exception {
        int databaseSizeBeforeUpdate = constanteRepository.findAll().size();
        constante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(constante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Constante in the database
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConstante() throws Exception {
        int databaseSizeBeforeUpdate = constanteRepository.findAll().size();
        constante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(constante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Constante in the database
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConstanteWithPatch() throws Exception {
        // Initialize the database
        constanteRepository.saveAndFlush(constante);

        int databaseSizeBeforeUpdate = constanteRepository.findAll().size();

        // Update the constante using partial update
        Constante partialUpdatedConstante = new Constante();
        partialUpdatedConstante.setId(constante.getId());

        partialUpdatedConstante.pad(UPDATED_PAD).cholesterol(UPDATED_CHOLESTEROL);

        restConstanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConstante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConstante))
            )
            .andExpect(status().isOk());

        // Validate the Constante in the database
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeUpdate);
        Constante testConstante = constanteList.get(constanteList.size() - 1);
        assertThat(testConstante.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testConstante.getPoid()).isEqualTo(DEFAULT_POID);
        assertThat(testConstante.getTaille()).isEqualTo(DEFAULT_TAILLE);
        assertThat(testConstante.getPas()).isEqualTo(DEFAULT_PAS);
        assertThat(testConstante.getPad()).isEqualTo(UPDATED_PAD);
        assertThat(testConstante.getPouls()).isEqualTo(DEFAULT_POULS);
        assertThat(testConstante.getTemp()).isEqualTo(DEFAULT_TEMP);
        assertThat(testConstante.getGlycemie()).isEqualTo(DEFAULT_GLYCEMIE);
        assertThat(testConstante.getCholesterol()).isEqualTo(UPDATED_CHOLESTEROL);
    }

    @Test
    @Transactional
    void fullUpdateConstanteWithPatch() throws Exception {
        // Initialize the database
        constanteRepository.saveAndFlush(constante);

        int databaseSizeBeforeUpdate = constanteRepository.findAll().size();

        // Update the constante using partial update
        Constante partialUpdatedConstante = new Constante();
        partialUpdatedConstante.setId(constante.getId());

        partialUpdatedConstante
            .date(UPDATED_DATE)
            .poid(UPDATED_POID)
            .taille(UPDATED_TAILLE)
            .pas(UPDATED_PAS)
            .pad(UPDATED_PAD)
            .pouls(UPDATED_POULS)
            .temp(UPDATED_TEMP)
            .glycemie(UPDATED_GLYCEMIE)
            .cholesterol(UPDATED_CHOLESTEROL);

        restConstanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConstante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConstante))
            )
            .andExpect(status().isOk());

        // Validate the Constante in the database
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeUpdate);
        Constante testConstante = constanteList.get(constanteList.size() - 1);
        assertThat(testConstante.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testConstante.getPoid()).isEqualTo(UPDATED_POID);
        assertThat(testConstante.getTaille()).isEqualTo(UPDATED_TAILLE);
        assertThat(testConstante.getPas()).isEqualTo(UPDATED_PAS);
        assertThat(testConstante.getPad()).isEqualTo(UPDATED_PAD);
        assertThat(testConstante.getPouls()).isEqualTo(UPDATED_POULS);
        assertThat(testConstante.getTemp()).isEqualTo(UPDATED_TEMP);
        assertThat(testConstante.getGlycemie()).isEqualTo(UPDATED_GLYCEMIE);
        assertThat(testConstante.getCholesterol()).isEqualTo(UPDATED_CHOLESTEROL);
    }

    @Test
    @Transactional
    void patchNonExistingConstante() throws Exception {
        int databaseSizeBeforeUpdate = constanteRepository.findAll().size();
        constante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConstanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, constante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(constante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Constante in the database
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConstante() throws Exception {
        int databaseSizeBeforeUpdate = constanteRepository.findAll().size();
        constante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(constante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Constante in the database
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConstante() throws Exception {
        int databaseSizeBeforeUpdate = constanteRepository.findAll().size();
        constante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstanteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(constante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Constante in the database
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConstante() throws Exception {
        // Initialize the database
        constanteRepository.saveAndFlush(constante);

        int databaseSizeBeforeDelete = constanteRepository.findAll().size();

        // Delete the constante
        restConstanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, constante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Constante> constanteList = constanteRepository.findAll();
        assertThat(constanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
