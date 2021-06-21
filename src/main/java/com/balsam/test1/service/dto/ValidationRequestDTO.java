package com.balsam.test1.service.dto;

import com.balsam.test1.domain.enumeration.ValidationRequestStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.balsam.test1.domain.ValidationRequest} entity.
 */
public class ValidationRequestDTO implements Serializable {

    private Long id;

    private ValidationRequestStatus status;

    private Instant actionDateTime;

    @Size(max = 2000)
    private String reason;

    @Size(max = 5000)
    private String notes;

    private UserDTO user;

    private UserDTO createdBy;

    private UserDTO acceptedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ValidationRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ValidationRequestStatus status) {
        this.status = status;
    }

    public Instant getActionDateTime() {
        return actionDateTime;
    }

    public void setActionDateTime(Instant actionDateTime) {
        this.actionDateTime = actionDateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public UserDTO getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(UserDTO acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValidationRequestDTO)) {
            return false;
        }

        ValidationRequestDTO validationRequestDTO = (ValidationRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, validationRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValidationRequestDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", actionDateTime='" + getActionDateTime() + "'" +
            ", reason='" + getReason() + "'" +
            ", notes='" + getNotes() + "'" +
            ", user=" + getUser() +
            ", createdBy=" + getCreatedBy() +
            ", acceptedBy=" + getAcceptedBy() +
            "}";
    }
}
