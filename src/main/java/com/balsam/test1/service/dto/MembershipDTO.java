package com.balsam.test1.service.dto;

import com.balsam.test1.domain.enumeration.MemberType;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.balsam.test1.domain.Membership} entity.
 */
public class MembershipDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String membershipId;

    private MemberType memberType;

    private Boolean active;

    private Boolean hasPhysicalVersion;

    private String memberShare;

    private String corporateShare;

    private Instant printingDateTime;

    private ServicePackageDTO servicePackage;

    private CorporateDTO corporate;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getHasPhysicalVersion() {
        return hasPhysicalVersion;
    }

    public void setHasPhysicalVersion(Boolean hasPhysicalVersion) {
        this.hasPhysicalVersion = hasPhysicalVersion;
    }

    public String getMemberShare() {
        return memberShare;
    }

    public void setMemberShare(String memberShare) {
        this.memberShare = memberShare;
    }

    public String getCorporateShare() {
        return corporateShare;
    }

    public void setCorporateShare(String corporateShare) {
        this.corporateShare = corporateShare;
    }

    public Instant getPrintingDateTime() {
        return printingDateTime;
    }

    public void setPrintingDateTime(Instant printingDateTime) {
        this.printingDateTime = printingDateTime;
    }

    public ServicePackageDTO getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(ServicePackageDTO servicePackage) {
        this.servicePackage = servicePackage;
    }

    public CorporateDTO getCorporate() {
        return corporate;
    }

    public void setCorporate(CorporateDTO corporate) {
        this.corporate = corporate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipDTO)) {
            return false;
        }

        MembershipDTO membershipDTO = (MembershipDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, membershipDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipDTO{" +
            "id=" + getId() +
            ", membershipId='" + getMembershipId() + "'" +
            ", memberType='" + getMemberType() + "'" +
            ", active='" + getActive() + "'" +
            ", hasPhysicalVersion='" + getHasPhysicalVersion() + "'" +
            ", memberShare='" + getMemberShare() + "'" +
            ", corporateShare='" + getCorporateShare() + "'" +
            ", printingDateTime='" + getPrintingDateTime() + "'" +
            ", servicePackage=" + getServicePackage() +
            ", corporate=" + getCorporate() +
            ", user=" + getUser() +
            "}";
    }
}
