package com.scada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScadaBackendsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScadaBackendsApplication.class, args);
		
		System.out.println("Running ....");
	}

}
