package com.mcshane.search.text

import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

import com.mcshane.search.api.FileLoader

class TextSearchStrategySpec extends Specification {

	@Rule
	TemporaryFolder temporaryFolder
	
	TextSearchStrategy textSearchStrategy = new TextSearchStrategy()
	
	FileLoader loader = new FileLoader()
	
	def setup() {
		textSearchStrategy.fileLoader = loader
		loader.setHomeDirectory(temporaryFolder.getRoot().toPath())
		def file = temporaryFolder.newFile('test.txt')
		file.withWriter ('UTF-8') { writer ->
			writer.writeLine "This is the first line of text"
			writer.writeLine "This is a second line"
		}
	}
	
	def 'textSearchStrategy should identify all exact matches in lines of file'() {
		when:
		def result = textSearchStrategy.executeSearch("this").getCounts()
		then:
		1 == result.size()
		def entrySet = result.keySet()
		2 == result.get(entrySet.first())
	}
	
	def 'textSearchStrategy returns empty result when no matches'() {
		when:
		def result = textSearchStrategy.executeSearch("quantum").getCounts()
		then:
		0 == result.size()
	}
}
