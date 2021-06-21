package com.balsam.test1.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.balsam.test1.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorporateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Corporate.class);
        Corporate corporate1 = new Corporate();
        corporate1.setId(1L);
        Corporate corporate2 = new Corporate();
        corporate2.setId(corporate1.getId());
        assertThat(corporate1).isEqualTo(corporate2);
        corporate2.setId(2L);
        assertThat(corporate1).isNotEqualTo(corporate2);
        corporate1.setId(null);
        assertThat(corporate1).isNotEqualTo(corporate2);
    }
}
