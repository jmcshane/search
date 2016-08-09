package com.mcshane.search.api.files

import groovy.io.FileType

import java.nio.file.Path
import java.nio.file.Paths

import java.util.List

import com.mcshane.search.api.domain.SearchableDocument

class FileLoader {

	private File homeDirectory
	def docs
	
	def List doFileScan(scanFunction) {
		def result = []
		docs.each { searchFile ->
			Scanner scanner = new Scanner(searchFile.getFilePath().toFile())
			while(scanner.hasNextLine()) {
				result << scanFunction.apply(searchFile,scanner.nextLine())
			}
		}
		result
	}

	def void setHomeDirectory(Path directory) {
		this.homeDirectory = directory.toFile();
		docs = []
		homeDirectory.eachFileRecurse (FileType.FILES) { locatedFile ->
			docs << new SearchableDocument(locatedFile.toPath(),[:])
		}
	}
	
	def File getHomeDirectory() {
		homeDirectory
	}
}
