package com.balsam.test1.repository;

import com.balsam.test1.domain.ValidationRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ValidationRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValidationRequestRepository extends JpaRepository<ValidationRequest, Long>, JpaSpecificationExecutor<ValidationRequest> {}
