package com.namo.dist.server;

import java.io.File;

class RequestContextImpl implements RequestContext {

	private RequestFileProcessor requestedFileProcessor;

	private int responseCode = 0;
	private boolean acceptRanges = true; // by default supporting http range headers

	private long cacheMaxAge = 604800; // by default Keeping cache max age as 7 days

	public RequestContextImpl() {
	}

	public RequestContextImpl(boolean acceptRanges, long cacheMaxAge) {
		this.acceptRanges = acceptRanges;
		this.cacheMaxAge = cacheMaxAge;
	}

	@Override
	public RequestFileProcessor getRequestedFileProcessor() {
		return requestedFileProcessor;
	}

	@Override
	public void setRequestedFile(File requestedFile) {
		this.requestedFileProcessor = new RequestFileProcessorImpl(requestedFile);
	}
	
	@Override
	public int getResponseCode() {
		return responseCode;
	}

	@Override
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	@Override
	public boolean isAcceptRanges() {
		return acceptRanges;
	}

	@Override
	public long getCacheMaxAge() {
		return cacheMaxAge;
	}
}
