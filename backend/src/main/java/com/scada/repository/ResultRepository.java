
package com.scada.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.scada.entities.ResultRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResultRepository extends JpaRepository<ResultRecord, Long> {

    // TOTAL count
    long count();

    // PASS count
    long countByStatus(String status);

    // DATE + STATUS count
    long countByStartTimeBetween(Long start, Long end);

    // DATE + STATUS
    long countByStatusAndStartTimeBetween(String status, Long start, Long end);

    List<ResultRecord> findByUserNameOrderByStartTimeDesc(String userName);
    Optional<ResultRecord> findBySessionId(String sessionId);
    Page<ResultRecord> findAll(Pageable pageable);
    Page<ResultRecord> findByUserNameOrderByStartTimeDesc(String username, Pageable pageable);
    Page<ResultRecord> findByStartTimeBetweenOrderByStartTimeDesc(
            Long start, Long end, Pageable pageable
    );
    Page<ResultRecord> findByStartTimeGreaterThanEqualOrderByStartTimeDesc(
            Long start, Pageable pageable
    );
    Page<ResultRecord> findByStartTimeLessThanEqualOrderByStartTimeDesc(
            Long end, Pageable pageable
    );

    @Query("""
    SELECT 
    COUNT(r),
    COALESCE(SUM(CASE WHEN r.status = 'PASS' THEN 1 ELSE 0 END),0),
    COALESCE(SUM(CASE WHEN r.status = 'FAIL' THEN 1 ELSE 0 END),0)
    FROM ResultRecord r
    WHERE (:start IS NULL OR r.startTime >= :start)
    AND (:end IS NULL OR r.startTime <= :end)
""")
    List<Object[]> getStatsOptimized(
            @Param("start") Long start,
            @Param("end") Long end
    );



}
