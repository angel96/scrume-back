package com.spring.Scrume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "com.spring")
@EnableJpaRepositories(value = { "com.spring.Security", "com.spring.Repository" })
@EntityScan(value = { "com.spring.Model" })
@SpringBootApplication
public class ScrumeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrumeApplication.class, args);
	}

}
