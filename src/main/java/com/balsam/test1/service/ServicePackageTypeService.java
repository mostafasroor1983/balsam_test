package com.balsam.test1.service;

import com.balsam.test1.service.dto.ServicePackageTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.balsam.test1.domain.ServicePackageType}.
 */
public interface ServicePackageTypeService {
    /**
     * Save a servicePackageType.
     *
     * @param servicePackageTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ServicePackageTypeDTO save(ServicePackageTypeDTO servicePackageTypeDTO);

    /**
     * Partially updates a servicePackageType.
     *
     * @param servicePackageTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServicePackageTypeDTO> partialUpdate(ServicePackageTypeDTO servicePackageTypeDTO);

    /**
     * Get all the servicePackageTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServicePackageTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" servicePackageType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServicePackageTypeDTO> findOne(Long id);

    /**
     * Delete the "id" servicePackageType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
