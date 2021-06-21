package com.balsam.test1.repository;

import com.balsam.test1.domain.ValidationRequestFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ValidationRequestFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValidationRequestFileRepository
    extends JpaRepository<ValidationRequestFile, Long>, JpaSpecificationExecutor<ValidationRequestFile> {}
