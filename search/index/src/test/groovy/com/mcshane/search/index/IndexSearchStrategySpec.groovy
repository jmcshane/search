package com.mcshane.search.index

import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Query

import spock.lang.Specification

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.mcshane.search.index.domain.DocumentResult
import com.mcshane.search.index.domain.DocumentStore

class IndexSearchStrategySpec extends Specification {

	@Subject
	IndexSearchStrategy indexSearchStrategy
	
	@Collaborator
	Datastore datastore = Mock()
	
	Query<Object> createQuery = Mock()
	Query<Object> findQuery = Mock()
	def input = "text"
	
	def setup() {
		1*datastore.createQuery(DocumentStore.class) >> createQuery
		1*createQuery.filter('word ==',input) >> findQuery
	}
	
	def 'searchResult should contain datastore document'() {
		given:
		DocumentStore doc = new DocumentStore()
		doc.setResults([new DocumentResult("file1.txt",2), new DocumentResult("file2.txt",5)])
		when:
		1*findQuery.get() >> doc
		def result = indexSearchStrategy.executeSearch(input)
		then:
		result.getTime() > 0
		result.getCounts() == ["file1.txt":2, "file2.txt":5]		
	}
	
	def 'when search document is null result string has no entries'() {
		when:
		1*findQuery.get() >> null
		def result = indexSearchStrategy.executeSearch(input)
		then:
		result.getCounts() == [:]
	}
}
