package com.mcshane.search.api

import java.util.function.Function

import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class FileLoaderSpec extends Specification {

	@Rule
	TemporaryFolder temporaryFolder
	
	def fileLoader
	def rootPath
	
	def setup() {
		fileLoader = new FileLoader()
		rootPath = temporaryFolder.getRoot()
		fileLoader.setHomeDirectory rootPath
	}
	
	def 'basic getter and setter test'() {
		expect:
		rootPath == fileLoader.getHomeDirectory()
	}
	
	def 'doFileScan should apply scanFunction argument to each line'() {
		given:
		File file = temporaryFolder.newFile('writeFile.txt')
		file.withWriter('UTF-8') { writer -> 
			writer.writeLine "This is the first line"
			writer.writeLine "This is a second line"
		}
		when:
		def output = fileLoader.doFileScan([apply: {a,b -> b}])
		then:
		output == ["This is the first line","This is a second line"]
		
	}
}
