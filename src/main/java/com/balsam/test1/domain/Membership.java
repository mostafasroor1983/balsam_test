package com.balsam.test1.domain;

import com.balsam.test1.domain.enumeration.MemberType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Membership.
 */
@Entity
@Table(name = "membership")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Membership implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "membership_id", length = 20, nullable = false)
    private String membershipId;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type")
    private MemberType memberType;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "has_physical_version")
    private Boolean hasPhysicalVersion;

    @Column(name = "member_share")
    private String memberShare;

    @Column(name = "corporate_share")
    private String corporateShare;

    @Column(name = "printing_date_time")
    private Instant printingDateTime;

    @JsonIgnoreProperties(value = { "country", "packageType" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ServicePackage servicePackage;

    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Corporate corporate;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Membership id(Long id) {
        this.id = id;
        return this;
    }

    public String getMembershipId() {
        return this.membershipId;
    }

    public Membership membershipId(String membershipId) {
        this.membershipId = membershipId;
        return this;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public MemberType getMemberType() {
        return this.memberType;
    }

    public Membership memberType(MemberType memberType) {
        this.memberType = memberType;
        return this;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Membership active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getHasPhysicalVersion() {
        return this.hasPhysicalVersion;
    }

    public Membership hasPhysicalVersion(Boolean hasPhysicalVersion) {
        this.hasPhysicalVersion = hasPhysicalVersion;
        return this;
    }

    public void setHasPhysicalVersion(Boolean hasPhysicalVersion) {
        this.hasPhysicalVersion = hasPhysicalVersion;
    }

    public String getMemberShare() {
        return this.memberShare;
    }

    public Membership memberShare(String memberShare) {
        this.memberShare = memberShare;
        return this;
    }

    public void setMemberShare(String memberShare) {
        this.memberShare = memberShare;
    }

    public String getCorporateShare() {
        return this.corporateShare;
    }

    public Membership corporateShare(String corporateShare) {
        this.corporateShare = corporateShare;
        return this;
    }

    public void setCorporateShare(String corporateShare) {
        this.corporateShare = corporateShare;
    }

    public Instant getPrintingDateTime() {
        return this.printingDateTime;
    }

    public Membership printingDateTime(Instant printingDateTime) {
        this.printingDateTime = printingDateTime;
        return this;
    }

    public void setPrintingDateTime(Instant printingDateTime) {
        this.printingDateTime = printingDateTime;
    }

    public ServicePackage getServicePackage() {
        return this.servicePackage;
    }

    public Membership servicePackage(ServicePackage servicePackage) {
        this.setServicePackage(servicePackage);
        return this;
    }

    public void setServicePackage(ServicePackage servicePackage) {
        this.servicePackage = servicePackage;
    }

    public Corporate getCorporate() {
        return this.corporate;
    }

    public Membership corporate(Corporate corporate) {
        this.setCorporate(corporate);
        return this;
    }

    public void setCorporate(Corporate corporate) {
        this.corporate = corporate;
    }

    public User getUser() {
        return this.user;
    }

    public Membership user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Membership)) {
            return false;
        }
        return id != null && id.equals(((Membership) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Membership{" +
            "id=" + getId() +
            ", membershipId='" + getMembershipId() + "'" +
            ", memberType='" + getMemberType() + "'" +
            ", active='" + getActive() + "'" +
            ", hasPhysicalVersion='" + getHasPhysicalVersion() + "'" +
            ", memberShare='" + getMemberShare() + "'" +
            ", corporateShare='" + getCorporateShare() + "'" +
            ", printingDateTime='" + getPrintingDateTime() + "'" +
            "}";
    }
}
