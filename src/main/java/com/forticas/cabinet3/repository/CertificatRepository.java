package com.forticas.cabinet3.repository;

import com.forticas.cabinet3.domain.Certificat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Certificat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CertificatRepository extends JpaRepository<Certificat, Long> {}
