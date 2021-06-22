package com.balsam.test1.service;

import com.balsam.test1.domain.*; // for static metamodels
import com.balsam.test1.domain.ValidationRequestFile;
import com.balsam.test1.repository.ValidationRequestFileRepository;
import com.balsam.test1.service.criteria.ValidationRequestFileCriteria;
import com.balsam.test1.service.dto.ValidationRequestFileDTO;
import com.balsam.test1.service.mapper.ValidationRequestFileMapper;
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
 * Service for executing complex queries for {@link ValidationRequestFile} entities in the database.
 * The main input is a {@link ValidationRequestFileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ValidationRequestFileDTO} or a {@link Page} of {@link ValidationRequestFileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ValidationRequestFileQueryService extends QueryService<ValidationRequestFile> {

    private final Logger log = LoggerFactory.getLogger(ValidationRequestFileQueryService.class);

    private final ValidationRequestFileRepository validationRequestFileRepository;

    private final ValidationRequestFileMapper validationRequestFileMapper;

    public ValidationRequestFileQueryService(
        ValidationRequestFileRepository validationRequestFileRepository,
        ValidationRequestFileMapper validationRequestFileMapper
    ) {
        this.validationRequestFileRepository = validationRequestFileRepository;
        this.validationRequestFileMapper = validationRequestFileMapper;
    }

    /**
     * Return a {@link List} of {@link ValidationRequestFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ValidationRequestFileDTO> findByCriteria(ValidationRequestFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ValidationRequestFile> specification = createSpecification(criteria);
        return validationRequestFileMapper.toDto(validationRequestFileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ValidationRequestFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ValidationRequestFileDTO> findByCriteria(ValidationRequestFileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ValidationRequestFile> specification = createSpecification(criteria);
        return validationRequestFileRepository.findAll(specification, page).map(validationRequestFileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ValidationRequestFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ValidationRequestFile> specification = createSpecification(criteria);
        return validationRequestFileRepository.count(specification);
    }

    /**
     * Function to convert {@link ValidationRequestFileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ValidationRequestFile> createSpecification(ValidationRequestFileCriteria criteria) {
        Specification<ValidationRequestFile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ValidationRequestFile_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ValidationRequestFile_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), ValidationRequestFile_.type));
            }
            if (criteria.getRequestId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestId(),
                            root -> root.join(ValidationRequestFile_.request, JoinType.LEFT).get(ValidationRequest_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
