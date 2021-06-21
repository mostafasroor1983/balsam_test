package com.balsam.test1.service;

import com.balsam.test1.domain.*; // for static metamodels
import com.balsam.test1.domain.ValidationRequest;
import com.balsam.test1.repository.ValidationRequestRepository;
import com.balsam.test1.service.criteria.ValidationRequestCriteria;
import com.balsam.test1.service.dto.ValidationRequestDTO;
import com.balsam.test1.service.mapper.ValidationRequestMapper;
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
 * Service for executing complex queries for {@link ValidationRequest} entities in the database.
 * The main input is a {@link ValidationRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ValidationRequestDTO} or a {@link Page} of {@link ValidationRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ValidationRequestQueryService extends QueryService<ValidationRequest> {

    private final Logger log = LoggerFactory.getLogger(ValidationRequestQueryService.class);

    private final ValidationRequestRepository validationRequestRepository;

    private final ValidationRequestMapper validationRequestMapper;

    public ValidationRequestQueryService(
        ValidationRequestRepository validationRequestRepository,
        ValidationRequestMapper validationRequestMapper
    ) {
        this.validationRequestRepository = validationRequestRepository;
        this.validationRequestMapper = validationRequestMapper;
    }

    /**
     * Return a {@link List} of {@link ValidationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ValidationRequestDTO> findByCriteria(ValidationRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ValidationRequest> specification = createSpecification(criteria);
        return validationRequestMapper.toDto(validationRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ValidationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ValidationRequestDTO> findByCriteria(ValidationRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ValidationRequest> specification = createSpecification(criteria);
        return validationRequestRepository.findAll(specification, page).map(validationRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ValidationRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ValidationRequest> specification = createSpecification(criteria);
        return validationRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link ValidationRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ValidationRequest> createSpecification(ValidationRequestCriteria criteria) {
        Specification<ValidationRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ValidationRequest_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ValidationRequest_.status));
            }
            if (criteria.getActionDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActionDateTime(), ValidationRequest_.actionDateTime));
            }
            if (criteria.getReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReason(), ValidationRequest_.reason));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), ValidationRequest_.notes));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(ValidationRequest_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(ValidationRequest_.createdBy, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getAcceptedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAcceptedById(),
                            root -> root.join(ValidationRequest_.acceptedBy, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getFilesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFilesId(),
                            root -> root.join(ValidationRequest_.files, JoinType.LEFT).get(ValidationRequestFile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
