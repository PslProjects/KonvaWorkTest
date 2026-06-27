package com.scada.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor
{

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> handleBadRequestException(BadRequestException ex)
	{
		Map<String,Object> map=new LinkedHashMap<>();
		map.put("timestamp",LocalDateTime.now());
		map.put("message", ex.getMessage());
		ex.printStackTrace();
	    return new ResponseEntity<Object>(map,HttpStatus.BAD_REQUEST);
	}
	
  
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex)
	{
		Map<String,Object> map=new LinkedHashMap<>();
		map.put("timestamp ", LocalDateTime.now());
		map.put("message", ex.getMessage());
		ex.printStackTrace();
		return new ResponseEntity<Object>(map,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}


















