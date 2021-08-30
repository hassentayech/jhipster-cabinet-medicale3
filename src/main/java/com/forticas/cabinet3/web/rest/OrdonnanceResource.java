package com.forticas.cabinet3.web.rest;

import com.forticas.cabinet3.domain.Ordonnance;
import com.forticas.cabinet3.repository.OrdonnanceRepository;
import com.forticas.cabinet3.service.OrdonnanceService;
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
 * REST controller for managing {@link com.forticas.cabinet3.domain.Ordonnance}.
 */
@RestController
@RequestMapping("/api")
public class OrdonnanceResource {

    private final Logger log = LoggerFactory.getLogger(OrdonnanceResource.class);

    private static final String ENTITY_NAME = "ordonnance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdonnanceService ordonnanceService;

    private final OrdonnanceRepository ordonnanceRepository;

    public OrdonnanceResource(OrdonnanceService ordonnanceService, OrdonnanceRepository ordonnanceRepository) {
        this.ordonnanceService = ordonnanceService;
        this.ordonnanceRepository = ordonnanceRepository;
    }

    /**
     * {@code POST  /ordonnances} : Create a new ordonnance.
     *
     * @param ordonnance the ordonnance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordonnance, or with status {@code 400 (Bad Request)} if the ordonnance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordonnances")
    public ResponseEntity<Ordonnance> createOrdonnance(@Valid @RequestBody Ordonnance ordonnance) throws URISyntaxException {
        log.debug("REST request to save Ordonnance : {}", ordonnance);
        if (ordonnance.getId() != null) {
            throw new BadRequestAlertException("A new ordonnance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ordonnance result = ordonnanceService.save(ordonnance);
        return ResponseEntity
            .created(new URI("/api/ordonnances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordonnances/:id} : Updates an existing ordonnance.
     *
     * @param id the id of the ordonnance to save.
     * @param ordonnance the ordonnance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordonnance,
     * or with status {@code 400 (Bad Request)} if the ordonnance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordonnance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordonnances/{id}")
    public ResponseEntity<Ordonnance> updateOrdonnance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Ordonnance ordonnance
    ) throws URISyntaxException {
        log.debug("REST request to update Ordonnance : {}, {}", id, ordonnance);
        if (ordonnance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordonnance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordonnanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ordonnance result = ordonnanceService.save(ordonnance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ordonnance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ordonnances/:id} : Partial updates given fields of an existing ordonnance, field will ignore if it is null
     *
     * @param id the id of the ordonnance to save.
     * @param ordonnance the ordonnance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordonnance,
     * or with status {@code 400 (Bad Request)} if the ordonnance is not valid,
     * or with status {@code 404 (Not Found)} if the ordonnance is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordonnance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ordonnances/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Ordonnance> partialUpdateOrdonnance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ordonnance ordonnance
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ordonnance partially : {}, {}", id, ordonnance);
        if (ordonnance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordonnance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordonnanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ordonnance> result = ordonnanceService.partialUpdate(ordonnance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ordonnance.getId().toString())
        );
    }

    /**
     * {@code GET  /ordonnances} : get all the ordonnances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordonnances in body.
     */
    @GetMapping("/ordonnances")
    public List<Ordonnance> getAllOrdonnances() {
        log.debug("REST request to get all Ordonnances");
        return ordonnanceService.findAll();
    }

    /**
     * {@code GET  /ordonnances/:id} : get the "id" ordonnance.
     *
     * @param id the id of the ordonnance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordonnance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordonnances/{id}")
    public ResponseEntity<Ordonnance> getOrdonnance(@PathVariable Long id) {
        log.debug("REST request to get Ordonnance : {}", id);
        Optional<Ordonnance> ordonnance = ordonnanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordonnance);
    }

    /**
     * {@code DELETE  /ordonnances/:id} : delete the "id" ordonnance.
     *
     * @param id the id of the ordonnance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordonnances/{id}")
    public ResponseEntity<Void> deleteOrdonnance(@PathVariable Long id) {
        log.debug("REST request to delete Ordonnance : {}", id);
        ordonnanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
