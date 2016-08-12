package com.mcshane.search;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.springframework.web.client.RestTemplate;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;

@AxisRange(min = 0, max = 0.1)
@BenchmarkMethodChart(filePrefix = "benchmark-lists")
public class SearchIntegrationTest {
	
	private static final RestTemplate restTemplate = new RestTemplate();
	
	private static final String SEARCH_URL = "http://localhost:9999/";
	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();
	
	static Process process = null;
	static List<String> words = Collections.synchronizedList(new ArrayList<>());
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void initTest() throws IOException, InterruptedException {
		ExecutorService wordExecutor = Executors.newFixedThreadPool(5);
		List<CompletableFuture<Boolean>> wordRetrievalThread = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			wordRetrievalThread.add(getRandomWords(wordExecutor));
		}
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "boot.jar");
		pb.directory(new File("build/libs"));
		File log = new File("build/boot.log");
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.to(log));
		process = pb.start();
		int i = 0;
		
		while (i < 200) {
			try {
				Map<String,Object> health = (Map<String,Object>) restTemplate
					.getForObject(SEARCH_URL + "/health", Map.class);
				if ("UP".equals(health.get("status"))) {
					break;
				}
				Thread.sleep(100);
				i++;
			} catch(Exception e) {
				//keep going
			}
		}
		boolean keepGettingWords = true;
		while (keepGettingWords) {
			keepGettingWords = wordRetrievalThread.stream()
				.reduce(false, (res, fut) -> res || ! fut.isDone(), (a,b) -> a || b);
		}
	}
	
	static CompletableFuture<Boolean> getRandomWords(ExecutorService executor) {
		 return CompletableFuture.supplyAsync(() -> {
			 int i = 0;
				while (i < 1000) {
					String word = restTemplate
						.getForObject("http://randomword.setgetgo.com/get.php", String.class);
					words.add(word);
					i++;
				}			    
				return true;
			}, executor);
	}
	
	@AfterClass
	public static void closeProcess() throws IOException {
		process.destroyForcibly();
	}

	@Test
	@BenchmarkOptions(callgc = false, benchmarkRounds = 20, warmupRounds = 3)
	public void textIntegrationTest() {
		RestTemplate restTemplate = new RestTemplate();
		Integer index = ThreadLocalRandom.current().nextInt(0, words.size());
		String output = restTemplate.getForObject("http://localhost:9999/search/text?input=" + words.get(index)
			, String.class);
		assertTrue(output != null);
		assertTrue(!StringUtils.isEmpty(output));
	}
	
	@Test
	@BenchmarkOptions(callgc = false, benchmarkRounds = 20000, warmupRounds = 3)
	public void regexIntegrationTest() {
		RestTemplate restTemplate = new RestTemplate();
		Integer index = ThreadLocalRandom.current().nextInt(0, words.size());
		String output = restTemplate.getForObject("http://localhost:9999/search/regex?input=" + words.get(index)
			, String.class);
		assertTrue(output != null);
		assertTrue(!StringUtils.isEmpty(output));
	}
	
	@Test
	@BenchmarkOptions(callgc = false, benchmarkRounds = 20000, warmupRounds = 3)
	public void indexIntegrationTest() {
		RestTemplate restTemplate = new RestTemplate();
		Integer index = ThreadLocalRandom.current().nextInt(0, words.size());
		String output = restTemplate.getForObject("http://localhost:9999/search/index?input=" + words.get(index)
			, String.class);
		assertTrue(output != null);
		assertTrue(!StringUtils.isEmpty(output));
	}
}
