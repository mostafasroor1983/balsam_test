package com.balsam.test1.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.balsam.test1.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicePackageTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicePackageType.class);
        ServicePackageType servicePackageType1 = new ServicePackageType();
        servicePackageType1.setId(1L);
        ServicePackageType servicePackageType2 = new ServicePackageType();
        servicePackageType2.setId(servicePackageType1.getId());
        assertThat(servicePackageType1).isEqualTo(servicePackageType2);
        servicePackageType2.setId(2L);
        assertThat(servicePackageType1).isNotEqualTo(servicePackageType2);
        servicePackageType1.setId(null);
        assertThat(servicePackageType1).isNotEqualTo(servicePackageType2);
    }
}
