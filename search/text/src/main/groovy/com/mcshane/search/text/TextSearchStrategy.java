package com.mcshane.search.text;

import java.util.function.BiFunction;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.mcshane.search.api.AbstractLineSearchStrategy;
import com.mcshane.search.api.SearchStrategy;
import com.mcshane.search.api.domain.LineSearchResult;
import com.mcshane.search.api.domain.Result;
import com.mcshane.search.api.domain.SearchableDocument;

@Service("text")
public class TextSearchStrategy extends AbstractLineSearchStrategy implements SearchStrategy {

	@Override
	public Result executeSearch(String input) {
		return resultGenerator(new BiFunction<SearchableDocument,String,LineSearchResult>(){
				
			@Override
			public LineSearchResult apply(SearchableDocument doc, String line) {
				int count = StringUtils.countMatches(line.toLowerCase(), input.toLowerCase());
				if (count == 0) {
					return null;
				}
				return new LineSearchResult(count,doc);
			}
		});
	}
}
