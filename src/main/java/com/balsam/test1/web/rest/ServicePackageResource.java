package com.balsam.test1.web.rest;

import com.balsam.test1.repository.ServicePackageRepository;
import com.balsam.test1.service.ServicePackageQueryService;
import com.balsam.test1.service.ServicePackageService;
import com.balsam.test1.service.criteria.ServicePackageCriteria;
import com.balsam.test1.service.dto.ServicePackageDTO;
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
 * REST controller for managing {@link com.balsam.test1.domain.ServicePackage}.
 */
@RestController
@RequestMapping("/api")
public class ServicePackageResource {

    private final Logger log = LoggerFactory.getLogger(ServicePackageResource.class);

    private static final String ENTITY_NAME = "servicePackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServicePackageService servicePackageService;

    private final ServicePackageRepository servicePackageRepository;

    private final ServicePackageQueryService servicePackageQueryService;

    public ServicePackageResource(
        ServicePackageService servicePackageService,
        ServicePackageRepository servicePackageRepository,
        ServicePackageQueryService servicePackageQueryService
    ) {
        this.servicePackageService = servicePackageService;
        this.servicePackageRepository = servicePackageRepository;
        this.servicePackageQueryService = servicePackageQueryService;
    }

    /**
     * {@code POST  /service-packages} : Create a new servicePackage.
     *
     * @param servicePackageDTO the servicePackageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new servicePackageDTO, or with status {@code 400 (Bad Request)} if the servicePackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-packages")
    public ResponseEntity<ServicePackageDTO> createServicePackage(@Valid @RequestBody ServicePackageDTO servicePackageDTO)
        throws URISyntaxException {
        log.debug("REST request to save ServicePackage : {}", servicePackageDTO);
        if (servicePackageDTO.getId() != null) {
            throw new BadRequestAlertException("A new servicePackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServicePackageDTO result = servicePackageService.save(servicePackageDTO);
        return ResponseEntity
            .created(new URI("/api/service-packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-packages/:id} : Updates an existing servicePackage.
     *
     * @param id the id of the servicePackageDTO to save.
     * @param servicePackageDTO the servicePackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicePackageDTO,
     * or with status {@code 400 (Bad Request)} if the servicePackageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the servicePackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-packages/{id}")
    public ResponseEntity<ServicePackageDTO> updateServicePackage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServicePackageDTO servicePackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ServicePackage : {}, {}", id, servicePackageDTO);
        if (servicePackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicePackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicePackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServicePackageDTO result = servicePackageService.save(servicePackageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicePackageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-packages/:id} : Partial updates given fields of an existing servicePackage, field will ignore if it is null
     *
     * @param id the id of the servicePackageDTO to save.
     * @param servicePackageDTO the servicePackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicePackageDTO,
     * or with status {@code 400 (Bad Request)} if the servicePackageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the servicePackageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the servicePackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-packages/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ServicePackageDTO> partialUpdateServicePackage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServicePackageDTO servicePackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServicePackage partially : {}, {}", id, servicePackageDTO);
        if (servicePackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicePackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicePackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServicePackageDTO> result = servicePackageService.partialUpdate(servicePackageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicePackageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-packages} : get all the servicePackages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of servicePackages in body.
     */
    @GetMapping("/service-packages")
    public ResponseEntity<List<ServicePackageDTO>> getAllServicePackages(ServicePackageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ServicePackages by criteria: {}", criteria);
        Page<ServicePackageDTO> page = servicePackageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-packages/count} : count all the servicePackages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/service-packages/count")
    public ResponseEntity<Long> countServicePackages(ServicePackageCriteria criteria) {
        log.debug("REST request to count ServicePackages by criteria: {}", criteria);
        return ResponseEntity.ok().body(servicePackageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /service-packages/:id} : get the "id" servicePackage.
     *
     * @param id the id of the servicePackageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the servicePackageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-packages/{id}")
    public ResponseEntity<ServicePackageDTO> getServicePackage(@PathVariable Long id) {
        log.debug("REST request to get ServicePackage : {}", id);
        Optional<ServicePackageDTO> servicePackageDTO = servicePackageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(servicePackageDTO);
    }

    /**
     * {@code DELETE  /service-packages/:id} : delete the "id" servicePackage.
     *
     * @param id the id of the servicePackageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-packages/{id}")
    public ResponseEntity<Void> deleteServicePackage(@PathVariable Long id) {
        log.debug("REST request to delete ServicePackage : {}", id);
        servicePackageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
