package com.example.fintechspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FintechspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(FintechspringApplication.class, args);
	}

}
