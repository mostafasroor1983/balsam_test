package com.balsam.test1.service.mapper;

import com.balsam.test1.domain.*;
import com.balsam.test1.service.dto.ValidationRequestFileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ValidationRequestFile} and its DTO {@link ValidationRequestFileDTO}.
 */
@Mapper(componentModel = "spring", uses = { ValidationRequestMapper.class })
public interface ValidationRequestFileMapper extends EntityMapper<ValidationRequestFileDTO, ValidationRequestFile> {
    @Mapping(target = "request", source = "request", qualifiedByName = "id")
    ValidationRequestFileDTO toDto(ValidationRequestFile s);
}
