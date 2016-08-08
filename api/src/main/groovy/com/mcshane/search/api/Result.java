package com.mcshane.search.api;

import java.util.List;

public interface Result {

	List<Document> getDocuments();
	double getSearchTime();

}