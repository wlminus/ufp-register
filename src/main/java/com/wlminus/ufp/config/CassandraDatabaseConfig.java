//package com.wlminus.ufp.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
//import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
//
//@Configuration
//@EnableCassandraRepositories("com.wlminus.ufp.repository")
//@EntityScan("com.wlminus.ufp.domain.*")
//public class CassandraDatabaseConfig extends AbstractCassandraConfiguration {
//    @Value("${spring.data.cassandra.contactPoints}")
//    private String contactPoints;
//
//    @Value("${spring.data.cassandra.protocolVersion}")
//    private String protocolVersion;
//
//    @Value("${spring.data.cassandra.keyspaceName}")
//    private String keySpaceName;
//
//    @Override
//    protected String getKeyspaceName() {
//        return keySpaceName;
//    }
//}
