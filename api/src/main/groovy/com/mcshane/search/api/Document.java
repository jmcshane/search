package com.mcshane.search.api;

import java.nio.file.Path;
import java.util.Map;

public interface Document {

	Path getFilePath();
	Map<String,String> getFileMetadata();

}