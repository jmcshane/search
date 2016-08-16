package com.mcshane.search;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;

@AxisRange(min = 0, max = 0.2)
@BenchmarkMethodChart(filePrefix = "build/benchmark/search-benchmark")
public class SearchIntegrationTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchIntegrationTest.class);

	private static final RestTemplate restTemplate = new RestTemplate();
	
	private static String SEARCH_URL;
	
	private static AtomicInteger textCounter = new AtomicInteger();
	private static AtomicInteger indexCounter = new AtomicInteger();
	private static AtomicInteger regexCounter = new AtomicInteger();
	
	private static int totalSearchTerms;
	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();
	
	static Process process = null;
	static List<String> words = Collections.synchronizedList(new ArrayList<>());
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void initTest() throws IOException, InterruptedException {
		SEARCH_URL = "http://127.0.0.1:9999/";
		ExecutorService wordExecutor = Executors.newFixedThreadPool(5);
		List<CompletableFuture<Boolean>> wordRetrievalThread = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			wordRetrievalThread.add(getRandomWords(wordExecutor));
		}
		String testDir = System.getProperty("user.dir") + "/src/integration-test/resources/search";
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "boot.jar",
			"--home.directory=" + testDir).inheritIO();
		pb.directory(new File("build/libs"));
		/*File log = new File("build/boot.log");
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.to(log));*/
		logger.info("Starting process");
		process = pb.start();
		int i = 0;
		boolean up = false;
		while (i < 10) {
			try {
				Map<String,Object> health = (Map<String,Object>) restTemplate
					.getForObject(SEARCH_URL + "/health", Map.class);
				if ("UP".equals(health.get("status"))) {
					up = true;
					break;
				}
			} catch(Exception e) {
				//keep going
			}
			Thread.sleep(4000);
			i++;
		}
		if (!up) {
			throw new RuntimeException("Fail to initialize boot.jar");
		}
		boolean keepGettingWords = true;
		while (keepGettingWords) {
			keepGettingWords = wordRetrievalThread.stream()
				.reduce(false, (res, fut) -> res || ! fut.isDone(), (a,b) -> a || b);
		}
		logger.info("Ready to begin");
		totalSearchTerms = words.size();
	}
	
	static CompletableFuture<Boolean> getRandomWords(ExecutorService executor) {
		 return CompletableFuture.supplyAsync(() -> {
			 int i = 0;
				while (i < 100) {
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
	@BenchmarkOptions(callgc = false, benchmarkRounds = 2000000, warmupRounds = 3, concurrency = 100)
	public void textIntegrationTest() {
		RestTemplate restTemplate = new RestTemplate();
		String output = restTemplate.getForObject(SEARCH_URL + "search/text?input=" 
			+ words.get(textCounter.getAndIncrement() % totalSearchTerms)
			, String.class);
		assertTrue(output != null);
		assertTrue(!StringUtils.isEmpty(output));
		int text = textCounter.get();
		if (text % 1000 == 0) {
			logger.info("Text at {}", text);
		}
	}
	
	@Test
	@BenchmarkOptions(callgc = false, benchmarkRounds = 2000000, warmupRounds = 3, concurrency = 100)
	public void regexIntegrationTest() {
		RestTemplate restTemplate = new RestTemplate();
		String output = restTemplate.getForObject(SEARCH_URL + "search/regex?input=" 
			+ words.get(regexCounter.getAndIncrement() % totalSearchTerms)
			, String.class);
		assertTrue(output != null);
		assertTrue(!StringUtils.isEmpty(output));
		int text = regexCounter.get();
		if (text % 1000 == 0) {
			logger.info("Text at {}", text);
		}
	}
	
	@Test
	@BenchmarkOptions(callgc = false, benchmarkRounds = 2000000, warmupRounds = 3, concurrency = 100)
	public void indexIntegrationTest() {
		RestTemplate restTemplate = new RestTemplate();
		String output = restTemplate.getForObject(SEARCH_URL + "search/index?input=" 
			+ words.get(indexCounter.getAndIncrement() % totalSearchTerms)
			, String.class);
		assertTrue(output != null);
		assertTrue(!StringUtils.isEmpty(output));
		int text = indexCounter.get();
		if (text % 1000 == 0) {
			logger.info("Text at {}", text);
		}

	}
}
