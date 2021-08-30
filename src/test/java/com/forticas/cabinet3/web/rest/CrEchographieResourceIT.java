package com.forticas.cabinet3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.forticas.cabinet3.IntegrationTest;
import com.forticas.cabinet3.domain.CrEchographie;
import com.forticas.cabinet3.domain.Visite;
import com.forticas.cabinet3.domain.enumeration.EchoAspect;
import com.forticas.cabinet3.domain.enumeration.EchoAspect;
import com.forticas.cabinet3.domain.enumeration.EchoAspect;
import com.forticas.cabinet3.domain.enumeration.EchoAspect;
import com.forticas.cabinet3.domain.enumeration.EchoAspect;
import com.forticas.cabinet3.domain.enumeration.EchoAspect;
import com.forticas.cabinet3.repository.CrEchographieRepository;
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
 * Integration tests for the {@link CrEchographieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CrEchographieResourceIT {

    private static final EchoAspect DEFAULT_ASPECT_FOIE = EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE;
    private static final EchoAspect UPDATED_ASPECT_FOIE = EchoAspect.TAILLE_AUGMENTEE;

    private static final String DEFAULT_OBSERVATION_FOIE = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION_FOIE = "BBBBBBBBBB";

    private static final EchoAspect DEFAULT_ASPECT_VESICULE = EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE;
    private static final EchoAspect UPDATED_ASPECT_VESICULE = EchoAspect.TAILLE_AUGMENTEE;

    private static final String DEFAULT_OBSERVATION_VESICULE = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION_VESICULE = "BBBBBBBBBB";

    private static final EchoAspect DEFAULT_ASPECT_TROC_VOIE_VEINE = EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE;
    private static final EchoAspect UPDATED_ASPECT_TROC_VOIE_VEINE = EchoAspect.TAILLE_AUGMENTEE;

    private static final String DEFAULT_OBSERVATION_TROC_VOIE_VEINE = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION_TROC_VOIE_VEINE = "BBBBBBBBBB";

    private static final EchoAspect DEFAULT_ASPECT_REINS = EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE;
    private static final EchoAspect UPDATED_ASPECT_REINS = EchoAspect.TAILLE_AUGMENTEE;

    private static final String DEFAULT_OBSERVATION_REINS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION_REINS = "BBBBBBBBBB";

    private static final EchoAspect DEFAULT_ASPECT_RATE = EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE;
    private static final EchoAspect UPDATED_ASPECT_RATE = EchoAspect.TAILLE_AUGMENTEE;

    private static final String DEFAULT_OBSERVATION_RATE = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION_RATE = "BBBBBBBBBB";

    private static final EchoAspect DEFAULT_ASPECT_PANCREAS = EchoAspect.TAILLE_ECHOSTRUCURE_NORMALE;
    private static final EchoAspect UPDATED_ASPECT_PANCREAS = EchoAspect.TAILLE_AUGMENTEE;

    private static final String DEFAULT_OBSERVATION_PANCREAS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION_PANCREAS = "BBBBBBBBBB";

    private static final String DEFAULT_AUTRE_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cr-echographies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrEchographieRepository crEchographieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrEchographieMockMvc;

    private CrEchographie crEchographie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrEchographie createEntity(EntityManager em) {
        CrEchographie crEchographie = new CrEchographie()
            .aspectFoie(DEFAULT_ASPECT_FOIE)
            .observationFoie(DEFAULT_OBSERVATION_FOIE)
            .aspectVesicule(DEFAULT_ASPECT_VESICULE)
            .observationVesicule(DEFAULT_OBSERVATION_VESICULE)
            .aspectTrocVoieVeine(DEFAULT_ASPECT_TROC_VOIE_VEINE)
            .observationTrocVoieVeine(DEFAULT_OBSERVATION_TROC_VOIE_VEINE)
            .aspectReins(DEFAULT_ASPECT_REINS)
            .observationReins(DEFAULT_OBSERVATION_REINS)
            .aspectRate(DEFAULT_ASPECT_RATE)
            .observationRate(DEFAULT_OBSERVATION_RATE)
            .aspectPancreas(DEFAULT_ASPECT_PANCREAS)
            .observationPancreas(DEFAULT_OBSERVATION_PANCREAS)
            .autreObservation(DEFAULT_AUTRE_OBSERVATION);
        // Add required entity
        Visite visite;
        if (TestUtil.findAll(em, Visite.class).isEmpty()) {
            visite = VisiteResourceIT.createEntity(em);
            em.persist(visite);
            em.flush();
        } else {
            visite = TestUtil.findAll(em, Visite.class).get(0);
        }
        crEchographie.setVisite(visite);
        return crEchographie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrEchographie createUpdatedEntity(EntityManager em) {
        CrEchographie crEchographie = new CrEchographie()
            .aspectFoie(UPDATED_ASPECT_FOIE)
            .observationFoie(UPDATED_OBSERVATION_FOIE)
            .aspectVesicule(UPDATED_ASPECT_VESICULE)
            .observationVesicule(UPDATED_OBSERVATION_VESICULE)
            .aspectTrocVoieVeine(UPDATED_ASPECT_TROC_VOIE_VEINE)
            .observationTrocVoieVeine(UPDATED_OBSERVATION_TROC_VOIE_VEINE)
            .aspectReins(UPDATED_ASPECT_REINS)
            .observationReins(UPDATED_OBSERVATION_REINS)
            .aspectRate(UPDATED_ASPECT_RATE)
            .observationRate(UPDATED_OBSERVATION_RATE)
            .aspectPancreas(UPDATED_ASPECT_PANCREAS)
            .observationPancreas(UPDATED_OBSERVATION_PANCREAS)
            .autreObservation(UPDATED_AUTRE_OBSERVATION);
        // Add required entity
        Visite visite;
        if (TestUtil.findAll(em, Visite.class).isEmpty()) {
            visite = VisiteResourceIT.createUpdatedEntity(em);
            em.persist(visite);
            em.flush();
        } else {
            visite = TestUtil.findAll(em, Visite.class).get(0);
        }
        crEchographie.setVisite(visite);
        return crEchographie;
    }

    @BeforeEach
    public void initTest() {
        crEchographie = createEntity(em);
    }

    @Test
    @Transactional
    void createCrEchographie() throws Exception {
        int databaseSizeBeforeCreate = crEchographieRepository.findAll().size();
        // Create the CrEchographie
        restCrEchographieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crEchographie)))
            .andExpect(status().isCreated());

        // Validate the CrEchographie in the database
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeCreate + 1);
        CrEchographie testCrEchographie = crEchographieList.get(crEchographieList.size() - 1);
        assertThat(testCrEchographie.getAspectFoie()).isEqualTo(DEFAULT_ASPECT_FOIE);
        assertThat(testCrEchographie.getObservationFoie()).isEqualTo(DEFAULT_OBSERVATION_FOIE);
        assertThat(testCrEchographie.getAspectVesicule()).isEqualTo(DEFAULT_ASPECT_VESICULE);
        assertThat(testCrEchographie.getObservationVesicule()).isEqualTo(DEFAULT_OBSERVATION_VESICULE);
        assertThat(testCrEchographie.getAspectTrocVoieVeine()).isEqualTo(DEFAULT_ASPECT_TROC_VOIE_VEINE);
        assertThat(testCrEchographie.getObservationTrocVoieVeine()).isEqualTo(DEFAULT_OBSERVATION_TROC_VOIE_VEINE);
        assertThat(testCrEchographie.getAspectReins()).isEqualTo(DEFAULT_ASPECT_REINS);
        assertThat(testCrEchographie.getObservationReins()).isEqualTo(DEFAULT_OBSERVATION_REINS);
        assertThat(testCrEchographie.getAspectRate()).isEqualTo(DEFAULT_ASPECT_RATE);
        assertThat(testCrEchographie.getObservationRate()).isEqualTo(DEFAULT_OBSERVATION_RATE);
        assertThat(testCrEchographie.getAspectPancreas()).isEqualTo(DEFAULT_ASPECT_PANCREAS);
        assertThat(testCrEchographie.getObservationPancreas()).isEqualTo(DEFAULT_OBSERVATION_PANCREAS);
        assertThat(testCrEchographie.getAutreObservation()).isEqualTo(DEFAULT_AUTRE_OBSERVATION);
    }

    @Test
    @Transactional
    void createCrEchographieWithExistingId() throws Exception {
        // Create the CrEchographie with an existing ID
        crEchographie.setId(1L);

        int databaseSizeBeforeCreate = crEchographieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrEchographieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crEchographie)))
            .andExpect(status().isBadRequest());

        // Validate the CrEchographie in the database
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCrEchographies() throws Exception {
        // Initialize the database
        crEchographieRepository.saveAndFlush(crEchographie);

        // Get all the crEchographieList
        restCrEchographieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crEchographie.getId().intValue())))
            .andExpect(jsonPath("$.[*].aspectFoie").value(hasItem(DEFAULT_ASPECT_FOIE.toString())))
            .andExpect(jsonPath("$.[*].observationFoie").value(hasItem(DEFAULT_OBSERVATION_FOIE)))
            .andExpect(jsonPath("$.[*].aspectVesicule").value(hasItem(DEFAULT_ASPECT_VESICULE.toString())))
            .andExpect(jsonPath("$.[*].observationVesicule").value(hasItem(DEFAULT_OBSERVATION_VESICULE)))
            .andExpect(jsonPath("$.[*].aspectTrocVoieVeine").value(hasItem(DEFAULT_ASPECT_TROC_VOIE_VEINE.toString())))
            .andExpect(jsonPath("$.[*].observationTrocVoieVeine").value(hasItem(DEFAULT_OBSERVATION_TROC_VOIE_VEINE)))
            .andExpect(jsonPath("$.[*].aspectReins").value(hasItem(DEFAULT_ASPECT_REINS.toString())))
            .andExpect(jsonPath("$.[*].observationReins").value(hasItem(DEFAULT_OBSERVATION_REINS)))
            .andExpect(jsonPath("$.[*].aspectRate").value(hasItem(DEFAULT_ASPECT_RATE.toString())))
            .andExpect(jsonPath("$.[*].observationRate").value(hasItem(DEFAULT_OBSERVATION_RATE)))
            .andExpect(jsonPath("$.[*].aspectPancreas").value(hasItem(DEFAULT_ASPECT_PANCREAS.toString())))
            .andExpect(jsonPath("$.[*].observationPancreas").value(hasItem(DEFAULT_OBSERVATION_PANCREAS)))
            .andExpect(jsonPath("$.[*].autreObservation").value(hasItem(DEFAULT_AUTRE_OBSERVATION)));
    }

    @Test
    @Transactional
    void getCrEchographie() throws Exception {
        // Initialize the database
        crEchographieRepository.saveAndFlush(crEchographie);

        // Get the crEchographie
        restCrEchographieMockMvc
            .perform(get(ENTITY_API_URL_ID, crEchographie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crEchographie.getId().intValue()))
            .andExpect(jsonPath("$.aspectFoie").value(DEFAULT_ASPECT_FOIE.toString()))
            .andExpect(jsonPath("$.observationFoie").value(DEFAULT_OBSERVATION_FOIE))
            .andExpect(jsonPath("$.aspectVesicule").value(DEFAULT_ASPECT_VESICULE.toString()))
            .andExpect(jsonPath("$.observationVesicule").value(DEFAULT_OBSERVATION_VESICULE))
            .andExpect(jsonPath("$.aspectTrocVoieVeine").value(DEFAULT_ASPECT_TROC_VOIE_VEINE.toString()))
            .andExpect(jsonPath("$.observationTrocVoieVeine").value(DEFAULT_OBSERVATION_TROC_VOIE_VEINE))
            .andExpect(jsonPath("$.aspectReins").value(DEFAULT_ASPECT_REINS.toString()))
            .andExpect(jsonPath("$.observationReins").value(DEFAULT_OBSERVATION_REINS))
            .andExpect(jsonPath("$.aspectRate").value(DEFAULT_ASPECT_RATE.toString()))
            .andExpect(jsonPath("$.observationRate").value(DEFAULT_OBSERVATION_RATE))
            .andExpect(jsonPath("$.aspectPancreas").value(DEFAULT_ASPECT_PANCREAS.toString()))
            .andExpect(jsonPath("$.observationPancreas").value(DEFAULT_OBSERVATION_PANCREAS))
            .andExpect(jsonPath("$.autreObservation").value(DEFAULT_AUTRE_OBSERVATION));
    }

    @Test
    @Transactional
    void getNonExistingCrEchographie() throws Exception {
        // Get the crEchographie
        restCrEchographieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrEchographie() throws Exception {
        // Initialize the database
        crEchographieRepository.saveAndFlush(crEchographie);

        int databaseSizeBeforeUpdate = crEchographieRepository.findAll().size();

        // Update the crEchographie
        CrEchographie updatedCrEchographie = crEchographieRepository.findById(crEchographie.getId()).get();
        // Disconnect from session so that the updates on updatedCrEchographie are not directly saved in db
        em.detach(updatedCrEchographie);
        updatedCrEchographie
            .aspectFoie(UPDATED_ASPECT_FOIE)
            .observationFoie(UPDATED_OBSERVATION_FOIE)
            .aspectVesicule(UPDATED_ASPECT_VESICULE)
            .observationVesicule(UPDATED_OBSERVATION_VESICULE)
            .aspectTrocVoieVeine(UPDATED_ASPECT_TROC_VOIE_VEINE)
            .observationTrocVoieVeine(UPDATED_OBSERVATION_TROC_VOIE_VEINE)
            .aspectReins(UPDATED_ASPECT_REINS)
            .observationReins(UPDATED_OBSERVATION_REINS)
            .aspectRate(UPDATED_ASPECT_RATE)
            .observationRate(UPDATED_OBSERVATION_RATE)
            .aspectPancreas(UPDATED_ASPECT_PANCREAS)
            .observationPancreas(UPDATED_OBSERVATION_PANCREAS)
            .autreObservation(UPDATED_AUTRE_OBSERVATION);

        restCrEchographieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCrEchographie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCrEchographie))
            )
            .andExpect(status().isOk());

        // Validate the CrEchographie in the database
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeUpdate);
        CrEchographie testCrEchographie = crEchographieList.get(crEchographieList.size() - 1);
        assertThat(testCrEchographie.getAspectFoie()).isEqualTo(UPDATED_ASPECT_FOIE);
        assertThat(testCrEchographie.getObservationFoie()).isEqualTo(UPDATED_OBSERVATION_FOIE);
        assertThat(testCrEchographie.getAspectVesicule()).isEqualTo(UPDATED_ASPECT_VESICULE);
        assertThat(testCrEchographie.getObservationVesicule()).isEqualTo(UPDATED_OBSERVATION_VESICULE);
        assertThat(testCrEchographie.getAspectTrocVoieVeine()).isEqualTo(UPDATED_ASPECT_TROC_VOIE_VEINE);
        assertThat(testCrEchographie.getObservationTrocVoieVeine()).isEqualTo(UPDATED_OBSERVATION_TROC_VOIE_VEINE);
        assertThat(testCrEchographie.getAspectReins()).isEqualTo(UPDATED_ASPECT_REINS);
        assertThat(testCrEchographie.getObservationReins()).isEqualTo(UPDATED_OBSERVATION_REINS);
        assertThat(testCrEchographie.getAspectRate()).isEqualTo(UPDATED_ASPECT_RATE);
        assertThat(testCrEchographie.getObservationRate()).isEqualTo(UPDATED_OBSERVATION_RATE);
        assertThat(testCrEchographie.getAspectPancreas()).isEqualTo(UPDATED_ASPECT_PANCREAS);
        assertThat(testCrEchographie.getObservationPancreas()).isEqualTo(UPDATED_OBSERVATION_PANCREAS);
        assertThat(testCrEchographie.getAutreObservation()).isEqualTo(UPDATED_AUTRE_OBSERVATION);
    }

    @Test
    @Transactional
    void putNonExistingCrEchographie() throws Exception {
        int databaseSizeBeforeUpdate = crEchographieRepository.findAll().size();
        crEchographie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrEchographieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crEchographie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crEchographie))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrEchographie in the database
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrEchographie() throws Exception {
        int databaseSizeBeforeUpdate = crEchographieRepository.findAll().size();
        crEchographie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrEchographieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crEchographie))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrEchographie in the database
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrEchographie() throws Exception {
        int databaseSizeBeforeUpdate = crEchographieRepository.findAll().size();
        crEchographie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrEchographieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crEchographie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrEchographie in the database
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCrEchographieWithPatch() throws Exception {
        // Initialize the database
        crEchographieRepository.saveAndFlush(crEchographie);

        int databaseSizeBeforeUpdate = crEchographieRepository.findAll().size();

        // Update the crEchographie using partial update
        CrEchographie partialUpdatedCrEchographie = new CrEchographie();
        partialUpdatedCrEchographie.setId(crEchographie.getId());

        partialUpdatedCrEchographie
            .aspectFoie(UPDATED_ASPECT_FOIE)
            .observationFoie(UPDATED_OBSERVATION_FOIE)
            .aspectVesicule(UPDATED_ASPECT_VESICULE)
            .aspectTrocVoieVeine(UPDATED_ASPECT_TROC_VOIE_VEINE)
            .observationTrocVoieVeine(UPDATED_OBSERVATION_TROC_VOIE_VEINE)
            .aspectRate(UPDATED_ASPECT_RATE)
            .observationRate(UPDATED_OBSERVATION_RATE)
            .autreObservation(UPDATED_AUTRE_OBSERVATION);

        restCrEchographieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrEchographie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrEchographie))
            )
            .andExpect(status().isOk());

        // Validate the CrEchographie in the database
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeUpdate);
        CrEchographie testCrEchographie = crEchographieList.get(crEchographieList.size() - 1);
        assertThat(testCrEchographie.getAspectFoie()).isEqualTo(UPDATED_ASPECT_FOIE);
        assertThat(testCrEchographie.getObservationFoie()).isEqualTo(UPDATED_OBSERVATION_FOIE);
        assertThat(testCrEchographie.getAspectVesicule()).isEqualTo(UPDATED_ASPECT_VESICULE);
        assertThat(testCrEchographie.getObservationVesicule()).isEqualTo(DEFAULT_OBSERVATION_VESICULE);
        assertThat(testCrEchographie.getAspectTrocVoieVeine()).isEqualTo(UPDATED_ASPECT_TROC_VOIE_VEINE);
        assertThat(testCrEchographie.getObservationTrocVoieVeine()).isEqualTo(UPDATED_OBSERVATION_TROC_VOIE_VEINE);
        assertThat(testCrEchographie.getAspectReins()).isEqualTo(DEFAULT_ASPECT_REINS);
        assertThat(testCrEchographie.getObservationReins()).isEqualTo(DEFAULT_OBSERVATION_REINS);
        assertThat(testCrEchographie.getAspectRate()).isEqualTo(UPDATED_ASPECT_RATE);
        assertThat(testCrEchographie.getObservationRate()).isEqualTo(UPDATED_OBSERVATION_RATE);
        assertThat(testCrEchographie.getAspectPancreas()).isEqualTo(DEFAULT_ASPECT_PANCREAS);
        assertThat(testCrEchographie.getObservationPancreas()).isEqualTo(DEFAULT_OBSERVATION_PANCREAS);
        assertThat(testCrEchographie.getAutreObservation()).isEqualTo(UPDATED_AUTRE_OBSERVATION);
    }

    @Test
    @Transactional
    void fullUpdateCrEchographieWithPatch() throws Exception {
        // Initialize the database
        crEchographieRepository.saveAndFlush(crEchographie);

        int databaseSizeBeforeUpdate = crEchographieRepository.findAll().size();

        // Update the crEchographie using partial update
        CrEchographie partialUpdatedCrEchographie = new CrEchographie();
        partialUpdatedCrEchographie.setId(crEchographie.getId());

        partialUpdatedCrEchographie
            .aspectFoie(UPDATED_ASPECT_FOIE)
            .observationFoie(UPDATED_OBSERVATION_FOIE)
            .aspectVesicule(UPDATED_ASPECT_VESICULE)
            .observationVesicule(UPDATED_OBSERVATION_VESICULE)
            .aspectTrocVoieVeine(UPDATED_ASPECT_TROC_VOIE_VEINE)
            .observationTrocVoieVeine(UPDATED_OBSERVATION_TROC_VOIE_VEINE)
            .aspectReins(UPDATED_ASPECT_REINS)
            .observationReins(UPDATED_OBSERVATION_REINS)
            .aspectRate(UPDATED_ASPECT_RATE)
            .observationRate(UPDATED_OBSERVATION_RATE)
            .aspectPancreas(UPDATED_ASPECT_PANCREAS)
            .observationPancreas(UPDATED_OBSERVATION_PANCREAS)
            .autreObservation(UPDATED_AUTRE_OBSERVATION);

        restCrEchographieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrEchographie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrEchographie))
            )
            .andExpect(status().isOk());

        // Validate the CrEchographie in the database
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeUpdate);
        CrEchographie testCrEchographie = crEchographieList.get(crEchographieList.size() - 1);
        assertThat(testCrEchographie.getAspectFoie()).isEqualTo(UPDATED_ASPECT_FOIE);
        assertThat(testCrEchographie.getObservationFoie()).isEqualTo(UPDATED_OBSERVATION_FOIE);
        assertThat(testCrEchographie.getAspectVesicule()).isEqualTo(UPDATED_ASPECT_VESICULE);
        assertThat(testCrEchographie.getObservationVesicule()).isEqualTo(UPDATED_OBSERVATION_VESICULE);
        assertThat(testCrEchographie.getAspectTrocVoieVeine()).isEqualTo(UPDATED_ASPECT_TROC_VOIE_VEINE);
        assertThat(testCrEchographie.getObservationTrocVoieVeine()).isEqualTo(UPDATED_OBSERVATION_TROC_VOIE_VEINE);
        assertThat(testCrEchographie.getAspectReins()).isEqualTo(UPDATED_ASPECT_REINS);
        assertThat(testCrEchographie.getObservationReins()).isEqualTo(UPDATED_OBSERVATION_REINS);
        assertThat(testCrEchographie.getAspectRate()).isEqualTo(UPDATED_ASPECT_RATE);
        assertThat(testCrEchographie.getObservationRate()).isEqualTo(UPDATED_OBSERVATION_RATE);
        assertThat(testCrEchographie.getAspectPancreas()).isEqualTo(UPDATED_ASPECT_PANCREAS);
        assertThat(testCrEchographie.getObservationPancreas()).isEqualTo(UPDATED_OBSERVATION_PANCREAS);
        assertThat(testCrEchographie.getAutreObservation()).isEqualTo(UPDATED_AUTRE_OBSERVATION);
    }

    @Test
    @Transactional
    void patchNonExistingCrEchographie() throws Exception {
        int databaseSizeBeforeUpdate = crEchographieRepository.findAll().size();
        crEchographie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrEchographieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crEchographie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crEchographie))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrEchographie in the database
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrEchographie() throws Exception {
        int databaseSizeBeforeUpdate = crEchographieRepository.findAll().size();
        crEchographie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrEchographieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crEchographie))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrEchographie in the database
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrEchographie() throws Exception {
        int databaseSizeBeforeUpdate = crEchographieRepository.findAll().size();
        crEchographie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrEchographieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(crEchographie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrEchographie in the database
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCrEchographie() throws Exception {
        // Initialize the database
        crEchographieRepository.saveAndFlush(crEchographie);

        int databaseSizeBeforeDelete = crEchographieRepository.findAll().size();

        // Delete the crEchographie
        restCrEchographieMockMvc
            .perform(delete(ENTITY_API_URL_ID, crEchographie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrEchographie> crEchographieList = crEchographieRepository.findAll();
        assertThat(crEchographieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
