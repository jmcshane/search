package com.mcshane.search.api;

import java.util.List;

import com.mcshane.search.api.domain.SearchableDocument;

public interface Result {

	List<SearchableDocument> getDocuments();
	double getSearchTime();

}