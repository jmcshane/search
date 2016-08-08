package com.mcshane.search.api.domain;

import java.nio.file.Path;
import java.util.Map;

public class SearchableDocument {
	
	private final Path filePath;
	
	private final Map<String,String> fileMetadata;
	
	public SearchableDocument(Path filePath, Map<String,String> metadata) {
		this.filePath = filePath;
		this.fileMetadata = metadata;
	}

	public Path getFilePath() {
		return this.filePath;
	}
	
	public Map<String,String> getFileMetadata() {
		return this.fileMetadata;
	}
	
	public String getFileName() {
		return this.filePath.getFileName().toString();
	}	
}