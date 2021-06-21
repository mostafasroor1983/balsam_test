package com.balsam.test1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ServicePackageType.
 */
@Entity
@Table(name = "service_package_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServicePackageType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 500)
    @Column(name = "name", length = 500, nullable = false)
    private String name;

    @OneToMany(mappedBy = "packageType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "country", "packageType" }, allowSetters = true)
    private Set<ServicePackage> packages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServicePackageType id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ServicePackageType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ServicePackage> getPackages() {
        return this.packages;
    }

    public ServicePackageType packages(Set<ServicePackage> servicePackages) {
        this.setPackages(servicePackages);
        return this;
    }

    public ServicePackageType addPackages(ServicePackage servicePackage) {
        this.packages.add(servicePackage);
        servicePackage.setPackageType(this);
        return this;
    }

    public ServicePackageType removePackages(ServicePackage servicePackage) {
        this.packages.remove(servicePackage);
        servicePackage.setPackageType(null);
        return this;
    }

    public void setPackages(Set<ServicePackage> servicePackages) {
        if (this.packages != null) {
            this.packages.forEach(i -> i.setPackageType(null));
        }
        if (servicePackages != null) {
            servicePackages.forEach(i -> i.setPackageType(this));
        }
        this.packages = servicePackages;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicePackageType)) {
            return false;
        }
        return id != null && id.equals(((ServicePackageType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicePackageType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
