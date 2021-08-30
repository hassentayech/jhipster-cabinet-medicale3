package com.forticas.cabinet3.service;

import com.forticas.cabinet3.domain.Constante;
import com.forticas.cabinet3.repository.ConstanteRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Constante}.
 */
@Service
@Transactional
public class ConstanteService {

    private final Logger log = LoggerFactory.getLogger(ConstanteService.class);

    private final ConstanteRepository constanteRepository;

    public ConstanteService(ConstanteRepository constanteRepository) {
        this.constanteRepository = constanteRepository;
    }

    /**
     * Save a constante.
     *
     * @param constante the entity to save.
     * @return the persisted entity.
     */
    public Constante save(Constante constante) {
        log.debug("Request to save Constante : {}", constante);
        return constanteRepository.save(constante);
    }

    /**
     * Partially update a constante.
     *
     * @param constante the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Constante> partialUpdate(Constante constante) {
        log.debug("Request to partially update Constante : {}", constante);

        return constanteRepository
            .findById(constante.getId())
            .map(
                existingConstante -> {
                    if (constante.getDate() != null) {
                        existingConstante.setDate(constante.getDate());
                    }
                    if (constante.getPoid() != null) {
                        existingConstante.setPoid(constante.getPoid());
                    }
                    if (constante.getTaille() != null) {
                        existingConstante.setTaille(constante.getTaille());
                    }
                    if (constante.getPas() != null) {
                        existingConstante.setPas(constante.getPas());
                    }
                    if (constante.getPad() != null) {
                        existingConstante.setPad(constante.getPad());
                    }
                    if (constante.getPouls() != null) {
                        existingConstante.setPouls(constante.getPouls());
                    }
                    if (constante.getTemp() != null) {
                        existingConstante.setTemp(constante.getTemp());
                    }
                    if (constante.getGlycemie() != null) {
                        existingConstante.setGlycemie(constante.getGlycemie());
                    }
                    if (constante.getCholesterol() != null) {
                        existingConstante.setCholesterol(constante.getCholesterol());
                    }

                    return existingConstante;
                }
            )
            .map(constanteRepository::save);
    }

    /**
     * Get all the constantes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Constante> findAll() {
        log.debug("Request to get all Constantes");
        return constanteRepository.findAll();
    }

    /**
     * Get one constante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Constante> findOne(Long id) {
        log.debug("Request to get Constante : {}", id);
        return constanteRepository.findById(id);
    }

    /**
     * Delete the constante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Constante : {}", id);
        constanteRepository.deleteById(id);
    }
}
