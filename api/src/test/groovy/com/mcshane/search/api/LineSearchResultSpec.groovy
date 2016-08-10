package com.mcshane.search.api

import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class LineSearchResultSpec extends Specification {

	def 'basic test of getters and constructor'() {
		when:
		LineSearchResult lsr = new LineSearchResult(5, 'filename.txt')
		then:
		5 == lsr.getOccurrences()
		'filename.txt' == lsr.getDocName()
	}
}
