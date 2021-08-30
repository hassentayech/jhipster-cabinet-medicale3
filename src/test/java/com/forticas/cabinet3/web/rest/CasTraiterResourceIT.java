package com.forticas.cabinet3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.forticas.cabinet3.IntegrationTest;
import com.forticas.cabinet3.domain.CasTraiter;
import com.forticas.cabinet3.domain.Patient;
import com.forticas.cabinet3.domain.enumeration.EtatActuel;
import com.forticas.cabinet3.domain.enumeration.ModeFacturation;
import com.forticas.cabinet3.repository.CasTraiterRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CasTraiterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CasTraiterResourceIT {

    private static final String DEFAULT_CAS = "AAAAAAAAAA";
    private static final String UPDATED_CAS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEPUIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEPUIS = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_HISTOIRE = "AAAAAAAAAA";
    private static final String UPDATED_HISTOIRE = "BBBBBBBBBB";

    private static final String DEFAULT_REMARQUES = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUES = "BBBBBBBBBB";

    private static final EtatActuel DEFAULT_ETAT_ACTUEL = EtatActuel.EN_TRAITEMENT;
    private static final EtatActuel UPDATED_ETAT_ACTUEL = EtatActuel.TRAITE;

    private static final ModeFacturation DEFAULT_MODE_FACTURATION = ModeFacturation.VISITES;
    private static final ModeFacturation UPDATED_MODE_FACTURATION = ModeFacturation.ACTES;

    private static final Integer DEFAULT_PRIX_FORFAITAIRE = 1;
    private static final Integer UPDATED_PRIX_FORFAITAIRE = 2;

    private static final String ENTITY_API_URL = "/api/cas-traiters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CasTraiterRepository casTraiterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCasTraiterMockMvc;

    private CasTraiter casTraiter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CasTraiter createEntity(EntityManager em) {
        CasTraiter casTraiter = new CasTraiter()
            .cas(DEFAULT_CAS)
            .depuis(DEFAULT_DEPUIS)
            .histoire(DEFAULT_HISTOIRE)
            .remarques(DEFAULT_REMARQUES)
            .etatActuel(DEFAULT_ETAT_ACTUEL)
            .modeFacturation(DEFAULT_MODE_FACTURATION)
            .prixForfaitaire(DEFAULT_PRIX_FORFAITAIRE);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        casTraiter.setPatient(patient);
        return casTraiter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CasTraiter createUpdatedEntity(EntityManager em) {
        CasTraiter casTraiter = new CasTraiter()
            .cas(UPDATED_CAS)
            .depuis(UPDATED_DEPUIS)
            .histoire(UPDATED_HISTOIRE)
            .remarques(UPDATED_REMARQUES)
            .etatActuel(UPDATED_ETAT_ACTUEL)
            .modeFacturation(UPDATED_MODE_FACTURATION)
            .prixForfaitaire(UPDATED_PRIX_FORFAITAIRE);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        casTraiter.setPatient(patient);
        return casTraiter;
    }

    @BeforeEach
    public void initTest() {
        casTraiter = createEntity(em);
    }

    @Test
    @Transactional
    void createCasTraiter() throws Exception {
        int databaseSizeBeforeCreate = casTraiterRepository.findAll().size();
        // Create the CasTraiter
        restCasTraiterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casTraiter)))
            .andExpect(status().isCreated());

        // Validate the CasTraiter in the database
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeCreate + 1);
        CasTraiter testCasTraiter = casTraiterList.get(casTraiterList.size() - 1);
        assertThat(testCasTraiter.getCas()).isEqualTo(DEFAULT_CAS);
        assertThat(testCasTraiter.getDepuis()).isEqualTo(DEFAULT_DEPUIS);
        assertThat(testCasTraiter.getHistoire()).isEqualTo(DEFAULT_HISTOIRE);
        assertThat(testCasTraiter.getRemarques()).isEqualTo(DEFAULT_REMARQUES);
        assertThat(testCasTraiter.getEtatActuel()).isEqualTo(DEFAULT_ETAT_ACTUEL);
        assertThat(testCasTraiter.getModeFacturation()).isEqualTo(DEFAULT_MODE_FACTURATION);
        assertThat(testCasTraiter.getPrixForfaitaire()).isEqualTo(DEFAULT_PRIX_FORFAITAIRE);
    }

    @Test
    @Transactional
    void createCasTraiterWithExistingId() throws Exception {
        // Create the CasTraiter with an existing ID
        casTraiter.setId(1L);

        int databaseSizeBeforeCreate = casTraiterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCasTraiterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casTraiter)))
            .andExpect(status().isBadRequest());

        // Validate the CasTraiter in the database
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCasIsRequired() throws Exception {
        int databaseSizeBeforeTest = casTraiterRepository.findAll().size();
        // set the field null
        casTraiter.setCas(null);

        // Create the CasTraiter, which fails.

        restCasTraiterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casTraiter)))
            .andExpect(status().isBadRequest());

        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepuisIsRequired() throws Exception {
        int databaseSizeBeforeTest = casTraiterRepository.findAll().size();
        // set the field null
        casTraiter.setDepuis(null);

        // Create the CasTraiter, which fails.

        restCasTraiterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casTraiter)))
            .andExpect(status().isBadRequest());

        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatActuelIsRequired() throws Exception {
        int databaseSizeBeforeTest = casTraiterRepository.findAll().size();
        // set the field null
        casTraiter.setEtatActuel(null);

        // Create the CasTraiter, which fails.

        restCasTraiterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casTraiter)))
            .andExpect(status().isBadRequest());

        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModeFacturationIsRequired() throws Exception {
        int databaseSizeBeforeTest = casTraiterRepository.findAll().size();
        // set the field null
        casTraiter.setModeFacturation(null);

        // Create the CasTraiter, which fails.

        restCasTraiterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casTraiter)))
            .andExpect(status().isBadRequest());

        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCasTraiters() throws Exception {
        // Initialize the database
        casTraiterRepository.saveAndFlush(casTraiter);

        // Get all the casTraiterList
        restCasTraiterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(casTraiter.getId().intValue())))
            .andExpect(jsonPath("$.[*].cas").value(hasItem(DEFAULT_CAS)))
            .andExpect(jsonPath("$.[*].depuis").value(hasItem(DEFAULT_DEPUIS.toString())))
            .andExpect(jsonPath("$.[*].histoire").value(hasItem(DEFAULT_HISTOIRE.toString())))
            .andExpect(jsonPath("$.[*].remarques").value(hasItem(DEFAULT_REMARQUES.toString())))
            .andExpect(jsonPath("$.[*].etatActuel").value(hasItem(DEFAULT_ETAT_ACTUEL.toString())))
            .andExpect(jsonPath("$.[*].modeFacturation").value(hasItem(DEFAULT_MODE_FACTURATION.toString())))
            .andExpect(jsonPath("$.[*].prixForfaitaire").value(hasItem(DEFAULT_PRIX_FORFAITAIRE)));
    }

    @Test
    @Transactional
    void getCasTraiter() throws Exception {
        // Initialize the database
        casTraiterRepository.saveAndFlush(casTraiter);

        // Get the casTraiter
        restCasTraiterMockMvc
            .perform(get(ENTITY_API_URL_ID, casTraiter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(casTraiter.getId().intValue()))
            .andExpect(jsonPath("$.cas").value(DEFAULT_CAS))
            .andExpect(jsonPath("$.depuis").value(DEFAULT_DEPUIS.toString()))
            .andExpect(jsonPath("$.histoire").value(DEFAULT_HISTOIRE.toString()))
            .andExpect(jsonPath("$.remarques").value(DEFAULT_REMARQUES.toString()))
            .andExpect(jsonPath("$.etatActuel").value(DEFAULT_ETAT_ACTUEL.toString()))
            .andExpect(jsonPath("$.modeFacturation").value(DEFAULT_MODE_FACTURATION.toString()))
            .andExpect(jsonPath("$.prixForfaitaire").value(DEFAULT_PRIX_FORFAITAIRE));
    }

    @Test
    @Transactional
    void getNonExistingCasTraiter() throws Exception {
        // Get the casTraiter
        restCasTraiterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCasTraiter() throws Exception {
        // Initialize the database
        casTraiterRepository.saveAndFlush(casTraiter);

        int databaseSizeBeforeUpdate = casTraiterRepository.findAll().size();

        // Update the casTraiter
        CasTraiter updatedCasTraiter = casTraiterRepository.findById(casTraiter.getId()).get();
        // Disconnect from session so that the updates on updatedCasTraiter are not directly saved in db
        em.detach(updatedCasTraiter);
        updatedCasTraiter
            .cas(UPDATED_CAS)
            .depuis(UPDATED_DEPUIS)
            .histoire(UPDATED_HISTOIRE)
            .remarques(UPDATED_REMARQUES)
            .etatActuel(UPDATED_ETAT_ACTUEL)
            .modeFacturation(UPDATED_MODE_FACTURATION)
            .prixForfaitaire(UPDATED_PRIX_FORFAITAIRE);

        restCasTraiterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCasTraiter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCasTraiter))
            )
            .andExpect(status().isOk());

        // Validate the CasTraiter in the database
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeUpdate);
        CasTraiter testCasTraiter = casTraiterList.get(casTraiterList.size() - 1);
        assertThat(testCasTraiter.getCas()).isEqualTo(UPDATED_CAS);
        assertThat(testCasTraiter.getDepuis()).isEqualTo(UPDATED_DEPUIS);
        assertThat(testCasTraiter.getHistoire()).isEqualTo(UPDATED_HISTOIRE);
        assertThat(testCasTraiter.getRemarques()).isEqualTo(UPDATED_REMARQUES);
        assertThat(testCasTraiter.getEtatActuel()).isEqualTo(UPDATED_ETAT_ACTUEL);
        assertThat(testCasTraiter.getModeFacturation()).isEqualTo(UPDATED_MODE_FACTURATION);
        assertThat(testCasTraiter.getPrixForfaitaire()).isEqualTo(UPDATED_PRIX_FORFAITAIRE);
    }

    @Test
    @Transactional
    void putNonExistingCasTraiter() throws Exception {
        int databaseSizeBeforeUpdate = casTraiterRepository.findAll().size();
        casTraiter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasTraiterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casTraiter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casTraiter))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasTraiter in the database
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCasTraiter() throws Exception {
        int databaseSizeBeforeUpdate = casTraiterRepository.findAll().size();
        casTraiter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasTraiterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casTraiter))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasTraiter in the database
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCasTraiter() throws Exception {
        int databaseSizeBeforeUpdate = casTraiterRepository.findAll().size();
        casTraiter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasTraiterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casTraiter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CasTraiter in the database
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCasTraiterWithPatch() throws Exception {
        // Initialize the database
        casTraiterRepository.saveAndFlush(casTraiter);

        int databaseSizeBeforeUpdate = casTraiterRepository.findAll().size();

        // Update the casTraiter using partial update
        CasTraiter partialUpdatedCasTraiter = new CasTraiter();
        partialUpdatedCasTraiter.setId(casTraiter.getId());

        partialUpdatedCasTraiter.depuis(UPDATED_DEPUIS).histoire(UPDATED_HISTOIRE).etatActuel(UPDATED_ETAT_ACTUEL);

        restCasTraiterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasTraiter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCasTraiter))
            )
            .andExpect(status().isOk());

        // Validate the CasTraiter in the database
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeUpdate);
        CasTraiter testCasTraiter = casTraiterList.get(casTraiterList.size() - 1);
        assertThat(testCasTraiter.getCas()).isEqualTo(DEFAULT_CAS);
        assertThat(testCasTraiter.getDepuis()).isEqualTo(UPDATED_DEPUIS);
        assertThat(testCasTraiter.getHistoire()).isEqualTo(UPDATED_HISTOIRE);
        assertThat(testCasTraiter.getRemarques()).isEqualTo(DEFAULT_REMARQUES);
        assertThat(testCasTraiter.getEtatActuel()).isEqualTo(UPDATED_ETAT_ACTUEL);
        assertThat(testCasTraiter.getModeFacturation()).isEqualTo(DEFAULT_MODE_FACTURATION);
        assertThat(testCasTraiter.getPrixForfaitaire()).isEqualTo(DEFAULT_PRIX_FORFAITAIRE);
    }

    @Test
    @Transactional
    void fullUpdateCasTraiterWithPatch() throws Exception {
        // Initialize the database
        casTraiterRepository.saveAndFlush(casTraiter);

        int databaseSizeBeforeUpdate = casTraiterRepository.findAll().size();

        // Update the casTraiter using partial update
        CasTraiter partialUpdatedCasTraiter = new CasTraiter();
        partialUpdatedCasTraiter.setId(casTraiter.getId());

        partialUpdatedCasTraiter
            .cas(UPDATED_CAS)
            .depuis(UPDATED_DEPUIS)
            .histoire(UPDATED_HISTOIRE)
            .remarques(UPDATED_REMARQUES)
            .etatActuel(UPDATED_ETAT_ACTUEL)
            .modeFacturation(UPDATED_MODE_FACTURATION)
            .prixForfaitaire(UPDATED_PRIX_FORFAITAIRE);

        restCasTraiterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasTraiter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCasTraiter))
            )
            .andExpect(status().isOk());

        // Validate the CasTraiter in the database
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeUpdate);
        CasTraiter testCasTraiter = casTraiterList.get(casTraiterList.size() - 1);
        assertThat(testCasTraiter.getCas()).isEqualTo(UPDATED_CAS);
        assertThat(testCasTraiter.getDepuis()).isEqualTo(UPDATED_DEPUIS);
        assertThat(testCasTraiter.getHistoire()).isEqualTo(UPDATED_HISTOIRE);
        assertThat(testCasTraiter.getRemarques()).isEqualTo(UPDATED_REMARQUES);
        assertThat(testCasTraiter.getEtatActuel()).isEqualTo(UPDATED_ETAT_ACTUEL);
        assertThat(testCasTraiter.getModeFacturation()).isEqualTo(UPDATED_MODE_FACTURATION);
        assertThat(testCasTraiter.getPrixForfaitaire()).isEqualTo(UPDATED_PRIX_FORFAITAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingCasTraiter() throws Exception {
        int databaseSizeBeforeUpdate = casTraiterRepository.findAll().size();
        casTraiter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasTraiterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, casTraiter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casTraiter))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasTraiter in the database
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCasTraiter() throws Exception {
        int databaseSizeBeforeUpdate = casTraiterRepository.findAll().size();
        casTraiter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasTraiterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casTraiter))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasTraiter in the database
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCasTraiter() throws Exception {
        int databaseSizeBeforeUpdate = casTraiterRepository.findAll().size();
        casTraiter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasTraiterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(casTraiter))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CasTraiter in the database
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCasTraiter() throws Exception {
        // Initialize the database
        casTraiterRepository.saveAndFlush(casTraiter);

        int databaseSizeBeforeDelete = casTraiterRepository.findAll().size();

        // Delete the casTraiter
        restCasTraiterMockMvc
            .perform(delete(ENTITY_API_URL_ID, casTraiter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CasTraiter> casTraiterList = casTraiterRepository.findAll();
        assertThat(casTraiterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
