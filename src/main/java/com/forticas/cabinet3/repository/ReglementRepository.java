package com.forticas.cabinet3.repository;

import com.forticas.cabinet3.domain.Reglement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Reglement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReglementRepository extends JpaRepository<Reglement, Long> {}
