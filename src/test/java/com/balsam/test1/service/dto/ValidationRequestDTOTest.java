package com.balsam.test1.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.balsam.test1.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValidationRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValidationRequestDTO.class);
        ValidationRequestDTO validationRequestDTO1 = new ValidationRequestDTO();
        validationRequestDTO1.setId(1L);
        ValidationRequestDTO validationRequestDTO2 = new ValidationRequestDTO();
        assertThat(validationRequestDTO1).isNotEqualTo(validationRequestDTO2);
        validationRequestDTO2.setId(validationRequestDTO1.getId());
        assertThat(validationRequestDTO1).isEqualTo(validationRequestDTO2);
        validationRequestDTO2.setId(2L);
        assertThat(validationRequestDTO1).isNotEqualTo(validationRequestDTO2);
        validationRequestDTO1.setId(null);
        assertThat(validationRequestDTO1).isNotEqualTo(validationRequestDTO2);
    }
}
