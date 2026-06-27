package com.scada.controller;

import com.scada.sess.FaultSession;
import com.scada.sess.FaultSessionManager;
import com.scada.service.FaultAlgoService;
import com.scada.entities.FaultAlgo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/fault-session")
@CrossOrigin(origins = {
        "https://scada.pratikshat.com",
        "http://198.7.114.147:9999",
        "http://localhost:9999",
        "http://localhost:4200",
        "http://207.180.233.141:9999"
})
public class FaultSessionController {

    @Autowired
    private FaultSessionManager sessionManager;

    @Autowired
    private FaultAlgoService faultAlgoService;

    // 🔑 Shared SSE Map (userId → emitter)
    private final Map<String, SseEmitter> userEmitters = new HashMap<>();

    // ------------------------------------------------
    // USER connects SSE
    // ------------------------------------------------
@GetMapping(
        path = "/stream/{userId}",
        produces = MediaType.TEXT_EVENT_STREAM_VALUE
)
public SseEmitter connect(@PathVariable String userId) {

    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    userEmitters.put(userId, emitter);

    System.out.println("🟢 User connected: " + userId);

    emitter.onCompletion(() -> userEmitters.remove(userId));
    emitter.onTimeout(() -> userEmitters.remove(userId));
    emitter.onError(e -> userEmitters.remove(userId));

    // 🔥 NEW: Send pending fault requests
    List<FaultSession> pending =
            sessionManager.getPendingSessionsForUser(userId);

    for (FaultSession s : pending) {
        try {
            emitter.send(SseEmitter.event()
                    .name("fault-request")
                    .data(Map.of(
                            "sessionId", s.getSessionId(),
                            "adminId", s.getAdminId()
                    )));
            System.out.println("📨 Delivered pending request to user: " + userId);
        } catch (IOException e) {
            userEmitters.remove(userId);
        }
    }

    return emitter;
}


    // ------------------------------------------------
    // ADMIN requests fault session
    // ------------------------------------------------
    @PostMapping("/request")
    public ResponseEntity<?> requestSession(@RequestBody Map<String, Object> body) {

        String adminId = (String) body.get("adminId");
        List<String> userIds = (List<String>) body.get("userIds");

        Set<String> onlineUsers = userEmitters.keySet();

        List<FaultSession> sessions =
                sessionManager.requestSessions(adminId, userIds, onlineUsers);

        // Notify ONLY selected & online users
        for (FaultSession s : sessions) {
            SseEmitter emitter = userEmitters.get(s.getUserId());
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("fault-request")
                            .data(Map.of(
                                    "sessionId", s.getSessionId(),
                                    "adminId", adminId
                            )));
                } catch (IOException e) {
                    userEmitters.remove(s.getUserId());
                }
            }
        }
        return ResponseEntity.ok(sessions);
    }

    // ------------------------------------------------
    // USER accepts request
    // ------------------------------------------------
    @PostMapping("/accept")
    public ResponseEntity<?> accept(@RequestBody Map<String, String> body) {

        String sessionId = body.get("sessionId");
        String userId = body.get("userId");

        FaultSession session =
                sessionManager.acceptSession(sessionId, userId);

        return ResponseEntity.ok(session);
    }

    // ------------------------------------------------
    // ADMIN activates fault mode
    // ------------------------------------------------
    @PostMapping("/activate")
    public ResponseEntity<?> activate(@RequestBody Map<String, String> body) {

        String sessionId = body.get("sessionId");
        String adminId = body.get("adminId");
        String line = body.get("line");
        String circuitName = body.get("circuitName"); // 🔥 ONLY THIS


        FaultSession session =
                sessionManager.activateSession(sessionId, adminId);

        Optional<FaultAlgo> algo =
                faultAlgoService.getSequenceByLine(line);

        String sequence =
                algo.map(FaultAlgo::getSequence)
                    .orElse("NO_SEQUENCE");

        // 🔥 Send fault ONLY to that user
        SseEmitter emitter = userEmitters.get(session.getUserId());
        if (emitter != null) {
            try {
//                emitter.send(SseEmitter.event().name("fault").data("FAULT_DETECTED"));
//                emitter.send(SseEmitter.event().name("fault-line").data(line));
                emitter.send(SseEmitter.event().name("fault-sequence").data(sequence));
                // 🔥🔥🔥 MAIN PART
                emitter.send(
                        SseEmitter.event()
                                .name("fault-activated")
                                .data(Map.of(
                                        "circuitName", circuitName,
                                        "line", line
                                ))
                );

            } catch (IOException e) {
                userEmitters.remove(session.getUserId());
            }
        }

        return ResponseEntity.ok(Map.of(
                "status", "ACTIVE",
                "sessionId", sessionId
        ));
    }
    @GetMapping("/admin/{adminId}")
    public List<FaultSession> getSessionsForAdmin(
            @PathVariable String adminId
    ) {
        return sessionManager.getSessionsByAdmin(adminId);
    }
    
    @PostMapping("/reject")
    public ResponseEntity<?> reject(@RequestBody Map<String, String> body) {

        String sessionId = body.get("sessionId");
        String userId = body.get("userId");

        FaultSession session =
                sessionManager.rejectSession(sessionId, userId);

        return ResponseEntity.ok(session);
    }
 // ------------------------------------------------
 // USER resolves fault
 // ------------------------------------------------
    @PostMapping("/resolve")
    public ResponseEntity<?> resolve(@RequestBody Map<String, String> body) {

        String sessionId = body.get("sessionId");
        String userId = body.get("userId");

        FaultSession session =
                sessionManager.resolveSession(sessionId, userId);

        return ResponseEntity.ok(Map.of(
                "status", session.getState().name(),
                "sessionId", sessionId
        ));
    }
}
