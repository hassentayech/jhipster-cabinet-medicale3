package com.forticas.cabinet3.service;

import com.forticas.cabinet3.domain.CrEchographie;
import com.forticas.cabinet3.repository.CrEchographieRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrEchographie}.
 */
@Service
@Transactional
public class CrEchographieService {

    private final Logger log = LoggerFactory.getLogger(CrEchographieService.class);

    private final CrEchographieRepository crEchographieRepository;

    public CrEchographieService(CrEchographieRepository crEchographieRepository) {
        this.crEchographieRepository = crEchographieRepository;
    }

    /**
     * Save a crEchographie.
     *
     * @param crEchographie the entity to save.
     * @return the persisted entity.
     */
    public CrEchographie save(CrEchographie crEchographie) {
        log.debug("Request to save CrEchographie : {}", crEchographie);
        return crEchographieRepository.save(crEchographie);
    }

    /**
     * Partially update a crEchographie.
     *
     * @param crEchographie the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CrEchographie> partialUpdate(CrEchographie crEchographie) {
        log.debug("Request to partially update CrEchographie : {}", crEchographie);

        return crEchographieRepository
            .findById(crEchographie.getId())
            .map(
                existingCrEchographie -> {
                    if (crEchographie.getAspectFoie() != null) {
                        existingCrEchographie.setAspectFoie(crEchographie.getAspectFoie());
                    }
                    if (crEchographie.getObservationFoie() != null) {
                        existingCrEchographie.setObservationFoie(crEchographie.getObservationFoie());
                    }
                    if (crEchographie.getAspectVesicule() != null) {
                        existingCrEchographie.setAspectVesicule(crEchographie.getAspectVesicule());
                    }
                    if (crEchographie.getObservationVesicule() != null) {
                        existingCrEchographie.setObservationVesicule(crEchographie.getObservationVesicule());
                    }
                    if (crEchographie.getAspectTrocVoieVeine() != null) {
                        existingCrEchographie.setAspectTrocVoieVeine(crEchographie.getAspectTrocVoieVeine());
                    }
                    if (crEchographie.getObservationTrocVoieVeine() != null) {
                        existingCrEchographie.setObservationTrocVoieVeine(crEchographie.getObservationTrocVoieVeine());
                    }
                    if (crEchographie.getAspectReins() != null) {
                        existingCrEchographie.setAspectReins(crEchographie.getAspectReins());
                    }
                    if (crEchographie.getObservationReins() != null) {
                        existingCrEchographie.setObservationReins(crEchographie.getObservationReins());
                    }
                    if (crEchographie.getAspectRate() != null) {
                        existingCrEchographie.setAspectRate(crEchographie.getAspectRate());
                    }
                    if (crEchographie.getObservationRate() != null) {
                        existingCrEchographie.setObservationRate(crEchographie.getObservationRate());
                    }
                    if (crEchographie.getAspectPancreas() != null) {
                        existingCrEchographie.setAspectPancreas(crEchographie.getAspectPancreas());
                    }
                    if (crEchographie.getObservationPancreas() != null) {
                        existingCrEchographie.setObservationPancreas(crEchographie.getObservationPancreas());
                    }
                    if (crEchographie.getAutreObservation() != null) {
                        existingCrEchographie.setAutreObservation(crEchographie.getAutreObservation());
                    }

                    return existingCrEchographie;
                }
            )
            .map(crEchographieRepository::save);
    }

    /**
     * Get all the crEchographies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CrEchographie> findAll() {
        log.debug("Request to get all CrEchographies");
        return crEchographieRepository.findAll();
    }

    /**
     *  Get all the crEchographies where Visite is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CrEchographie> findAllWhereVisiteIsNull() {
        log.debug("Request to get all crEchographies where Visite is null");
        return StreamSupport
            .stream(crEchographieRepository.findAll().spliterator(), false)
            .filter(crEchographie -> crEchographie.getVisite() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one crEchographie by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CrEchographie> findOne(Long id) {
        log.debug("Request to get CrEchographie : {}", id);
        return crEchographieRepository.findById(id);
    }

    /**
     * Delete the crEchographie by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CrEchographie : {}", id);
        crEchographieRepository.deleteById(id);
    }
}
