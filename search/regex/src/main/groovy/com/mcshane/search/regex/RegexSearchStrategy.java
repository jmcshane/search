package com.mcshane.search.regex;

import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.mcshane.search.api.AbstractLineSearchStrategy;
import com.mcshane.search.api.SearchStrategy;
import com.mcshane.search.api.domain.LineSearchResult;
import com.mcshane.search.api.domain.Result;
import com.mcshane.search.api.domain.SearchableDocument;

@Service("regex")
public class RegexSearchStrategy extends AbstractLineSearchStrategy implements SearchStrategy {

	@Override
	public Result executeSearch(String input) {
		if (!input.contains("(?i)")) {
			input = "(?i)" + input;
		}
		final Pattern pattern = Pattern.compile(input);
		return resultGenerator(new BiFunction<SearchableDocument,String,LineSearchResult>(){
			@Override
			public LineSearchResult apply(SearchableDocument doc, String line) {
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
