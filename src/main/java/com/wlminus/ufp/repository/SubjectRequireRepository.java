package com.wlminus.ufp.repository;

import com.wlminus.ufp.domain.SubjectRequire;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data Cassandra repository for the SubjectRequire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectRequireRepository extends CassandraRepository<SubjectRequire, UUID> {

}
