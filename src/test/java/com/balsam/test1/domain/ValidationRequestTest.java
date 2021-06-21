package com.balsam.test1.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.balsam.test1.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValidationRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValidationRequest.class);
        ValidationRequest validationRequest1 = new ValidationRequest();
        validationRequest1.setId(1L);
        ValidationRequest validationRequest2 = new ValidationRequest();
        validationRequest2.setId(validationRequest1.getId());
        assertThat(validationRequest1).isEqualTo(validationRequest2);
        validationRequest2.setId(2L);
        assertThat(validationRequest1).isNotEqualTo(validationRequest2);
        validationRequest1.setId(null);
        assertThat(validationRequest1).isNotEqualTo(validationRequest2);
    }
}
