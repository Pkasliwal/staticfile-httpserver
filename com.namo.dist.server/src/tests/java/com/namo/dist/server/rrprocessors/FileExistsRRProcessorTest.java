package com.namo.dist.server.rrprocessors;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseExchange;
import com.namo.dist.server.ResponseCodes;
import com.namo.dist.server.TestUtils;

/**
 * Test cases for FileExistsRRProcessor.
 */
public class FileExistsRRProcessorTest {

	/**
	 * Testing scenario when file exists and appropriate response headers populated.
	 */
	@Test
	public void testPositiveScenario() {
		List<String> files = TestUtils.INSTANCE.getExpectedFilesList();
		for (String fileName : files) {
			URI requestUri = URI
					.create(TestUtils.INSTANCE.getServerPath() + TestUtils.INSTANCE.getStaticFilesPath() + fileName);
			RequestResponseExchange rrExchange = new DummyRequestResponseExchange(requestUri, "GET");
			RequestContext ctx = TestUtils.INSTANCE.getDummyRequestContext();
			FileExistsRRProcessor processorToTest = new FileExistsRRProcessor();
			try {
				processorToTest.process(rrExchange, ctx);
				Assert.assertEquals("Non-zero response code is incorrect, file should exists", 0,
						ctx.getResponseCode());
				for (String header : FileExistsRRProcessor.EXPECTED_HEADERS) {
					Assert.assertTrue("Expected Header not found: " + header,
							rrExchange.getResponseHeaders().containsKey(header));
				}
			} catch (IOException e) {
				Assert.fail(e.getMessage());
			}
		}
	}

	/**
	 * Testing scenario when file doesn't exists and to return correct error response code.
	 */
	@Test
	public void testNegativeScenario() {
		List<String> files = TestUtils.INSTANCE.getExpectedFilesList();
		for (String fileName : files) {
			URI requestUri = URI
					.create(TestUtils.INSTANCE.getServerPath() + TestUtils.INSTANCE.getStaticFilesPath() + "abc"+fileName);
			RequestResponseExchange rrExchange = new DummyRequestResponseExchange(requestUri, "GET");
			RequestContext ctx = TestUtils.INSTANCE.getDummyRequestContext();
			FileExistsRRProcessor processorToTest = new FileExistsRRProcessor();
			try {
				processorToTest.process(rrExchange, ctx);
				Assert.assertEquals("Response code should have been 404", ResponseCodes.NOT_FOUND.getResponseCode(),
						ctx.getResponseCode());
				Assert.assertTrue("Response Headers should have been empty when file doesn't exist", rrExchange.getResponseHeaders().isEmpty());
			} catch (IOException e) {
				Assert.fail(e.getMessage());
			}
		}
	}
}
