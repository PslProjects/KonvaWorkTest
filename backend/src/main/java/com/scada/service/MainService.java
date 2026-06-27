package com.scada.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MainService 
{
     
	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	public String saveCircuitData(List<Map<String,Object>> list)
	{
		//For Now Truncate the data from table
		 String t="TRUNCATE TABLE canvas_data";
		 jdbcTemplate.update(t);
		
		
		System.out.println("Printing the JDBC Template ....");
		System.out.println(jdbcTemplate);
		
		String s="INSERT INTO canvas_data (canvas_data) VALUE(?)";
		
		for(Map<String,Object> m : list)
		{
			jdbcTemplate.update(s,m.toString());
		}
		
		return null;
	}
	
}
