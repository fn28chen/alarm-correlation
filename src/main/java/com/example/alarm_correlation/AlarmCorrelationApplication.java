package com.example.alarm_correlation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AlarmCorrelationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmCorrelationApplication.class, args);
	}

	@GetMapping
	public String hello() {
		return "Hello World";
	}
}
