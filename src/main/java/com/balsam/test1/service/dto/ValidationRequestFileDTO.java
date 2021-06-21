package com.balsam.test1.service.dto;

import com.balsam.test1.domain.enumeration.ValidationRequestFileType;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.balsam.test1.domain.ValidationRequestFile} entity.
 */
public class ValidationRequestFileDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 1000)
    private String name;

    @Lob
    private byte[] file;

    private String fileContentType;
    private ValidationRequestFileType type;

    private ValidationRequestDTO validationRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public ValidationRequestFileType getType() {
        return type;
    }

    public void setType(ValidationRequestFileType type) {
        this.type = type;
    }

    public ValidationRequestDTO getValidationRequest() {
        return validationRequest;
    }

    public void setValidationRequest(ValidationRequestDTO validationRequest) {
        this.validationRequest = validationRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValidationRequestFileDTO)) {
            return false;
        }

        ValidationRequestFileDTO validationRequestFileDTO = (ValidationRequestFileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, validationRequestFileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValidationRequestFileDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", file='" + getFile() + "'" +
            ", type='" + getType() + "'" +
            ", validationRequest=" + getValidationRequest() +
            "}";
    }
}
