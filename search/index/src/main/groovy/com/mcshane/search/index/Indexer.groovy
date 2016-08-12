package com.mcshane.search.index

import groovy.io.FileType

import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Query
import org.mongodb.morphia.query.UpdateOperations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

import com.mcshane.search.api.FileLoader
import com.mcshane.search.index.domain.DocumentResult
import com.mcshane.search.index.domain.DocumentStore

@Service
class Indexer {
	def keys = [] as Set
	@Autowired
	private FileLoader fileLoader;
	
	@Autowired
	private Datastore datastore;
		
	def void indexFiles() {
		datastore.delete(datastore.createQuery(DocumentStore.class));
		
		fileLoader.homeDirectory.eachFileRecurse (FileType.FILES) { locatedFile ->
			String text = locatedFile.text
			text = text.replaceAll('[^A-Za-z\\s]','').toLowerCase()
			def reduction = text.split('\\s').findAll { !StringUtils.isEmpty(it)}
				.inject([:]) { result,i ->
				
				if (result.containsKey(i)) {
					result << [(i):++result.get(i)]
				} else {
					result << [(i):1]
				}
			}
			reduction.each { key,value ->
				Query<DocumentStore> baseQuery = datastore.createQuery(DocumentStore.class).filter('word ==', key);
				if (keys.contains(key) || baseQuery.countAll() == 1) {
					UpdateOperations ops = datastore.createUpdateOperations(DocumentStore.class).add('results', new DocumentResult(locatedFile.getName(), value));
					datastore.update(baseQuery, ops);
				} else {
					DocumentStore documentStore = new DocumentStore(key, [new DocumentResult(locatedFile.getName(), value)])
					datastore.save(documentStore)
					keys << key
				}
			}
		}
	}
}
