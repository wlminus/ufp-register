package com.wlminus.ufp.oraclerepository;

import com.wlminus.ufp.oracledomain.Subject;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Subject entity.
 */
@SuppressWarnings("unused")
@Repository
@ComponentScan("com.wlminus.ufp.oracledomain")
public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
