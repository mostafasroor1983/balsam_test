package com.balsam.test1.web.rest;

import com.balsam.test1.repository.ValidationRequestFileRepository;
import com.balsam.test1.service.ValidationRequestFileQueryService;
import com.balsam.test1.service.ValidationRequestFileService;
import com.balsam.test1.service.criteria.ValidationRequestFileCriteria;
import com.balsam.test1.service.dto.ValidationRequestFileDTO;
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
 * REST controller for managing {@link com.balsam.test1.domain.ValidationRequestFile}.
 */
@RestController
@RequestMapping("/api")
public class ValidationRequestFileResource {

    private final Logger log = LoggerFactory.getLogger(ValidationRequestFileResource.class);

    private static final String ENTITY_NAME = "validationRequestFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValidationRequestFileService validationRequestFileService;

    private final ValidationRequestFileRepository validationRequestFileRepository;

    private final ValidationRequestFileQueryService validationRequestFileQueryService;

    public ValidationRequestFileResource(
        ValidationRequestFileService validationRequestFileService,
        ValidationRequestFileRepository validationRequestFileRepository,
        ValidationRequestFileQueryService validationRequestFileQueryService
    ) {
        this.validationRequestFileService = validationRequestFileService;
        this.validationRequestFileRepository = validationRequestFileRepository;
        this.validationRequestFileQueryService = validationRequestFileQueryService;
    }

    /**
     * {@code POST  /validation-request-files} : Create a new validationRequestFile.
     *
     * @param validationRequestFileDTO the validationRequestFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new validationRequestFileDTO, or with status {@code 400 (Bad Request)} if the validationRequestFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/validation-request-files")
    public ResponseEntity<ValidationRequestFileDTO> createValidationRequestFile(
        @Valid @RequestBody ValidationRequestFileDTO validationRequestFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ValidationRequestFile : {}", validationRequestFileDTO);
        if (validationRequestFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new validationRequestFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValidationRequestFileDTO result = validationRequestFileService.save(validationRequestFileDTO);
        return ResponseEntity
            .created(new URI("/api/validation-request-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /validation-request-files/:id} : Updates an existing validationRequestFile.
     *
     * @param id the id of the validationRequestFileDTO to save.
     * @param validationRequestFileDTO the validationRequestFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validationRequestFileDTO,
     * or with status {@code 400 (Bad Request)} if the validationRequestFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the validationRequestFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/validation-request-files/{id}")
    public ResponseEntity<ValidationRequestFileDTO> updateValidationRequestFile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ValidationRequestFileDTO validationRequestFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ValidationRequestFile : {}, {}", id, validationRequestFileDTO);
        if (validationRequestFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, validationRequestFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!validationRequestFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ValidationRequestFileDTO result = validationRequestFileService.save(validationRequestFileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validationRequestFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /validation-request-files/:id} : Partial updates given fields of an existing validationRequestFile, field will ignore if it is null
     *
     * @param id the id of the validationRequestFileDTO to save.
     * @param validationRequestFileDTO the validationRequestFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validationRequestFileDTO,
     * or with status {@code 400 (Bad Request)} if the validationRequestFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the validationRequestFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the validationRequestFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/validation-request-files/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ValidationRequestFileDTO> partialUpdateValidationRequestFile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ValidationRequestFileDTO validationRequestFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ValidationRequestFile partially : {}, {}", id, validationRequestFileDTO);
        if (validationRequestFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, validationRequestFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!validationRequestFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ValidationRequestFileDTO> result = validationRequestFileService.partialUpdate(validationRequestFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validationRequestFileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /validation-request-files} : get all the validationRequestFiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of validationRequestFiles in body.
     */
    @GetMapping("/validation-request-files")
    public ResponseEntity<List<ValidationRequestFileDTO>> getAllValidationRequestFiles(
        ValidationRequestFileCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ValidationRequestFiles by criteria: {}", criteria);
        Page<ValidationRequestFileDTO> page = validationRequestFileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /validation-request-files/count} : count all the validationRequestFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/validation-request-files/count")
    public ResponseEntity<Long> countValidationRequestFiles(ValidationRequestFileCriteria criteria) {
        log.debug("REST request to count ValidationRequestFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(validationRequestFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /validation-request-files/:id} : get the "id" validationRequestFile.
     *
     * @param id the id of the validationRequestFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the validationRequestFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/validation-request-files/{id}")
    public ResponseEntity<ValidationRequestFileDTO> getValidationRequestFile(@PathVariable Long id) {
        log.debug("REST request to get ValidationRequestFile : {}", id);
        Optional<ValidationRequestFileDTO> validationRequestFileDTO = validationRequestFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(validationRequestFileDTO);
    }

    /**
     * {@code DELETE  /validation-request-files/:id} : delete the "id" validationRequestFile.
     *
     * @param id the id of the validationRequestFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/validation-request-files/{id}")
    public ResponseEntity<Void> deleteValidationRequestFile(@PathVariable Long id) {
        log.debug("REST request to delete ValidationRequestFile : {}", id);
        validationRequestFileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
