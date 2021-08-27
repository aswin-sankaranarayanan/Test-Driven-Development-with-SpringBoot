package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EntityScan(basePackages = {"com"})
@ComponentScan(basePackages = {"com"})
public class Playground1Application {

	public static void main(String[] args) {
		SpringApplication.run(Playground1Application.class, args);
	}

}
