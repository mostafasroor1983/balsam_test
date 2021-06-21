package com.balsam.test1.service.criteria;

import com.balsam.test1.domain.enumeration.ClientSize;
import com.balsam.test1.domain.enumeration.EmployeeSize;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.balsam.test1.domain.Corporate} entity. This class is used
 * in {@link com.balsam.test1.web.rest.CorporateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /corporates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CorporateCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EmployeeSize
     */
    public static class EmployeeSizeFilter extends Filter<EmployeeSize> {

        public EmployeeSizeFilter() {}

        public EmployeeSizeFilter(EmployeeSizeFilter filter) {
            super(filter);
        }

        @Override
        public EmployeeSizeFilter copy() {
            return new EmployeeSizeFilter(this);
        }
    }

    /**
     * Class for filtering ClientSize
     */
    public static class ClientSizeFilter extends Filter<ClientSize> {

        public ClientSizeFilter() {}

        public ClientSizeFilter(ClientSizeFilter filter) {
            super(filter);
        }

        @Override
        public ClientSizeFilter copy() {
            return new ClientSizeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter description;

    private StringFilter contactPerson;

    private EmployeeSizeFilter employeeSize;

    private ClientSizeFilter clientSize;

    private StringFilter email;

    private StringFilter website;

    private LongFilter countryId;

    public CorporateCriteria() {}

    public CorporateCriteria(CorporateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.contactPerson = other.contactPerson == null ? null : other.contactPerson.copy();
        this.employeeSize = other.employeeSize == null ? null : other.employeeSize.copy();
        this.clientSize = other.clientSize == null ? null : other.clientSize.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.website = other.website == null ? null : other.website.copy();
        this.countryId = other.countryId == null ? null : other.countryId.copy();
    }

    @Override
    public CorporateCriteria copy() {
        return new CorporateCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getContactPerson() {
        return contactPerson;
    }

    public StringFilter contactPerson() {
        if (contactPerson == null) {
            contactPerson = new StringFilter();
        }
        return contactPerson;
    }

    public void setContactPerson(StringFilter contactPerson) {
        this.contactPerson = contactPerson;
    }

    public EmployeeSizeFilter getEmployeeSize() {
        return employeeSize;
    }

    public EmployeeSizeFilter employeeSize() {
        if (employeeSize == null) {
            employeeSize = new EmployeeSizeFilter();
        }
        return employeeSize;
    }

    public void setEmployeeSize(EmployeeSizeFilter employeeSize) {
        this.employeeSize = employeeSize;
    }

    public ClientSizeFilter getClientSize() {
        return clientSize;
    }

    public ClientSizeFilter clientSize() {
        if (clientSize == null) {
            clientSize = new ClientSizeFilter();
        }
        return clientSize;
    }

    public void setClientSize(ClientSizeFilter clientSize) {
        this.clientSize = clientSize;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getWebsite() {
        return website;
    }

    public StringFilter website() {
        if (website == null) {
            website = new StringFilter();
        }
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
    }

    public LongFilter getCountryId() {
        return countryId;
    }

    public LongFilter countryId() {
        if (countryId == null) {
            countryId = new LongFilter();
        }
        return countryId;
    }

    public void setCountryId(LongFilter countryId) {
        this.countryId = countryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CorporateCriteria that = (CorporateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(contactPerson, that.contactPerson) &&
            Objects.equals(employeeSize, that.employeeSize) &&
            Objects.equals(clientSize, that.clientSize) &&
            Objects.equals(email, that.email) &&
            Objects.equals(website, that.website) &&
            Objects.equals(countryId, that.countryId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, description, contactPerson, employeeSize, clientSize, email, website, countryId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorporateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (contactPerson != null ? "contactPerson=" + contactPerson + ", " : "") +
            (employeeSize != null ? "employeeSize=" + employeeSize + ", " : "") +
            (clientSize != null ? "clientSize=" + clientSize + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (website != null ? "website=" + website + ", " : "") +
            (countryId != null ? "countryId=" + countryId + ", " : "") +
            "}";
    }
}
