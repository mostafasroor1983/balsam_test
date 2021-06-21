package com.balsam.test1.service;

import com.balsam.test1.service.dto.ValidationRequestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.balsam.test1.domain.ValidationRequest}.
 */
public interface ValidationRequestService {
    /**
     * Save a validationRequest.
     *
     * @param validationRequestDTO the entity to save.
     * @return the persisted entity.
     */
    ValidationRequestDTO save(ValidationRequestDTO validationRequestDTO);

    /**
     * Partially updates a validationRequest.
     *
     * @param validationRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ValidationRequestDTO> partialUpdate(ValidationRequestDTO validationRequestDTO);

    /**
     * Get all the validationRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ValidationRequestDTO> findAll(Pageable pageable);

    /**
     * Get the "id" validationRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ValidationRequestDTO> findOne(Long id);

    /**
     * Delete the "id" validationRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
