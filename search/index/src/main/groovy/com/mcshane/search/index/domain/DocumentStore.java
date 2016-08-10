package com.mcshane.search.index.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

@Entity("documentStore")
public class DocumentStore {

	@Id
	private ObjectId id;
	
	@Indexed(options = @IndexOptions(unique=true))
	private String word;
	
	private List<DocumentResult> results;
	
	public DocumentStore() {
		//no-args
	}
	
	public DocumentStore(String word, List<DocumentResult> results) {
		this.word = word;
		this.results = results;
	}
	
	public Map<String,Integer> toCountsMap() {
		return results.stream()
			.collect(Collectors.toMap(DocumentResult::getFilename, DocumentResult::getCount));
	}
	
	public ObjectId getId() {
		return this.id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public List<DocumentResult> getResults() {
		return results;
	}

	public void setResults(List<DocumentResult> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return String.format("DocumentStore [id=%s, word=%s, results=%s]", id, word, results);
	}
}
