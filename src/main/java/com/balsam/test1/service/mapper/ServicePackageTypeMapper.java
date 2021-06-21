package com.balsam.test1.service.mapper;

import com.balsam.test1.domain.*;
import com.balsam.test1.service.dto.ServicePackageTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServicePackageType} and its DTO {@link ServicePackageTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServicePackageTypeMapper extends EntityMapper<ServicePackageTypeDTO, ServicePackageType> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServicePackageTypeDTO toDtoId(ServicePackageType servicePackageType);
}
