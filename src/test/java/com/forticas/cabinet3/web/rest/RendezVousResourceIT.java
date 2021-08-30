package com.forticas.cabinet3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.forticas.cabinet3.IntegrationTest;
import com.forticas.cabinet3.domain.Patient;
import com.forticas.cabinet3.domain.RendezVous;
import com.forticas.cabinet3.repository.RendezVousRepository;
import com.forticas.cabinet3.service.criteria.RendezVousCriteria;
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

/**
 * Integration tests for the {@link RendezVousResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RendezVousResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TRANCHE_HORAIRE = "AAAAAAAAAA";
    private static final String UPDATED_TRANCHE_HORAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_NBR_TRANCHE = "AAAAAAAAAA";
    private static final String UPDATED_NBR_TRANCHE = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIF = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PRESENT = false;
    private static final Boolean UPDATED_PRESENT = true;

    private static final String ENTITY_API_URL = "/api/rendez-vous";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRendezVousMockMvc;

    private RendezVous rendezVous;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RendezVous createEntity(EntityManager em) {
        RendezVous rendezVous = new RendezVous()
            .date(DEFAULT_DATE)
            .trancheHoraire(DEFAULT_TRANCHE_HORAIRE)
            .nbrTranche(DEFAULT_NBR_TRANCHE)
            .motif(DEFAULT_MOTIF)
            .present(DEFAULT_PRESENT);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        rendezVous.setPatient(patient);
        return rendezVous;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RendezVous createUpdatedEntity(EntityManager em) {
        RendezVous rendezVous = new RendezVous()
            .date(UPDATED_DATE)
            .trancheHoraire(UPDATED_TRANCHE_HORAIRE)
            .nbrTranche(UPDATED_NBR_TRANCHE)
            .motif(UPDATED_MOTIF)
            .present(UPDATED_PRESENT);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        rendezVous.setPatient(patient);
        return rendezVous;
    }

    @BeforeEach
    public void initTest() {
        rendezVous = createEntity(em);
    }

    @Test
    @Transactional
    void createRendezVous() throws Exception {
        int databaseSizeBeforeCreate = rendezVousRepository.findAll().size();
        // Create the RendezVous
        restRendezVousMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isCreated());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeCreate + 1);
        RendezVous testRendezVous = rendezVousList.get(rendezVousList.size() - 1);
        assertThat(testRendezVous.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRendezVous.getTrancheHoraire()).isEqualTo(DEFAULT_TRANCHE_HORAIRE);
        assertThat(testRendezVous.getNbrTranche()).isEqualTo(DEFAULT_NBR_TRANCHE);
        assertThat(testRendezVous.getMotif()).isEqualTo(DEFAULT_MOTIF);
        assertThat(testRendezVous.getPresent()).isEqualTo(DEFAULT_PRESENT);
    }

    @Test
    @Transactional
    void createRendezVousWithExistingId() throws Exception {
        // Create the RendezVous with an existing ID
        rendezVous.setId(1L);

        int databaseSizeBeforeCreate = rendezVousRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRendezVousMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isBadRequest());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezVousRepository.findAll().size();
        // set the field null
        rendezVous.setDate(null);

        // Create the RendezVous, which fails.

        restRendezVousMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isBadRequest());

        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTrancheHoraireIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezVousRepository.findAll().size();
        // set the field null
        rendezVous.setTrancheHoraire(null);

        // Create the RendezVous, which fails.

        restRendezVousMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isBadRequest());

        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbrTrancheIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezVousRepository.findAll().size();
        // set the field null
        rendezVous.setNbrTranche(null);

        // Create the RendezVous, which fails.

        restRendezVousMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isBadRequest());

        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRendezVous() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList
        restRendezVousMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rendezVous.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].trancheHoraire").value(hasItem(DEFAULT_TRANCHE_HORAIRE)))
            .andExpect(jsonPath("$.[*].nbrTranche").value(hasItem(DEFAULT_NBR_TRANCHE)))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)))
            .andExpect(jsonPath("$.[*].present").value(hasItem(DEFAULT_PRESENT.booleanValue())));
    }

    @Test
    @Transactional
    void getRendezVous() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get the rendezVous
        restRendezVousMockMvc
            .perform(get(ENTITY_API_URL_ID, rendezVous.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rendezVous.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.trancheHoraire").value(DEFAULT_TRANCHE_HORAIRE))
            .andExpect(jsonPath("$.nbrTranche").value(DEFAULT_NBR_TRANCHE))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF))
            .andExpect(jsonPath("$.present").value(DEFAULT_PRESENT.booleanValue()));
    }

    @Test
    @Transactional
    void getRendezVousByIdFiltering() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        Long id = rendezVous.getId();

        defaultRendezVousShouldBeFound("id.equals=" + id);
        defaultRendezVousShouldNotBeFound("id.notEquals=" + id);

        defaultRendezVousShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRendezVousShouldNotBeFound("id.greaterThan=" + id);

        defaultRendezVousShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRendezVousShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRendezVousByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where date equals to DEFAULT_DATE
        defaultRendezVousShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the rendezVousList where date equals to UPDATED_DATE
        defaultRendezVousShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllRendezVousByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where date not equals to DEFAULT_DATE
        defaultRendezVousShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the rendezVousList where date not equals to UPDATED_DATE
        defaultRendezVousShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllRendezVousByDateIsInShouldWork() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where date in DEFAULT_DATE or UPDATED_DATE
        defaultRendezVousShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the rendezVousList where date equals to UPDATED_DATE
        defaultRendezVousShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllRendezVousByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where date is not null
        defaultRendezVousShouldBeFound("date.specified=true");

        // Get all the rendezVousList where date is null
        defaultRendezVousShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllRendezVousByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where date is greater than or equal to DEFAULT_DATE
        defaultRendezVousShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the rendezVousList where date is greater than or equal to UPDATED_DATE
        defaultRendezVousShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllRendezVousByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where date is less than or equal to DEFAULT_DATE
        defaultRendezVousShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the rendezVousList where date is less than or equal to SMALLER_DATE
        defaultRendezVousShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllRendezVousByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where date is less than DEFAULT_DATE
        defaultRendezVousShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the rendezVousList where date is less than UPDATED_DATE
        defaultRendezVousShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllRendezVousByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where date is greater than DEFAULT_DATE
        defaultRendezVousShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the rendezVousList where date is greater than SMALLER_DATE
        defaultRendezVousShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllRendezVousByTrancheHoraireIsEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where trancheHoraire equals to DEFAULT_TRANCHE_HORAIRE
        defaultRendezVousShouldBeFound("trancheHoraire.equals=" + DEFAULT_TRANCHE_HORAIRE);

        // Get all the rendezVousList where trancheHoraire equals to UPDATED_TRANCHE_HORAIRE
        defaultRendezVousShouldNotBeFound("trancheHoraire.equals=" + UPDATED_TRANCHE_HORAIRE);
    }

    @Test
    @Transactional
    void getAllRendezVousByTrancheHoraireIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where trancheHoraire not equals to DEFAULT_TRANCHE_HORAIRE
        defaultRendezVousShouldNotBeFound("trancheHoraire.notEquals=" + DEFAULT_TRANCHE_HORAIRE);

        // Get all the rendezVousList where trancheHoraire not equals to UPDATED_TRANCHE_HORAIRE
        defaultRendezVousShouldBeFound("trancheHoraire.notEquals=" + UPDATED_TRANCHE_HORAIRE);
    }

    @Test
    @Transactional
    void getAllRendezVousByTrancheHoraireIsInShouldWork() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where trancheHoraire in DEFAULT_TRANCHE_HORAIRE or UPDATED_TRANCHE_HORAIRE
        defaultRendezVousShouldBeFound("trancheHoraire.in=" + DEFAULT_TRANCHE_HORAIRE + "," + UPDATED_TRANCHE_HORAIRE);

        // Get all the rendezVousList where trancheHoraire equals to UPDATED_TRANCHE_HORAIRE
        defaultRendezVousShouldNotBeFound("trancheHoraire.in=" + UPDATED_TRANCHE_HORAIRE);
    }

    @Test
    @Transactional
    void getAllRendezVousByTrancheHoraireIsNullOrNotNull() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where trancheHoraire is not null
        defaultRendezVousShouldBeFound("trancheHoraire.specified=true");

        // Get all the rendezVousList where trancheHoraire is null
        defaultRendezVousShouldNotBeFound("trancheHoraire.specified=false");
    }

    @Test
    @Transactional
    void getAllRendezVousByTrancheHoraireContainsSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where trancheHoraire contains DEFAULT_TRANCHE_HORAIRE
        defaultRendezVousShouldBeFound("trancheHoraire.contains=" + DEFAULT_TRANCHE_HORAIRE);

        // Get all the rendezVousList where trancheHoraire contains UPDATED_TRANCHE_HORAIRE
        defaultRendezVousShouldNotBeFound("trancheHoraire.contains=" + UPDATED_TRANCHE_HORAIRE);
    }

    @Test
    @Transactional
    void getAllRendezVousByTrancheHoraireNotContainsSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where trancheHoraire does not contain DEFAULT_TRANCHE_HORAIRE
        defaultRendezVousShouldNotBeFound("trancheHoraire.doesNotContain=" + DEFAULT_TRANCHE_HORAIRE);

        // Get all the rendezVousList where trancheHoraire does not contain UPDATED_TRANCHE_HORAIRE
        defaultRendezVousShouldBeFound("trancheHoraire.doesNotContain=" + UPDATED_TRANCHE_HORAIRE);
    }

    @Test
    @Transactional
    void getAllRendezVousByNbrTrancheIsEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where nbrTranche equals to DEFAULT_NBR_TRANCHE
        defaultRendezVousShouldBeFound("nbrTranche.equals=" + DEFAULT_NBR_TRANCHE);

        // Get all the rendezVousList where nbrTranche equals to UPDATED_NBR_TRANCHE
        defaultRendezVousShouldNotBeFound("nbrTranche.equals=" + UPDATED_NBR_TRANCHE);
    }

    @Test
    @Transactional
    void getAllRendezVousByNbrTrancheIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where nbrTranche not equals to DEFAULT_NBR_TRANCHE
        defaultRendezVousShouldNotBeFound("nbrTranche.notEquals=" + DEFAULT_NBR_TRANCHE);

        // Get all the rendezVousList where nbrTranche not equals to UPDATED_NBR_TRANCHE
        defaultRendezVousShouldBeFound("nbrTranche.notEquals=" + UPDATED_NBR_TRANCHE);
    }

    @Test
    @Transactional
    void getAllRendezVousByNbrTrancheIsInShouldWork() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where nbrTranche in DEFAULT_NBR_TRANCHE or UPDATED_NBR_TRANCHE
        defaultRendezVousShouldBeFound("nbrTranche.in=" + DEFAULT_NBR_TRANCHE + "," + UPDATED_NBR_TRANCHE);

        // Get all the rendezVousList where nbrTranche equals to UPDATED_NBR_TRANCHE
        defaultRendezVousShouldNotBeFound("nbrTranche.in=" + UPDATED_NBR_TRANCHE);
    }

    @Test
    @Transactional
    void getAllRendezVousByNbrTrancheIsNullOrNotNull() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where nbrTranche is not null
        defaultRendezVousShouldBeFound("nbrTranche.specified=true");

        // Get all the rendezVousList where nbrTranche is null
        defaultRendezVousShouldNotBeFound("nbrTranche.specified=false");
    }

    @Test
    @Transactional
    void getAllRendezVousByNbrTrancheContainsSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where nbrTranche contains DEFAULT_NBR_TRANCHE
        defaultRendezVousShouldBeFound("nbrTranche.contains=" + DEFAULT_NBR_TRANCHE);

        // Get all the rendezVousList where nbrTranche contains UPDATED_NBR_TRANCHE
        defaultRendezVousShouldNotBeFound("nbrTranche.contains=" + UPDATED_NBR_TRANCHE);
    }

    @Test
    @Transactional
    void getAllRendezVousByNbrTrancheNotContainsSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where nbrTranche does not contain DEFAULT_NBR_TRANCHE
        defaultRendezVousShouldNotBeFound("nbrTranche.doesNotContain=" + DEFAULT_NBR_TRANCHE);

        // Get all the rendezVousList where nbrTranche does not contain UPDATED_NBR_TRANCHE
        defaultRendezVousShouldBeFound("nbrTranche.doesNotContain=" + UPDATED_NBR_TRANCHE);
    }

    @Test
    @Transactional
    void getAllRendezVousByMotifIsEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where motif equals to DEFAULT_MOTIF
        defaultRendezVousShouldBeFound("motif.equals=" + DEFAULT_MOTIF);

        // Get all the rendezVousList where motif equals to UPDATED_MOTIF
        defaultRendezVousShouldNotBeFound("motif.equals=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    void getAllRendezVousByMotifIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where motif not equals to DEFAULT_MOTIF
        defaultRendezVousShouldNotBeFound("motif.notEquals=" + DEFAULT_MOTIF);

        // Get all the rendezVousList where motif not equals to UPDATED_MOTIF
        defaultRendezVousShouldBeFound("motif.notEquals=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    void getAllRendezVousByMotifIsInShouldWork() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where motif in DEFAULT_MOTIF or UPDATED_MOTIF
        defaultRendezVousShouldBeFound("motif.in=" + DEFAULT_MOTIF + "," + UPDATED_MOTIF);

        // Get all the rendezVousList where motif equals to UPDATED_MOTIF
        defaultRendezVousShouldNotBeFound("motif.in=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    void getAllRendezVousByMotifIsNullOrNotNull() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where motif is not null
        defaultRendezVousShouldBeFound("motif.specified=true");

        // Get all the rendezVousList where motif is null
        defaultRendezVousShouldNotBeFound("motif.specified=false");
    }

    @Test
    @Transactional
    void getAllRendezVousByMotifContainsSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where motif contains DEFAULT_MOTIF
        defaultRendezVousShouldBeFound("motif.contains=" + DEFAULT_MOTIF);

        // Get all the rendezVousList where motif contains UPDATED_MOTIF
        defaultRendezVousShouldNotBeFound("motif.contains=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    void getAllRendezVousByMotifNotContainsSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where motif does not contain DEFAULT_MOTIF
        defaultRendezVousShouldNotBeFound("motif.doesNotContain=" + DEFAULT_MOTIF);

        // Get all the rendezVousList where motif does not contain UPDATED_MOTIF
        defaultRendezVousShouldBeFound("motif.doesNotContain=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    void getAllRendezVousByPresentIsEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where present equals to DEFAULT_PRESENT
        defaultRendezVousShouldBeFound("present.equals=" + DEFAULT_PRESENT);

        // Get all the rendezVousList where present equals to UPDATED_PRESENT
        defaultRendezVousShouldNotBeFound("present.equals=" + UPDATED_PRESENT);
    }

    @Test
    @Transactional
    void getAllRendezVousByPresentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where present not equals to DEFAULT_PRESENT
        defaultRendezVousShouldNotBeFound("present.notEquals=" + DEFAULT_PRESENT);

        // Get all the rendezVousList where present not equals to UPDATED_PRESENT
        defaultRendezVousShouldBeFound("present.notEquals=" + UPDATED_PRESENT);
    }

    @Test
    @Transactional
    void getAllRendezVousByPresentIsInShouldWork() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where present in DEFAULT_PRESENT or UPDATED_PRESENT
        defaultRendezVousShouldBeFound("present.in=" + DEFAULT_PRESENT + "," + UPDATED_PRESENT);

        // Get all the rendezVousList where present equals to UPDATED_PRESENT
        defaultRendezVousShouldNotBeFound("present.in=" + UPDATED_PRESENT);
    }

    @Test
    @Transactional
    void getAllRendezVousByPresentIsNullOrNotNull() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList where present is not null
        defaultRendezVousShouldBeFound("present.specified=true");

        // Get all the rendezVousList where present is null
        defaultRendezVousShouldNotBeFound("present.specified=false");
    }

    @Test
    @Transactional
    void getAllRendezVousByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);
        Patient patient = PatientResourceIT.createEntity(em);
        em.persist(patient);
        em.flush();
        rendezVous.setPatient(patient);
        rendezVousRepository.saveAndFlush(rendezVous);
        Long patientId = patient.getId();

        // Get all the rendezVousList where patient equals to patientId
        defaultRendezVousShouldBeFound("patientId.equals=" + patientId);

        // Get all the rendezVousList where patient equals to (patientId + 1)
        defaultRendezVousShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRendezVousShouldBeFound(String filter) throws Exception {
        restRendezVousMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rendezVous.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].trancheHoraire").value(hasItem(DEFAULT_TRANCHE_HORAIRE)))
            .andExpect(jsonPath("$.[*].nbrTranche").value(hasItem(DEFAULT_NBR_TRANCHE)))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)))
            .andExpect(jsonPath("$.[*].present").value(hasItem(DEFAULT_PRESENT.booleanValue())));

        // Check, that the count call also returns 1
        restRendezVousMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRendezVousShouldNotBeFound(String filter) throws Exception {
        restRendezVousMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRendezVousMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRendezVous() throws Exception {
        // Get the rendezVous
        restRendezVousMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRendezVous() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();

        // Update the rendezVous
        RendezVous updatedRendezVous = rendezVousRepository.findById(rendezVous.getId()).get();
        // Disconnect from session so that the updates on updatedRendezVous are not directly saved in db
        em.detach(updatedRendezVous);
        updatedRendezVous
            .date(UPDATED_DATE)
            .trancheHoraire(UPDATED_TRANCHE_HORAIRE)
            .nbrTranche(UPDATED_NBR_TRANCHE)
            .motif(UPDATED_MOTIF)
            .present(UPDATED_PRESENT);

        restRendezVousMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRendezVous.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRendezVous))
            )
            .andExpect(status().isOk());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeUpdate);
        RendezVous testRendezVous = rendezVousList.get(rendezVousList.size() - 1);
        assertThat(testRendezVous.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRendezVous.getTrancheHoraire()).isEqualTo(UPDATED_TRANCHE_HORAIRE);
        assertThat(testRendezVous.getNbrTranche()).isEqualTo(UPDATED_NBR_TRANCHE);
        assertThat(testRendezVous.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testRendezVous.getPresent()).isEqualTo(UPDATED_PRESENT);
    }

    @Test
    @Transactional
    void putNonExistingRendezVous() throws Exception {
        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();
        rendezVous.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRendezVousMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rendezVous.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rendezVous))
            )
            .andExpect(status().isBadRequest());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRendezVous() throws Exception {
        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();
        rendezVous.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRendezVousMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rendezVous))
            )
            .andExpect(status().isBadRequest());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRendezVous() throws Exception {
        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();
        rendezVous.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRendezVousMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRendezVousWithPatch() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();

        // Update the rendezVous using partial update
        RendezVous partialUpdatedRendezVous = new RendezVous();
        partialUpdatedRendezVous.setId(rendezVous.getId());

        partialUpdatedRendezVous
            .date(UPDATED_DATE)
            .trancheHoraire(UPDATED_TRANCHE_HORAIRE)
            .nbrTranche(UPDATED_NBR_TRANCHE)
            .motif(UPDATED_MOTIF)
            .present(UPDATED_PRESENT);

        restRendezVousMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRendezVous.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRendezVous))
            )
            .andExpect(status().isOk());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeUpdate);
        RendezVous testRendezVous = rendezVousList.get(rendezVousList.size() - 1);
        assertThat(testRendezVous.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRendezVous.getTrancheHoraire()).isEqualTo(UPDATED_TRANCHE_HORAIRE);
        assertThat(testRendezVous.getNbrTranche()).isEqualTo(UPDATED_NBR_TRANCHE);
        assertThat(testRendezVous.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testRendezVous.getPresent()).isEqualTo(UPDATED_PRESENT);
    }

    @Test
    @Transactional
    void fullUpdateRendezVousWithPatch() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();

        // Update the rendezVous using partial update
        RendezVous partialUpdatedRendezVous = new RendezVous();
        partialUpdatedRendezVous.setId(rendezVous.getId());

        partialUpdatedRendezVous
            .date(UPDATED_DATE)
            .trancheHoraire(UPDATED_TRANCHE_HORAIRE)
            .nbrTranche(UPDATED_NBR_TRANCHE)
            .motif(UPDATED_MOTIF)
            .present(UPDATED_PRESENT);

        restRendezVousMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRendezVous.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRendezVous))
            )
            .andExpect(status().isOk());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeUpdate);
        RendezVous testRendezVous = rendezVousList.get(rendezVousList.size() - 1);
        assertThat(testRendezVous.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRendezVous.getTrancheHoraire()).isEqualTo(UPDATED_TRANCHE_HORAIRE);
        assertThat(testRendezVous.getNbrTranche()).isEqualTo(UPDATED_NBR_TRANCHE);
        assertThat(testRendezVous.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testRendezVous.getPresent()).isEqualTo(UPDATED_PRESENT);
    }

    @Test
    @Transactional
    void patchNonExistingRendezVous() throws Exception {
        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();
        rendezVous.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRendezVousMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rendezVous.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rendezVous))
            )
            .andExpect(status().isBadRequest());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRendezVous() throws Exception {
        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();
        rendezVous.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRendezVousMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rendezVous))
            )
            .andExpect(status().isBadRequest());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRendezVous() throws Exception {
        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();
        rendezVous.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRendezVousMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rendezVous))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRendezVous() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        int databaseSizeBeforeDelete = rendezVousRepository.findAll().size();

        // Delete the rendezVous
        restRendezVousMockMvc
            .perform(delete(ENTITY_API_URL_ID, rendezVous.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
