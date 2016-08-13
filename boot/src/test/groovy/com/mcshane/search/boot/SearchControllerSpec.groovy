package com.mcshane.search.boot

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.springframework.http.HttpStatus

import spock.lang.Specification

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.mcshane.search.api.FileLoader
import com.mcshane.search.api.Result
import com.mcshane.search.api.SearchStrategy
import com.mcshane.search.index.Indexer

class SearchControllerSpec extends Specification {
	
	@Subject
	SearchController searchController
	
	@Collaborator
	FileLoader fileLoader = Mock()
	
	@Collaborator
	Indexer indexer = Mock()
	
	SearchStrategy search = Mock()
	
	@Rule
	TemporaryFolder temporaryFolder
	
	def setup() {
		searchController.searchStrategies = [search:search]
	}

	def 'when searchStrategy is not present, 404 status is returned'() {
		when:
		def resp = searchController.executeSearch("searching","text")
		then:
		HttpStatus.NOT_FOUND == resp.getStatusCode()
	}
	
	def 'when searchStrategy is present, returns Result string'() {
		when:
		Result result = new Result()
		result.setTime(4)
		result.setCounts([file1:5])
		1*search.executeSearch("text") >> result
		def resp = searchController.executeSearch("search","text")
		then:
		HttpStatus.OK == resp.getStatusCode()
		'''\
Search results :
	file1 - 5 matches
Elapsed time: 4 ms''' == resp.getBody()
	}
	
	def 'setHomeDirectory delegates to fileLoader'() {
		given:
		def path = temporaryFolder.getRoot().getAbsolutePath()
		when:
		def resp = searchController.setHomeDirectory(path)
		then:
		HttpStatus.OK == resp.getStatusCode()
		1*fileLoader.setHomeDirectory(temporaryFolder.getRoot())
	}
	
	def 'setHomeDirectory returns 404 status on invalidDirectory'() {
		when:
		def resp = searchController.setHomeDirectory('!@#$%^&*')
		then:
		HttpStatus.NOT_FOUND == resp.getStatusCode()
	}
	
	def 'indexFiles delegates to indexer and returns 202'() {
		when:
		def resp = searchController.indexFiles()
		Thread.sleep(50)
		then:
		1*indexer.indexFiles()
		HttpStatus.ACCEPTED == resp.getStatusCode()
	}
}
