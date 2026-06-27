package com.scada.controller;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scada.entities.CanvasData;
import com.scada.exception.BadRequestException;
import com.scada.repository.CanvasRepository;
import com.scada.service.MainService;

@RestController
@RequestMapping("api/")
public class MainController 
{
 
	@Autowired
	CanvasRepository cnvasRepo;
	
	@Autowired
	MainService mainService;

	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping("/save")
	public ResponseEntity<?> saveData(@RequestBody String canvasJson) {
		CanvasData cd = new CanvasData();
		cd.setCanvasData(canvasJson); // JSON string जो frontend ने भेजा है
		cnvasRepo.save(cd);
		return new ResponseEntity<>("Saved", HttpStatus.CREATED);
	}


	@GetMapping("/get")
	public List<CanvasData> getData()
	{
		return cnvasRepo.findAll();
	}

	@GetMapping("/test")
	public ResponseEntity<String> test() throws BadRequestException
	{
		System.out.println("Calling..........");
		return new ResponseEntity<String>("Testing ", HttpStatus.OK);
	}

}
