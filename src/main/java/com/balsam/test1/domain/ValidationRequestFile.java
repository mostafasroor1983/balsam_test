package com.balsam.test1.domain;

import com.balsam.test1.domain.enumeration.ValidationRequestFileType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ValidationRequestFile.
 */
@Entity
@Table(name = "validation_request_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ValidationRequestFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 1000)
    @Column(name = "name", length = 1000, nullable = false)
    private String name;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @Column(name = "file_content_type")
    private String fileContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ValidationRequestFileType type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "createdBy", "acceptedBy", "files" }, allowSetters = true)
    private ValidationRequest validationRequest;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ValidationRequestFile id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ValidationRequestFile name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFile() {
        return this.file;
    }

    public ValidationRequestFile file(byte[] file) {
        this.file = file;
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return this.fileContentType;
    }

    public ValidationRequestFile fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public ValidationRequestFileType getType() {
        return this.type;
    }

    public ValidationRequestFile type(ValidationRequestFileType type) {
        this.type = type;
        return this;
    }

    public void setType(ValidationRequestFileType type) {
        this.type = type;
    }

    public ValidationRequest getValidationRequest() {
        return this.validationRequest;
    }

    public ValidationRequestFile validationRequest(ValidationRequest validationRequest) {
        this.setValidationRequest(validationRequest);
        return this;
    }

    public void setValidationRequest(ValidationRequest validationRequest) {
        this.validationRequest = validationRequest;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValidationRequestFile)) {
            return false;
        }
        return id != null && id.equals(((ValidationRequestFile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValidationRequestFile{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + getFileContentType() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
