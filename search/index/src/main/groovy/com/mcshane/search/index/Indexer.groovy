package com.mcshane.search.index

import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.mcshane.search.api.files.FileLoader
import com.mongodb.async.client.MongoCollection
import com.mongodb.async.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions

@Service
public class Indexer {
	
	private static final String COLL_NAME = "searchIndex";

	@Autowired
	private FileLoader fileLoader;
	
	@Autowired
	private MongoDatabase mongoDatabase;
	
	def void indexFiles() {
		MongoCollection<Document> collection = mongoDatabase.getCollection("test");
		fileLoader.docs.each { searchableDoc ->
			String text = searchableDoc.filePath.toFile().text
			text = text.replaceAll("[^A-Za-z]","").toLowerCase()
			text.split(" ").inject([:]) { result,i ->
				if (result.containsKey(i)) {
					result << [(i):++result.get(i)]
				} else {
					result << [(i):1]
				}
			}
			result.each { key,value ->
				collection.replaceOne(new Document("word" : key),
					new Document("files.${searchableDoc.getFileName()}", value),
					(new UpdateOptions()).upsert(true));
			}
		}
	}
}
