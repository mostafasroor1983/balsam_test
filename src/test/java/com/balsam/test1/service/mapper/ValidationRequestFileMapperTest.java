package com.balsam.test1.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidationRequestFileMapperTest {

    private ValidationRequestFileMapper validationRequestFileMapper;

    @BeforeEach
    public void setUp() {
        validationRequestFileMapper = new ValidationRequestFileMapperImpl();
    }
}