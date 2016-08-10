package com.mcshane.search.text;

import java.io.File;
import java.util.function.BiFunction;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.mcshane.search.api.AbstractLineSearchStrategy;
import com.mcshane.search.api.LineSearchResult;
import com.mcshane.search.api.Result;
import com.mcshane.search.api.SearchStrategy;

@Service("text")
public class TextSearchStrategy extends AbstractLineSearchStrategy implements SearchStrategy {

	@Override
	public Result executeSearch(String input) {
		return resultGenerator(new BiFunction<File,String,LineSearchResult>(){
				
			@Override
			public LineSearchResult apply(File doc, String line) {
				int count = StringUtils.countMatches(line.toLowerCase(), input.toLowerCase());
				if (count == 0) {
					return null;
				}
				return new LineSearchResult(count,doc);
			}
		});
	}
}
