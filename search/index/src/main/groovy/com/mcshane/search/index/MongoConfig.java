package com.mcshane.search.index;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoDatabase;

@Configuration
public class MongoConfig {
	
	@Value("${mongodb.url:mongodb://localhost}")
	private String mongoUrl;
	
	@Value("${mongodb.dbname:test}")
	private String dbName;
	
	@Bean
	public MongoDatabase mongoDatabase() {
		MongoClient mongoClient = MongoClients.create(mongoUrl);
		return mongoClient.getDatabase(dbName);
	}

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }	
}
