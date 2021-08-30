package com.forticas.cabinet3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.forticas.cabinet3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReglementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reglement.class);
        Reglement reglement1 = new Reglement();
        reglement1.setId(1L);
        Reglement reglement2 = new Reglement();
        reglement2.setId(reglement1.getId());
        assertThat(reglement1).isEqualTo(reglement2);
        reglement2.setId(2L);
        assertThat(reglement1).isNotEqualTo(reglement2);
        reglement1.setId(null);
        assertThat(reglement1).isNotEqualTo(reglement2);
    }
}
