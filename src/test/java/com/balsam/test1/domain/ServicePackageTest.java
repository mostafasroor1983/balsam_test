package com.balsam.test1.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.balsam.test1.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicePackageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicePackage.class);
        ServicePackage servicePackage1 = new ServicePackage();
        servicePackage1.setId(1L);
        ServicePackage servicePackage2 = new ServicePackage();
        servicePackage2.setId(servicePackage1.getId());
        assertThat(servicePackage1).isEqualTo(servicePackage2);
        servicePackage2.setId(2L);
        assertThat(servicePackage1).isNotEqualTo(servicePackage2);
        servicePackage1.setId(null);
        assertThat(servicePackage1).isNotEqualTo(servicePackage2);
    }
}
