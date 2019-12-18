package com.wlminus.ufp.config;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@EntityScan("com.wlminus.ufp.oracledomain")
@EnableJpaRepositories(transactionManagerRef = "partialTransactionManager", entityManagerFactoryRef = "partialEntityManagerFactory", basePackages = "com.wlminus.ufp.oraclerepository")
public class OracleDatabaseConfig {
    @Bean
    @ConfigurationProperties("partial.datasource")
    @Primary
    public DataSourceProperties partialDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("partial.datasource.hikari")
    @Primary
    public HikariDataSource partialDataSource() {
        return partialDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "partialEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(
        EntityManagerFactoryBuilder builder) {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");

        LocalContainerEntityManagerFactoryBean emf = builder
            .dataSource(partialDataSource())
            .packages("com.wlminus.ufp.oracledomain")
            .persistenceUnit("partial")
            .build();
        emf.setJpaProperties(properties);
        return emf;
    }

    @Bean(name = "partialTransactionManager")
    public JpaTransactionManager db2TransactionManager(@Qualifier("partialEntityManagerFactory") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
