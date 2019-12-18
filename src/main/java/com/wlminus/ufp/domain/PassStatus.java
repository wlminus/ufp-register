package com.wlminus.ufp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

/**
 * A PassStatus.
 */
@Table("passStatus")
public class PassStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    private String studentId;

    private String coursePass;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public PassStatus studentId(String studentId) {
        this.studentId = studentId;
        return this;
    }

    public String getCoursePass() {
        return coursePass;
    }

    public void setCoursePass(String coursePass) {
        this.coursePass = coursePass;
    }

    public PassStatus coursePass(String coursePass) {
        this.coursePass = coursePass;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PassStatus)) {
            return false;
        }
        return id != null && id.equals(((PassStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PassStatus{" +
            "id=" + getId() +
            ", studentId='" + getStudentId() + "'" +
            ", coursePass='" + getCoursePass() + "'" +
            "}";
    }
}
