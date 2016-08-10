package com.mcshane.search.index

import org.mongodb.morphia.Datastore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.mcshane.search.api.Result
import com.mcshane.search.api.SearchStrategy
import com.mcshane.search.index.domain.DocumentStore

@Service('index')
class IndexSearchStrategy implements SearchStrategy {

	@Autowired
	Datastore datastore;
	
	@Override
	public Result executeSearch(String input) {
		def searchResult = new Result()
		long start = System.currentTimeMillis();
		searchResult.setCounts datastore.createQuery(DocumentStore.class).filter('word ==',input).get().toCountsMap()	
		searchResult.setTime System.currentTimeMillis() - start
		return searchResult
	}

}
