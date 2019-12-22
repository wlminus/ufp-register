package com.wlminus.ufp.oracledomain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "register")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RegisterModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Sequence_Generator")
    @SequenceGenerator(name = "Sequence_Generator")
    private Long id;

    @Column(name = "semester")
    private String semester;

    @Column(name = "status")
    private String status;

    @ManyToOne
//    @JsonIgnoreProperties("courses")
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
//    @JsonIgnoreProperties("courses")
    @JoinColumn(name = "student_id")
    private Student student;

    public RegisterModel() {
    }

    public RegisterModel(String semester, Course course, Student student) {
        this.semester = semester;
        this.course = course;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "RegisterModel{" +
            "id=" + id +
            ", semester='" + semester + '\'' +
            ", course=" + course +
            ", student=" + student +
            '}';
    }
}
