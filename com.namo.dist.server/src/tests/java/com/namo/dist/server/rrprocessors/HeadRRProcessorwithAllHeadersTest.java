package com.namo.dist.server.rrprocessors;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.namo.dist.server.RequestFileProcessor;
import com.namo.dist.server.ResponseCodes;
import com.namo.dist.server.StaticFileHttpServer;
import com.namo.dist.server.TestUtils;

/**
 * Test ensuring all the appropriate response headers and response codes are
 * returned based on configured request headers. Testing include testcases for
 * HEAD request processing with E-tag and Last-Modified processors.
 */
public class HeadRRProcessorwithAllHeadersTest {

	public static StaticFileHttpServer HTTP_SERVER;

	private static HttpClient HTTP_CLIENT;
	private static final Logger logger = Logger.getLogger(HeadRRProcessorwithAllHeadersTest.class.getName());

	@BeforeClass
	public static void initialize() throws IOException {
		HTTP_SERVER = new StaticFileHttpServer();
		HTTP_SERVER.startServer();
		HTTP_CLIENT = java.net.http.HttpClient.newHttpClient();
	}

	@AfterClass
	public static void cleanup() {
		HTTP_SERVER.stopServer();
	}

	/**
	 * Testing HEAD requests to see if all the file processing response headers are
	 * correctly populated
	 */
	@Test
	public void testHeadReqWithMultiThreading() {
		// Running multi-threaded HEAD requests
		TestUtils.INSTANCE.getExpectedFilesList().parallelStream()
				.forEach((String fileName) -> testHeadRequest(fileName));
	}

	/**
	 * Testing that if IF_MATCH header is present then etags of cached and current
	 * files are compared and if cached copy is still good then 304 response is
	 * returned.
	 */
	@Test
	public void testHeadReqForEtagProcessor() {
		// Running multi-threaded HEAD requests for verifying etag related headers
		TestUtils.INSTANCE.getExpectedFilesList().parallelStream()
				.forEach((String fileName) -> testEtagHeaders(fileName));
	}

	/**
	 * Testing that if IF_MODIFIED_SINCE header is present then modification
	 * timestamps of cached and current files are compared and if cached copy is
	 * still good then 304 response is returned.
	 */
	@Test
	public void testHeadReqForLastModifiedProcessor() {
		// Running multi-threaded HEAD requests for verifying last-modified related
		// headers
		TestUtils.INSTANCE.getExpectedFilesList().parallelStream()
				.forEach((String fileName) -> testLastModifiedHeader(fileName));
	}

