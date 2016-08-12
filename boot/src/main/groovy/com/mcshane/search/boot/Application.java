package com.mcshane.search.boot;

import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.mcshane.search.api.FileLoader;
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
	FileLoader fileLoader;
	
	@Autowired
	Indexer indexer;
	
	@PostConstruct
	private void init() {
		fileLoader.setHomeDirectory(Paths.get(homeDirectory).toFile());
		indexer.indexFiles();
	}
}
