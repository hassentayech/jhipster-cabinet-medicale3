package com.forticas.cabinet3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.forticas.cabinet3.IntegrationTest;
import com.forticas.cabinet3.domain.Antecedent;
import com.forticas.cabinet3.domain.CasTraiter;
import com.forticas.cabinet3.domain.Constante;
import com.forticas.cabinet3.domain.Patient;
import com.forticas.cabinet3.domain.RendezVous;
import com.forticas.cabinet3.domain.enumeration.EtatCivil;
import com.forticas.cabinet3.domain.enumeration.Sexe;
import com.forticas.cabinet3.repository.PatientRepository;
import com.forticas.cabinet3.service.criteria.PatientCriteria;
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
 * Integration tests for the {@link PatientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_NAISSANCE = LocalDate.ofEpochDay(-1L);

    private static final Sexe DEFAULT_SEXE = Sexe.HOMME;
    private static final Sexe UPDATED_SEXE = Sexe.FEMME;

    private static final EtatCivil DEFAULT_ETAT_CIVIL = EtatCivil.CELIBATAIRE;
    private static final EtatCivil UPDATED_ETAT_CIVIL = EtatCivil.MARIE;

    private static final String DEFAULT_FONCTION = "AAAAAAAAAA";
    private static final String UPDATED_FONCTION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_FIXE = "AAAAAAAAAA";
    private static final String UPDATED_TEL_FIXE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_REMARQUE = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/patients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .reference(DEFAULT_REFERENCE)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .naissance(DEFAULT_NAISSANCE)
            .sexe(DEFAULT_SEXE)
            .etatCivil(DEFAULT_ETAT_CIVIL)
            .fonction(DEFAULT_FONCTION)
            .email(DEFAULT_EMAIL)
            .tel(DEFAULT_TEL)
            .telFixe(DEFAULT_TEL_FIXE)
            .adresse(DEFAULT_ADRESSE)
            .remarque(DEFAULT_REMARQUE);
        return patient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .reference(UPDATED_REFERENCE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .naissance(UPDATED_NAISSANCE)
            .sexe(UPDATED_SEXE)
            .etatCivil(UPDATED_ETAT_CIVIL)
            .fonction(UPDATED_FONCTION)
            .email(UPDATED_EMAIL)
            .tel(UPDATED_TEL)
            .telFixe(UPDATED_TEL_FIXE)
            .adresse(UPDATED_ADRESSE)
            .remarque(UPDATED_REMARQUE);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();
        // Create the Patient
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testPatient.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPatient.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testPatient.getNaissance()).isEqualTo(DEFAULT_NAISSANCE);
        assertThat(testPatient.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testPatient.getEtatCivil()).isEqualTo(DEFAULT_ETAT_CIVIL);
        assertThat(testPatient.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testPatient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPatient.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testPatient.getTelFixe()).isEqualTo(DEFAULT_TEL_FIXE);
        assertThat(testPatient.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testPatient.getRemarque()).isEqualTo(DEFAULT_REMARQUE);
    }

    @Test
    @Transactional
    void createPatientWithExistingId() throws Exception {
        // Create the Patient with an existing ID
        patient.setId(1L);

        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setReference(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setNom(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setPrenom(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNaissanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setNaissance(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setSexe(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatCivilIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setEtatCivil(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setTel(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelFixeIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setTelFixe(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].naissance").value(hasItem(DEFAULT_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].etatCivil").value(hasItem(DEFAULT_ETAT_CIVIL.toString())))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].telFixe").value(hasItem(DEFAULT_TEL_FIXE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc
            .perform(get(ENTITY_API_URL_ID, patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.naissance").value(DEFAULT_NAISSANCE.toString()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.etatCivil").value(DEFAULT_ETAT_CIVIL.toString()))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.telFixe").value(DEFAULT_TEL_FIXE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.remarque").value(DEFAULT_REMARQUE.toString()));
    }

    @Test
    @Transactional
    void getPatientsByIdFiltering() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        Long id = patient.getId();

        defaultPatientShouldBeFound("id.equals=" + id);
        defaultPatientShouldNotBeFound("id.notEquals=" + id);

        defaultPatientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPatientsByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where reference equals to DEFAULT_REFERENCE
        defaultPatientShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the patientList where reference equals to UPDATED_REFERENCE
        defaultPatientShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPatientsByReferenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where reference not equals to DEFAULT_REFERENCE
        defaultPatientShouldNotBeFound("reference.notEquals=" + DEFAULT_REFERENCE);

        // Get all the patientList where reference not equals to UPDATED_REFERENCE
        defaultPatientShouldBeFound("reference.notEquals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPatientsByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultPatientShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the patientList where reference equals to UPDATED_REFERENCE
        defaultPatientShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPatientsByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where reference is not null
        defaultPatientShouldBeFound("reference.specified=true");

        // Get all the patientList where reference is null
        defaultPatientShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByReferenceContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where reference contains DEFAULT_REFERENCE
        defaultPatientShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the patientList where reference contains UPDATED_REFERENCE
        defaultPatientShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPatientsByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where reference does not contain DEFAULT_REFERENCE
        defaultPatientShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the patientList where reference does not contain UPDATED_REFERENCE
        defaultPatientShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPatientsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where nom equals to DEFAULT_NOM
        defaultPatientShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the patientList where nom equals to UPDATED_NOM
        defaultPatientShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPatientsByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where nom not equals to DEFAULT_NOM
        defaultPatientShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the patientList where nom not equals to UPDATED_NOM
        defaultPatientShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPatientsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultPatientShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the patientList where nom equals to UPDATED_NOM
        defaultPatientShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPatientsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where nom is not null
        defaultPatientShouldBeFound("nom.specified=true");

        // Get all the patientList where nom is null
        defaultPatientShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByNomContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where nom contains DEFAULT_NOM
        defaultPatientShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the patientList where nom contains UPDATED_NOM
        defaultPatientShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPatientsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where nom does not contain DEFAULT_NOM
        defaultPatientShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the patientList where nom does not contain UPDATED_NOM
        defaultPatientShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPatientsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where prenom equals to DEFAULT_PRENOM
        defaultPatientShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the patientList where prenom equals to UPDATED_PRENOM
        defaultPatientShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPatientsByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where prenom not equals to DEFAULT_PRENOM
        defaultPatientShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the patientList where prenom not equals to UPDATED_PRENOM
        defaultPatientShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPatientsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultPatientShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the patientList where prenom equals to UPDATED_PRENOM
        defaultPatientShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPatientsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where prenom is not null
        defaultPatientShouldBeFound("prenom.specified=true");

        // Get all the patientList where prenom is null
        defaultPatientShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where prenom contains DEFAULT_PRENOM
        defaultPatientShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the patientList where prenom contains UPDATED_PRENOM
        defaultPatientShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPatientsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where prenom does not contain DEFAULT_PRENOM
        defaultPatientShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the patientList where prenom does not contain UPDATED_PRENOM
        defaultPatientShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPatientsByNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where naissance equals to DEFAULT_NAISSANCE
        defaultPatientShouldBeFound("naissance.equals=" + DEFAULT_NAISSANCE);

        // Get all the patientList where naissance equals to UPDATED_NAISSANCE
        defaultPatientShouldNotBeFound("naissance.equals=" + UPDATED_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPatientsByNaissanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where naissance not equals to DEFAULT_NAISSANCE
        defaultPatientShouldNotBeFound("naissance.notEquals=" + DEFAULT_NAISSANCE);

        // Get all the patientList where naissance not equals to UPDATED_NAISSANCE
        defaultPatientShouldBeFound("naissance.notEquals=" + UPDATED_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPatientsByNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where naissance in DEFAULT_NAISSANCE or UPDATED_NAISSANCE
        defaultPatientShouldBeFound("naissance.in=" + DEFAULT_NAISSANCE + "," + UPDATED_NAISSANCE);

        // Get all the patientList where naissance equals to UPDATED_NAISSANCE
        defaultPatientShouldNotBeFound("naissance.in=" + UPDATED_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPatientsByNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where naissance is not null
        defaultPatientShouldBeFound("naissance.specified=true");

        // Get all the patientList where naissance is null
        defaultPatientShouldNotBeFound("naissance.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where naissance is greater than or equal to DEFAULT_NAISSANCE
        defaultPatientShouldBeFound("naissance.greaterThanOrEqual=" + DEFAULT_NAISSANCE);

        // Get all the patientList where naissance is greater than or equal to UPDATED_NAISSANCE
        defaultPatientShouldNotBeFound("naissance.greaterThanOrEqual=" + UPDATED_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPatientsByNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where naissance is less than or equal to DEFAULT_NAISSANCE
        defaultPatientShouldBeFound("naissance.lessThanOrEqual=" + DEFAULT_NAISSANCE);

        // Get all the patientList where naissance is less than or equal to SMALLER_NAISSANCE
        defaultPatientShouldNotBeFound("naissance.lessThanOrEqual=" + SMALLER_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPatientsByNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where naissance is less than DEFAULT_NAISSANCE
        defaultPatientShouldNotBeFound("naissance.lessThan=" + DEFAULT_NAISSANCE);

        // Get all the patientList where naissance is less than UPDATED_NAISSANCE
        defaultPatientShouldBeFound("naissance.lessThan=" + UPDATED_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPatientsByNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where naissance is greater than DEFAULT_NAISSANCE
        defaultPatientShouldNotBeFound("naissance.greaterThan=" + DEFAULT_NAISSANCE);

        // Get all the patientList where naissance is greater than SMALLER_NAISSANCE
        defaultPatientShouldBeFound("naissance.greaterThan=" + SMALLER_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPatientsBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where sexe equals to DEFAULT_SEXE
        defaultPatientShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the patientList where sexe equals to UPDATED_SEXE
        defaultPatientShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllPatientsBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where sexe not equals to DEFAULT_SEXE
        defaultPatientShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the patientList where sexe not equals to UPDATED_SEXE
        defaultPatientShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllPatientsBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultPatientShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the patientList where sexe equals to UPDATED_SEXE
        defaultPatientShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllPatientsBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where sexe is not null
        defaultPatientShouldBeFound("sexe.specified=true");

        // Get all the patientList where sexe is null
        defaultPatientShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByEtatCivilIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where etatCivil equals to DEFAULT_ETAT_CIVIL
        defaultPatientShouldBeFound("etatCivil.equals=" + DEFAULT_ETAT_CIVIL);

        // Get all the patientList where etatCivil equals to UPDATED_ETAT_CIVIL
        defaultPatientShouldNotBeFound("etatCivil.equals=" + UPDATED_ETAT_CIVIL);
    }

    @Test
    @Transactional
    void getAllPatientsByEtatCivilIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where etatCivil not equals to DEFAULT_ETAT_CIVIL
        defaultPatientShouldNotBeFound("etatCivil.notEquals=" + DEFAULT_ETAT_CIVIL);

        // Get all the patientList where etatCivil not equals to UPDATED_ETAT_CIVIL
        defaultPatientShouldBeFound("etatCivil.notEquals=" + UPDATED_ETAT_CIVIL);
    }

    @Test
    @Transactional
    void getAllPatientsByEtatCivilIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where etatCivil in DEFAULT_ETAT_CIVIL or UPDATED_ETAT_CIVIL
        defaultPatientShouldBeFound("etatCivil.in=" + DEFAULT_ETAT_CIVIL + "," + UPDATED_ETAT_CIVIL);

        // Get all the patientList where etatCivil equals to UPDATED_ETAT_CIVIL
        defaultPatientShouldNotBeFound("etatCivil.in=" + UPDATED_ETAT_CIVIL);
    }

    @Test
    @Transactional
    void getAllPatientsByEtatCivilIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where etatCivil is not null
        defaultPatientShouldBeFound("etatCivil.specified=true");

        // Get all the patientList where etatCivil is null
        defaultPatientShouldNotBeFound("etatCivil.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByFonctionIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fonction equals to DEFAULT_FONCTION
        defaultPatientShouldBeFound("fonction.equals=" + DEFAULT_FONCTION);

        // Get all the patientList where fonction equals to UPDATED_FONCTION
        defaultPatientShouldNotBeFound("fonction.equals=" + UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void getAllPatientsByFonctionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fonction not equals to DEFAULT_FONCTION
        defaultPatientShouldNotBeFound("fonction.notEquals=" + DEFAULT_FONCTION);

        // Get all the patientList where fonction not equals to UPDATED_FONCTION
        defaultPatientShouldBeFound("fonction.notEquals=" + UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void getAllPatientsByFonctionIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fonction in DEFAULT_FONCTION or UPDATED_FONCTION
        defaultPatientShouldBeFound("fonction.in=" + DEFAULT_FONCTION + "," + UPDATED_FONCTION);

        // Get all the patientList where fonction equals to UPDATED_FONCTION
        defaultPatientShouldNotBeFound("fonction.in=" + UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void getAllPatientsByFonctionIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fonction is not null
        defaultPatientShouldBeFound("fonction.specified=true");

        // Get all the patientList where fonction is null
        defaultPatientShouldNotBeFound("fonction.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByFonctionContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fonction contains DEFAULT_FONCTION
        defaultPatientShouldBeFound("fonction.contains=" + DEFAULT_FONCTION);

        // Get all the patientList where fonction contains UPDATED_FONCTION
        defaultPatientShouldNotBeFound("fonction.contains=" + UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void getAllPatientsByFonctionNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fonction does not contain DEFAULT_FONCTION
        defaultPatientShouldNotBeFound("fonction.doesNotContain=" + DEFAULT_FONCTION);

        // Get all the patientList where fonction does not contain UPDATED_FONCTION
        defaultPatientShouldBeFound("fonction.doesNotContain=" + UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void getAllPatientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email equals to DEFAULT_EMAIL
        defaultPatientShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the patientList where email equals to UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPatientsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email not equals to DEFAULT_EMAIL
        defaultPatientShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the patientList where email not equals to UPDATED_EMAIL
        defaultPatientShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPatientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPatientShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the patientList where email equals to UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPatientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email is not null
        defaultPatientShouldBeFound("email.specified=true");

        // Get all the patientList where email is null
        defaultPatientShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByEmailContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email contains DEFAULT_EMAIL
        defaultPatientShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the patientList where email contains UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPatientsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email does not contain DEFAULT_EMAIL
        defaultPatientShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the patientList where email does not contain UPDATED_EMAIL
        defaultPatientShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPatientsByTelIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where tel equals to DEFAULT_TEL
        defaultPatientShouldBeFound("tel.equals=" + DEFAULT_TEL);

        // Get all the patientList where tel equals to UPDATED_TEL
        defaultPatientShouldNotBeFound("tel.equals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllPatientsByTelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where tel not equals to DEFAULT_TEL
        defaultPatientShouldNotBeFound("tel.notEquals=" + DEFAULT_TEL);

        // Get all the patientList where tel not equals to UPDATED_TEL
        defaultPatientShouldBeFound("tel.notEquals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllPatientsByTelIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where tel in DEFAULT_TEL or UPDATED_TEL
        defaultPatientShouldBeFound("tel.in=" + DEFAULT_TEL + "," + UPDATED_TEL);

        // Get all the patientList where tel equals to UPDATED_TEL
        defaultPatientShouldNotBeFound("tel.in=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllPatientsByTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where tel is not null
        defaultPatientShouldBeFound("tel.specified=true");

        // Get all the patientList where tel is null
        defaultPatientShouldNotBeFound("tel.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByTelContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where tel contains DEFAULT_TEL
        defaultPatientShouldBeFound("tel.contains=" + DEFAULT_TEL);

        // Get all the patientList where tel contains UPDATED_TEL
        defaultPatientShouldNotBeFound("tel.contains=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllPatientsByTelNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where tel does not contain DEFAULT_TEL
        defaultPatientShouldNotBeFound("tel.doesNotContain=" + DEFAULT_TEL);

        // Get all the patientList where tel does not contain UPDATED_TEL
        defaultPatientShouldBeFound("tel.doesNotContain=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllPatientsByTelFixeIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where telFixe equals to DEFAULT_TEL_FIXE
        defaultPatientShouldBeFound("telFixe.equals=" + DEFAULT_TEL_FIXE);

        // Get all the patientList where telFixe equals to UPDATED_TEL_FIXE
        defaultPatientShouldNotBeFound("telFixe.equals=" + UPDATED_TEL_FIXE);
    }

    @Test
    @Transactional
    void getAllPatientsByTelFixeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where telFixe not equals to DEFAULT_TEL_FIXE
        defaultPatientShouldNotBeFound("telFixe.notEquals=" + DEFAULT_TEL_FIXE);

        // Get all the patientList where telFixe not equals to UPDATED_TEL_FIXE
        defaultPatientShouldBeFound("telFixe.notEquals=" + UPDATED_TEL_FIXE);
    }

    @Test
    @Transactional
    void getAllPatientsByTelFixeIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where telFixe in DEFAULT_TEL_FIXE or UPDATED_TEL_FIXE
        defaultPatientShouldBeFound("telFixe.in=" + DEFAULT_TEL_FIXE + "," + UPDATED_TEL_FIXE);

        // Get all the patientList where telFixe equals to UPDATED_TEL_FIXE
        defaultPatientShouldNotBeFound("telFixe.in=" + UPDATED_TEL_FIXE);
    }

    @Test
    @Transactional
    void getAllPatientsByTelFixeIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where telFixe is not null
        defaultPatientShouldBeFound("telFixe.specified=true");

        // Get all the patientList where telFixe is null
        defaultPatientShouldNotBeFound("telFixe.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByTelFixeContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where telFixe contains DEFAULT_TEL_FIXE
        defaultPatientShouldBeFound("telFixe.contains=" + DEFAULT_TEL_FIXE);

        // Get all the patientList where telFixe contains UPDATED_TEL_FIXE
        defaultPatientShouldNotBeFound("telFixe.contains=" + UPDATED_TEL_FIXE);
    }

    @Test
    @Transactional
    void getAllPatientsByTelFixeNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where telFixe does not contain DEFAULT_TEL_FIXE
        defaultPatientShouldNotBeFound("telFixe.doesNotContain=" + DEFAULT_TEL_FIXE);

        // Get all the patientList where telFixe does not contain UPDATED_TEL_FIXE
        defaultPatientShouldBeFound("telFixe.doesNotContain=" + UPDATED_TEL_FIXE);
    }

    @Test
    @Transactional
    void getAllPatientsByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adresse equals to DEFAULT_ADRESSE
        defaultPatientShouldBeFound("adresse.equals=" + DEFAULT_ADRESSE);

        // Get all the patientList where adresse equals to UPDATED_ADRESSE
        defaultPatientShouldNotBeFound("adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllPatientsByAdresseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adresse not equals to DEFAULT_ADRESSE
        defaultPatientShouldNotBeFound("adresse.notEquals=" + DEFAULT_ADRESSE);

        // Get all the patientList where adresse not equals to UPDATED_ADRESSE
        defaultPatientShouldBeFound("adresse.notEquals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllPatientsByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adresse in DEFAULT_ADRESSE or UPDATED_ADRESSE
        defaultPatientShouldBeFound("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE);

        // Get all the patientList where adresse equals to UPDATED_ADRESSE
        defaultPatientShouldNotBeFound("adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllPatientsByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adresse is not null
        defaultPatientShouldBeFound("adresse.specified=true");

        // Get all the patientList where adresse is null
        defaultPatientShouldNotBeFound("adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByAdresseContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adresse contains DEFAULT_ADRESSE
        defaultPatientShouldBeFound("adresse.contains=" + DEFAULT_ADRESSE);

        // Get all the patientList where adresse contains UPDATED_ADRESSE
        defaultPatientShouldNotBeFound("adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllPatientsByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adresse does not contain DEFAULT_ADRESSE
        defaultPatientShouldNotBeFound("adresse.doesNotContain=" + DEFAULT_ADRESSE);

        // Get all the patientList where adresse does not contain UPDATED_ADRESSE
        defaultPatientShouldBeFound("adresse.doesNotContain=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllPatientsByRendezVousIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        RendezVous rendezVous = RendezVousResourceIT.createEntity(em);
        em.persist(rendezVous);
        em.flush();
        patient.addRendezVous(rendezVous);
        patientRepository.saveAndFlush(patient);
        Long rendezVousId = rendezVous.getId();

        // Get all the patientList where rendezVous equals to rendezVousId
        defaultPatientShouldBeFound("rendezVousId.equals=" + rendezVousId);

        // Get all the patientList where rendezVous equals to (rendezVousId + 1)
        defaultPatientShouldNotBeFound("rendezVousId.equals=" + (rendezVousId + 1));
    }

    @Test
    @Transactional
    void getAllPatientsByCasTraiterIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        CasTraiter casTraiter = CasTraiterResourceIT.createEntity(em);
        em.persist(casTraiter);
        em.flush();
        patient.addCasTraiter(casTraiter);
        patientRepository.saveAndFlush(patient);
        Long casTraiterId = casTraiter.getId();

        // Get all the patientList where casTraiter equals to casTraiterId
        defaultPatientShouldBeFound("casTraiterId.equals=" + casTraiterId);

        // Get all the patientList where casTraiter equals to (casTraiterId + 1)
        defaultPatientShouldNotBeFound("casTraiterId.equals=" + (casTraiterId + 1));
    }

    @Test
    @Transactional
    void getAllPatientsByConstanteIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Constante constante = ConstanteResourceIT.createEntity(em);
        em.persist(constante);
        em.flush();
        patient.addConstante(constante);
        patientRepository.saveAndFlush(patient);
        Long constanteId = constante.getId();

        // Get all the patientList where constante equals to constanteId
        defaultPatientShouldBeFound("constanteId.equals=" + constanteId);

        // Get all the patientList where constante equals to (constanteId + 1)
        defaultPatientShouldNotBeFound("constanteId.equals=" + (constanteId + 1));
    }

    @Test
    @Transactional
    void getAllPatientsByAntecedentIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Antecedent antecedent = AntecedentResourceIT.createEntity(em);
        em.persist(antecedent);
        em.flush();
        patient.addAntecedent(antecedent);
        patientRepository.saveAndFlush(patient);
        Long antecedentId = antecedent.getId();

        // Get all the patientList where antecedent equals to antecedentId
        defaultPatientShouldBeFound("antecedentId.equals=" + antecedentId);

        // Get all the patientList where antecedent equals to (antecedentId + 1)
        defaultPatientShouldNotBeFound("antecedentId.equals=" + (antecedentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientShouldBeFound(String filter) throws Exception {
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].naissance").value(hasItem(DEFAULT_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].etatCivil").value(hasItem(DEFAULT_ETAT_CIVIL.toString())))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].telFixe").value(hasItem(DEFAULT_TEL_FIXE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));

        // Check, that the count call also returns 1
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientShouldNotBeFound(String filter) throws Exception {
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .reference(UPDATED_REFERENCE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .naissance(UPDATED_NAISSANCE)
            .sexe(UPDATED_SEXE)
            .etatCivil(UPDATED_ETAT_CIVIL)
            .fonction(UPDATED_FONCTION)
            .email(UPDATED_EMAIL)
            .tel(UPDATED_TEL)
            .telFixe(UPDATED_TEL_FIXE)
            .adresse(UPDATED_ADRESSE)
            .remarque(UPDATED_REMARQUE);

        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPatient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testPatient.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPatient.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPatient.getNaissance()).isEqualTo(UPDATED_NAISSANCE);
        assertThat(testPatient.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testPatient.getEtatCivil()).isEqualTo(UPDATED_ETAT_CIVIL);
        assertThat(testPatient.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testPatient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatient.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testPatient.getTelFixe()).isEqualTo(UPDATED_TEL_FIXE);
        assertThat(testPatient.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testPatient.getRemarque()).isEqualTo(UPDATED_REMARQUE);
    }

    @Test
    @Transactional
    void putNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .reference(UPDATED_REFERENCE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .naissance(UPDATED_NAISSANCE)
            .etatCivil(UPDATED_ETAT_CIVIL)
            .adresse(UPDATED_ADRESSE);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testPatient.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPatient.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPatient.getNaissance()).isEqualTo(UPDATED_NAISSANCE);
        assertThat(testPatient.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testPatient.getEtatCivil()).isEqualTo(UPDATED_ETAT_CIVIL);
        assertThat(testPatient.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testPatient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPatient.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testPatient.getTelFixe()).isEqualTo(DEFAULT_TEL_FIXE);
        assertThat(testPatient.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testPatient.getRemarque()).isEqualTo(DEFAULT_REMARQUE);
    }

    @Test
    @Transactional
    void fullUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .reference(UPDATED_REFERENCE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .naissance(UPDATED_NAISSANCE)
            .sexe(UPDATED_SEXE)
            .etatCivil(UPDATED_ETAT_CIVIL)
            .fonction(UPDATED_FONCTION)
            .email(UPDATED_EMAIL)
            .tel(UPDATED_TEL)
            .telFixe(UPDATED_TEL_FIXE)
            .adresse(UPDATED_ADRESSE)
            .remarque(UPDATED_REMARQUE);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testPatient.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPatient.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPatient.getNaissance()).isEqualTo(UPDATED_NAISSANCE);
        assertThat(testPatient.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testPatient.getEtatCivil()).isEqualTo(UPDATED_ETAT_CIVIL);
        assertThat(testPatient.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testPatient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatient.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testPatient.getTelFixe()).isEqualTo(UPDATED_TEL_FIXE);
        assertThat(testPatient.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testPatient.getRemarque()).isEqualTo(UPDATED_REMARQUE);
    }

    @Test
    @Transactional
    void patchNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc
            .perform(delete(ENTITY_API_URL_ID, patient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
