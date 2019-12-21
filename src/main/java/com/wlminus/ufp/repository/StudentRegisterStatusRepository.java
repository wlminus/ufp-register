package com.wlminus.ufp.repository;

import com.wlminus.ufp.domain.StudentRegisterStatus;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data Cassandra repository for the StudentRegisterStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRegisterStatusRepository extends CassandraRepository<StudentRegisterStatus, UUID> {
    Optional<StudentRegisterStatus> findByStudentId(String studentId);
}
