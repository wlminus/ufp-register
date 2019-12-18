package com.wlminus.ufp.oraclerepository;

import com.wlminus.ufp.oracledomain.Schedule;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Schedule entity.
 */
@SuppressWarnings("unused")
@Repository
@ComponentScan("com.wlminus.ufp.oracledomain")
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
