package com.forticas.cabinet3.web.rest;

import com.forticas.cabinet3.domain.CasTraiter;
import com.forticas.cabinet3.repository.CasTraiterRepository;
import com.forticas.cabinet3.service.CasTraiterService;
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
 * REST controller for managing {@link com.forticas.cabinet3.domain.CasTraiter}.
 */
@RestController
@RequestMapping("/api")
public class CasTraiterResource {

    private final Logger log = LoggerFactory.getLogger(CasTraiterResource.class);

    private static final String ENTITY_NAME = "casTraiter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CasTraiterService casTraiterService;

    private final CasTraiterRepository casTraiterRepository;

    public CasTraiterResource(CasTraiterService casTraiterService, CasTraiterRepository casTraiterRepository) {
        this.casTraiterService = casTraiterService;
        this.casTraiterRepository = casTraiterRepository;
    }

    /**
     * {@code POST  /cas-traiters} : Create a new casTraiter.
     *
     * @param casTraiter the casTraiter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new casTraiter, or with status {@code 400 (Bad Request)} if the casTraiter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cas-traiters")
    public ResponseEntity<CasTraiter> createCasTraiter(@Valid @RequestBody CasTraiter casTraiter) throws URISyntaxException {
        log.debug("REST request to save CasTraiter : {}", casTraiter);
        if (casTraiter.getId() != null) {
            throw new BadRequestAlertException("A new casTraiter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CasTraiter result = casTraiterService.save(casTraiter);
        return ResponseEntity
            .created(new URI("/api/cas-traiters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cas-traiters/:id} : Updates an existing casTraiter.
     *
     * @param id the id of the casTraiter to save.
     * @param casTraiter the casTraiter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casTraiter,
     * or with status {@code 400 (Bad Request)} if the casTraiter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the casTraiter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cas-traiters/{id}")
    public ResponseEntity<CasTraiter> updateCasTraiter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CasTraiter casTraiter
    ) throws URISyntaxException {
        log.debug("REST request to update CasTraiter : {}, {}", id, casTraiter);
        if (casTraiter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casTraiter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casTraiterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CasTraiter result = casTraiterService.save(casTraiter);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, casTraiter.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cas-traiters/:id} : Partial updates given fields of an existing casTraiter, field will ignore if it is null
     *
     * @param id the id of the casTraiter to save.
     * @param casTraiter the casTraiter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casTraiter,
     * or with status {@code 400 (Bad Request)} if the casTraiter is not valid,
     * or with status {@code 404 (Not Found)} if the casTraiter is not found,
     * or with status {@code 500 (Internal Server Error)} if the casTraiter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cas-traiters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CasTraiter> partialUpdateCasTraiter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CasTraiter casTraiter
    ) throws URISyntaxException {
        log.debug("REST request to partial update CasTraiter partially : {}, {}", id, casTraiter);
        if (casTraiter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casTraiter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casTraiterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CasTraiter> result = casTraiterService.partialUpdate(casTraiter);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, casTraiter.getId().toString())
        );
    }

    /**
     * {@code GET  /cas-traiters} : get all the casTraiters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of casTraiters in body.
     */
    @GetMapping("/cas-traiters")
    public List<CasTraiter> getAllCasTraiters() {
        log.debug("REST request to get all CasTraiters");
        return casTraiterService.findAll();
    }

    /**
     * {@code GET  /cas-traiters/:id} : get the "id" casTraiter.
     *
     * @param id the id of the casTraiter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the casTraiter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cas-traiters/{id}")
    public ResponseEntity<CasTraiter> getCasTraiter(@PathVariable Long id) {
        log.debug("REST request to get CasTraiter : {}", id);
        Optional<CasTraiter> casTraiter = casTraiterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(casTraiter);
    }

    /**
     * {@code DELETE  /cas-traiters/:id} : delete the "id" casTraiter.
     *
     * @param id the id of the casTraiter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cas-traiters/{id}")
    public ResponseEntity<Void> deleteCasTraiter(@PathVariable Long id) {
        log.debug("REST request to delete CasTraiter : {}", id);
        casTraiterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
