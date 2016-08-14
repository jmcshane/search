package com.mcshane.search.boot;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.mcshane.search.index.Indexer;

@SpringBootApplication
@ComponentScan(basePackages="com.mcshane.search")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Value("${home.directory}")
	private String homeDirectory;
	
	@Autowired
	SearchController searchController;
	
	@Autowired
	Indexer indexer;
	
	@PostConstruct
	private void init() {
		searchController.setHomeDirectory(homeDirectory);
		indexer.indexFiles();
	}
}
