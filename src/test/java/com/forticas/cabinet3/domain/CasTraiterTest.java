package com.forticas.cabinet3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.forticas.cabinet3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CasTraiterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CasTraiter.class);
        CasTraiter casTraiter1 = new CasTraiter();
        casTraiter1.setId(1L);
        CasTraiter casTraiter2 = new CasTraiter();
        casTraiter2.setId(casTraiter1.getId());
        assertThat(casTraiter1).isEqualTo(casTraiter2);
        casTraiter2.setId(2L);
        assertThat(casTraiter1).isNotEqualTo(casTraiter2);
        casTraiter1.setId(null);
        assertThat(casTraiter1).isNotEqualTo(casTraiter2);
    }
}
