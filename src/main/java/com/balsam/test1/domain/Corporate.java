package com.balsam.test1.domain;

import com.balsam.test1.domain.enumeration.ClientSize;
import com.balsam.test1.domain.enumeration.EmployeeSize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Corporate.
 */
@Entity
@Table(name = "corporate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Corporate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 5)
    @Column(name = "code", length = 5, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Lob
    @Column(name = "logo", nullable = false)
    private byte[] logo;

    @Column(name = "logo_content_type", nullable = false)
    private String logoContentType;

    @Size(max = 1000)
    @Column(name = "contact_person", length = 1000)
    private String contactPerson;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employee_size", nullable = false)
    private EmployeeSize employeeSize;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "client_size", nullable = false)
    private ClientSize clientSize;

    @Pattern(
        regexp = "^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)"
    )
    @Column(name = "email")
    private String email;

    @Pattern(regexp = "^(https?|ftp|file):\\\\/\\\\/[-a-zA-Z0-9+&@#\\\\/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#\\\\/%=~_|]$")
    @Column(name = "website")
    private String website;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cities", "corporates" }, allowSetters = true)
    private Country country;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Corporate id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Corporate code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Corporate name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Corporate description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Corporate logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Corporate logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public String getContactPerson() {
        return this.contactPerson;
    }

    public Corporate contactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
        return this;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public EmployeeSize getEmployeeSize() {
        return this.employeeSize;
    }

    public Corporate employeeSize(EmployeeSize employeeSize) {
        this.employeeSize = employeeSize;
        return this;
    }

    public void setEmployeeSize(EmployeeSize employeeSize) {
        this.employeeSize = employeeSize;
    }

    public ClientSize getClientSize() {
        return this.clientSize;
    }

    public Corporate clientSize(ClientSize clientSize) {
        this.clientSize = clientSize;
        return this;
    }

    public void setClientSize(ClientSize clientSize) {
        this.clientSize = clientSize;
    }

    public String getEmail() {
        return this.email;
    }

    public Corporate email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return this.website;
    }

    public Corporate website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Country getCountry() {
        return this.country;
    }

    public Corporate country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Corporate)) {
            return false;
        }
        return id != null && id.equals(((Corporate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Corporate{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", contactPerson='" + getContactPerson() + "'" +
            ", employeeSize='" + getEmployeeSize() + "'" +
            ", clientSize='" + getClientSize() + "'" +
            ", email='" + getEmail() + "'" +
            ", website='" + getWebsite() + "'" +
            "}";
    }
}
