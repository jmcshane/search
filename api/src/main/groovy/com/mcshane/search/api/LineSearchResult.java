package com.mcshane.search.api;

import java.io.File;

public class LineSearchResult {
	
	private final int occurrences;
	private final File doc;
	
	public LineSearchResult(int occurrences, File doc) {
		this.occurrences = occurrences;
		this.doc = doc;
	}

	public int getOccurrences() {
		return occurrences;
	}

	public File getDoc() {
		return doc;
	}
}
