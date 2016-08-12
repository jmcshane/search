package com.mcshane.search.index

import org.bson.BsonString
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Query
import org.mongodb.morphia.query.UpdateOperations

import spock.lang.Specification

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.mcshane.search.api.FileLoader
import com.mcshane.search.index.domain.DocumentStore

class IndexerSpec extends Specification {
	
	@Rule
	TemporaryFolder temporaryFolder

	@Subject
	Indexer indexer
	
	FileLoader fileLoader = new FileLoader()
	
	@Collaborator
	Datastore datastore = Mock()
	
	def setup() {
		indexer.fileLoader = fileLoader
		fileLoader.setHomeDirectory(temporaryFolder.getRoot())
		def file = temporaryFolder.newFile('test.txt')
		file.withWriter ('UTF-8') { writer ->
			writer.writeLine 'Two words'
			writer.writeLine 'Words twice'
		}
	}
	
	def void 'indexer algorithm should replace docs in the mongo collection'() {
		given:
		def checkSearch = { doc,word -> 
			doc.get('word') == new BsonString(word)
		}
		def checkInput = { doc, count -> 
			doc.getInteger('files.test\uff0etxt') == count
		}
		indexer.keys << 'words'
		Query query = Mock()
		Query q1 = Mock()
		Query q2 = Mock()
		Query q3 = Mock()
		UpdateOperations update = Mock()
		4*datastore.createQuery(DocumentStore.class) >> query
		1*query.filter('word ==','two') >> q1
		1*query.filter('word ==','twice') >> q2
		1*query.filter('word ==','words') >> q3
		1*q1.countAll() >> 1
		1*q2.countAll() >> 0
		2*datastore.createUpdateOperations(DocumentStore.class) >> update
		when:
		indexer.indexFiles()
		then:
		1==1
		1*datastore.save({it.getWord() == 'twice' && it.getResults().size() == 1 && it.getResults()[0].getCount() == 1})
		1*update.add('results',{it.getCount()==1})
		1*update.add('results',{it.getCount()==2})
		1*datastore.update(q1, _);
		1*datastore.update(q3,_);	
	}
}
