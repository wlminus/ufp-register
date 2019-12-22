package com.wlminus.ufp.repository;

import com.wlminus.ufp.domain.CourseSlotStatus;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data Cassandra repository for the CourseSlotStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseSlotStatusRepository extends CassandraRepository<CourseSlotStatus, UUID> {

    Optional<CourseSlotStatus> findByCourseCode(String course);
}
