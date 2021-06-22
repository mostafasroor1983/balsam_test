package com.balsam.test1.service.mapper;

import com.balsam.test1.domain.*;
import com.balsam.test1.service.dto.MembershipDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Membership} and its DTO {@link MembershipDTO}.
 */
@Mapper(componentModel = "spring", uses = { ServicePackageMapper.class, CorporateMapper.class, UserMapper.class })
public interface MembershipMapper extends EntityMapper<MembershipDTO, Membership> {
    @Mapping(target = "servicePackage", source = "servicePackage", qualifiedByName = "name")
    @Mapping(target = "corporate", source = "corporate", qualifiedByName = "name")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    MembershipDTO toDto(Membership s);
}
