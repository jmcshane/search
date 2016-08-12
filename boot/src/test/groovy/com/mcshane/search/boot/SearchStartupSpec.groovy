package com.mcshane.search.boot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.ActiveProfiles

import spock.lang.Specification

@ContextConfiguration(loader = SpringBootContextLoader.class, classes = Application.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SearchStartupSpec extends Specification {

	@Autowired
	SearchController searchController
  
	public void testthetextSearch() {
		expect:
	    searchController != null
	}
	
}
