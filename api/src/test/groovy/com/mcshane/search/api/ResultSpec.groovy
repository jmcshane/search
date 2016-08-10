package com.mcshane.search.api

import java.nio.file.Path

import spock.lang.Specification

class ResultSpec extends Specification {
	
	def result;
	
	def setup() {
		result = new Result()
	}

	def 'basic getter and setter test'() {
		when:
		result.setTime(1)
		then:
		1 == result.time
		[:] == result.getCounts()
	}
	
	def 'addToCounts should check whether the document exists and increment'() {
		given:
		result.setCounts([doc1:3,doc2:4])
		when:
		result.addToCounts(new LineSearchResult(2,'doc2'))
		result.addToCounts(new LineSearchResult(5,'doc3'))
		def map = result.getCounts()
		then:
		map['doc1'] == 3
		map['doc2'] == 6
		map['doc3'] == 5
	}
	
	def 'toString should order by occurrences'() {
		given:
		result.setTime 3
		result.setCounts([doc1:3,doc3:1,doc5:7])
		when:
		def str = result.toString()
		then:
		str == '''\
Search results :
	doc5 - 7 matches
	doc1 - 3 matches
	doc3 - 1 match
Elapsed time: 3 ms'''
	}
}
