package com.balsam.test1.service.mapper;

import com.balsam.test1.domain.*;
import com.balsam.test1.service.dto.CityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class })
public interface CityMapper extends EntityMapper<CityDTO, City> {
    @Mapping(target = "country", source = "country", qualifiedByName = "name")
    CityDTO toDto(City s);
}
