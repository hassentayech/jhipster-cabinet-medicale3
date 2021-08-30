package com.forticas.cabinet3.web.rest;

import com.forticas.cabinet3.domain.Prescription;
import com.forticas.cabinet3.repository.PrescriptionRepository;
import com.forticas.cabinet3.service.PrescriptionService;
import com.forticas.cabinet3.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.forticas.cabinet3.domain.Prescription}.
 */
@RestController
@RequestMapping("/api")
public class PrescriptionResource {

    private final Logger log = LoggerFactory.getLogger(PrescriptionResource.class);

    private static final String ENTITY_NAME = "prescription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrescriptionService prescriptionService;

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionResource(PrescriptionService prescriptionService, PrescriptionRepository prescriptionRepository) {
        this.prescriptionService = prescriptionService;
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * {@code POST  /prescriptions} : Create a new prescription.
     *
     * @param prescription the prescription to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prescription, or with status {@code 400 (Bad Request)} if the prescription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prescriptions")
    public ResponseEntity<Prescription> createPrescription(@Valid @RequestBody Prescription prescription) throws URISyntaxException {
        log.debug("REST request to save Prescription : {}", prescription);
        if (prescription.getId() != null) {
            throw new BadRequestAlertException("A new prescription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prescription result = prescriptionService.save(prescription);
        return ResponseEntity
            .created(new URI("/api/prescriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prescriptions/:id} : Updates an existing prescription.
     *
     * @param id the id of the prescription to save.
     * @param prescription the prescription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prescription,
     * or with status {@code 400 (Bad Request)} if the prescription is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prescription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prescriptions/{id}")
    public ResponseEntity<Prescription> updatePrescription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Prescription prescription
    ) throws URISyntaxException {
        log.debug("REST request to update Prescription : {}, {}", id, prescription);
        if (prescription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prescription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prescriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Prescription result = prescriptionService.save(prescription);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prescription.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prescriptions/:id} : Partial updates given fields of an existing prescription, field will ignore if it is null
     *
     * @param id the id of the prescription to save.
     * @param prescription the prescription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prescription,
     * or with status {@code 400 (Bad Request)} if the prescription is not valid,
     * or with status {@code 404 (Not Found)} if the prescription is not found,
     * or with status {@code 500 (Internal Server Error)} if the prescription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prescriptions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Prescription> partialUpdatePrescription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Prescription prescription
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prescription partially : {}, {}", id, prescription);
        if (prescription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prescription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prescriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Prescription> result = prescriptionService.partialUpdate(prescription);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prescription.getId().toString())
        );
    }

    /**
     * {@code GET  /prescriptions} : get all the prescriptions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prescriptions in body.
     */
    @GetMapping("/prescriptions")
    public List<Prescription> getAllPrescriptions() {
        log.debug("REST request to get all Prescriptions");
        return prescriptionService.findAll();
    }

    /**
     * {@code GET  /prescriptions/:id} : get the "id" prescription.
     *
     * @param id the id of the prescription to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prescription, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<Prescription> getPrescription(@PathVariable Long id) {
        log.debug("REST request to get Prescription : {}", id);
        Optional<Prescription> prescription = prescriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prescription);
    }

    /**
     * {@code DELETE  /prescriptions/:id} : delete the "id" prescription.
     *
     * @param id the id of the prescription to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prescriptions/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        log.debug("REST request to delete Prescription : {}", id);
        prescriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
