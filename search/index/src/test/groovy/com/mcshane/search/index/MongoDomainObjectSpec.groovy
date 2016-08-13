package com.mcshane.search.index

import spock.lang.Specification

import com.mcshane.search.index.domain.DocumentResult
import com.mcshane.search.index.domain.DocumentStore

class MongoDomainObjectSpec extends Specification {
	
	def 'domain methods return constructor and setter values'() {
		when:
		DocumentResult docResult = new DocumentResult()
		docResult.setFilename("setterfile.txt")
		docResult.setCount(12)
		DocumentResult resultConstructor = new DocumentResult("file.txt", 5)
		DocumentStore store = new DocumentStore()
		store.setWord("word")
		store.setResults([docResult, resultConstructor])
		DocumentStore storeConstructor = new DocumentStore("word", [docResult, resultConstructor])
		then:
		store.getWord() == storeConstructor.getWord()
		store.getResults() == storeConstructor.getResults()
		store.toString() == storeConstructor.toString()
	}
}
