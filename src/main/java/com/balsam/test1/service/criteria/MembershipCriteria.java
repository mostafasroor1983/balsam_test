package com.balsam.test1.service.criteria;

import com.balsam.test1.domain.enumeration.MemberType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.balsam.test1.domain.Membership} entity. This class is used
 * in {@link com.balsam.test1.web.rest.MembershipResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /memberships?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MembershipCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MemberType
     */
    public static class MemberTypeFilter extends Filter<MemberType> {

        public MemberTypeFilter() {}

        public MemberTypeFilter(MemberTypeFilter filter) {
            super(filter);
        }

        @Override
        public MemberTypeFilter copy() {
            return new MemberTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter membershipId;

    private MemberTypeFilter memberType;

    private BooleanFilter active;

    private BooleanFilter hasPhysicalVersion;

    private StringFilter memberShare;

    private StringFilter corporateShare;

    private InstantFilter printingDateTime;

    private LongFilter servicePackageId;

    private LongFilter corporateId;

    private LongFilter userId;

    public MembershipCriteria() {}

    public MembershipCriteria(MembershipCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.membershipId = other.membershipId == null ? null : other.membershipId.copy();
        this.memberType = other.memberType == null ? null : other.memberType.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.hasPhysicalVersion = other.hasPhysicalVersion == null ? null : other.hasPhysicalVersion.copy();
        this.memberShare = other.memberShare == null ? null : other.memberShare.copy();
        this.corporateShare = other.corporateShare == null ? null : other.corporateShare.copy();
        this.printingDateTime = other.printingDateTime == null ? null : other.printingDateTime.copy();
        this.servicePackageId = other.servicePackageId == null ? null : other.servicePackageId.copy();
        this.corporateId = other.corporateId == null ? null : other.corporateId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public MembershipCriteria copy() {
        return new MembershipCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMembershipId() {
        return membershipId;
    }

    public StringFilter membershipId() {
        if (membershipId == null) {
            membershipId = new StringFilter();
        }
        return membershipId;
    }

    public void setMembershipId(StringFilter membershipId) {
        this.membershipId = membershipId;
    }

    public MemberTypeFilter getMemberType() {
        return memberType;
    }

    public MemberTypeFilter memberType() {
        if (memberType == null) {
            memberType = new MemberTypeFilter();
        }
        return memberType;
    }

    public void setMemberType(MemberTypeFilter memberType) {
        this.memberType = memberType;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public BooleanFilter getHasPhysicalVersion() {
        return hasPhysicalVersion;
    }

    public BooleanFilter hasPhysicalVersion() {
        if (hasPhysicalVersion == null) {
            hasPhysicalVersion = new BooleanFilter();
        }
        return hasPhysicalVersion;
    }

    public void setHasPhysicalVersion(BooleanFilter hasPhysicalVersion) {
        this.hasPhysicalVersion = hasPhysicalVersion;
    }

    public StringFilter getMemberShare() {
        return memberShare;
    }

    public StringFilter memberShare() {
        if (memberShare == null) {
            memberShare = new StringFilter();
        }
        return memberShare;
    }

    public void setMemberShare(StringFilter memberShare) {
        this.memberShare = memberShare;
    }

    public StringFilter getCorporateShare() {
        return corporateShare;
    }

    public StringFilter corporateShare() {
        if (corporateShare == null) {
            corporateShare = new StringFilter();
        }
        return corporateShare;
    }

    public void setCorporateShare(StringFilter corporateShare) {
        this.corporateShare = corporateShare;
    }

    public InstantFilter getPrintingDateTime() {
        return printingDateTime;
    }

    public InstantFilter printingDateTime() {
        if (printingDateTime == null) {
            printingDateTime = new InstantFilter();
        }
        return printingDateTime;
    }

    public void setPrintingDateTime(InstantFilter printingDateTime) {
        this.printingDateTime = printingDateTime;
    }

    public LongFilter getServicePackageId() {
        return servicePackageId;
    }

    public LongFilter servicePackageId() {
        if (servicePackageId == null) {
            servicePackageId = new LongFilter();
        }
        return servicePackageId;
    }

    public void setServicePackageId(LongFilter servicePackageId) {
        this.servicePackageId = servicePackageId;
    }

    public LongFilter getCorporateId() {
        return corporateId;
    }

    public LongFilter corporateId() {
        if (corporateId == null) {
            corporateId = new LongFilter();
        }
        return corporateId;
    }

    public void setCorporateId(LongFilter corporateId) {
        this.corporateId = corporateId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MembershipCriteria that = (MembershipCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(membershipId, that.membershipId) &&
            Objects.equals(memberType, that.memberType) &&
            Objects.equals(active, that.active) &&
            Objects.equals(hasPhysicalVersion, that.hasPhysicalVersion) &&
            Objects.equals(memberShare, that.memberShare) &&
            Objects.equals(corporateShare, that.corporateShare) &&
            Objects.equals(printingDateTime, that.printingDateTime) &&
            Objects.equals(servicePackageId, that.servicePackageId) &&
            Objects.equals(corporateId, that.corporateId) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            membershipId,
            memberType,
            active,
            hasPhysicalVersion,
            memberShare,
            corporateShare,
            printingDateTime,
            servicePackageId,
            corporateId,
            userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (membershipId != null ? "membershipId=" + membershipId + ", " : "") +
            (memberType != null ? "memberType=" + memberType + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (hasPhysicalVersion != null ? "hasPhysicalVersion=" + hasPhysicalVersion + ", " : "") +
            (memberShare != null ? "memberShare=" + memberShare + ", " : "") +
            (corporateShare != null ? "corporateShare=" + corporateShare + ", " : "") +
            (printingDateTime != null ? "printingDateTime=" + printingDateTime + ", " : "") +
            (servicePackageId != null ? "servicePackageId=" + servicePackageId + ", " : "") +
            (corporateId != null ? "corporateId=" + corporateId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
