package com.balsam.test1.service;

import com.balsam.test1.service.dto.CorporateDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.balsam.test1.domain.Corporate}.
 */
public interface CorporateService {
    /**
     * Save a corporate.
     *
     * @param corporateDTO the entity to save.
     * @return the persisted entity.
     */
    CorporateDTO save(CorporateDTO corporateDTO);

    /**
     * Partially updates a corporate.
     *
     * @param corporateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CorporateDTO> partialUpdate(CorporateDTO corporateDTO);

    /**
     * Get all the corporates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CorporateDTO> findAll(Pageable pageable);

    /**
     * Get the "id" corporate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CorporateDTO> findOne(Long id);

    /**
     * Delete the "id" corporate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
