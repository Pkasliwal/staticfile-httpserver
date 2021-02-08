package com.namo.dist.server.rrprocessors;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.junit.Assert;
import org.junit.Test;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestFileProcessor;
import com.namo.dist.server.RequestResponseExchange;
import com.namo.dist.server.ResponseCodes;
import com.namo.dist.server.TestUtils;

/**
 * Test case ensuring partial range file contents retrieval for big files, for
 * positive and negative scenario.
 */
public class FileRangeRRProcessorTest {

	/**
	 * Test for ensuring partial range GET requests could be correctly handled with accurate file contents.
	 */
	@Test
	public void testPartialRangeScenarios() {

		// File handle for a large files to be processed using partial range
		File largeFile = TestUtils.INSTANCE.getLargeStaticFileToTest();
		RequestFileProcessor fileAccessor = TestUtils.INSTANCE.getRequestFileProcessor(largeFile);
		long fileLength = fileAccessor.getFileLengthBytes();

		URI requestUri = URI.create(
				TestUtils.INSTANCE.getServerPath() + TestUtils.INSTANCE.getStaticFilesPath() + largeFile.getName());
		try {

			long startByteIndex = 0;
			// Divided file length in 3 parts for partial processing
			int rangeLength = (int) (fileLength / 3);
			for (int index = 0; index < 4; index++) {

				long endByteIndex = startByteIndex + rangeLength - 1;
				// ensuring index boundaries for 3rd range to be requested as the last one
				if (index == 2) {
					endByteIndex = fileLength - 1;
					rangeLength = (int) (endByteIndex - startByteIndex + 1);
				}
				String rangeHeaderValue = "bytes=" + startByteIndex + "-" + endByteIndex;

				RequestResponseExchange rrExchange = new DummyRequestResponseExchange(requestUri, "GET");
				RequestContext ctx = TestUtils.INSTANCE.getDummyRequestContext();
				FileExistsRRProcessor fileExistsProcessor = new FileExistsRRProcessor();
				FileRRProcessor processorToTest = new FileRRProcessor();

				rrExchange.getRequestHeaders().set("Range", rangeHeaderValue);
				fileExistsProcessor.process(rrExchange, ctx);
				processorToTest.process(rrExchange, ctx);

				if (index <= 2) {
					// Response code needs to be 216 ir PARTIAL
					Assert.assertEquals("Incorrect response code", ResponseCodes.PARTIAL.getResponseCode(),
							ctx.getResponseCode());
					Assert.assertTrue("Content-Range header not found",
							rrExchange.getResponseHeaders().containsKey("Content-Range"));
					String contentRange = rrExchange.getResponseHeaders().getFirst("Content-Range");
					// Content-Range format
					String expectedContentRange = "bytes " + startByteIndex + "-" + endByteIndex + "/" + fileLength;
					Assert.assertEquals("Content-Range header value not as expected", expectedContentRange,
							contentRange);
					Assert.assertEquals("Content-Length header value not as expected", rangeLength,
							Integer.parseInt(rrExchange.getResponseHeaders().getFirst("Content-Length").toString()));
					Assert.assertArrayEquals(
							"Partial file contents retrieved didn't match with the actual file contents",
							fileAccessor.readByteRange(startByteIndex, rangeLength), ctx.getResponseBytes());
					startByteIndex = endByteIndex + 1;
				} else {
					// Testing failure scenario because for index 3, indices are out of range
					Assert.assertEquals("Incorrect response code", ResponseCodes.PARTIAL_FAILURE.getResponseCode(),
							ctx.getResponseCode());
				}
			}
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

}
