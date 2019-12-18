package com.wlminus.ufp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

/**
 * A SubjectRequire.
 */
@Table("subjectRequire")
public class SubjectRequire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    private String subjectCode;

    private String requireCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public SubjectRequire subjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }

    public String getRequireCode() {
        return requireCode;
    }

    public void setRequireCode(String requireCode) {
        this.requireCode = requireCode;
    }

    public SubjectRequire requireCode(String requireCode) {
        this.requireCode = requireCode;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubjectRequire)) {
            return false;
        }
        return id != null && id.equals(((SubjectRequire) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SubjectRequire{" +
            "id=" + getId() +
            ", subjectCode='" + getSubjectCode() + "'" +
            ", requireCode='" + getRequireCode() + "'" +
            "}";
    }
}
