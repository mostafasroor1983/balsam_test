package com.balsam.test1.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidationRequestMapperTest {

    private ValidationRequestMapper validationRequestMapper;

    @BeforeEach
    public void setUp() {
        validationRequestMapper = new ValidationRequestMapperImpl();
    }
}
