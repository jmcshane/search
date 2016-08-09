package com.mcshane.search.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mcshane.search.api.files.FileLoader;
import com.mongodb.async.client.MongoDatabase;

@Service
public class Indexer {
	
	private static final String COLL_NAME = "searchIndex";

	@Autowired
	private FileLoader fileLoader;
	
	@Autowired
	private MongoDatabase mongoDatabase;
	
	public void indexFiles() {

	}
}
