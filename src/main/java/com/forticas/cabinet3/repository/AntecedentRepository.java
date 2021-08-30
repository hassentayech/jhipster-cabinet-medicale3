package com.forticas.cabinet3.repository;

import com.forticas.cabinet3.domain.Antecedent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Antecedent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AntecedentRepository extends JpaRepository<Antecedent, Long> {}
