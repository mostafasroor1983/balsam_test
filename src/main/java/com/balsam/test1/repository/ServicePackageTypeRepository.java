package com.balsam.test1.repository;

import com.balsam.test1.domain.ServicePackageType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ServicePackageType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServicePackageTypeRepository
    extends JpaRepository<ServicePackageType, Long>, JpaSpecificationExecutor<ServicePackageType> {}
