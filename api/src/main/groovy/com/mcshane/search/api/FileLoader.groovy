package com.mcshane.search.api

import groovy.io.FileType

import java.nio.file.Path

import org.springframework.stereotype.Service

@Service
class FileLoader {

	private File homeDirectory
	
	def List doFileScan(scanFunction) {
		def result = []
		homeDirectory.eachFileRecurse (FileType.FILES) { locatedFile ->
			Scanner scanner = new Scanner(locatedFile)
			while(scanner.hasNextLine()) {
				result << scanFunction.apply(locatedFile,scanner.nextLine())
			}
		}
		result
	}

	def void setHomeDirectory(Path directory) {
		this.homeDirectory = directory.toFile();
	}
	
	def File getHomeDirectory() {
		homeDirectory
	}
}
