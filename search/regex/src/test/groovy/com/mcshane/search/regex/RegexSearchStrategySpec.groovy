package com.mcshane.search.regex

import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

import com.mcshane.search.api.files.FileLoader

class RegexSearchStrategySpec extends Specification {
	@Rule
	TemporaryFolder temporaryFolder
	
	RegexSearchStrategy regexSearchStrategy = new RegexSearchStrategy()
	
	FileLoader loader = new FileLoader()
	
	def setup() {
		regexSearchStrategy.fileLoader = loader
		def file = temporaryFolder.newFile('test.txt')
		loader.setHomeDirectory(temporaryFolder.getRoot().toPath())
		file.withWriter ('UTF-8') { writer ->
			writer.writeLine "This is the first line of text"
			writer.writeLine "This is a second line"
		}
	}
	
	def 'regexSearchStrategy ignores case of input'() {
		when:
		def result = regexSearchStrategy.executeSearch("Of").getCounts()
		then:
		def entrySet = result.keySet()
		1 == entrySet.size()
		1 == result.get(entrySet.first())
	}
	
	def 'regexSearchStrategy works if case insensitivity is provided in the input'() {
		given:
		def searchStr = "(?i)" + input
		when:
		def result = regexSearchStrategy.executeSearch(searchStr).getCounts()
		then:
		def entrySet = result.keySet()
		1 == entrySet.size()
		count == result.get(entrySet.first())
		where:
		input               | count
		"IS"                | 4
		"lIne"              | 2
	}
	
	def 'regexSearchStrategy provides empty result if no matches'() {
		when:
		def result = regexSearchStrategy.executeSearch("failures").getCounts()
		then:
		0 == result.size()
	}
}
