package com.forticas.cabinet3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.forticas.cabinet3.IntegrationTest;
import com.forticas.cabinet3.domain.Certificat;
import com.forticas.cabinet3.domain.Visite;
import com.forticas.cabinet3.repository.CertificatRepository;
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
 * Integration tests for the {@link CertificatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CertificatResourceIT {

    private static final Integer DEFAULT_NBR_JOURS = 1;
    private static final Integer UPDATED_NBR_JOURS = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/certificats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CertificatRepository certificatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCertificatMockMvc;

    private Certificat certificat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Certificat createEntity(EntityManager em) {
        Certificat certificat = new Certificat().nbrJours(DEFAULT_NBR_JOURS).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Visite visite;
        if (TestUtil.findAll(em, Visite.class).isEmpty()) {
            visite = VisiteResourceIT.createEntity(em);
            em.persist(visite);
            em.flush();
        } else {
            visite = TestUtil.findAll(em, Visite.class).get(0);
        }
        certificat.setVisite(visite);
        return certificat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Certificat createUpdatedEntity(EntityManager em) {
        Certificat certificat = new Certificat().nbrJours(UPDATED_NBR_JOURS).description(UPDATED_DESCRIPTION);
        // Add required entity
        Visite visite;
        if (TestUtil.findAll(em, Visite.class).isEmpty()) {
            visite = VisiteResourceIT.createUpdatedEntity(em);
            em.persist(visite);
            em.flush();
        } else {
            visite = TestUtil.findAll(em, Visite.class).get(0);
        }
        certificat.setVisite(visite);
        return certificat;
    }

    @BeforeEach
    public void initTest() {
        certificat = createEntity(em);
    }

    @Test
    @Transactional
    void createCertificat() throws Exception {
        int databaseSizeBeforeCreate = certificatRepository.findAll().size();
        // Create the Certificat
        restCertificatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificat)))
            .andExpect(status().isCreated());

        // Validate the Certificat in the database
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeCreate + 1);
        Certificat testCertificat = certificatList.get(certificatList.size() - 1);
        assertThat(testCertificat.getNbrJours()).isEqualTo(DEFAULT_NBR_JOURS);
        assertThat(testCertificat.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCertificatWithExistingId() throws Exception {
        // Create the Certificat with an existing ID
        certificat.setId(1L);

        int databaseSizeBeforeCreate = certificatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCertificatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificat)))
            .andExpect(status().isBadRequest());

        // Validate the Certificat in the database
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNbrJoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = certificatRepository.findAll().size();
        // set the field null
        certificat.setNbrJours(null);

        // Create the Certificat, which fails.

        restCertificatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificat)))
            .andExpect(status().isBadRequest());

        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCertificats() throws Exception {
        // Initialize the database
        certificatRepository.saveAndFlush(certificat);

        // Get all the certificatList
        restCertificatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(certificat.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbrJours").value(hasItem(DEFAULT_NBR_JOURS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCertificat() throws Exception {
        // Initialize the database
        certificatRepository.saveAndFlush(certificat);

        // Get the certificat
        restCertificatMockMvc
            .perform(get(ENTITY_API_URL_ID, certificat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(certificat.getId().intValue()))
            .andExpect(jsonPath("$.nbrJours").value(DEFAULT_NBR_JOURS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCertificat() throws Exception {
        // Get the certificat
        restCertificatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCertificat() throws Exception {
        // Initialize the database
        certificatRepository.saveAndFlush(certificat);

        int databaseSizeBeforeUpdate = certificatRepository.findAll().size();

        // Update the certificat
        Certificat updatedCertificat = certificatRepository.findById(certificat.getId()).get();
        // Disconnect from session so that the updates on updatedCertificat are not directly saved in db
        em.detach(updatedCertificat);
        updatedCertificat.nbrJours(UPDATED_NBR_JOURS).description(UPDATED_DESCRIPTION);

        restCertificatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCertificat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCertificat))
            )
            .andExpect(status().isOk());

        // Validate the Certificat in the database
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeUpdate);
        Certificat testCertificat = certificatList.get(certificatList.size() - 1);
        assertThat(testCertificat.getNbrJours()).isEqualTo(UPDATED_NBR_JOURS);
        assertThat(testCertificat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingCertificat() throws Exception {
        int databaseSizeBeforeUpdate = certificatRepository.findAll().size();
        certificat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCertificatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, certificat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(certificat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificat in the database
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCertificat() throws Exception {
        int databaseSizeBeforeUpdate = certificatRepository.findAll().size();
        certificat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(certificat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificat in the database
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCertificat() throws Exception {
        int databaseSizeBeforeUpdate = certificatRepository.findAll().size();
        certificat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Certificat in the database
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCertificatWithPatch() throws Exception {
        // Initialize the database
        certificatRepository.saveAndFlush(certificat);

        int databaseSizeBeforeUpdate = certificatRepository.findAll().size();

        // Update the certificat using partial update
        Certificat partialUpdatedCertificat = new Certificat();
        partialUpdatedCertificat.setId(certificat.getId());

        partialUpdatedCertificat.nbrJours(UPDATED_NBR_JOURS).description(UPDATED_DESCRIPTION);

        restCertificatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCertificat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCertificat))
            )
            .andExpect(status().isOk());

        // Validate the Certificat in the database
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeUpdate);
        Certificat testCertificat = certificatList.get(certificatList.size() - 1);
        assertThat(testCertificat.getNbrJours()).isEqualTo(UPDATED_NBR_JOURS);
        assertThat(testCertificat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCertificatWithPatch() throws Exception {
        // Initialize the database
        certificatRepository.saveAndFlush(certificat);

        int databaseSizeBeforeUpdate = certificatRepository.findAll().size();

        // Update the certificat using partial update
        Certificat partialUpdatedCertificat = new Certificat();
        partialUpdatedCertificat.setId(certificat.getId());

        partialUpdatedCertificat.nbrJours(UPDATED_NBR_JOURS).description(UPDATED_DESCRIPTION);

        restCertificatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCertificat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCertificat))
            )
            .andExpect(status().isOk());

        // Validate the Certificat in the database
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeUpdate);
        Certificat testCertificat = certificatList.get(certificatList.size() - 1);
        assertThat(testCertificat.getNbrJours()).isEqualTo(UPDATED_NBR_JOURS);
        assertThat(testCertificat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCertificat() throws Exception {
        int databaseSizeBeforeUpdate = certificatRepository.findAll().size();
        certificat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCertificatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, certificat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(certificat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificat in the database
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCertificat() throws Exception {
        int databaseSizeBeforeUpdate = certificatRepository.findAll().size();
        certificat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(certificat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificat in the database
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCertificat() throws Exception {
        int databaseSizeBeforeUpdate = certificatRepository.findAll().size();
        certificat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificatMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(certificat))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Certificat in the database
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCertificat() throws Exception {
        // Initialize the database
        certificatRepository.saveAndFlush(certificat);

        int databaseSizeBeforeDelete = certificatRepository.findAll().size();

        // Delete the certificat
        restCertificatMockMvc
            .perform(delete(ENTITY_API_URL_ID, certificat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Certificat> certificatList = certificatRepository.findAll();
        assertThat(certificatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
