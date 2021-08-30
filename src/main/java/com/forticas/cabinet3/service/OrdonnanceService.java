package com.forticas.cabinet3.service;

import com.forticas.cabinet3.domain.Ordonnance;
import com.forticas.cabinet3.repository.OrdonnanceRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ordonnance}.
 */
@Service
@Transactional
public class OrdonnanceService {

    private final Logger log = LoggerFactory.getLogger(OrdonnanceService.class);

    private final OrdonnanceRepository ordonnanceRepository;

    public OrdonnanceService(OrdonnanceRepository ordonnanceRepository) {
        this.ordonnanceRepository = ordonnanceRepository;
    }

    /**
     * Save a ordonnance.
     *
     * @param ordonnance the entity to save.
     * @return the persisted entity.
     */
    public Ordonnance save(Ordonnance ordonnance) {
        log.debug("Request to save Ordonnance : {}", ordonnance);
        return ordonnanceRepository.save(ordonnance);
    }

    /**
     * Partially update a ordonnance.
     *
     * @param ordonnance the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Ordonnance> partialUpdate(Ordonnance ordonnance) {
        log.debug("Request to partially update Ordonnance : {}", ordonnance);

        return ordonnanceRepository
            .findById(ordonnance.getId())
            .map(
                existingOrdonnance -> {
                    return existingOrdonnance;
                }
            )
            .map(ordonnanceRepository::save);
    }

    /**
     * Get all the ordonnances.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Ordonnance> findAll() {
        log.debug("Request to get all Ordonnances");
        return ordonnanceRepository.findAll();
    }

    /**
     * Get one ordonnance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Ordonnance> findOne(Long id) {
        log.debug("Request to get Ordonnance : {}", id);
        return ordonnanceRepository.findById(id);
    }

    /**
     * Delete the ordonnance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ordonnance : {}", id);
        ordonnanceRepository.deleteById(id);
    }
}
