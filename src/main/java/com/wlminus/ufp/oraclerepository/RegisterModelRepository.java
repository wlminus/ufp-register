package com.wlminus.ufp.oraclerepository;

import com.wlminus.ufp.oracledomain.RegisterModel;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@EntityScan(basePackages = "com.wlminus.ufp.oracledomain")
public interface RegisterModelRepository extends JpaRepository<RegisterModel, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM finalproject.register r WHERE r.STUDENT_ID = :stuId AND r.COURSE_ID = :courseId", nativeQuery = true)
    Integer deleteByStudentIdAndCourseId(@Param("stuId") Long studentId, @Param("courseId") Long courseId);
}
