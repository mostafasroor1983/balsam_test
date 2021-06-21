package com.balsam.test1.service;

import com.balsam.test1.domain.*; // for static metamodels
import com.balsam.test1.domain.ServicePackage;
import com.balsam.test1.repository.ServicePackageRepository;
import com.balsam.test1.service.criteria.ServicePackageCriteria;
import com.balsam.test1.service.dto.ServicePackageDTO;
import com.balsam.test1.service.mapper.ServicePackageMapper;
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
 * Service for executing complex queries for {@link ServicePackage} entities in the database.
 * The main input is a {@link ServicePackageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ServicePackageDTO} or a {@link Page} of {@link ServicePackageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServicePackageQueryService extends QueryService<ServicePackage> {

    private final Logger log = LoggerFactory.getLogger(ServicePackageQueryService.class);

    private final ServicePackageRepository servicePackageRepository;

    private final ServicePackageMapper servicePackageMapper;

    public ServicePackageQueryService(ServicePackageRepository servicePackageRepository, ServicePackageMapper servicePackageMapper) {
        this.servicePackageRepository = servicePackageRepository;
        this.servicePackageMapper = servicePackageMapper;
    }

    /**
     * Return a {@link List} of {@link ServicePackageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ServicePackageDTO> findByCriteria(ServicePackageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ServicePackage> specification = createSpecification(criteria);
        return servicePackageMapper.toDto(servicePackageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ServicePackageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServicePackageDTO> findByCriteria(ServicePackageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServicePackage> specification = createSpecification(criteria);
        return servicePackageRepository.findAll(specification, page).map(servicePackageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServicePackageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ServicePackage> specification = createSpecification(criteria);
        return servicePackageRepository.count(specification);
    }

    /**
     * Function to convert {@link ServicePackageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServicePackage> createSpecification(ServicePackageCriteria criteria) {
        Specification<ServicePackage> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ServicePackage_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ServicePackage_.name));
            }
            if (criteria.getRecommended() != null) {
                specification = specification.and(buildSpecification(criteria.getRecommended(), ServicePackage_.recommended));
            }
            if (criteria.getTagName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTagName(), ServicePackage_.tagName));
            }
            if (criteria.getCountryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountryId(),
                            root -> root.join(ServicePackage_.country, JoinType.LEFT).get(Country_.id)
                        )
                    );
            }
            if (criteria.getPackageTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPackageTypeId(),
                            root -> root.join(ServicePackage_.packageType, JoinType.LEFT).get(ServicePackageType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
