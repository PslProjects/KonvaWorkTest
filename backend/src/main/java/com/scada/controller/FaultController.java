package com.scada.controller;

import com.scada.entities.FaultValidationResponse;
import com.scada.service.FaultService;

import com.scada.service.FaultSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"https://scada.pratikshat.com", "http://198.7.114.147:9999", "http://207.180.233.141:9999", "http://localhost:9999", "http://localhost:4200"})
@RequestMapping("/fault")
public class FaultController {

	@Autowired
	private  FaultService faultService;

	@Autowired
	private FaultSessionService faultSessionService;

	@PostMapping("/start")
	public ResponseEntity<Map<String, Object>> startFaultSession() {
		// Instead of static faultLiness list and random selection,
		// get the last triggered line from the shared service
		String faultLine = faultSessionService.getLastTriggeredLine();

		// Fallback to default if none stored yet
		if (faultLine == null) {
			faultLine = "LG24";  // default value
		}

		// Create a session ID (could be generated from DB in real app)
		long sessionId = System.currentTimeMillis();

		Map<String, Object> response = Map.of(
				"id", sessionId,
				"faultLine", faultLine,
				"message", "⚡ Fault session started successfully"
		);

		System.out.println("⚡ Started Fault Session: " + response);

		return ResponseEntity.ok(response);
	}



	@PostMapping("/verify/{line}")
	public ResponseEntity<FaultValidationResponse> verifyFault(
	        @PathVariable("line") String faultLine,
	        @RequestBody List<String> userActions) {

	    FaultValidationResponse response =
	            faultService.validateFaultResolution(faultLine, userActions);

	    return ResponseEntity.ok(response);
	}

}