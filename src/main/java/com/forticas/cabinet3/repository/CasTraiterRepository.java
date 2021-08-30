package com.forticas.cabinet3.repository;

import com.forticas.cabinet3.domain.CasTraiter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CasTraiter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CasTraiterRepository extends JpaRepository<CasTraiter, Long> {}
