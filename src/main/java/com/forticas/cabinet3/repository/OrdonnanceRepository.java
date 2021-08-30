package com.forticas.cabinet3.repository;

import com.forticas.cabinet3.domain.Ordonnance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ordonnance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdonnanceRepository extends JpaRepository<Ordonnance, Long> {}
