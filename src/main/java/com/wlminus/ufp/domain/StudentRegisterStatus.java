package com.wlminus.ufp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

/**
 * A StudentRegisterStatus.
 */
@Table("studentRegisterStatus")
public class StudentRegisterStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    private String studentId;

    private Long isPrior;

    private Long maxRegister;

    private Long currentRegister;

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

    public StudentRegisterStatus studentId(String studentId) {
        this.studentId = studentId;
        return this;
    }

    public Long getIsPrior() {
        return isPrior;
    }

    public void setIsPrior(Long isPrior) {
        this.isPrior = isPrior;
    }

    public StudentRegisterStatus isPrior(Long isPrior) {
        this.isPrior = isPrior;
        return this;
    }

    public Long getMaxRegister() {
        return maxRegister;
    }

    public void setMaxRegister(Long maxRegister) {
        this.maxRegister = maxRegister;
    }

    public StudentRegisterStatus maxRegister(Long maxRegister) {
        this.maxRegister = maxRegister;
        return this;
    }

    public Long getCurrentRegister() {
        return currentRegister;
    }

    public void setCurrentRegister(Long currentRegister) {
        this.currentRegister = currentRegister;
    }

    public StudentRegisterStatus currentRegister(Long currentRegister) {
        this.currentRegister = currentRegister;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentRegisterStatus)) {
            return false;
        }
        return id != null && id.equals(((StudentRegisterStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StudentRegisterStatus{" +
            "id=" + getId() +
            ", studentId='" + getStudentId() + "'" +
            ", isPrior=" + getIsPrior() +
            ", maxRegister=" + getMaxRegister() +
            ", currentRegister=" + getCurrentRegister() +
            "}";
    }
}
