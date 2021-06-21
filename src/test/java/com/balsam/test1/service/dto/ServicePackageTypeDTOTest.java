package com.balsam.test1.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.balsam.test1.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicePackageTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicePackageTypeDTO.class);
        ServicePackageTypeDTO servicePackageTypeDTO1 = new ServicePackageTypeDTO();
        servicePackageTypeDTO1.setId(1L);
        ServicePackageTypeDTO servicePackageTypeDTO2 = new ServicePackageTypeDTO();
        assertThat(servicePackageTypeDTO1).isNotEqualTo(servicePackageTypeDTO2);
        servicePackageTypeDTO2.setId(servicePackageTypeDTO1.getId());
        assertThat(servicePackageTypeDTO1).isEqualTo(servicePackageTypeDTO2);
        servicePackageTypeDTO2.setId(2L);
        assertThat(servicePackageTypeDTO1).isNotEqualTo(servicePackageTypeDTO2);
        servicePackageTypeDTO1.setId(null);
        assertThat(servicePackageTypeDTO1).isNotEqualTo(servicePackageTypeDTO2);
    }
}
