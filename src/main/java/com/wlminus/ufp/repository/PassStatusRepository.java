package com.wlminus.ufp.repository;

import com.wlminus.ufp.domain.PassStatus;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data Cassandra repository for the PassStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PassStatusRepository extends CassandraRepository<PassStatus, UUID> {

}
