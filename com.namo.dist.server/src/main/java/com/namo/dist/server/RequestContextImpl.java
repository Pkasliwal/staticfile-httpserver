package com.namo.dist.server;

import java.io.File;

/**
 * Simple POJO implementation for RequestContext.
 */
class RequestContextImpl implements RequestContext {

	private RequestFileProcessor requestedFileProcessor;

	private int responseCode = 0;
	private byte[] responseBytes;
	private boolean acceptRanges = true; // by default supporting http range headers
	private long cacheMaxAge = 604800; // by default Keeping cache max age as 7 days in seconds

	/**
	 * Default constructor to define cacheMaxAge of 7 days and accepts range requests.
	 */
	RequestContextImpl() {
	}

	/**
	 * Constructor to customize acceptRanges and cacheMaxAge.
	 * TODO to add 1 year limit to this value because that is the max value supported.
	 * @param acceptRanges accepts range requests
	 * @param cacheMaxAge max cache age to use on response headers
	 */
	RequestContextImpl(boolean acceptRanges, long cacheMaxAge) {
		this.acceptRanges = acceptRanges;
		this.cacheMaxAge = cacheMaxAge;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RequestFileProcessor getRequestedFileProcessor() {
		return requestedFileProcessor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRequestedFile(File requestedFile) {
		this.requestedFileProcessor = new RequestFileProcessorImpl(requestedFile);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAcceptRanges() {
		return acceptRanges;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getCacheMaxAge() {
		return cacheMaxAge;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResponseBytes(byte[] responseBytes) {
		this.responseBytes = responseBytes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getResponseBytes() {
		return responseBytes;
	}
}
