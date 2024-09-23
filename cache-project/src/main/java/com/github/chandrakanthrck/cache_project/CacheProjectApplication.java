package com.github.chandrakanthrck.cache_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.github.chandrakanthrck.cache_project.repository")
public class CacheProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(CacheProjectApplication.class, args);
	}
}

