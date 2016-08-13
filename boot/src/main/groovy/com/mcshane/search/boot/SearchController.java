package com.mcshane.search.boot;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mcshane.search.api.FileLoader;
import com.mcshane.search.api.SearchStrategy;
import com.mcshane.search.index.Indexer;

@RestController
public class SearchController {
	
	@Autowired
	private FileLoader fileLoader;
	
	@Autowired
	private Indexer indexer;

	@Autowired
	private Map<String,SearchStrategy> searchStrategies;
	
	@RequestMapping(method=RequestMethod.GET, value="/search/{searchStrategy}", produces={"application/json"})
	public ResponseEntity<String> executeSearch(
			@PathVariable("searchStrategy") String searchStrategy, 
			@RequestParam("input") String input
		) {
		
		if (!searchStrategies.containsKey(searchStrategy)) {
			return new ResponseEntity<String>("Invalid entry.  Valid search strategies are " + searchStrategies.keySet().toString(),
				HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(searchStrategies.get(searchStrategy)
			.executeSearch(input).toString(), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/setHomeDirectory", produces={"application/json"}, consumes={"application/x-www-form-urlencoded"})
	public ResponseEntity<String> setHomeDirectory(@RequestParam("path") String path) {
		File homeDir;
		try {
			homeDir = Paths.get(path).toFile();
		} catch(Exception e) {
			return new ResponseEntity<String>("Invalid directory", HttpStatus.NOT_FOUND);
		}
		fileLoader.setHomeDirectory(homeDir);
		return new ResponseEntity<String>("Home directory set to " + path, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/indexFiles", produces={"application/json"})
	public ResponseEntity<String> indexFiles() {
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				indexer.indexFiles();				
			}
		};
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.submit(runnable);
		return new ResponseEntity<String>("Indexing started", HttpStatus.ACCEPTED);
	}
}
