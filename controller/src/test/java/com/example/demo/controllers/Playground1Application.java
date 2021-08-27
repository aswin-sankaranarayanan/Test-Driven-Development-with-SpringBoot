package com.example.demo.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com"})
public class Playground1Application {

	public static void main(String[] args) {
		SpringApplication.run(Playground1Application.class, args);
	}

}
