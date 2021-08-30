package com.forticas.cabinet3.service;

import com.forticas.cabinet3.domain.Visite;
import com.forticas.cabinet3.repository.VisiteRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Visite}.
 */
@Service
@Transactional
public class VisiteService {

    private final Logger log = LoggerFactory.getLogger(VisiteService.class);

    private final VisiteRepository visiteRepository;

    public VisiteService(VisiteRepository visiteRepository) {
        this.visiteRepository = visiteRepository;
    }

    /**
     * Save a visite.
     *
     * @param visite the entity to save.
     * @return the persisted entity.
     */
    public Visite save(Visite visite) {
        log.debug("Request to save Visite : {}", visite);
        return visiteRepository.save(visite);
    }

    /**
     * Partially update a visite.
     *
     * @param visite the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Visite> partialUpdate(Visite visite) {
        log.debug("Request to partially update Visite : {}", visite);

        return visiteRepository
            .findById(visite.getId())
            .map(
                existingVisite -> {
                    if (visite.getControl() != null) {
                        existingVisite.setControl(visite.getControl());
                    }
                    if (visite.getDate() != null) {
                        existingVisite.setDate(visite.getDate());
                    }
                    if (visite.getMotif() != null) {
                        existingVisite.setMotif(visite.getMotif());
                    }
                    if (visite.getInterrogatoire() != null) {
                        existingVisite.setInterrogatoire(visite.getInterrogatoire());
                    }
                    if (visite.getExamen() != null) {
                        existingVisite.setExamen(visite.getExamen());
                    }
                    if (visite.getConclusion() != null) {
                        existingVisite.setConclusion(visite.getConclusion());
                    }
                    if (visite.getHonoraire() != null) {
                        existingVisite.setHonoraire(visite.getHonoraire());
                    }

                    return existingVisite;
                }
            )
            .map(visiteRepository::save);
    }

    /**
     * Get all the visites.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Visite> findAll() {
        log.debug("Request to get all Visites");
        return visiteRepository.findAll();
    }

    /**
     * Get one visite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Visite> findOne(Long id) {
        log.debug("Request to get Visite : {}", id);
        return visiteRepository.findById(id);
    }

    /**
     * Delete the visite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Visite : {}", id);
        visiteRepository.deleteById(id);
    }
}
