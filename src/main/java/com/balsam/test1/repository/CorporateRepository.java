package com.balsam.test1.repository;

import com.balsam.test1.domain.Corporate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Corporate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorporateRepository extends JpaRepository<Corporate, Long>, JpaSpecificationExecutor<Corporate> {}
