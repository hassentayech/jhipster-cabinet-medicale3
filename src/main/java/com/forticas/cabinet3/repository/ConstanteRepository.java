package com.forticas.cabinet3.repository;

import com.forticas.cabinet3.domain.Constante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Constante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConstanteRepository extends JpaRepository<Constante, Long> {}
