package com.forticas.cabinet3.repository;

import com.forticas.cabinet3.domain.RendezVous;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RendezVous entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long>, JpaSpecificationExecutor<RendezVous> {}
