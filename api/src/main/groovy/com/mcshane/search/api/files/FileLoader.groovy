package com.mcshane.search.api.files

import groovy.io.FileType

import java.nio.file.Path

class FileLoader {
	
	private File homeDirectory;
	
	def List doFileScan(String filename, Closure applyScanClosure) {
		def result = []
		homeDirectory.eachFileRecurse (FileType.FILES) { searchFile ->
			Scanner scanner = new Scanner(searchFile)
			while(scanner.hasNextLine()) {
				applyScanClosure(scanner.nextLine()) >> result
			}
		}
	}

	def void setHomeDirectory(Path directory) {
		this.homeDirectory = directory.toFile();
	}
}
