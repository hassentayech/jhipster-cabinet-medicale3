package com.forticas.cabinet3.service;

import com.forticas.cabinet3.domain.Prescription;
import com.forticas.cabinet3.repository.PrescriptionRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Prescription}.
 */
@Service
@Transactional
public class PrescriptionService {

    private final Logger log = LoggerFactory.getLogger(PrescriptionService.class);

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * Save a prescription.
     *
     * @param prescription the entity to save.
     * @return the persisted entity.
     */
    public Prescription save(Prescription prescription) {
        log.debug("Request to save Prescription : {}", prescription);
        return prescriptionRepository.save(prescription);
    }

    /**
     * Partially update a prescription.
     *
     * @param prescription the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Prescription> partialUpdate(Prescription prescription) {
        log.debug("Request to partially update Prescription : {}", prescription);

        return prescriptionRepository
            .findById(prescription.getId())
            .map(
                existingPrescription -> {
                    if (prescription.getPrescription() != null) {
                        existingPrescription.setPrescription(prescription.getPrescription());
                    }
                    if (prescription.getPrise() != null) {
                        existingPrescription.setPrise(prescription.getPrise());
                    }

                    return existingPrescription;
                }
            )
            .map(prescriptionRepository::save);
    }

    /**
     * Get all the prescriptions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Prescription> findAll() {
        log.debug("Request to get all Prescriptions");
        return prescriptionRepository.findAll();
    }

    /**
     * Get one prescription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Prescription> findOne(Long id) {
        log.debug("Request to get Prescription : {}", id);
        return prescriptionRepository.findById(id);
    }

    /**
     * Delete the prescription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Prescription : {}", id);
        prescriptionRepository.deleteById(id);
    }
}
