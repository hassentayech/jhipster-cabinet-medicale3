package com.forticas.cabinet3.service;

import com.forticas.cabinet3.domain.Certificat;
import com.forticas.cabinet3.repository.CertificatRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Certificat}.
 */
@Service
@Transactional
public class CertificatService {

    private final Logger log = LoggerFactory.getLogger(CertificatService.class);

    private final CertificatRepository certificatRepository;

    public CertificatService(CertificatRepository certificatRepository) {
        this.certificatRepository = certificatRepository;
    }

    /**
     * Save a certificat.
     *
     * @param certificat the entity to save.
     * @return the persisted entity.
     */
    public Certificat save(Certificat certificat) {
        log.debug("Request to save Certificat : {}", certificat);
        return certificatRepository.save(certificat);
    }

    /**
     * Partially update a certificat.
     *
     * @param certificat the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Certificat> partialUpdate(Certificat certificat) {
        log.debug("Request to partially update Certificat : {}", certificat);

        return certificatRepository
            .findById(certificat.getId())
            .map(
                existingCertificat -> {
                    if (certificat.getNbrJours() != null) {
                        existingCertificat.setNbrJours(certificat.getNbrJours());
                    }
                    if (certificat.getDescription() != null) {
                        existingCertificat.setDescription(certificat.getDescription());
                    }

                    return existingCertificat;
                }
            )
            .map(certificatRepository::save);
    }

    /**
     * Get all the certificats.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Certificat> findAll() {
        log.debug("Request to get all Certificats");
        return certificatRepository.findAll();
    }

    /**
     *  Get all the certificats where Visite is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Certificat> findAllWhereVisiteIsNull() {
        log.debug("Request to get all certificats where Visite is null");
        return StreamSupport
            .stream(certificatRepository.findAll().spliterator(), false)
            .filter(certificat -> certificat.getVisite() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one certificat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Certificat> findOne(Long id) {
        log.debug("Request to get Certificat : {}", id);
        return certificatRepository.findById(id);
    }

    /**
     * Delete the certificat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Certificat : {}", id);
        certificatRepository.deleteById(id);
    }
}
