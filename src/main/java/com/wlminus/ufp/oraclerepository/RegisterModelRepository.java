package com.wlminus.ufp.oraclerepository;

import com.wlminus.ufp.oracledomain.RegisterModel;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@EntityScan(basePackages = "com.wlminus.ufp.oracledomain")
public interface RegisterModelRepository extends JpaRepository<RegisterModel, Long> {
}
