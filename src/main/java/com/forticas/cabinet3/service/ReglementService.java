package com.forticas.cabinet3.service;

import com.forticas.cabinet3.domain.Reglement;
import com.forticas.cabinet3.repository.ReglementRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reglement}.
 */
@Service
@Transactional
public class ReglementService {

    private final Logger log = LoggerFactory.getLogger(ReglementService.class);

    private final ReglementRepository reglementRepository;

    public ReglementService(ReglementRepository reglementRepository) {
        this.reglementRepository = reglementRepository;
    }

    /**
     * Save a reglement.
     *
     * @param reglement the entity to save.
     * @return the persisted entity.
     */
    public Reglement save(Reglement reglement) {
        log.debug("Request to save Reglement : {}", reglement);
        return reglementRepository.save(reglement);
    }

    /**
     * Partially update a reglement.
     *
     * @param reglement the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Reglement> partialUpdate(Reglement reglement) {
        log.debug("Request to partially update Reglement : {}", reglement);

        return reglementRepository
            .findById(reglement.getId())
            .map(
                existingReglement -> {
                    if (reglement.getDate() != null) {
                        existingReglement.setDate(reglement.getDate());
                    }
                    if (reglement.getValeur() != null) {
                        existingReglement.setValeur(reglement.getValeur());
                    }
                    if (reglement.getTypePayement() != null) {
                        existingReglement.setTypePayement(reglement.getTypePayement());
                    }
                    if (reglement.getRemarque() != null) {
                        existingReglement.setRemarque(reglement.getRemarque());
                    }

                    return existingReglement;
                }
            )
            .map(reglementRepository::save);
    }

    /**
     * Get all the reglements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Reglement> findAll() {
        log.debug("Request to get all Reglements");
        return reglementRepository.findAll();
    }

    /**
     * Get one reglement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Reglement> findOne(Long id) {
        log.debug("Request to get Reglement : {}", id);
        return reglementRepository.findById(id);
    }

    /**
     * Delete the reglement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reglement : {}", id);
        reglementRepository.deleteById(id);
    }
}
