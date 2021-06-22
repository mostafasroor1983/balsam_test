package com.balsam.test1.service.mapper;

import com.balsam.test1.domain.*;
import com.balsam.test1.service.dto.ValidationRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ValidationRequest} and its DTO {@link ValidationRequestDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ValidationRequestMapper extends EntityMapper<ValidationRequestDTO, ValidationRequest> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "login")
    @Mapping(target = "acceptedBy", source = "acceptedBy", qualifiedByName = "login")
    ValidationRequestDTO toDto(ValidationRequest s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ValidationRequestDTO toDtoId(ValidationRequest validationRequest);
}