	private void testHeadRequest(String fileName) {
		try {
			logger.info("Submitting head request for file: " + fileName);
			RequestFileProcessor fileAccessor = TestUtils.INSTANCE.getRequestFileProcessor(fileName);
			HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI
					.create(TestUtils.INSTANCE.getServerPath() + TestUtils.INSTANCE.getStaticFilesPath() + fileName));
			// Setting request of type HEAD
			requestBuilder.method("HEAD", HttpRequest.BodyPublishers.noBody());
			HttpRequest request = requestBuilder.build();

			HttpResponse<Void> response = HTTP_CLIENT.send(request, BodyHandlers.discarding());
			Assert.assertEquals(fileName + " - Non successful response code received",
					ResponseCodes.SUCCESSFUL.getResponseCode(), response.statusCode());
			expectedHeadersForHeadRequest(response.headers(), fileAccessor);
		} catch (IOException | InterruptedException ex) {
			Assert.fail("Failed due to exception " + ex.getMessage());
		}
	}

	private void expectedHeadersForHeadRequest(HttpHeaders responseHeaders, RequestFileProcessor fileAccessor) {
		try {
			Optional<String> etagValue = responseHeaders.firstValue(ETagRRProcessor.ETAG_RES_HEADER_KEY);
			Optional<String> lastModifiedValue = responseHeaders
					.firstValue(LastModifiedRRProcessor.LASTMODIFIED_RES_HEADER_KEY);
			Optional<String> contentTypeValue = responseHeaders.firstValue(FileExistsRRProcessor.CONTENT_TYPE);
			Optional<String> contentLengthValue = responseHeaders.firstValue(FileExistsRRProcessor.CONTENT_LENGTH);
			Optional<String> acceptRangeValue = responseHeaders.firstValue(FileExistsRRProcessor.ACCEPT_RANGE);

			Assert.assertTrue("ETAG header not found", etagValue.isPresent());
			Assert.assertTrue("LASTMODIFIED header not found", lastModifiedValue.isPresent());
			Assert.assertTrue("CONTENT_TYPE header not found", contentTypeValue.isPresent());
			Assert.assertTrue("CONTENT_LENGTH header not found", contentLengthValue.isPresent());
			Assert.assertTrue("ACCEPT_RANGE header not found", acceptRangeValue.isPresent());

			// Comparing response header values to the actual file values
			Assert.assertEquals(fileAccessor.getETag(), etagValue.get());
			Assert.assertEquals(fileAccessor.getLastModifiedTime(DateTimeFormatter.RFC_1123_DATE_TIME),
					lastModifiedValue.get());
			Assert.assertEquals(fileAccessor.getContentType(), contentTypeValue.get());
			Assert.assertEquals(fileAccessor.getFileLengthBytes(), Long.valueOf(contentLengthValue.get()));
		} catch (IOException e) {
			Assert.fail("Failed due to exception " + e.getMessage());
		}
	}

	private void testEtagHeaders(String fileName) {
		try {
			logger.info("Executing etag test cases for file: " + fileName);
			RequestFileProcessor fileAccessor = TestUtils.INSTANCE.getRequestFileProcessor(fileName);
			HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI
					.create(TestUtils.INSTANCE.getServerPath() + TestUtils.INSTANCE.getStaticFilesPath() + fileName));
			// Setting request of type HEAD
			requestBuilder.method("HEAD", HttpRequest.BodyPublishers.noBody());
			requestBuilder.setHeader(ETagRRProcessor.IF_MATCH_HEADER_KEY, fileAccessor.getETag());
			HttpRequest request = requestBuilder.build();

			HttpResponse<Void> response = HTTP_CLIENT.send(request, BodyHandlers.discarding());
			Assert.assertEquals(fileName + " - expected cached response code", ResponseCodes.CACHED.getResponseCode(),
					response.statusCode());
		} catch (IOException | InterruptedException ex) {
			Assert.fail("Failed due to exception " + ex.getMessage());
		}
	}

	private void testLastModifiedHeader(String fileName) {
		try {
			logger.info("Executing last modified test cases for file: " + fileName);
			RequestFileProcessor fileAccessor = TestUtils.INSTANCE.getRequestFileProcessor(fileName);
			HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI
					.create(TestUtils.INSTANCE.getServerPath() + TestUtils.INSTANCE.getStaticFilesPath() + fileName));
			// Setting request of type HEAD
			requestBuilder.method("HEAD", HttpRequest.BodyPublishers.noBody());
			
			//Adding 10secs so that cached version to take care of exact millisec comparison
			long cachedModifiedInMillis = fileAccessor.getFile().lastModified() + 10000;
			OffsetDateTime cachedModifiedOffsetTime = Instant.ofEpochMilli(cachedModifiedInMillis)
					.atOffset(ZoneOffset.UTC);

			requestBuilder.setHeader(LastModifiedRRProcessor.IF_MODIFIED_SINCE_HEADER,
					cachedModifiedOffsetTime.format(DateTimeFormatter.RFC_1123_DATE_TIME));
			HttpRequest request = requestBuilder.build();

			HttpResponse<Void> response = HTTP_CLIENT.send(request, BodyHandlers.discarding());
			Assert.assertEquals(fileName + " - expected cached response code", ResponseCodes.CACHED.getResponseCode(),
					response.statusCode());
		} catch (IOException | InterruptedException ex) {
			Assert.fail("Failed due to exception " + ex.getMessage());
		}
	}
}
