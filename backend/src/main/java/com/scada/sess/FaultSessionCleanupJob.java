package com.scada.sess;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FaultSessionCleanupJob {

    private final FaultSessionManager manager;

    public FaultSessionCleanupJob(FaultSessionManager manager) {
        this.manager = manager;
    }

    // runs every 30 seconds
    @Scheduled(fixedDelay = 30000)
    public void cleanup() {
        manager.cleanupExpiredSessions();
    }
}
