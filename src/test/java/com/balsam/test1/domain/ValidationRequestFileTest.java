package com.balsam.test1.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.balsam.test1.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValidationRequestFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValidationRequestFile.class);
        ValidationRequestFile validationRequestFile1 = new ValidationRequestFile();
        validationRequestFile1.setId(1L);
        ValidationRequestFile validationRequestFile2 = new ValidationRequestFile();
        validationRequestFile2.setId(validationRequestFile1.getId());
        assertThat(validationRequestFile1).isEqualTo(validationRequestFile2);
        validationRequestFile2.setId(2L);
        assertThat(validationRequestFile1).isNotEqualTo(validationRequestFile2);
        validationRequestFile1.setId(null);
        assertThat(validationRequestFile1).isNotEqualTo(validationRequestFile2);
    }
}
