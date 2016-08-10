package com.mcshane.search.api;

import java.util.Objects;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractLineSearchStrategy implements SearchStrategy{

	@Autowired
	protected FileLoader fileLoader;
	
	@SuppressWarnings("unchecked")
	public Result resultGenerator(BiFunction<String, String, LineSearchResult> mapper) {
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
