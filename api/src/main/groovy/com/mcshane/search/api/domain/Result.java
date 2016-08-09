package com.mcshane.search.api.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Result {

	private Map<SearchableDocument, Integer> counts;
	private long time;
	
	public Result() {
		counts = new HashMap<>();
	}
	
	public void addToCounts(LineSearchResult lineResult) {
		if (counts.keySet().contains(lineResult.getDoc())) {
			counts.put(lineResult.getDoc(),
				counts.get(lineResult.getDoc()) + lineResult.getOccurrences());
		} else {
			counts.put(lineResult.getDoc(),lineResult.getOccurrences());
		}
	}
	
	public Map<SearchableDocument,Integer> getCounts() {
		return this.counts;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public String toString() {
		List<String> reportResults = counts.entrySet().stream()
			.<String>map(e -> "\t" + e.getKey().getFileName() + " - " + e.getValue() + " matches")
			.collect(Collectors.toList());
		return "Search results :\n" + String.join("\n", reportResults)
			+ "\nElapsed time: " + time + " ms";
		
	}
}
