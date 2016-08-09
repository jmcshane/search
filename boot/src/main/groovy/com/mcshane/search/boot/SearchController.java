package com.mcshane.search.boot;

import java.nio.file.Paths;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mcshane.search.api.SearchStrategy;
import com.mcshane.search.api.files.FileLoader;

@RestController
public class SearchController {
	
	@Autowired
	private FileLoader fileLoader;

	@Autowired
	private Map<String,SearchStrategy> searchStrategies;
	
	@RequestMapping(method=RequestMethod.GET, value="/search/{searchStrategy}", produces={"application/json"})
	public ResponseEntity<String> executeSearch(
			@PathVariable("searchStrategy") String searchStrategy, 
			@RequestParam("input") String input
		) {
		
		if (!searchStrategies.containsKey(searchStrategy)) {
			return new ResponseEntity<String>("Invalid entry.  Valid search strategies are " + searchStrategies.keySet().toString(),
				HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(searchStrategies.get(searchStrategy)
			.executeSearch(input).toString(), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/setHomeDirectory", produces={"application/json"})
	public ResponseEntity<String> setHomeDirectory(@RequestParam("path") String path) {
		fileLoader.setHomeDirectory(Paths.get(path));
		return new ResponseEntity<String>("Home directory set to " + path, HttpStatus.OK);
	}
}
