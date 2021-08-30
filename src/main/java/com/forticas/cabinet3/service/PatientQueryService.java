package com.forticas.cabinet3.service;

import com.forticas.cabinet3.domain.*; // for static metamodels
import com.forticas.cabinet3.domain.Patient;
import com.forticas.cabinet3.repository.PatientRepository;
import com.forticas.cabinet3.service.criteria.PatientCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Patient} entities in the database.
 * The main input is a {@link PatientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Patient} or a {@link Page} of {@link Patient} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientQueryService extends QueryService<Patient> {

    private final Logger log = LoggerFactory.getLogger(PatientQueryService.class);

    private final PatientRepository patientRepository;

    public PatientQueryService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Return a {@link List} of {@link Patient} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Patient> findByCriteria(PatientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Patient} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Patient> findByCriteria(PatientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.count(specification);
    }

    /**
     * Function to convert {@link PatientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Patient> createSpecification(PatientCriteria criteria) {
        Specification<Patient> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Patient_.id));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), Patient_.reference));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Patient_.nom));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Patient_.prenom));
            }
            if (criteria.getNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNaissance(), Patient_.naissance));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildSpecification(criteria.getSexe(), Patient_.sexe));
            }
            if (criteria.getEtatCivil() != null) {
                specification = specification.and(buildSpecification(criteria.getEtatCivil(), Patient_.etatCivil));
            }
            if (criteria.getFonction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFonction(), Patient_.fonction));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Patient_.email));
            }
            if (criteria.getTel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTel(), Patient_.tel));
            }
            if (criteria.getTelFixe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelFixe(), Patient_.telFixe));
            }
            if (criteria.getAdresse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdresse(), Patient_.adresse));
            }
            if (criteria.getRendezVousId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRendezVousId(),
                            root -> root.join(Patient_.rendezVous, JoinType.LEFT).get(RendezVous_.id)
                        )
                    );
            }
            if (criteria.getCasTraiterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCasTraiterId(),
                            root -> root.join(Patient_.casTraiters, JoinType.LEFT).get(CasTraiter_.id)
                        )
                    );
            }
            if (criteria.getConstanteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConstanteId(),
                            root -> root.join(Patient_.constantes, JoinType.LEFT).get(Constante_.id)
                        )
                    );
            }
            if (criteria.getAntecedentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAntecedentId(),
                            root -> root.join(Patient_.antecedents, JoinType.LEFT).get(Antecedent_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
