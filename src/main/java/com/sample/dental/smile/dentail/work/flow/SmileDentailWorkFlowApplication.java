package com.sample.dental.smile.dentail.work.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sample.dental.smile.dentail.work.flow.authToken.JwtDetails;
import com.sample.dental.smile.dentail.work.flow.authToken.JwtDetailsHolder;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EntityScan(basePackages = "com.sample.dental.smile.dentail.work.flow.entity")
public class SmileDentailWorkFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmileDentailWorkFlowApplication.class, args);
	}

	@PostConstruct
	public void initialize() {
		JwtDetails jwtDetails = JwtDetailsHolder.getJwtDetails();
		if (jwtDetails != null) {
			System.out.println("Initialized with user: " + jwtDetails.getEmail());
		}
	}

}
