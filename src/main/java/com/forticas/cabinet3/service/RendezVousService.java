package com.forticas.cabinet3.service;

import com.forticas.cabinet3.domain.RendezVous;
import com.forticas.cabinet3.repository.RendezVousRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RendezVous}.
 */
@Service
@Transactional
public class RendezVousService {

    private final Logger log = LoggerFactory.getLogger(RendezVousService.class);

    private final RendezVousRepository rendezVousRepository;

    public RendezVousService(RendezVousRepository rendezVousRepository) {
        this.rendezVousRepository = rendezVousRepository;
    }

    /**
     * Save a rendezVous.
     *
     * @param rendezVous the entity to save.
     * @return the persisted entity.
     */
    public RendezVous save(RendezVous rendezVous) {
        log.debug("Request to save RendezVous : {}", rendezVous);
        return rendezVousRepository.save(rendezVous);
    }

    /**
     * Partially update a rendezVous.
     *
     * @param rendezVous the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RendezVous> partialUpdate(RendezVous rendezVous) {
        log.debug("Request to partially update RendezVous : {}", rendezVous);

        return rendezVousRepository
            .findById(rendezVous.getId())
            .map(
                existingRendezVous -> {
                    if (rendezVous.getDate() != null) {
                        existingRendezVous.setDate(rendezVous.getDate());
                    }
                    if (rendezVous.getTrancheHoraire() != null) {
                        existingRendezVous.setTrancheHoraire(rendezVous.getTrancheHoraire());
                    }
                    if (rendezVous.getNbrTranche() != null) {
                        existingRendezVous.setNbrTranche(rendezVous.getNbrTranche());
                    }
                    if (rendezVous.getMotif() != null) {
                        existingRendezVous.setMotif(rendezVous.getMotif());
                    }
                    if (rendezVous.getPresent() != null) {
                        existingRendezVous.setPresent(rendezVous.getPresent());
                    }

                    return existingRendezVous;
                }
            )
            .map(rendezVousRepository::save);
    }

    /**
     * Get all the rendezVous.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RendezVous> findAll() {
        log.debug("Request to get all RendezVous");
        return rendezVousRepository.findAll();
    }

    /**
     * Get one rendezVous by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RendezVous> findOne(Long id) {
        log.debug("Request to get RendezVous : {}", id);
        return rendezVousRepository.findById(id);
    }

    /**
     * Delete the rendezVous by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RendezVous : {}", id);
        rendezVousRepository.deleteById(id);
    }
}
