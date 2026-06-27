package com.scada.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.scada.entities.ResultRecord;
import com.scada.repository.ResultRepository;
import com.scada.repository.UserRepository; // ✅ NEW

import java.util.*;

@Service
public class ResultService {

    @Autowired
    private ResultRepository repo;

    @Autowired
    private UserRepository userRepo; // ✅ NEW

    public ResultRecord save(ResultRecord r) {
        return repo.save(r);
    }

    public Optional<ResultRecord> findById(Long id) {
        return repo.findById(id);
    }

    public Page<ResultRecord> findByUser(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByUserNameOrderByStartTimeDesc(username, pageable);
    }

//    public List<ResultRecord> findAll() {
//        return repo.findAll();
//    }

    public Page<ResultRecord> findAll(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("startTime").descending()
        );

        return repo.findAll(pageable);
    }

    public Page<ResultRecord> findAllWithDateFilter(
            int page, int size, Long startDate, Long endDate) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("startTime").descending()
        );

        if (startDate == null && endDate == null) {
            return repo.findAll(pageable).map(this::enrichWithUserPhoto);
        }

        if (startDate != null && endDate != null) {
            return repo.findByStartTimeBetweenOrderByStartTimeDesc(
                    startDate, endDate, pageable
            ).map(this::enrichWithUserPhoto);
        }

        if (startDate != null) {
            return repo.findByStartTimeGreaterThanEqualOrderByStartTimeDesc(
                    startDate, pageable
            ).map(this::enrichWithUserPhoto);
        }

        return repo.findByStartTimeLessThanEqualOrderByStartTimeDesc(
                endDate, pageable
        ).map(this::enrichWithUserPhoto);
    }

    public Map<String, Long> getStats(Long startDate, Long endDate) {

        List<Object[]> resultList = repo.getStatsOptimized(startDate, endDate);

        if (resultList == null || resultList.isEmpty()) {
            throw new RuntimeException("No data returned from stats query");
        }

        Object[] result = resultList.get(0);

        long total = result[0] != null ? ((Number) result[0]).longValue() : 0;
        long pass  = result[1] != null ? ((Number) result[1]).longValue() : 0;
        long fail  = result[2] != null ? ((Number) result[2]).longValue() : 0;

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("pass", pass);
        stats.put("fail", fail);

        return stats;
    }

    public ResultRecord findBySessionId(String sessionId) {
        return repo.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Result not found"));
    }

    //  NEW METHOD
    public ResultRecord enrichWithUserPhoto(ResultRecord record) {
        userRepo.findByUsername(record.getUserName()).ifPresent(user -> {
            if (user.getPhoto() != null) {
                record.setUserPhoto("data:image/jpeg;base64,"
                        + Base64.getEncoder().encodeToString(user.getPhoto()));
            }
            record.setUserDesignation(user.getDesignation());
        });
        return record;
    }

    public List<ResultRecord> findAllForPdf(
            Long startDate,
            Long endDate) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        100000,
                        Sort.by("startTime").descending()
                );

        return repo
                .findByStartTimeBetweenOrderByStartTimeDesc(
                        startDate,
                        endDate,
                        pageable
                )
                .getContent();
    }
}
