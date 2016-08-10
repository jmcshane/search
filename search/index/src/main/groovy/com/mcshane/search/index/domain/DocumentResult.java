package com.mcshane.search.index.domain;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class DocumentResult {

	private String filename;
	
	private int count;
	
	public DocumentResult() {
		//no-arg constructor
	}
	
	public DocumentResult(String filename, int count) {
		this.filename = filename;
		this.count = count;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return String.format("DocumentResult [filename=%s, count=%s]", filename, count);
	}
}
