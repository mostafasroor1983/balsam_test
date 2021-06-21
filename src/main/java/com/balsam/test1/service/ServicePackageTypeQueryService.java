package com.balsam.test1.service;

import com.balsam.test1.domain.*; // for static metamodels
import com.balsam.test1.domain.ServicePackageType;
import com.balsam.test1.repository.ServicePackageTypeRepository;
import com.balsam.test1.service.criteria.ServicePackageTypeCriteria;
import com.balsam.test1.service.dto.ServicePackageTypeDTO;
import com.balsam.test1.service.mapper.ServicePackageTypeMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ServicePackageType} entities in the database.
 * The main input is a {@link ServicePackageTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ServicePackageTypeDTO} or a {@link Page} of {@link ServicePackageTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServicePackageTypeQueryService extends QueryService<ServicePackageType> {

    private final Logger log = LoggerFactory.getLogger(ServicePackageTypeQueryService.class);

    private final ServicePackageTypeRepository servicePackageTypeRepository;

    private final ServicePackageTypeMapper servicePackageTypeMapper;

    public ServicePackageTypeQueryService(
        ServicePackageTypeRepository servicePackageTypeRepository,
        ServicePackageTypeMapper servicePackageTypeMapper
    ) {
        this.servicePackageTypeRepository = servicePackageTypeRepository;
        this.servicePackageTypeMapper = servicePackageTypeMapper;
    }

    /**
     * Return a {@link List} of {@link ServicePackageTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ServicePackageTypeDTO> findByCriteria(ServicePackageTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ServicePackageType> specification = createSpecification(criteria);
        return servicePackageTypeMapper.toDto(servicePackageTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ServicePackageTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServicePackageTypeDTO> findByCriteria(ServicePackageTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServicePackageType> specification = createSpecification(criteria);
        return servicePackageTypeRepository.findAll(specification, page).map(servicePackageTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServicePackageTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ServicePackageType> specification = createSpecification(criteria);
        return servicePackageTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ServicePackageTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServicePackageType> createSpecification(ServicePackageTypeCriteria criteria) {
        Specification<ServicePackageType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ServicePackageType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ServicePackageType_.name));
            }
            if (criteria.getPackagesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPackagesId(),
                            root -> root.join(ServicePackageType_.packages, JoinType.LEFT).get(ServicePackage_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
