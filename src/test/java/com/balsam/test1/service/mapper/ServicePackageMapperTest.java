package com.balsam.test1.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServicePackageMapperTest {

    private ServicePackageMapper servicePackageMapper;

    @BeforeEach
    public void setUp() {
        servicePackageMapper = new ServicePackageMapperImpl();
    }
}
