package com.mcshane.search.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.mcshane.search")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
