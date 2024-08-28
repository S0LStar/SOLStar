package com.common.solstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SolStarApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolStarApplication.class, args);
	}

}
