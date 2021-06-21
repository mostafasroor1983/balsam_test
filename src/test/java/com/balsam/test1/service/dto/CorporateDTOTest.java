package com.balsam.test1.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.balsam.test1.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorporateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorporateDTO.class);
        CorporateDTO corporateDTO1 = new CorporateDTO();
        corporateDTO1.setId(1L);
        CorporateDTO corporateDTO2 = new CorporateDTO();
        assertThat(corporateDTO1).isNotEqualTo(corporateDTO2);
        corporateDTO2.setId(corporateDTO1.getId());
        assertThat(corporateDTO1).isEqualTo(corporateDTO2);
        corporateDTO2.setId(2L);
        assertThat(corporateDTO1).isNotEqualTo(corporateDTO2);
        corporateDTO1.setId(null);
        assertThat(corporateDTO1).isNotEqualTo(corporateDTO2);
    }
}
