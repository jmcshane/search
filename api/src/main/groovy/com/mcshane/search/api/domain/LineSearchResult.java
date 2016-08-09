package com.mcshane.search.api.domain;

public class LineSearchResult {
	
	private final int occurrences;
	private final SearchableDocument doc;
	
	public LineSearchResult(int occurrences, SearchableDocument doc) {
		this.occurrences = occurrences;
		this.doc = doc;
	}

	public int getOccurrences() {
		return occurrences;
	}

	public SearchableDocument getDoc() {
		return doc;
	}
}
