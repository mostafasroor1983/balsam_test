package com.balsam.test1.service.mapper;

import com.balsam.test1.domain.*;
import com.balsam.test1.service.dto.ServicePackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServicePackage} and its DTO {@link ServicePackageDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class, ServicePackageTypeMapper.class })
public interface ServicePackageMapper extends EntityMapper<ServicePackageDTO, ServicePackage> {
    @Mapping(target = "country", source = "country", qualifiedByName = "id")
    @Mapping(target = "packageType", source = "packageType", qualifiedByName = "id")
    ServicePackageDTO toDto(ServicePackage s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServicePackageDTO toDtoId(ServicePackage servicePackage);
}
