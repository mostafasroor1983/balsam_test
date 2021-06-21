package com.balsam.test1.service.dto;

import com.balsam.test1.domain.enumeration.ClientSize;
import com.balsam.test1.domain.enumeration.EmployeeSize;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.balsam.test1.domain.Corporate} entity.
 */
public class CorporateDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 5)
    private String code;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 500)
    private String description;

    @Lob
    private byte[] logo;

    private String logoContentType;

    @Size(max = 1000)
    private String contactPerson;

    @NotNull
    private EmployeeSize employeeSize;

    @NotNull
    private ClientSize clientSize;

    @Pattern(
        regexp = "^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)"
    )
    private String email;

    @Pattern(regexp = "^(https?|ftp|file):\\\\/\\\\/[-a-zA-Z0-9+&@#\\\\/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#\\\\/%=~_|]$")
    private String website;

    private CountryDTO country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public EmployeeSize getEmployeeSize() {
        return employeeSize;
    }

    public void setEmployeeSize(EmployeeSize employeeSize) {
        this.employeeSize = employeeSize;
    }

    public ClientSize getClientSize() {
        return clientSize;
    }

    public void setClientSize(ClientSize clientSize) {
        this.clientSize = clientSize;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CorporateDTO)) {
            return false;
        }

        CorporateDTO corporateDTO = (CorporateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, corporateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorporateDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", logo='" + getLogo() + "'" +
            ", contactPerson='" + getContactPerson() + "'" +
            ", employeeSize='" + getEmployeeSize() + "'" +
            ", clientSize='" + getClientSize() + "'" +
            ", email='" + getEmail() + "'" +
            ", website='" + getWebsite() + "'" +
            ", country=" + getCountry() +
            "}";
    }
}
