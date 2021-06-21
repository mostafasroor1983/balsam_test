package com.balsam.test1.repository;

import com.balsam.test1.domain.ServicePackage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ServicePackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long>, JpaSpecificationExecutor<ServicePackage> {}
