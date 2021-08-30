package com.forticas.cabinet3.web.rest;

import com.forticas.cabinet3.domain.CrEchographie;
import com.forticas.cabinet3.repository.CrEchographieRepository;
import com.forticas.cabinet3.service.CrEchographieService;
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
 * REST controller for managing {@link com.forticas.cabinet3.domain.CrEchographie}.
 */
@RestController
@RequestMapping("/api")
public class CrEchographieResource {

    private final Logger log = LoggerFactory.getLogger(CrEchographieResource.class);

    private static final String ENTITY_NAME = "crEchographie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrEchographieService crEchographieService;

    private final CrEchographieRepository crEchographieRepository;

    public CrEchographieResource(CrEchographieService crEchographieService, CrEchographieRepository crEchographieRepository) {
        this.crEchographieService = crEchographieService;
        this.crEchographieRepository = crEchographieRepository;
    }

    /**
     * {@code POST  /cr-echographies} : Create a new crEchographie.
     *
     * @param crEchographie the crEchographie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crEchographie, or with status {@code 400 (Bad Request)} if the crEchographie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cr-echographies")
    public ResponseEntity<CrEchographie> createCrEchographie(@Valid @RequestBody CrEchographie crEchographie) throws URISyntaxException {
        log.debug("REST request to save CrEchographie : {}", crEchographie);
        if (crEchographie.getId() != null) {
            throw new BadRequestAlertException("A new crEchographie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrEchographie result = crEchographieService.save(crEchographie);
        return ResponseEntity
            .created(new URI("/api/cr-echographies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cr-echographies/:id} : Updates an existing crEchographie.
     *
     * @param id the id of the crEchographie to save.
     * @param crEchographie the crEchographie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crEchographie,
     * or with status {@code 400 (Bad Request)} if the crEchographie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crEchographie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cr-echographies/{id}")
    public ResponseEntity<CrEchographie> updateCrEchographie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrEchographie crEchographie
    ) throws URISyntaxException {
        log.debug("REST request to update CrEchographie : {}, {}", id, crEchographie);
        if (crEchographie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crEchographie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crEchographieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrEchographie result = crEchographieService.save(crEchographie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, crEchographie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cr-echographies/:id} : Partial updates given fields of an existing crEchographie, field will ignore if it is null
     *
     * @param id the id of the crEchographie to save.
     * @param crEchographie the crEchographie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crEchographie,
     * or with status {@code 400 (Bad Request)} if the crEchographie is not valid,
     * or with status {@code 404 (Not Found)} if the crEchographie is not found,
     * or with status {@code 500 (Internal Server Error)} if the crEchographie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cr-echographies/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CrEchographie> partialUpdateCrEchographie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrEchographie crEchographie
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrEchographie partially : {}, {}", id, crEchographie);
        if (crEchographie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crEchographie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crEchographieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrEchographie> result = crEchographieService.partialUpdate(crEchographie);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, crEchographie.getId().toString())
        );
    }

    /**
     * {@code GET  /cr-echographies} : get all the crEchographies.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crEchographies in body.
     */
    @GetMapping("/cr-echographies")
    public List<CrEchographie> getAllCrEchographies(@RequestParam(required = false) String filter) {
        if ("visite-is-null".equals(filter)) {
            log.debug("REST request to get all CrEchographies where visite is null");
            return crEchographieService.findAllWhereVisiteIsNull();
        }
        log.debug("REST request to get all CrEchographies");
        return crEchographieService.findAll();
    }

    /**
     * {@code GET  /cr-echographies/:id} : get the "id" crEchographie.
     *
     * @param id the id of the crEchographie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crEchographie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cr-echographies/{id}")
    public ResponseEntity<CrEchographie> getCrEchographie(@PathVariable Long id) {
        log.debug("REST request to get CrEchographie : {}", id);
        Optional<CrEchographie> crEchographie = crEchographieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crEchographie);
    }

    /**
     * {@code DELETE  /cr-echographies/:id} : delete the "id" crEchographie.
     *
     * @param id the id of the crEchographie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cr-echographies/{id}")
    public ResponseEntity<Void> deleteCrEchographie(@PathVariable Long id) {
        log.debug("REST request to delete CrEchographie : {}", id);
        crEchographieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
