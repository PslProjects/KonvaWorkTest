package com.scada.repository;
import com.scada.entities.LineRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LineRuleRepository extends JpaRepository<LineRule, Long> {
//    List<LineRule> findByCircuitId(Long circuitId);
    List<LineRule> findByCircuitName(String circuitName);

}


