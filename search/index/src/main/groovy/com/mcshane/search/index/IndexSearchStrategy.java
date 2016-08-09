package com.mcshane.search.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mcshane.search.api.SearchStrategy;
import com.mcshane.search.api.domain.Result;
import com.mongodb.async.client.MongoDatabase;

@Service("index")
public class IndexSearchStrategy implements SearchStrategy {

	@Autowired
	private MongoDatabase mongoDatabase;
	
	@Override
	public Result executeSearch(String input) {
		return null;
	}

}
