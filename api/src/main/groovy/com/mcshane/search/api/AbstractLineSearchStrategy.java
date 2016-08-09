package com.mcshane.search.api;

import java.util.Objects;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;

import com.mcshane.search.api.domain.LineSearchResult;
import com.mcshane.search.api.domain.Result;
import com.mcshane.search.api.domain.SearchableDocument;
import com.mcshane.search.api.files.FileLoader;

public abstract class AbstractLineSearchStrategy implements SearchStrategy{

	@Autowired
	protected FileLoader fileLoader;
	
	@SuppressWarnings("unchecked")
	public Result resultGenerator(BiFunction<SearchableDocument, String, LineSearchResult> mapper) {
		Result result = new Result();
		long startTime = System.currentTimeMillis();
		fileLoader
			.doFileScan(mapper).stream()
	        .filter(Objects::nonNull)
			.forEach(r -> result.addToCounts((LineSearchResult)r));
		long endTime = System.currentTimeMillis();
		result.setTime(endTime - startTime);
		return result;
		
	}
}
