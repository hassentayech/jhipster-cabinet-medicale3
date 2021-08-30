package com.forticas.cabinet3.web.rest;

import com.forticas.cabinet3.domain.Certificat;
import com.forticas.cabinet3.repository.CertificatRepository;
import com.forticas.cabinet3.service.CertificatService;
import com.forticas.cabinet3.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.forticas.cabinet3.domain.Certificat}.
 */
@RestController
@RequestMapping("/api")
public class CertificatResource {

    private final Logger log = LoggerFactory.getLogger(CertificatResource.class);

    private static final String ENTITY_NAME = "certificat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CertificatService certificatService;

    private final CertificatRepository certificatRepository;

    public CertificatResource(CertificatService certificatService, CertificatRepository certificatRepository) {
        this.certificatService = certificatService;
        this.certificatRepository = certificatRepository;
    }

    /**
     * {@code POST  /certificats} : Create a new certificat.
     *
     * @param certificat the certificat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new certificat, or with status {@code 400 (Bad Request)} if the certificat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/certificats")
    public ResponseEntity<Certificat> createCertificat(@Valid @RequestBody Certificat certificat) throws URISyntaxException {
        log.debug("REST request to save Certificat : {}", certificat);
        if (certificat.getId() != null) {
            throw new BadRequestAlertException("A new certificat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Certificat result = certificatService.save(certificat);
        return ResponseEntity
            .created(new URI("/api/certificats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /certificats/:id} : Updates an existing certificat.
     *
     * @param id the id of the certificat to save.
     * @param certificat the certificat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated certificat,
     * or with status {@code 400 (Bad Request)} if the certificat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the certificat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/certificats/{id}")
    public ResponseEntity<Certificat> updateCertificat(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Certificat certificat
    ) throws URISyntaxException {
        log.debug("REST request to update Certificat : {}, {}", id, certificat);
        if (certificat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, certificat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!certificatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Certificat result = certificatService.save(certificat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, certificat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /certificats/:id} : Partial updates given fields of an existing certificat, field will ignore if it is null
     *
     * @param id the id of the certificat to save.
     * @param certificat the certificat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated certificat,
     * or with status {@code 400 (Bad Request)} if the certificat is not valid,
     * or with status {@code 404 (Not Found)} if the certificat is not found,
     * or with status {@code 500 (Internal Server Error)} if the certificat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/certificats/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Certificat> partialUpdateCertificat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Certificat certificat
    ) throws URISyntaxException {
        log.debug("REST request to partial update Certificat partially : {}, {}", id, certificat);
        if (certificat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, certificat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!certificatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Certificat> result = certificatService.partialUpdate(certificat);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, certificat.getId().toString())
        );
    }

    /**
     * {@code GET  /certificats} : get all the certificats.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of certificats in body.
     */
    @GetMapping("/certificats")
    public List<Certificat> getAllCertificats(@RequestParam(required = false) String filter) {
        if ("visite-is-null".equals(filter)) {
            log.debug("REST request to get all Certificats where visite is null");
            return certificatService.findAllWhereVisiteIsNull();
        }
        log.debug("REST request to get all Certificats");
        return certificatService.findAll();
    }

    /**
     * {@code GET  /certificats/:id} : get the "id" certificat.
     *
     * @param id the id of the certificat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the certificat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/certificats/{id}")
    public ResponseEntity<Certificat> getCertificat(@PathVariable Long id) {
        log.debug("REST request to get Certificat : {}", id);
        Optional<Certificat> certificat = certificatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(certificat);
    }

    /**
     * {@code DELETE  /certificats/:id} : delete the "id" certificat.
     *
     * @param id the id of the certificat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/certificats/{id}")
    public ResponseEntity<Void> deleteCertificat(@PathVariable Long id) {
        log.debug("REST request to delete Certificat : {}", id);
        certificatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
