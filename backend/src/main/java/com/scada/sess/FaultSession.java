package com.scada.sess;

import java.time.Instant;
import java.util.UUID;

public class FaultSession {

    private final String sessionId;
    private final String adminId;
    private final String userId;

    private FaultSessionState state;
    private final Instant createdAt;
    private Instant expiresAt;

    public FaultSession(String adminId, String userId, long ttlMillis) {
        this.sessionId = UUID.randomUUID().toString();
        this.adminId = adminId;
        this.userId = userId;
        this.state = FaultSessionState.REQUESTED;
        this.createdAt = Instant.now();
        this.expiresAt = createdAt.plusMillis(ttlMillis);
    }

    // ---------- getters ----------
    public String getSessionId() { return sessionId; }
    public String getAdminId() { return adminId; }
    public String getUserId() { return userId; }
    public FaultSessionState getState() { return state; }
    public Instant getExpiresAt() { return expiresAt; }

    // ---------- state transitions ----------
    public void accept() {
        if (state != FaultSessionState.REQUESTED) {
            throw new IllegalStateException("Session not in REQUESTED state");
        }
        this.state = FaultSessionState.ACCEPTED;
    }
    public void reject() {
        this.state = FaultSessionState.REJECTED;
    }

    public void activate() {
        if (state != FaultSessionState.ACCEPTED) {
            throw new IllegalStateException("Session not ACCEPTED");
        }
        this.state = FaultSessionState.ACTIVE;
    }

    public void end() {
        this.state = FaultSessionState.ENDED;
    }

    public void expire() {
        this.state = FaultSessionState.EXPIRED;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
    public void resolve() {
        if (this.state != FaultSessionState.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE session can be resolved");
        }
        this.state = FaultSessionState.RESOLVED;
      
    }

}
