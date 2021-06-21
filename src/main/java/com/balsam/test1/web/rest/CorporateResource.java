package com.balsam.test1.web.rest;

import com.balsam.test1.repository.CorporateRepository;
import com.balsam.test1.service.CorporateQueryService;
import com.balsam.test1.service.CorporateService;
import com.balsam.test1.service.criteria.CorporateCriteria;
import com.balsam.test1.service.dto.CorporateDTO;
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
 * REST controller for managing {@link com.balsam.test1.domain.Corporate}.
 */
@RestController
@RequestMapping("/api")
public class CorporateResource {

    private final Logger log = LoggerFactory.getLogger(CorporateResource.class);

    private static final String ENTITY_NAME = "corporate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CorporateService corporateService;

    private final CorporateRepository corporateRepository;

    private final CorporateQueryService corporateQueryService;

    public CorporateResource(
        CorporateService corporateService,
        CorporateRepository corporateRepository,
        CorporateQueryService corporateQueryService
    ) {
        this.corporateService = corporateService;
        this.corporateRepository = corporateRepository;
        this.corporateQueryService = corporateQueryService;
    }

    /**
     * {@code POST  /corporates} : Create a new corporate.
     *
     * @param corporateDTO the corporateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new corporateDTO, or with status {@code 400 (Bad Request)} if the corporate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/corporates")
    public ResponseEntity<CorporateDTO> createCorporate(@Valid @RequestBody CorporateDTO corporateDTO) throws URISyntaxException {
        log.debug("REST request to save Corporate : {}", corporateDTO);
        if (corporateDTO.getId() != null) {
            throw new BadRequestAlertException("A new corporate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CorporateDTO result = corporateService.save(corporateDTO);
        return ResponseEntity
            .created(new URI("/api/corporates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /corporates/:id} : Updates an existing corporate.
     *
     * @param id the id of the corporateDTO to save.
     * @param corporateDTO the corporateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corporateDTO,
     * or with status {@code 400 (Bad Request)} if the corporateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the corporateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/corporates/{id}")
    public ResponseEntity<CorporateDTO> updateCorporate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CorporateDTO corporateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Corporate : {}, {}", id, corporateDTO);
        if (corporateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corporateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!corporateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CorporateDTO result = corporateService.save(corporateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, corporateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /corporates/:id} : Partial updates given fields of an existing corporate, field will ignore if it is null
     *
     * @param id the id of the corporateDTO to save.
     * @param corporateDTO the corporateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corporateDTO,
     * or with status {@code 400 (Bad Request)} if the corporateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the corporateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the corporateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/corporates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CorporateDTO> partialUpdateCorporate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CorporateDTO corporateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Corporate partially : {}, {}", id, corporateDTO);
        if (corporateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corporateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!corporateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CorporateDTO> result = corporateService.partialUpdate(corporateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, corporateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /corporates} : get all the corporates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of corporates in body.
     */
    @GetMapping("/corporates")
    public ResponseEntity<List<CorporateDTO>> getAllCorporates(CorporateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Corporates by criteria: {}", criteria);
        Page<CorporateDTO> page = corporateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /corporates/count} : count all the corporates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/corporates/count")
    public ResponseEntity<Long> countCorporates(CorporateCriteria criteria) {
        log.debug("REST request to count Corporates by criteria: {}", criteria);
        return ResponseEntity.ok().body(corporateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /corporates/:id} : get the "id" corporate.
     *
     * @param id the id of the corporateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the corporateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/corporates/{id}")
    public ResponseEntity<CorporateDTO> getCorporate(@PathVariable Long id) {
        log.debug("REST request to get Corporate : {}", id);
        Optional<CorporateDTO> corporateDTO = corporateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(corporateDTO);
    }

    /**
     * {@code DELETE  /corporates/:id} : delete the "id" corporate.
     *
     * @param id the id of the corporateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/corporates/{id}")
    public ResponseEntity<Void> deleteCorporate(@PathVariable Long id) {
        log.debug("REST request to delete Corporate : {}", id);
        corporateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
