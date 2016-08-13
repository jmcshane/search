package com.mcshane.search.api;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Result {

	private Map<String, Integer> counts;
	private long time;
	
	public Result() {
		counts = new HashMap<>();
	}
	
	public void addToCounts(LineSearchResult lineResult) {
		if (counts.keySet().contains(lineResult.getDocName())) {
			counts.put(lineResult.getDocName(),
				counts.get(lineResult.getDocName()) + lineResult.getOccurrences());
		} else {
			counts.put(lineResult.getDocName(),lineResult.getOccurrences());
		}
	}
	
	public Map<String,Integer> getCounts() {
		return this.counts;
	}
	
	public void setCounts(Map<String,Integer> counts) {
		this.counts = counts;
	}
	
	public long getTime() {
		return this.time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public String toString() {
		List<String> reportResults = counts.entrySet().stream()
			.sorted(new Comparator<Entry<String,Integer>>() {
				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					if (o1.getValue() > o2.getValue()) {
						return -1;
					} else if (o1.getValue() < o2.getValue()) {
						return 1;
					} else {
						return o1.getKey().compareTo(o2.getKey());
					}
				}				
			})
			.<String>map(e -> "\t" + e.getKey() + " - " + e.getValue() + matchPluraization(e.getValue()))
			.collect(Collectors.toList());
		return "Search results :\n" + String.join("\n", reportResults)
			+ "\nElapsed time: " + time + " ms";
		
	}
	
	private String matchPluraization(int value) {
		if (value == 1) {
			return " match";
		} else {
			return " matches";
		}
	}
}
