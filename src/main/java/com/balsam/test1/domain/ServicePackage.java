package com.balsam.test1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ServicePackage.
 */
@Entity
@Table(name = "service_package")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServicePackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 1000)
    @Column(name = "name", length = 1000, nullable = false)
    private String name;

    @Column(name = "recommended")
    private Boolean recommended;

    @Size(max = 500)
    @Column(name = "tag_name", length = 500)
    private String tagName;

    @JsonIgnoreProperties(value = { "cities", "corporates" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Country country;

    @ManyToOne
    @JsonIgnoreProperties(value = { "packages" }, allowSetters = true)
    private ServicePackageType packageType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServicePackage id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ServicePackage name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRecommended() {
        return this.recommended;
    }

    public ServicePackage recommended(Boolean recommended) {
        this.recommended = recommended;
        return this;
    }

    public void setRecommended(Boolean recommended) {
        this.recommended = recommended;
    }

    public String getTagName() {
        return this.tagName;
    }

    public ServicePackage tagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Country getCountry() {
        return this.country;
    }

    public ServicePackage country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public ServicePackageType getPackageType() {
        return this.packageType;
    }

    public ServicePackage packageType(ServicePackageType servicePackageType) {
        this.setPackageType(servicePackageType);
        return this;
    }

    public void setPackageType(ServicePackageType servicePackageType) {
        this.packageType = servicePackageType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicePackage)) {
            return false;
        }
        return id != null && id.equals(((ServicePackage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicePackage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", recommended='" + getRecommended() + "'" +
            ", tagName='" + getTagName() + "'" +
            "}";
    }
}
