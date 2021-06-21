package com.balsam.test1.service.criteria;

import com.balsam.test1.domain.enumeration.ValidationRequestFileType;
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
 * Criteria class for the {@link com.balsam.test1.domain.ValidationRequestFile} entity. This class is used
 * in {@link com.balsam.test1.web.rest.ValidationRequestFileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /validation-request-files?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ValidationRequestFileCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ValidationRequestFileType
     */
    public static class ValidationRequestFileTypeFilter extends Filter<ValidationRequestFileType> {

        public ValidationRequestFileTypeFilter() {}

        public ValidationRequestFileTypeFilter(ValidationRequestFileTypeFilter filter) {
            super(filter);
        }

        @Override
        public ValidationRequestFileTypeFilter copy() {
            return new ValidationRequestFileTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ValidationRequestFileTypeFilter type;

    private LongFilter validationRequestId;

    public ValidationRequestFileCriteria() {}

    public ValidationRequestFileCriteria(ValidationRequestFileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.validationRequestId = other.validationRequestId == null ? null : other.validationRequestId.copy();
    }

    @Override
    public ValidationRequestFileCriteria copy() {
        return new ValidationRequestFileCriteria(this);
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

    public ValidationRequestFileTypeFilter getType() {
        return type;
    }

    public ValidationRequestFileTypeFilter type() {
        if (type == null) {
            type = new ValidationRequestFileTypeFilter();
        }
        return type;
    }

    public void setType(ValidationRequestFileTypeFilter type) {
        this.type = type;
    }

    public LongFilter getValidationRequestId() {
        return validationRequestId;
    }

    public LongFilter validationRequestId() {
        if (validationRequestId == null) {
            validationRequestId = new LongFilter();
        }
        return validationRequestId;
    }

    public void setValidationRequestId(LongFilter validationRequestId) {
        this.validationRequestId = validationRequestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ValidationRequestFileCriteria that = (ValidationRequestFileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(validationRequestId, that.validationRequestId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, validationRequestId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValidationRequestFileCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (validationRequestId != null ? "validationRequestId=" + validationRequestId + ", " : "") +
            "}";
    }
}
