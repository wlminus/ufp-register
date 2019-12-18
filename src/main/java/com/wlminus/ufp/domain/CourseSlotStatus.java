package com.wlminus.ufp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

/**
 * A CourseSlotStatus.
 */
@Table("courseSlotStatus")
public class CourseSlotStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    private String courseCode;

    private Long maxSlot;

    private Long currentSlot;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public CourseSlotStatus courseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }

    public Long getMaxSlot() {
        return maxSlot;
    }

    public void setMaxSlot(Long maxSlot) {
        this.maxSlot = maxSlot;
    }

    public CourseSlotStatus maxSlot(Long maxSlot) {
        this.maxSlot = maxSlot;
        return this;
    }

    public Long getCurrentSlot() {
        return currentSlot;
    }

    public void setCurrentSlot(Long currentSlot) {
        this.currentSlot = currentSlot;
    }

    public CourseSlotStatus currentSlot(Long currentSlot) {
        this.currentSlot = currentSlot;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseSlotStatus)) {
            return false;
        }
        return id != null && id.equals(((CourseSlotStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CourseSlotStatus{" +
            "id=" + getId() +
            ", courseCode='" + getCourseCode() + "'" +
            ", maxSlot=" + getMaxSlot() +
            ", currentSlot=" + getCurrentSlot() +
            "}";
    }
}
