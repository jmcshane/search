package com.mcshane.search.regex;

import java.io.File;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.mcshane.search.api.AbstractLineSearchStrategy;
import com.mcshane.search.api.LineSearchResult;
import com.mcshane.search.api.Result;
import com.mcshane.search.api.SearchStrategy;

@Service("regex")
public class RegexSearchStrategy extends AbstractLineSearchStrategy implements SearchStrategy {

	@Override
	public Result executeSearch(String input) {
		if (!input.contains("(?i)")) {
			input = "(?i)" + input;
		}
		final Pattern pattern = Pattern.compile(input);
		return resultGenerator(new BiFunction<String,String,LineSearchResult>(){
			@Override
			public LineSearchResult apply(String doc, String line) {
				int count = 0;
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					count++;
				}
				if (count == 0) {
					return null;
				}
				return new LineSearchResult(count,doc);
			}
		});
	}
}
