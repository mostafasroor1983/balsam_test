package com.balsam.test1.service.criteria;

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
 * Criteria class for the {@link com.balsam.test1.domain.Country} entity. This class is used
 * in {@link com.balsam.test1.web.rest.CountryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /countries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CountryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private StringFilter phoneCode;

    private StringFilter membershipCode;

    private LongFilter citiesId;

    private LongFilter corporatesId;

    public CountryCriteria() {}

    public CountryCriteria(CountryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.phoneCode = other.phoneCode == null ? null : other.phoneCode.copy();
        this.membershipCode = other.membershipCode == null ? null : other.membershipCode.copy();
        this.citiesId = other.citiesId == null ? null : other.citiesId.copy();
        this.corporatesId = other.corporatesId == null ? null : other.corporatesId.copy();
    }

    @Override
    public CountryCriteria copy() {
        return new CountryCriteria(this);
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

    public StringFilter getPhoneCode() {
        return phoneCode;
    }

    public StringFilter phoneCode() {
        if (phoneCode == null) {
            phoneCode = new StringFilter();
        }
        return phoneCode;
    }

    public void setPhoneCode(StringFilter phoneCode) {
        this.phoneCode = phoneCode;
    }

    public StringFilter getMembershipCode() {
        return membershipCode;
    }

    public StringFilter membershipCode() {
        if (membershipCode == null) {
            membershipCode = new StringFilter();
        }
        return membershipCode;
    }

    public void setMembershipCode(StringFilter membershipCode) {
        this.membershipCode = membershipCode;
    }

    public LongFilter getCitiesId() {
        return citiesId;
    }

    public LongFilter citiesId() {
        if (citiesId == null) {
            citiesId = new LongFilter();
        }
        return citiesId;
    }

    public void setCitiesId(LongFilter citiesId) {
        this.citiesId = citiesId;
    }

    public LongFilter getCorporatesId() {
        return corporatesId;
    }

    public LongFilter corporatesId() {
        if (corporatesId == null) {
            corporatesId = new LongFilter();
        }
        return corporatesId;
    }

    public void setCorporatesId(LongFilter corporatesId) {
        this.corporatesId = corporatesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CountryCriteria that = (CountryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(phoneCode, that.phoneCode) &&
            Objects.equals(membershipCode, that.membershipCode) &&
            Objects.equals(citiesId, that.citiesId) &&
            Objects.equals(corporatesId, that.corporatesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, phoneCode, membershipCode, citiesId, corporatesId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (phoneCode != null ? "phoneCode=" + phoneCode + ", " : "") +
            (membershipCode != null ? "membershipCode=" + membershipCode + ", " : "") +
            (citiesId != null ? "citiesId=" + citiesId + ", " : "") +
            (corporatesId != null ? "corporatesId=" + corporatesId + ", " : "") +
            "}";
    }
}
