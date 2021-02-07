package com.namo.dist.server;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class StaticFileHttpServerTest {

	public static StaticFileHttpServer HTTP_SERVER;

	private static HttpClient HTTP_CLIENT;
	private static final Logger logger = Logger.getLogger(StaticFileHttpServerTest.class.getName());
	
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

	@Test
	public void testStaticFilesPath1() throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(TestUtils.INSTANCE.getServerPath() + StaticFileHttpServer.STATIC_FILES_PATH)).build();
		HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
		Assert.assertEquals("Non successful response code received", ResponseCodes.SUCCESSFUL.getResponseCode(),
				response.statusCode());
		Assert.assertEquals("Response list seems incorrect", TestUtils.INSTANCE.getExpectedFilesList().toString().trim(),
				response.body().trim());
	}

	@Test
	public void testStaticFilesPathWithTrailingSlash() throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(TestUtils.INSTANCE.getServerPath() + StaticFileHttpServer.STATIC_FILES_PATH2)).build();
		HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
		Assert.assertEquals("Non successful response code received", ResponseCodes.SUCCESSFUL.getResponseCode(),
				response.statusCode());
		Assert.assertEquals("Response list seems incorrect", TestUtils.INSTANCE.getExpectedFilesList().toString().trim(),
				response.body().trim());
	}

	@Test
	public void testStaticFileRetrievalWithMultiThreading() {
		// Running multi-threaded retrieval and comparison of all files in parallel
		TestUtils.INSTANCE.getExpectedFilesList().parallelStream().forEach((String fileName) -> testFileRetrieval(fileName));
	}

	private void testFileRetrieval(String fileName) {
		try {
			logger.info("Submitting request for file: "+ fileName);
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(TestUtils.INSTANCE.getServerPath() + StaticFileHttpServer.STATIC_FILES_PATH2 + fileName)).build();
			HttpResponse<byte[]> response = HTTP_CLIENT.send(request, BodyHandlers.ofByteArray());
			Assert.assertEquals(fileName + " - Non successful response code received",
					ResponseCodes.SUCCESSFUL.getResponseCode(), response.statusCode());
			Assert.assertTrue(fileName + "File contents don't match", compareFileContents(fileName, response.body()));
		} catch (IOException | InterruptedException ex) {
			Assert.fail("Failed due to exception " + ex.getMessage());
		}
	}

	private boolean compareFileContents(String fileName, byte[] bytesToBeCompared) throws IOException {
		
		Path path = Paths.get(StaticFileHttpServer.FILES_DIR+File.separator+fileName);
	    byte[] data = Files.readAllBytes(path);
	    return Arrays.equals(bytesToBeCompared, data);
	}


}
