package com.namo.dist.server;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class TestUtils {
	
	public static TestUtils INSTANCE = new TestUtils();

	private String LOCALHOST_PATH = "http://localhost:"+StaticFileHttpServer.getServerPort();
	private static List<String> EXPECTED_FILES_LIST;
	
	
	public String getServerPath() {
		return LOCALHOST_PATH;
	}
	
	public String getStaticFilesPath() {
		return StaticFileHttpServer.STATIC_FILES_PATH2;
	}
	
	public List<String> getExpectedFilesList() {
		if(EXPECTED_FILES_LIST == null) {
			EXPECTED_FILES_LIST = expectedFilesList();
		}
		return EXPECTED_FILES_LIST;
	}
	
	public RequestContext getDummyRequestContext() {
		return new RequestContextImpl();
	}
	
	
	private static List<String> expectedFilesList() {
		File path = new File(StaticFileHttpServer.FILES_DIR);
		List<String> fileNameList = new LinkedList<>();
		for (File file : path.listFiles()) {
			if (file.isFile()) {
				fileNameList.add(file.getName());
			}
		}
		return fileNameList;
	}
	private TestUtils() {
		
	}

}
