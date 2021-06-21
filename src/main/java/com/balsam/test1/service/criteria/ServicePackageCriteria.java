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
 * Criteria class for the {@link com.balsam.test1.domain.ServicePackage} entity. This class is used
 * in {@link com.balsam.test1.web.rest.ServicePackageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /service-packages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ServicePackageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BooleanFilter recommended;

    private StringFilter tagName;

    private LongFilter countryId;

    private LongFilter packageTypeId;

    public ServicePackageCriteria() {}

    public ServicePackageCriteria(ServicePackageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.recommended = other.recommended == null ? null : other.recommended.copy();
        this.tagName = other.tagName == null ? null : other.tagName.copy();
        this.countryId = other.countryId == null ? null : other.countryId.copy();
        this.packageTypeId = other.packageTypeId == null ? null : other.packageTypeId.copy();
    }

    @Override
    public ServicePackageCriteria copy() {
        return new ServicePackageCriteria(this);
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

    public BooleanFilter getRecommended() {
        return recommended;
    }

    public BooleanFilter recommended() {
        if (recommended == null) {
            recommended = new BooleanFilter();
        }
        return recommended;
    }

    public void setRecommended(BooleanFilter recommended) {
        this.recommended = recommended;
    }

    public StringFilter getTagName() {
        return tagName;
    }

    public StringFilter tagName() {
        if (tagName == null) {
            tagName = new StringFilter();
        }
        return tagName;
    }

    public void setTagName(StringFilter tagName) {
        this.tagName = tagName;
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

    public LongFilter getPackageTypeId() {
        return packageTypeId;
    }

    public LongFilter packageTypeId() {
        if (packageTypeId == null) {
            packageTypeId = new LongFilter();
        }
        return packageTypeId;
    }

    public void setPackageTypeId(LongFilter packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ServicePackageCriteria that = (ServicePackageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(recommended, that.recommended) &&
            Objects.equals(tagName, that.tagName) &&
            Objects.equals(countryId, that.countryId) &&
            Objects.equals(packageTypeId, that.packageTypeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, recommended, tagName, countryId, packageTypeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicePackageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (recommended != null ? "recommended=" + recommended + ", " : "") +
            (tagName != null ? "tagName=" + tagName + ", " : "") +
            (countryId != null ? "countryId=" + countryId + ", " : "") +
            (packageTypeId != null ? "packageTypeId=" + packageTypeId + ", " : "") +
            "}";
    }
}
