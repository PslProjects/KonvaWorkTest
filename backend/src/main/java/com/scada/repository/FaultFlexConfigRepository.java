package com.scada.repository;

import com.scada.entities.FaultFlexConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FaultFlexConfigRepository extends JpaRepository<FaultFlexConfig, Long> {
    List<FaultFlexConfig> findByFaultLineAndVariant(String faultLine, String variant);
}
