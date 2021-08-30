package com.forticas.cabinet3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.forticas.cabinet3.IntegrationTest;
import com.forticas.cabinet3.domain.Antecedent;
import com.forticas.cabinet3.domain.Patient;
import com.forticas.cabinet3.domain.enumeration.TypeAntecedent;
import com.forticas.cabinet3.repository.AntecedentRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AntecedentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AntecedentResourceIT {

    private static final TypeAntecedent DEFAULT_TYPE = TypeAntecedent.MEDICAL;
    private static final TypeAntecedent UPDATED_TYPE = TypeAntecedent.CHIRURGICAL;

    private static final String DEFAULT_PERIODE = "AAAAAAAAAA";
    private static final String UPDATED_PERIODE = "BBBBBBBBBB";

    private static final String DEFAULT_ANTECEDENT = "AAAAAAAAAA";
    private static final String UPDATED_ANTECEDENT = "BBBBBBBBBB";

    private static final String DEFAULT_TRAITEMENT = "AAAAAAAAAA";
    private static final String UPDATED_TRAITEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/antecedents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AntecedentRepository antecedentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAntecedentMockMvc;

    private Antecedent antecedent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Antecedent createEntity(EntityManager em) {
        Antecedent antecedent = new Antecedent()
            .type(DEFAULT_TYPE)
            .periode(DEFAULT_PERIODE)
            .antecedent(DEFAULT_ANTECEDENT)
            .traitement(DEFAULT_TRAITEMENT);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        antecedent.setPatient(patient);
        return antecedent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Antecedent createUpdatedEntity(EntityManager em) {
        Antecedent antecedent = new Antecedent()
            .type(UPDATED_TYPE)
            .periode(UPDATED_PERIODE)
            .antecedent(UPDATED_ANTECEDENT)
            .traitement(UPDATED_TRAITEMENT);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        antecedent.setPatient(patient);
        return antecedent;
    }

    @BeforeEach
    public void initTest() {
        antecedent = createEntity(em);
    }

    @Test
    @Transactional
    void createAntecedent() throws Exception {
        int databaseSizeBeforeCreate = antecedentRepository.findAll().size();
        // Create the Antecedent
        restAntecedentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(antecedent)))
            .andExpect(status().isCreated());

        // Validate the Antecedent in the database
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeCreate + 1);
        Antecedent testAntecedent = antecedentList.get(antecedentList.size() - 1);
        assertThat(testAntecedent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAntecedent.getPeriode()).isEqualTo(DEFAULT_PERIODE);
        assertThat(testAntecedent.getAntecedent()).isEqualTo(DEFAULT_ANTECEDENT);
        assertThat(testAntecedent.getTraitement()).isEqualTo(DEFAULT_TRAITEMENT);
    }

    @Test
    @Transactional
    void createAntecedentWithExistingId() throws Exception {
        // Create the Antecedent with an existing ID
        antecedent.setId(1L);

        int databaseSizeBeforeCreate = antecedentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAntecedentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(antecedent)))
            .andExpect(status().isBadRequest());

        // Validate the Antecedent in the database
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = antecedentRepository.findAll().size();
        // set the field null
        antecedent.setType(null);

        // Create the Antecedent, which fails.

        restAntecedentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(antecedent)))
            .andExpect(status().isBadRequest());

        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = antecedentRepository.findAll().size();
        // set the field null
        antecedent.setPeriode(null);

        // Create the Antecedent, which fails.

        restAntecedentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(antecedent)))
            .andExpect(status().isBadRequest());

        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAntecedents() throws Exception {
        // Initialize the database
        antecedentRepository.saveAndFlush(antecedent);

        // Get all the antecedentList
        restAntecedentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(antecedent.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE)))
            .andExpect(jsonPath("$.[*].antecedent").value(hasItem(DEFAULT_ANTECEDENT.toString())))
            .andExpect(jsonPath("$.[*].traitement").value(hasItem(DEFAULT_TRAITEMENT.toString())));
    }

    @Test
    @Transactional
    void getAntecedent() throws Exception {
        // Initialize the database
        antecedentRepository.saveAndFlush(antecedent);

        // Get the antecedent
        restAntecedentMockMvc
            .perform(get(ENTITY_API_URL_ID, antecedent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(antecedent.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.periode").value(DEFAULT_PERIODE))
            .andExpect(jsonPath("$.antecedent").value(DEFAULT_ANTECEDENT.toString()))
            .andExpect(jsonPath("$.traitement").value(DEFAULT_TRAITEMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAntecedent() throws Exception {
        // Get the antecedent
        restAntecedentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAntecedent() throws Exception {
        // Initialize the database
        antecedentRepository.saveAndFlush(antecedent);

        int databaseSizeBeforeUpdate = antecedentRepository.findAll().size();

        // Update the antecedent
        Antecedent updatedAntecedent = antecedentRepository.findById(antecedent.getId()).get();
        // Disconnect from session so that the updates on updatedAntecedent are not directly saved in db
        em.detach(updatedAntecedent);
        updatedAntecedent.type(UPDATED_TYPE).periode(UPDATED_PERIODE).antecedent(UPDATED_ANTECEDENT).traitement(UPDATED_TRAITEMENT);

        restAntecedentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAntecedent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAntecedent))
            )
            .andExpect(status().isOk());

        // Validate the Antecedent in the database
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeUpdate);
        Antecedent testAntecedent = antecedentList.get(antecedentList.size() - 1);
        assertThat(testAntecedent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAntecedent.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testAntecedent.getAntecedent()).isEqualTo(UPDATED_ANTECEDENT);
        assertThat(testAntecedent.getTraitement()).isEqualTo(UPDATED_TRAITEMENT);
    }

    @Test
    @Transactional
    void putNonExistingAntecedent() throws Exception {
        int databaseSizeBeforeUpdate = antecedentRepository.findAll().size();
        antecedent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAntecedentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, antecedent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(antecedent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Antecedent in the database
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAntecedent() throws Exception {
        int databaseSizeBeforeUpdate = antecedentRepository.findAll().size();
        antecedent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntecedentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(antecedent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Antecedent in the database
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAntecedent() throws Exception {
        int databaseSizeBeforeUpdate = antecedentRepository.findAll().size();
        antecedent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntecedentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(antecedent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Antecedent in the database
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAntecedentWithPatch() throws Exception {
        // Initialize the database
        antecedentRepository.saveAndFlush(antecedent);

        int databaseSizeBeforeUpdate = antecedentRepository.findAll().size();

        // Update the antecedent using partial update
        Antecedent partialUpdatedAntecedent = new Antecedent();
        partialUpdatedAntecedent.setId(antecedent.getId());

        partialUpdatedAntecedent.antecedent(UPDATED_ANTECEDENT).traitement(UPDATED_TRAITEMENT);

        restAntecedentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAntecedent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAntecedent))
            )
            .andExpect(status().isOk());

        // Validate the Antecedent in the database
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeUpdate);
        Antecedent testAntecedent = antecedentList.get(antecedentList.size() - 1);
        assertThat(testAntecedent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAntecedent.getPeriode()).isEqualTo(DEFAULT_PERIODE);
        assertThat(testAntecedent.getAntecedent()).isEqualTo(UPDATED_ANTECEDENT);
        assertThat(testAntecedent.getTraitement()).isEqualTo(UPDATED_TRAITEMENT);
    }

    @Test
    @Transactional
    void fullUpdateAntecedentWithPatch() throws Exception {
        // Initialize the database
        antecedentRepository.saveAndFlush(antecedent);

        int databaseSizeBeforeUpdate = antecedentRepository.findAll().size();

        // Update the antecedent using partial update
        Antecedent partialUpdatedAntecedent = new Antecedent();
        partialUpdatedAntecedent.setId(antecedent.getId());

        partialUpdatedAntecedent.type(UPDATED_TYPE).periode(UPDATED_PERIODE).antecedent(UPDATED_ANTECEDENT).traitement(UPDATED_TRAITEMENT);

        restAntecedentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAntecedent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAntecedent))
            )
            .andExpect(status().isOk());

        // Validate the Antecedent in the database
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeUpdate);
        Antecedent testAntecedent = antecedentList.get(antecedentList.size() - 1);
        assertThat(testAntecedent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAntecedent.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testAntecedent.getAntecedent()).isEqualTo(UPDATED_ANTECEDENT);
        assertThat(testAntecedent.getTraitement()).isEqualTo(UPDATED_TRAITEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingAntecedent() throws Exception {
        int databaseSizeBeforeUpdate = antecedentRepository.findAll().size();
        antecedent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAntecedentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, antecedent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(antecedent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Antecedent in the database
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAntecedent() throws Exception {
        int databaseSizeBeforeUpdate = antecedentRepository.findAll().size();
        antecedent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntecedentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(antecedent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Antecedent in the database
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAntecedent() throws Exception {
        int databaseSizeBeforeUpdate = antecedentRepository.findAll().size();
        antecedent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntecedentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(antecedent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Antecedent in the database
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAntecedent() throws Exception {
        // Initialize the database
        antecedentRepository.saveAndFlush(antecedent);

        int databaseSizeBeforeDelete = antecedentRepository.findAll().size();

        // Delete the antecedent
        restAntecedentMockMvc
            .perform(delete(ENTITY_API_URL_ID, antecedent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Antecedent> antecedentList = antecedentRepository.findAll();
        assertThat(antecedentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
