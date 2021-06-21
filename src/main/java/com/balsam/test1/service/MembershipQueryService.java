package com.balsam.test1.service;

import com.balsam.test1.domain.*; // for static metamodels
import com.balsam.test1.domain.Membership;
import com.balsam.test1.repository.MembershipRepository;
import com.balsam.test1.service.criteria.MembershipCriteria;
import com.balsam.test1.service.dto.MembershipDTO;
import com.balsam.test1.service.mapper.MembershipMapper;
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
 * Service for executing complex queries for {@link Membership} entities in the database.
 * The main input is a {@link MembershipCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MembershipDTO} or a {@link Page} of {@link MembershipDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MembershipQueryService extends QueryService<Membership> {

    private final Logger log = LoggerFactory.getLogger(MembershipQueryService.class);

    private final MembershipRepository membershipRepository;

    private final MembershipMapper membershipMapper;

    public MembershipQueryService(MembershipRepository membershipRepository, MembershipMapper membershipMapper) {
        this.membershipRepository = membershipRepository;
        this.membershipMapper = membershipMapper;
    }

    /**
     * Return a {@link List} of {@link MembershipDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MembershipDTO> findByCriteria(MembershipCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Membership> specification = createSpecification(criteria);
        return membershipMapper.toDto(membershipRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MembershipDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MembershipDTO> findByCriteria(MembershipCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Membership> specification = createSpecification(criteria);
        return membershipRepository.findAll(specification, page).map(membershipMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MembershipCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Membership> specification = createSpecification(criteria);
        return membershipRepository.count(specification);
    }

    /**
     * Function to convert {@link MembershipCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Membership> createSpecification(MembershipCriteria criteria) {
        Specification<Membership> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Membership_.id));
            }
            if (criteria.getMembershipId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMembershipId(), Membership_.membershipId));
            }
            if (criteria.getMemberType() != null) {
                specification = specification.and(buildSpecification(criteria.getMemberType(), Membership_.memberType));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Membership_.active));
            }
            if (criteria.getHasPhysicalVersion() != null) {
                specification = specification.and(buildSpecification(criteria.getHasPhysicalVersion(), Membership_.hasPhysicalVersion));
            }
            if (criteria.getMemberShare() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMemberShare(), Membership_.memberShare));
            }
            if (criteria.getCorporateShare() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCorporateShare(), Membership_.corporateShare));
            }
            if (criteria.getPrintingDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrintingDateTime(), Membership_.printingDateTime));
            }
            if (criteria.getServicePackageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServicePackageId(),
                            root -> root.join(Membership_.servicePackage, JoinType.LEFT).get(ServicePackage_.id)
                        )
                    );
            }
            if (criteria.getCorporateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCorporateId(),
                            root -> root.join(Membership_.corporate, JoinType.LEFT).get(Corporate_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Membership_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
