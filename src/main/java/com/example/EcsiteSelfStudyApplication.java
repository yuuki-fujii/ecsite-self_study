package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EcsiteSelfStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcsiteSelfStudyApplication.class, args);
	}
	
	// RestTemplateをAutoWireするための記述
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
}
