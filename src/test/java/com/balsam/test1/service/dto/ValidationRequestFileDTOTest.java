package com.balsam.test1.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.balsam.test1.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValidationRequestFileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValidationRequestFileDTO.class);
        ValidationRequestFileDTO validationRequestFileDTO1 = new ValidationRequestFileDTO();
        validationRequestFileDTO1.setId(1L);
        ValidationRequestFileDTO validationRequestFileDTO2 = new ValidationRequestFileDTO();
        assertThat(validationRequestFileDTO1).isNotEqualTo(validationRequestFileDTO2);
        validationRequestFileDTO2.setId(validationRequestFileDTO1.getId());
        assertThat(validationRequestFileDTO1).isEqualTo(validationRequestFileDTO2);
        validationRequestFileDTO2.setId(2L);
        assertThat(validationRequestFileDTO1).isNotEqualTo(validationRequestFileDTO2);
        validationRequestFileDTO1.setId(null);
        assertThat(validationRequestFileDTO1).isNotEqualTo(validationRequestFileDTO2);
    }
}
