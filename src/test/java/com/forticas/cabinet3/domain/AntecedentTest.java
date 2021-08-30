package com.forticas.cabinet3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.forticas.cabinet3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AntecedentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Antecedent.class);
        Antecedent antecedent1 = new Antecedent();
        antecedent1.setId(1L);
        Antecedent antecedent2 = new Antecedent();
        antecedent2.setId(antecedent1.getId());
        assertThat(antecedent1).isEqualTo(antecedent2);
        antecedent2.setId(2L);
        assertThat(antecedent1).isNotEqualTo(antecedent2);
        antecedent1.setId(null);
        assertThat(antecedent1).isNotEqualTo(antecedent2);
    }
}
