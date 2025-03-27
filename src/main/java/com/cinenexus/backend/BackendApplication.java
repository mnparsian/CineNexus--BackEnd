package com.cinenexus.backend;

import com.cinenexus.backend.model.media.MediaType;
import com.cinenexus.backend.service.TMDBService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = "com.cinenexus.backend")
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(TMDBService tmdbService) {
		return args -> {
			System.out.println("⏳ Fetching and saving all media...");
			tmdbService.fetchAndSaveAllMedia();
			System.out.println("✅ Fetch and save completed!");
		};
	}


}
