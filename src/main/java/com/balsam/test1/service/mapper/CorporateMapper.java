package com.balsam.test1.service.mapper;

import com.balsam.test1.domain.*;
import com.balsam.test1.service.dto.CorporateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Corporate} and its DTO {@link CorporateDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class })
public interface CorporateMapper extends EntityMapper<CorporateDTO, Corporate> {
    @Mapping(target = "country", source = "country", qualifiedByName = "name")
    CorporateDTO toDto(Corporate s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CorporateDTO toDtoName(Corporate corporate);
}
