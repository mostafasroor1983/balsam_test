package com.balsam.test1.web.rest;

import com.balsam.test1.repository.ValidationRequestRepository;
import com.balsam.test1.service.ValidationRequestQueryService;
import com.balsam.test1.service.ValidationRequestService;
import com.balsam.test1.service.criteria.ValidationRequestCriteria;
import com.balsam.test1.service.dto.ValidationRequestDTO;
import com.balsam.test1.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.balsam.test1.domain.ValidationRequest}.
 */
@RestController
@RequestMapping("/api")
public class ValidationRequestResource {

    private final Logger log = LoggerFactory.getLogger(ValidationRequestResource.class);

    private static final String ENTITY_NAME = "validationRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValidationRequestService validationRequestService;

    private final ValidationRequestRepository validationRequestRepository;

    private final ValidationRequestQueryService validationRequestQueryService;

    public ValidationRequestResource(
        ValidationRequestService validationRequestService,
        ValidationRequestRepository validationRequestRepository,
        ValidationRequestQueryService validationRequestQueryService
    ) {
        this.validationRequestService = validationRequestService;
        this.validationRequestRepository = validationRequestRepository;
        this.validationRequestQueryService = validationRequestQueryService;
    }

    /**
     * {@code POST  /validation-requests} : Create a new validationRequest.
     *
     * @param validationRequestDTO the validationRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new validationRequestDTO, or with status {@code 400 (Bad Request)} if the validationRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/validation-requests")
    public ResponseEntity<ValidationRequestDTO> createValidationRequest(@Valid @RequestBody ValidationRequestDTO validationRequestDTO)
        throws URISyntaxException {
        log.debug("REST request to save ValidationRequest : {}", validationRequestDTO);
        if (validationRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new validationRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValidationRequestDTO result = validationRequestService.save(validationRequestDTO);
        return ResponseEntity
            .created(new URI("/api/validation-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /validation-requests/:id} : Updates an existing validationRequest.
     *
     * @param id the id of the validationRequestDTO to save.
     * @param validationRequestDTO the validationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the validationRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the validationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/validation-requests/{id}")
    public ResponseEntity<ValidationRequestDTO> updateValidationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ValidationRequestDTO validationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ValidationRequest : {}, {}", id, validationRequestDTO);
        if (validationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, validationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!validationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ValidationRequestDTO result = validationRequestService.save(validationRequestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validationRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /validation-requests/:id} : Partial updates given fields of an existing validationRequest, field will ignore if it is null
     *
     * @param id the id of the validationRequestDTO to save.
     * @param validationRequestDTO the validationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the validationRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the validationRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the validationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/validation-requests/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ValidationRequestDTO> partialUpdateValidationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ValidationRequestDTO validationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ValidationRequest partially : {}, {}", id, validationRequestDTO);
        if (validationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, validationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!validationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ValidationRequestDTO> result = validationRequestService.partialUpdate(validationRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validationRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /validation-requests} : get all the validationRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of validationRequests in body.
     */
    @GetMapping("/validation-requests")
    public ResponseEntity<List<ValidationRequestDTO>> getAllValidationRequests(ValidationRequestCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ValidationRequests by criteria: {}", criteria);
        Page<ValidationRequestDTO> page = validationRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /validation-requests/count} : count all the validationRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/validation-requests/count")
    public ResponseEntity<Long> countValidationRequests(ValidationRequestCriteria criteria) {
        log.debug("REST request to count ValidationRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(validationRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /validation-requests/:id} : get the "id" validationRequest.
     *
     * @param id the id of the validationRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the validationRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/validation-requests/{id}")
    public ResponseEntity<ValidationRequestDTO> getValidationRequest(@PathVariable Long id) {
        log.debug("REST request to get ValidationRequest : {}", id);
        Optional<ValidationRequestDTO> validationRequestDTO = validationRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(validationRequestDTO);
    }

    /**
     * {@code DELETE  /validation-requests/:id} : delete the "id" validationRequest.
     *
     * @param id the id of the validationRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/validation-requests/{id}")
    public ResponseEntity<Void> deleteValidationRequest(@PathVariable Long id) {
        log.debug("REST request to delete ValidationRequest : {}", id);
        validationRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
