package com.balsam.test1.service.criteria;

import com.balsam.test1.domain.enumeration.ValidationRequestStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.balsam.test1.domain.ValidationRequest} entity. This class is used
 * in {@link com.balsam.test1.web.rest.ValidationRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /validation-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ValidationRequestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ValidationRequestStatus
     */
    public static class ValidationRequestStatusFilter extends Filter<ValidationRequestStatus> {

        public ValidationRequestStatusFilter() {}

        public ValidationRequestStatusFilter(ValidationRequestStatusFilter filter) {
            super(filter);
        }

        @Override
        public ValidationRequestStatusFilter copy() {
            return new ValidationRequestStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ValidationRequestStatusFilter status;

    private InstantFilter actionDateTime;

    private StringFilter reason;

    private StringFilter notes;

    private LongFilter userId;

    private LongFilter createdById;

    private LongFilter acceptedById;

    private LongFilter filesId;

    public ValidationRequestCriteria() {}

    public ValidationRequestCriteria(ValidationRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.actionDateTime = other.actionDateTime == null ? null : other.actionDateTime.copy();
        this.reason = other.reason == null ? null : other.reason.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.acceptedById = other.acceptedById == null ? null : other.acceptedById.copy();
        this.filesId = other.filesId == null ? null : other.filesId.copy();
    }

    @Override
    public ValidationRequestCriteria copy() {
        return new ValidationRequestCriteria(this);
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

    public ValidationRequestStatusFilter getStatus() {
        return status;
    }

    public ValidationRequestStatusFilter status() {
        if (status == null) {
            status = new ValidationRequestStatusFilter();
        }
        return status;
    }

    public void setStatus(ValidationRequestStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getActionDateTime() {
        return actionDateTime;
    }

    public InstantFilter actionDateTime() {
        if (actionDateTime == null) {
            actionDateTime = new InstantFilter();
        }
        return actionDateTime;
    }

    public void setActionDateTime(InstantFilter actionDateTime) {
        this.actionDateTime = actionDateTime;
    }

    public StringFilter getReason() {
        return reason;
    }

    public StringFilter reason() {
        if (reason == null) {
            reason = new StringFilter();
        }
        return reason;
    }

    public void setReason(StringFilter reason) {
        this.reason = reason;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public LongFilter getAcceptedById() {
        return acceptedById;
    }

    public LongFilter acceptedById() {
        if (acceptedById == null) {
            acceptedById = new LongFilter();
        }
        return acceptedById;
    }

    public void setAcceptedById(LongFilter acceptedById) {
        this.acceptedById = acceptedById;
    }

    public LongFilter getFilesId() {
        return filesId;
    }

    public LongFilter filesId() {
        if (filesId == null) {
            filesId = new LongFilter();
        }
        return filesId;
    }

    public void setFilesId(LongFilter filesId) {
        this.filesId = filesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ValidationRequestCriteria that = (ValidationRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(actionDateTime, that.actionDateTime) &&
            Objects.equals(reason, that.reason) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(acceptedById, that.acceptedById) &&
            Objects.equals(filesId, that.filesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, actionDateTime, reason, notes, userId, createdById, acceptedById, filesId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValidationRequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (actionDateTime != null ? "actionDateTime=" + actionDateTime + ", " : "") +
            (reason != null ? "reason=" + reason + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (acceptedById != null ? "acceptedById=" + acceptedById + ", " : "") +
            (filesId != null ? "filesId=" + filesId + ", " : "") +
            "}";
    }
}
