package com.wlminus.ufp.oraclerepository;

import com.wlminus.ufp.oracledomain.Student;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Student entity.
 */
@SuppressWarnings("unused")
@Repository
@EntityScan(basePackages = "com.wlminus.ufp.oracledomain")
public interface StudentRepository extends JpaRepository<Student, Long> {

}
