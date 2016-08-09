package com.mcshane.search.index;

import org.springframework.stereotype.Service;

import com.mcshane.search.api.SearchStrategy;
import com.mcshane.search.api.domain.Result;

@Service("index")
public class IndexSearchStrategy implements SearchStrategy {

	@Override
	public Result executeSearch(String input) {
		return null;
	}

}
