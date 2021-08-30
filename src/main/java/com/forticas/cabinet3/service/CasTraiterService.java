package com.forticas.cabinet3.service;

import com.forticas.cabinet3.domain.CasTraiter;
import com.forticas.cabinet3.repository.CasTraiterRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CasTraiter}.
 */
@Service
@Transactional
public class CasTraiterService {

    private final Logger log = LoggerFactory.getLogger(CasTraiterService.class);

    private final CasTraiterRepository casTraiterRepository;

    public CasTraiterService(CasTraiterRepository casTraiterRepository) {
        this.casTraiterRepository = casTraiterRepository;
    }

    /**
     * Save a casTraiter.
     *
     * @param casTraiter the entity to save.
     * @return the persisted entity.
     */
    public CasTraiter save(CasTraiter casTraiter) {
        log.debug("Request to save CasTraiter : {}", casTraiter);
        return casTraiterRepository.save(casTraiter);
    }

    /**
     * Partially update a casTraiter.
     *
     * @param casTraiter the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CasTraiter> partialUpdate(CasTraiter casTraiter) {
        log.debug("Request to partially update CasTraiter : {}", casTraiter);

        return casTraiterRepository
            .findById(casTraiter.getId())
            .map(
                existingCasTraiter -> {
                    if (casTraiter.getCas() != null) {
                        existingCasTraiter.setCas(casTraiter.getCas());
                    }
                    if (casTraiter.getDepuis() != null) {
                        existingCasTraiter.setDepuis(casTraiter.getDepuis());
                    }
                    if (casTraiter.getHistoire() != null) {
                        existingCasTraiter.setHistoire(casTraiter.getHistoire());
                    }
                    if (casTraiter.getRemarques() != null) {
                        existingCasTraiter.setRemarques(casTraiter.getRemarques());
                    }
                    if (casTraiter.getEtatActuel() != null) {
                        existingCasTraiter.setEtatActuel(casTraiter.getEtatActuel());
                    }
                    if (casTraiter.getModeFacturation() != null) {
                        existingCasTraiter.setModeFacturation(casTraiter.getModeFacturation());
                    }
                    if (casTraiter.getPrixForfaitaire() != null) {
                        existingCasTraiter.setPrixForfaitaire(casTraiter.getPrixForfaitaire());
                    }

                    return existingCasTraiter;
                }
            )
            .map(casTraiterRepository::save);
    }

    /**
     * Get all the casTraiters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CasTraiter> findAll() {
        log.debug("Request to get all CasTraiters");
        return casTraiterRepository.findAll();
    }

    /**
     * Get one casTraiter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CasTraiter> findOne(Long id) {
        log.debug("Request to get CasTraiter : {}", id);
        return casTraiterRepository.findById(id);
    }

    /**
     * Delete the casTraiter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CasTraiter : {}", id);
        casTraiterRepository.deleteById(id);
    }
}
