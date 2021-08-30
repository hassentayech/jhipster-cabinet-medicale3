package com.forticas.cabinet3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.forticas.cabinet3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CertificatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Certificat.class);
        Certificat certificat1 = new Certificat();
        certificat1.setId(1L);
        Certificat certificat2 = new Certificat();
        certificat2.setId(certificat1.getId());
        assertThat(certificat1).isEqualTo(certificat2);
        certificat2.setId(2L);
        assertThat(certificat1).isNotEqualTo(certificat2);
        certificat1.setId(null);
        assertThat(certificat1).isNotEqualTo(certificat2);
    }
}
