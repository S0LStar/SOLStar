package com.shinhan.solstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SolstarApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolstarApplication.class, args);
	}

}
