package com.mcshane.search.api.files;

import java.util.Map;

import com.mcshane.search.api.domain.SearchableDocument;

public interface ScanResult {
	Map<SearchableDocument,Integer> getMatches();
}
