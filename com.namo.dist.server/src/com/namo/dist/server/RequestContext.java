package com.namo.dist.server;

import java.io.File;

public interface RequestContext {

	RequestFileProcessor getRequestedFileProcessor();
	
	void setRequestedFile(File requestedFile);

	int getResponseCode();

	void setResponseCode(int responseCode);

	boolean isAcceptRanges();

	long getCacheMaxAge();

}