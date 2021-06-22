package com.balsam.test1.service.mapper;

import com.balsam.test1.domain.*;
import com.balsam.test1.service.dto.ServicePackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServicePackage} and its DTO {@link ServicePackageDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class, ServicePackageTypeMapper.class })
public interface ServicePackageMapper extends EntityMapper<ServicePackageDTO, ServicePackage> {
    @Mapping(target = "country", source = "country", qualifiedByName = "name")
    @Mapping(target = "packageType", source = "packageType", qualifiedByName = "name")
    ServicePackageDTO toDto(ServicePackage s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServicePackageDTO toDtoName(ServicePackage servicePackage);
}
