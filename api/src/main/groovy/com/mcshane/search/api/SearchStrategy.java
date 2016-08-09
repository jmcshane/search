package com.mcshane.search.api;

import com.mcshane.search.api.domain.Result;

public interface SearchStrategy {

	Result executeSearch(String input);

}