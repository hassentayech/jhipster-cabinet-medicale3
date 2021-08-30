package com.forticas.cabinet3.repository;

import com.forticas.cabinet3.domain.CrEchographie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CrEchographie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CrEchographieRepository extends JpaRepository<CrEchographie, Long> {}
