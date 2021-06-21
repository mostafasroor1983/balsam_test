package com.balsam.test1.service;

import com.balsam.test1.service.dto.ValidationRequestFileDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.balsam.test1.domain.ValidationRequestFile}.
 */
public interface ValidationRequestFileService {
    /**
     * Save a validationRequestFile.
     *
     * @param validationRequestFileDTO the entity to save.
     * @return the persisted entity.
     */
    ValidationRequestFileDTO save(ValidationRequestFileDTO validationRequestFileDTO);

    /**
     * Partially updates a validationRequestFile.
     *
     * @param validationRequestFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ValidationRequestFileDTO> partialUpdate(ValidationRequestFileDTO validationRequestFileDTO);

    /**
     * Get all the validationRequestFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ValidationRequestFileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" validationRequestFile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ValidationRequestFileDTO> findOne(Long id);

    /**
     * Delete the "id" validationRequestFile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
