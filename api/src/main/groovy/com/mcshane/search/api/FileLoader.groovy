package com.mcshane.search.api

import groovy.io.FileType
import java.io.File
import java.util.List
import org.springframework.stereotype.Service

@Service
class FileLoader {

	private File homeDirectory
	
	def List doFileScan(scanFunction) {
		def result = []
		homeDirectory.eachFileRecurse (FileType.FILES) { locatedFile ->
			Scanner scanner = new Scanner(locatedFile)
			while(scanner.hasNextLine()) {
				result << scanFunction.apply(locatedFile.getName(),scanner.nextLine())
			}
		}
		result
	}

	def void setHomeDirectory(File directory) {
		this.homeDirectory = directory
	}
	
	def File getHomeDirectory() {
		homeDirectory
	}
}
