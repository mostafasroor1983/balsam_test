package com.balsam.test1.domain;

import com.balsam.test1.domain.enumeration.ValidationRequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ValidationRequest.
 */
@Entity
@Table(name = "validation_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ValidationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ValidationRequestStatus status;

    @Column(name = "action_date_time")
    private Instant actionDateTime;

    @Size(max = 2000)
    @Column(name = "reason", length = 2000)
    private String reason;

    @Size(max = 5000)
    @Column(name = "notes", length = 5000)
    private String notes;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToOne
    @JoinColumn(unique = true)
    private User createdBy;

    @OneToOne
    @JoinColumn(unique = true)
    private User acceptedBy;

    @OneToMany(mappedBy = "validationRequest")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "validationRequest" }, allowSetters = true)
    private Set<ValidationRequestFile> files = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ValidationRequest id(Long id) {
        this.id = id;
        return this;
    }

    public ValidationRequestStatus getStatus() {
        return this.status;
    }

    public ValidationRequest status(ValidationRequestStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ValidationRequestStatus status) {
        this.status = status;
    }

    public Instant getActionDateTime() {
        return this.actionDateTime;
    }

    public ValidationRequest actionDateTime(Instant actionDateTime) {
        this.actionDateTime = actionDateTime;
        return this;
    }

    public void setActionDateTime(Instant actionDateTime) {
        this.actionDateTime = actionDateTime;
    }

    public String getReason() {
        return this.reason;
    }

    public ValidationRequest reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return this.notes;
    }

    public ValidationRequest notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return this.user;
    }

    public ValidationRequest user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public ValidationRequest createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public User getAcceptedBy() {
        return this.acceptedBy;
    }

    public ValidationRequest acceptedBy(User user) {
        this.setAcceptedBy(user);
        return this;
    }

    public void setAcceptedBy(User user) {
        this.acceptedBy = user;
    }

    public Set<ValidationRequestFile> getFiles() {
        return this.files;
    }

    public ValidationRequest files(Set<ValidationRequestFile> validationRequestFiles) {
        this.setFiles(validationRequestFiles);
        return this;
    }

    public ValidationRequest addFiles(ValidationRequestFile validationRequestFile) {
        this.files.add(validationRequestFile);
        validationRequestFile.setValidationRequest(this);
        return this;
    }

    public ValidationRequest removeFiles(ValidationRequestFile validationRequestFile) {
        this.files.remove(validationRequestFile);
        validationRequestFile.setValidationRequest(null);
        return this;
    }

    public void setFiles(Set<ValidationRequestFile> validationRequestFiles) {
        if (this.files != null) {
            this.files.forEach(i -> i.setValidationRequest(null));
        }
        if (validationRequestFiles != null) {
            validationRequestFiles.forEach(i -> i.setValidationRequest(this));
        }
        this.files = validationRequestFiles;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValidationRequest)) {
            return false;
        }
        return id != null && id.equals(((ValidationRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValidationRequest{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", actionDateTime='" + getActionDateTime() + "'" +
            ", reason='" + getReason() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
