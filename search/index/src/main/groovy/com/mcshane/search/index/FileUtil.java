package com.mcshane.search.index;

public class FileUtil {

	private FileUtil() {
		//utility
	}
	
	public static String encode(String fileName) {
		int index = fileName.lastIndexOf('.');
		return fileName.substring(0, index) + "\uff0e" + fileName.substring(index+1);
	}
	
	public static String decode(String fileName) {
		return fileName.replace('\uff0e', '.');
	}
}
