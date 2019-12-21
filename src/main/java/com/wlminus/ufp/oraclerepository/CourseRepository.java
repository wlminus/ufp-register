package com.wlminus.ufp.oraclerepository;

import com.wlminus.ufp.oracledomain.Course;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
@EntityScan(basePackages = "com.wlminus.ufp.oracledomain")
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select c from Course c left join c.subject left join c.schedules where c.courseCode IN :courseCode")
    List<Course> findAllByCourseCodeInList(@Param("courseCode") List<String> requestCourse);

    @Query("select sum(c.subject.creditValueNumber) from Course c left join c.subject where c.courseCode IN :courseCode")
    Long sumCreditValueByCourseIn(@Param("courseCode") List<String> requestCourse);

}
