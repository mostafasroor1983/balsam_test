package com.balsam.test1.web.rest;

import com.balsam.test1.repository.ServicePackageTypeRepository;
import com.balsam.test1.service.ServicePackageTypeQueryService;
import com.balsam.test1.service.ServicePackageTypeService;
import com.balsam.test1.service.criteria.ServicePackageTypeCriteria;
import com.balsam.test1.service.dto.ServicePackageTypeDTO;
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
 * REST controller for managing {@link com.balsam.test1.domain.ServicePackageType}.
 */
@RestController
@RequestMapping("/api")
public class ServicePackageTypeResource {

    private final Logger log = LoggerFactory.getLogger(ServicePackageTypeResource.class);

    private static final String ENTITY_NAME = "servicePackageType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServicePackageTypeService servicePackageTypeService;

    private final ServicePackageTypeRepository servicePackageTypeRepository;

    private final ServicePackageTypeQueryService servicePackageTypeQueryService;

    public ServicePackageTypeResource(
        ServicePackageTypeService servicePackageTypeService,
        ServicePackageTypeRepository servicePackageTypeRepository,
        ServicePackageTypeQueryService servicePackageTypeQueryService
    ) {
        this.servicePackageTypeService = servicePackageTypeService;
        this.servicePackageTypeRepository = servicePackageTypeRepository;
        this.servicePackageTypeQueryService = servicePackageTypeQueryService;
    }

    /**
     * {@code POST  /service-package-types} : Create a new servicePackageType.
     *
     * @param servicePackageTypeDTO the servicePackageTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new servicePackageTypeDTO, or with status {@code 400 (Bad Request)} if the servicePackageType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-package-types")
    public ResponseEntity<ServicePackageTypeDTO> createServicePackageType(@Valid @RequestBody ServicePackageTypeDTO servicePackageTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save ServicePackageType : {}", servicePackageTypeDTO);
        if (servicePackageTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new servicePackageType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServicePackageTypeDTO result = servicePackageTypeService.save(servicePackageTypeDTO);
        return ResponseEntity
            .created(new URI("/api/service-package-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-package-types/:id} : Updates an existing servicePackageType.
     *
     * @param id the id of the servicePackageTypeDTO to save.
     * @param servicePackageTypeDTO the servicePackageTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicePackageTypeDTO,
     * or with status {@code 400 (Bad Request)} if the servicePackageTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the servicePackageTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-package-types/{id}")
    public ResponseEntity<ServicePackageTypeDTO> updateServicePackageType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServicePackageTypeDTO servicePackageTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ServicePackageType : {}, {}", id, servicePackageTypeDTO);
        if (servicePackageTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicePackageTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicePackageTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServicePackageTypeDTO result = servicePackageTypeService.save(servicePackageTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicePackageTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-package-types/:id} : Partial updates given fields of an existing servicePackageType, field will ignore if it is null
     *
     * @param id the id of the servicePackageTypeDTO to save.
     * @param servicePackageTypeDTO the servicePackageTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicePackageTypeDTO,
     * or with status {@code 400 (Bad Request)} if the servicePackageTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the servicePackageTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the servicePackageTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-package-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ServicePackageTypeDTO> partialUpdateServicePackageType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServicePackageTypeDTO servicePackageTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServicePackageType partially : {}, {}", id, servicePackageTypeDTO);
        if (servicePackageTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicePackageTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicePackageTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServicePackageTypeDTO> result = servicePackageTypeService.partialUpdate(servicePackageTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicePackageTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-package-types} : get all the servicePackageTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of servicePackageTypes in body.
     */
    @GetMapping("/service-package-types")
    public ResponseEntity<List<ServicePackageTypeDTO>> getAllServicePackageTypes(ServicePackageTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ServicePackageTypes by criteria: {}", criteria);
        Page<ServicePackageTypeDTO> page = servicePackageTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-package-types/count} : count all the servicePackageTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/service-package-types/count")
    public ResponseEntity<Long> countServicePackageTypes(ServicePackageTypeCriteria criteria) {
        log.debug("REST request to count ServicePackageTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(servicePackageTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /service-package-types/:id} : get the "id" servicePackageType.
     *
     * @param id the id of the servicePackageTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the servicePackageTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-package-types/{id}")
    public ResponseEntity<ServicePackageTypeDTO> getServicePackageType(@PathVariable Long id) {
        log.debug("REST request to get ServicePackageType : {}", id);
        Optional<ServicePackageTypeDTO> servicePackageTypeDTO = servicePackageTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(servicePackageTypeDTO);
    }

    /**
     * {@code DELETE  /service-package-types/:id} : delete the "id" servicePackageType.
     *
     * @param id the id of the servicePackageTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-package-types/{id}")
    public ResponseEntity<Void> deleteServicePackageType(@PathVariable Long id) {
        log.debug("REST request to delete ServicePackageType : {}", id);
        servicePackageTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
