package com.forticas.cabinet3.service;

import com.forticas.cabinet3.domain.*; // for static metamodels
import com.forticas.cabinet3.domain.RendezVous;
import com.forticas.cabinet3.repository.RendezVousRepository;
import com.forticas.cabinet3.service.criteria.RendezVousCriteria;
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
 * Service for executing complex queries for {@link RendezVous} entities in the database.
 * The main input is a {@link RendezVousCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RendezVous} or a {@link Page} of {@link RendezVous} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RendezVousQueryService extends QueryService<RendezVous> {

    private final Logger log = LoggerFactory.getLogger(RendezVousQueryService.class);

    private final RendezVousRepository rendezVousRepository;

    public RendezVousQueryService(RendezVousRepository rendezVousRepository) {
        this.rendezVousRepository = rendezVousRepository;
    }

    /**
     * Return a {@link List} of {@link RendezVous} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RendezVous> findByCriteria(RendezVousCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RendezVous> specification = createSpecification(criteria);
        return rendezVousRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RendezVous} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RendezVous> findByCriteria(RendezVousCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RendezVous> specification = createSpecification(criteria);
        return rendezVousRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RendezVousCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RendezVous> specification = createSpecification(criteria);
        return rendezVousRepository.count(specification);
    }

    /**
     * Function to convert {@link RendezVousCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RendezVous> createSpecification(RendezVousCriteria criteria) {
        Specification<RendezVous> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RendezVous_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), RendezVous_.date));
            }
            if (criteria.getTrancheHoraire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrancheHoraire(), RendezVous_.trancheHoraire));
            }
            if (criteria.getNbrTranche() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNbrTranche(), RendezVous_.nbrTranche));
            }
            if (criteria.getMotif() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMotif(), RendezVous_.motif));
            }
            if (criteria.getPresent() != null) {
                specification = specification.and(buildSpecification(criteria.getPresent(), RendezVous_.present));
            }
            if (criteria.getPatientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPatientId(), root -> root.join(RendezVous_.patient, JoinType.LEFT).get(Patient_.id))
                    );
            }
        }
        return specification;
    }
}
