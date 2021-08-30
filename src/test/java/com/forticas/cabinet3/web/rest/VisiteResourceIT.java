package com.forticas.cabinet3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.forticas.cabinet3.IntegrationTest;
import com.forticas.cabinet3.domain.CasTraiter;
import com.forticas.cabinet3.domain.Visite;
import com.forticas.cabinet3.repository.VisiteRepository;
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
 * Integration tests for the {@link VisiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VisiteResourceIT {

    private static final Boolean DEFAULT_CONTROL = false;
    private static final Boolean UPDATED_CONTROL = true;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MOTIF = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBB";

    private static final String DEFAULT_INTERROGATOIRE = "AAAAAAAAAA";
    private static final String UPDATED_INTERROGATOIRE = "BBBBBBBBBB";

    private static final String DEFAULT_EXAMEN = "AAAAAAAAAA";
    private static final String UPDATED_EXAMEN = "BBBBBBBBBB";

    private static final String DEFAULT_CONCLUSION = "AAAAAAAAAA";
    private static final String UPDATED_CONCLUSION = "BBBBBBBBBB";

    private static final Integer DEFAULT_HONORAIRE = 1;
    private static final Integer UPDATED_HONORAIRE = 2;

    private static final String ENTITY_API_URL = "/api/visites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VisiteRepository visiteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVisiteMockMvc;

    private Visite visite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visite createEntity(EntityManager em) {
        Visite visite = new Visite()
            .control(DEFAULT_CONTROL)
            .date(DEFAULT_DATE)
            .motif(DEFAULT_MOTIF)
            .interrogatoire(DEFAULT_INTERROGATOIRE)
            .examen(DEFAULT_EXAMEN)
            .conclusion(DEFAULT_CONCLUSION)
            .honoraire(DEFAULT_HONORAIRE);
        // Add required entity
        CasTraiter casTraiter;
        if (TestUtil.findAll(em, CasTraiter.class).isEmpty()) {
            casTraiter = CasTraiterResourceIT.createEntity(em);
            em.persist(casTraiter);
            em.flush();
        } else {
            casTraiter = TestUtil.findAll(em, CasTraiter.class).get(0);
        }
        visite.setCasTraiter(casTraiter);
        return visite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visite createUpdatedEntity(EntityManager em) {
        Visite visite = new Visite()
            .control(UPDATED_CONTROL)
            .date(UPDATED_DATE)
            .motif(UPDATED_MOTIF)
            .interrogatoire(UPDATED_INTERROGATOIRE)
            .examen(UPDATED_EXAMEN)
            .conclusion(UPDATED_CONCLUSION)
            .honoraire(UPDATED_HONORAIRE);
        // Add required entity
        CasTraiter casTraiter;
        if (TestUtil.findAll(em, CasTraiter.class).isEmpty()) {
            casTraiter = CasTraiterResourceIT.createUpdatedEntity(em);
            em.persist(casTraiter);
            em.flush();
        } else {
            casTraiter = TestUtil.findAll(em, CasTraiter.class).get(0);
        }
        visite.setCasTraiter(casTraiter);
        return visite;
    }

    @BeforeEach
    public void initTest() {
        visite = createEntity(em);
    }

    @Test
    @Transactional
    void createVisite() throws Exception {
        int databaseSizeBeforeCreate = visiteRepository.findAll().size();
        // Create the Visite
        restVisiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visite)))
            .andExpect(status().isCreated());

        // Validate the Visite in the database
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeCreate + 1);
        Visite testVisite = visiteList.get(visiteList.size() - 1);
        assertThat(testVisite.getControl()).isEqualTo(DEFAULT_CONTROL);
        assertThat(testVisite.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testVisite.getMotif()).isEqualTo(DEFAULT_MOTIF);
        assertThat(testVisite.getInterrogatoire()).isEqualTo(DEFAULT_INTERROGATOIRE);
        assertThat(testVisite.getExamen()).isEqualTo(DEFAULT_EXAMEN);
        assertThat(testVisite.getConclusion()).isEqualTo(DEFAULT_CONCLUSION);
        assertThat(testVisite.getHonoraire()).isEqualTo(DEFAULT_HONORAIRE);
    }

    @Test
    @Transactional
    void createVisiteWithExistingId() throws Exception {
        // Create the Visite with an existing ID
        visite.setId(1L);

        int databaseSizeBeforeCreate = visiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visite)))
            .andExpect(status().isBadRequest());

        // Validate the Visite in the database
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHonoraireIsRequired() throws Exception {
        int databaseSizeBeforeTest = visiteRepository.findAll().size();
        // set the field null
        visite.setHonoraire(null);

        // Create the Visite, which fails.

        restVisiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visite)))
            .andExpect(status().isBadRequest());

        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVisites() throws Exception {
        // Initialize the database
        visiteRepository.saveAndFlush(visite);

        // Get all the visiteList
        restVisiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visite.getId().intValue())))
            .andExpect(jsonPath("$.[*].control").value(hasItem(DEFAULT_CONTROL.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)))
            .andExpect(jsonPath("$.[*].interrogatoire").value(hasItem(DEFAULT_INTERROGATOIRE.toString())))
            .andExpect(jsonPath("$.[*].examen").value(hasItem(DEFAULT_EXAMEN.toString())))
            .andExpect(jsonPath("$.[*].conclusion").value(hasItem(DEFAULT_CONCLUSION.toString())))
            .andExpect(jsonPath("$.[*].honoraire").value(hasItem(DEFAULT_HONORAIRE)));
    }

    @Test
    @Transactional
    void getVisite() throws Exception {
        // Initialize the database
        visiteRepository.saveAndFlush(visite);

        // Get the visite
        restVisiteMockMvc
            .perform(get(ENTITY_API_URL_ID, visite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(visite.getId().intValue()))
            .andExpect(jsonPath("$.control").value(DEFAULT_CONTROL.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF))
            .andExpect(jsonPath("$.interrogatoire").value(DEFAULT_INTERROGATOIRE.toString()))
            .andExpect(jsonPath("$.examen").value(DEFAULT_EXAMEN.toString()))
            .andExpect(jsonPath("$.conclusion").value(DEFAULT_CONCLUSION.toString()))
            .andExpect(jsonPath("$.honoraire").value(DEFAULT_HONORAIRE));
    }

    @Test
    @Transactional
    void getNonExistingVisite() throws Exception {
        // Get the visite
        restVisiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVisite() throws Exception {
        // Initialize the database
        visiteRepository.saveAndFlush(visite);

        int databaseSizeBeforeUpdate = visiteRepository.findAll().size();

        // Update the visite
        Visite updatedVisite = visiteRepository.findById(visite.getId()).get();
        // Disconnect from session so that the updates on updatedVisite are not directly saved in db
        em.detach(updatedVisite);
        updatedVisite
            .control(UPDATED_CONTROL)
            .date(UPDATED_DATE)
            .motif(UPDATED_MOTIF)
            .interrogatoire(UPDATED_INTERROGATOIRE)
            .examen(UPDATED_EXAMEN)
            .conclusion(UPDATED_CONCLUSION)
            .honoraire(UPDATED_HONORAIRE);

        restVisiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVisite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVisite))
            )
            .andExpect(status().isOk());

        // Validate the Visite in the database
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeUpdate);
        Visite testVisite = visiteList.get(visiteList.size() - 1);
        assertThat(testVisite.getControl()).isEqualTo(UPDATED_CONTROL);
        assertThat(testVisite.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVisite.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testVisite.getInterrogatoire()).isEqualTo(UPDATED_INTERROGATOIRE);
        assertThat(testVisite.getExamen()).isEqualTo(UPDATED_EXAMEN);
        assertThat(testVisite.getConclusion()).isEqualTo(UPDATED_CONCLUSION);
        assertThat(testVisite.getHonoraire()).isEqualTo(UPDATED_HONORAIRE);
    }

    @Test
    @Transactional
    void putNonExistingVisite() throws Exception {
        int databaseSizeBeforeUpdate = visiteRepository.findAll().size();
        visite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visite in the database
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVisite() throws Exception {
        int databaseSizeBeforeUpdate = visiteRepository.findAll().size();
        visite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visite in the database
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVisite() throws Exception {
        int databaseSizeBeforeUpdate = visiteRepository.findAll().size();
        visite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visite in the database
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVisiteWithPatch() throws Exception {
        // Initialize the database
        visiteRepository.saveAndFlush(visite);

        int databaseSizeBeforeUpdate = visiteRepository.findAll().size();

        // Update the visite using partial update
        Visite partialUpdatedVisite = new Visite();
        partialUpdatedVisite.setId(visite.getId());

        partialUpdatedVisite.interrogatoire(UPDATED_INTERROGATOIRE).conclusion(UPDATED_CONCLUSION).honoraire(UPDATED_HONORAIRE);

        restVisiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisite))
            )
            .andExpect(status().isOk());

        // Validate the Visite in the database
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeUpdate);
        Visite testVisite = visiteList.get(visiteList.size() - 1);
        assertThat(testVisite.getControl()).isEqualTo(DEFAULT_CONTROL);
        assertThat(testVisite.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testVisite.getMotif()).isEqualTo(DEFAULT_MOTIF);
        assertThat(testVisite.getInterrogatoire()).isEqualTo(UPDATED_INTERROGATOIRE);
        assertThat(testVisite.getExamen()).isEqualTo(DEFAULT_EXAMEN);
        assertThat(testVisite.getConclusion()).isEqualTo(UPDATED_CONCLUSION);
        assertThat(testVisite.getHonoraire()).isEqualTo(UPDATED_HONORAIRE);
    }

    @Test
    @Transactional
    void fullUpdateVisiteWithPatch() throws Exception {
        // Initialize the database
        visiteRepository.saveAndFlush(visite);

        int databaseSizeBeforeUpdate = visiteRepository.findAll().size();

        // Update the visite using partial update
        Visite partialUpdatedVisite = new Visite();
        partialUpdatedVisite.setId(visite.getId());

        partialUpdatedVisite
            .control(UPDATED_CONTROL)
            .date(UPDATED_DATE)
            .motif(UPDATED_MOTIF)
            .interrogatoire(UPDATED_INTERROGATOIRE)
            .examen(UPDATED_EXAMEN)
            .conclusion(UPDATED_CONCLUSION)
            .honoraire(UPDATED_HONORAIRE);

        restVisiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisite))
            )
            .andExpect(status().isOk());

        // Validate the Visite in the database
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeUpdate);
        Visite testVisite = visiteList.get(visiteList.size() - 1);
        assertThat(testVisite.getControl()).isEqualTo(UPDATED_CONTROL);
        assertThat(testVisite.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVisite.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testVisite.getInterrogatoire()).isEqualTo(UPDATED_INTERROGATOIRE);
        assertThat(testVisite.getExamen()).isEqualTo(UPDATED_EXAMEN);
        assertThat(testVisite.getConclusion()).isEqualTo(UPDATED_CONCLUSION);
        assertThat(testVisite.getHonoraire()).isEqualTo(UPDATED_HONORAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingVisite() throws Exception {
        int databaseSizeBeforeUpdate = visiteRepository.findAll().size();
        visite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, visite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visite in the database
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVisite() throws Exception {
        int databaseSizeBeforeUpdate = visiteRepository.findAll().size();
        visite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visite in the database
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVisite() throws Exception {
        int databaseSizeBeforeUpdate = visiteRepository.findAll().size();
        visite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisiteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(visite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visite in the database
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVisite() throws Exception {
        // Initialize the database
        visiteRepository.saveAndFlush(visite);

        int databaseSizeBeforeDelete = visiteRepository.findAll().size();

        // Delete the visite
        restVisiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, visite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Visite> visiteList = visiteRepository.findAll();
        assertThat(visiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
