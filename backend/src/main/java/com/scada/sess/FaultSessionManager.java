package com.scada.sess;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
@Component
public class FaultSessionManager {

    // sessionId -> FaultSession
    private final Map<String, FaultSession> sessions = new ConcurrentHashMap<>();

    // userId -> active sessionIds
    private final Map<String, Set<String>> userSessions = new ConcurrentHashMap<>();

    // adminId -> sessionIds
    private final Map<String, Set<String>> adminSessions = new ConcurrentHashMap<>();

    // TTL for offline users (example: 2 minutes)
    private static final long OFFLINE_TTL_MS = 5 * 60 * 1000;

    // ------------------------------------------------
    // Admin requests fault session for users
    // ------------------------------------------------
    public List<FaultSession> requestSessions(
            String adminId,
            List<String> userIds,
            Set<String> currentlyOnlineUsers
    ) {

        List<FaultSession> created = new ArrayList<>();

        for (String userId : userIds) {

            FaultSession session =
                    new FaultSession(adminId, userId, OFFLINE_TTL_MS);

            sessions.put(session.getSessionId(), session);

            userSessions
                    .computeIfAbsent(userId, k -> new HashSet<>())
                    .add(session.getSessionId());

            adminSessions
                    .computeIfAbsent(adminId, k -> new HashSet<>())
                    .add(session.getSessionId());

            // if user is offline → still REQUESTED (wait till expiry)
            if (!currentlyOnlineUsers.contains(userId)) {
                System.out.println("📴 User offline, request stored: " + userId);
            }

            created.add(session);
        }

        return created;
    }

    // ------------------------------------------------
    // User accepts session
    // ------------------------------------------------
    public FaultSession acceptSession(String sessionId, String userId) {

        FaultSession session = getValidSession(sessionId);

        if (!session.getUserId().equals(userId)) {
            throw new SecurityException("User mismatch");
        }

        session.accept();
        return session;
    }

    // ------------------------------------------------
    // Admin activates fault mode
    // ------------------------------------------------
    public FaultSession activateSession(String sessionId, String adminId) {

        FaultSession session = getValidSession(sessionId);

        if (!session.getAdminId().equals(adminId)) {
            throw new SecurityException("Admin mismatch");
        }

        session.activate();
        return session;
    }

    // ------------------------------------------------
    // Cleanup expired sessions
    // ------------------------------------------------
    public void cleanupExpiredSessions() {

        List<String> expiredIds = sessions.values().stream()
                .filter(FaultSession::isExpired)
                .map(FaultSession::getSessionId)
                .collect(Collectors.toList());

        for (String id : expiredIds) {
            FaultSession s = sessions.remove(id);
            if (s != null) {
                s.expire();
                userSessions.getOrDefault(s.getUserId(), Set.of()).remove(id);
                adminSessions.getOrDefault(s.getAdminId(), Set.of()).remove(id);
            }
        }
    }

    // ------------------------------------------------
    // Helpers
    // ------------------------------------------------
    private FaultSession getValidSession(String sessionId) {

        FaultSession session = sessions.get(sessionId);

        if (session == null) {
            throw new IllegalArgumentException("Session not found");
        }

        if (session.isExpired()) {
            session.expire();
            throw new IllegalStateException("Session expired");
        }

        return session;
    }
 // inside FaultSessionManager

    public List<FaultSession> getPendingSessionsForUser(String userId) {

        Set<String> ids = userSessions.getOrDefault(userId, Set.of());

        List<FaultSession> pending = new ArrayList<>();

        for (String id : ids) {
            FaultSession s = sessions.get(id);
            if (s != null
                    && s.getState() == FaultSessionState.REQUESTED
                    && !s.isExpired()) {
                pending.add(s);
            }
        }
        return pending;
    }


 // ------------------------------------------------
 // Admin → get all sessions (REQUESTED / ACCEPTED / ACTIVE)
 // ------------------------------------------------
 public List<FaultSession> getSessionsByAdmin(String adminId) {

     Set<String> ids = adminSessions.getOrDefault(adminId, Set.of());

     List<FaultSession> result = new ArrayList<>();

     for (String id : ids) {
         FaultSession session = sessions.get(id);
         if (session != null && !session.isExpired()) {
             result.add(session);
         }
     }

     return result;
 }
 
 //REjected 
 public FaultSession rejectSession(String sessionId, String userId) {

     FaultSession session = getValidSession(sessionId);

     if (!session.getUserId().equals(userId)) {
         throw new SecurityException("User mismatch");
     }

     session.reject();
     return session;
 }
//------------------------------------------------
//User resolves fault
//------------------------------------------------
public FaultSession resolveSession(String sessionId, String userId) {

  FaultSession session = getValidSession(sessionId);

  if (!session.getUserId().equals(userId)) {
      throw new SecurityException("User mismatch");
  }

  if (session.getState() != FaultSessionState.ACTIVE) {
      throw new IllegalStateException("Session not ACTIVE");
  }

  session.resolve();
  return session;
}




}
