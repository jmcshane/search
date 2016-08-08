package com.mcshane.search.api.domain

import java.nio.file.Path

import spock.lang.Specification

class SearchableDocumentSpec extends Specification {

	void 'constructor sets values that are returned by getters'() {
		when:
		def path = Mock(Path)
		def namePath = Mock(Path)
		1*path.getFileName() >> namePath
		1*namePath.toString() >> "filename"
		def metadata = [a:'b']
		def doc = new SearchableDocument(path, metadata)
		then:
		path == doc.getFilePath()
		metadata == doc.getFileMetadata()
		"filename" == doc.getFileName()
	}
}
