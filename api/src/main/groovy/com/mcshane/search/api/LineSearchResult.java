package com.mcshane.search.api;

public class LineSearchResult {
	
	private final int occurrences;
	private final String docName;
	
	public LineSearchResult(int occurrences, String docName) {
		this.occurrences = occurrences;
		this.docName = docName;
	}

	public int getOccurrences() {
		return occurrences;
	}

	public String getDocName() {
		return docName;
	}
}
