package com.forticas.cabinet3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.forticas.cabinet3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CrEchographieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrEchographie.class);
        CrEchographie crEchographie1 = new CrEchographie();
        crEchographie1.setId(1L);
        CrEchographie crEchographie2 = new CrEchographie();
        crEchographie2.setId(crEchographie1.getId());
        assertThat(crEchographie1).isEqualTo(crEchographie2);
        crEchographie2.setId(2L);
        assertThat(crEchographie1).isNotEqualTo(crEchographie2);
        crEchographie1.setId(null);
        assertThat(crEchographie1).isNotEqualTo(crEchographie2);
    }
}
