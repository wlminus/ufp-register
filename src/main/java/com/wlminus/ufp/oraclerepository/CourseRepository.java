package com.wlminus.ufp.oraclerepository;

import com.wlminus.ufp.oracledomain.Course;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
@EntityScan(basePackages = "com.wlminus.ufp.oracledomain")
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByCourseCodeIn(List<String> listCode);
}
