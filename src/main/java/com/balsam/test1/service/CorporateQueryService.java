package com.balsam.test1.service;

import com.balsam.test1.domain.*; // for static metamodels
import com.balsam.test1.domain.Corporate;
import com.balsam.test1.repository.CorporateRepository;
import com.balsam.test1.service.criteria.CorporateCriteria;
import com.balsam.test1.service.dto.CorporateDTO;
import com.balsam.test1.service.mapper.CorporateMapper;
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
 * Service for executing complex queries for {@link Corporate} entities in the database.
 * The main input is a {@link CorporateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CorporateDTO} or a {@link Page} of {@link CorporateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CorporateQueryService extends QueryService<Corporate> {

    private final Logger log = LoggerFactory.getLogger(CorporateQueryService.class);

    private final CorporateRepository corporateRepository;

    private final CorporateMapper corporateMapper;

    public CorporateQueryService(CorporateRepository corporateRepository, CorporateMapper corporateMapper) {
        this.corporateRepository = corporateRepository;
        this.corporateMapper = corporateMapper;
    }

    /**
     * Return a {@link List} of {@link CorporateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CorporateDTO> findByCriteria(CorporateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Corporate> specification = createSpecification(criteria);
        return corporateMapper.toDto(corporateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CorporateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CorporateDTO> findByCriteria(CorporateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Corporate> specification = createSpecification(criteria);
        return corporateRepository.findAll(specification, page).map(corporateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CorporateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Corporate> specification = createSpecification(criteria);
        return corporateRepository.count(specification);
    }

    /**
     * Function to convert {@link CorporateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Corporate> createSpecification(CorporateCriteria criteria) {
        Specification<Corporate> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Corporate_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Corporate_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Corporate_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Corporate_.description));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), Corporate_.contactPerson));
            }
            if (criteria.getEmployeeSize() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployeeSize(), Corporate_.employeeSize));
            }
            if (criteria.getClientSize() != null) {
                specification = specification.and(buildSpecification(criteria.getClientSize(), Corporate_.clientSize));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Corporate_.email));
            }
            if (criteria.getWebsite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWebsite(), Corporate_.website));
            }
            if (criteria.getCountryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCountryId(), root -> root.join(Corporate_.country, JoinType.LEFT).get(Country_.id))
                    );
            }
        }
        return specification;
    }
}
