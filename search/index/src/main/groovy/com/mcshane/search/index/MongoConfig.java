package com.mcshane.search.index;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.mongodb.MongoClient;

@Configuration
public class MongoConfig {
	
	@Value("${mongodb.url:localhost:27017}")
	private String mongoUrl;
	
	@Value("${mongodb.dbname:test}")
	private String dbName;
	
	@Value("${mongodb.collection:index}")
	private String collName;
	
	@Bean
	public Datastore mongoDatabase() {
		Morphia morphia = new Morphia();
		morphia.mapPackage("com.mcshane.search.index.domain");
		final Datastore datastore = morphia.createDatastore(new MongoClient(mongoUrl), "morphia_index");
		datastore.ensureIndexes();
		return datastore;
	}

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }	
}
