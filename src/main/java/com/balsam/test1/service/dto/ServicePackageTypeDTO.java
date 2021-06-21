package com.balsam.test1.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.balsam.test1.domain.ServicePackageType} entity.
 */
public class ServicePackageTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 500)
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicePackageTypeDTO)) {
            return false;
        }

        ServicePackageTypeDTO servicePackageTypeDTO = (ServicePackageTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, servicePackageTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicePackageTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
