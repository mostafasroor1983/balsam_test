package com.balsam.test1.service;

import com.balsam.test1.service.dto.ServicePackageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.balsam.test1.domain.ServicePackage}.
 */
public interface ServicePackageService {
    /**
     * Save a servicePackage.
     *
     * @param servicePackageDTO the entity to save.
     * @return the persisted entity.
     */
    ServicePackageDTO save(ServicePackageDTO servicePackageDTO);

    /**
     * Partially updates a servicePackage.
     *
     * @param servicePackageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServicePackageDTO> partialUpdate(ServicePackageDTO servicePackageDTO);

    /**
     * Get all the servicePackages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServicePackageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" servicePackage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServicePackageDTO> findOne(Long id);

    /**
     * Delete the "id" servicePackage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
