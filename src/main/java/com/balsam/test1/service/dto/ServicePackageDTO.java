package com.balsam.test1.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.balsam.test1.domain.ServicePackage} entity.
 */
public class ServicePackageDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 1000)
    private String name;

    private Boolean recommended;

    @Size(max = 500)
    private String tagName;

    private CountryDTO country;

    private ServicePackageTypeDTO packageType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(Boolean recommended) {
        this.recommended = recommended;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    public ServicePackageTypeDTO getPackageType() {
        return packageType;
    }

    public void setPackageType(ServicePackageTypeDTO packageType) {
        this.packageType = packageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicePackageDTO)) {
            return false;
        }

        ServicePackageDTO servicePackageDTO = (ServicePackageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, servicePackageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicePackageDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", recommended='" + getRecommended() + "'" +
            ", tagName='" + getTagName() + "'" +
            ", country=" + getCountry() +
            ", packageType=" + getPackageType() +
            "}";
    }
}
