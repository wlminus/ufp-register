package com.wlminus.ufp.repository;

import com.wlminus.ufp.domain.RequirePair;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data Cassandra repository for the RequirePair entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequirePairRepository extends CassandraRepository<RequirePair, UUID> {

}
