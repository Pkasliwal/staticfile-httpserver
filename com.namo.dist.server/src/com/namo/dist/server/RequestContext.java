package com.namo.dist.server;

import java.io.File;

/**
 * Context interface, its instance gets passed on between request response processors to detect the current state of processing.
 *
 */
public interface RequestContext {

	/**
	 * Provides {@link RequestFileProcessor} for processors to get basic information on the file that is getting retrieved/streamed as part of the response.
	 * @return {@link RequestFileProcessor} instance.
	 */
	RequestFileProcessor getRequestedFileProcessor();
	
	/**
	 * Sets {@link RequestFileProcessor} instance, this is supposed to be just used by FileExistsRRProcessor and we should find another pattern to support this initialization. 
	 * @param requestedFile {@link RequestFileProcessor} instance.
	 */
	void setRequestedFile(File requestedFile);

	/**
	 * Gets response code for the request ie getting processed. When a processor sets the response code, it is an indication to terminate the pipeline and respond.
	 * Every processor's first job is to check if response code > 0 then return and terminate the pipeline.
	 * @return response code for the request ie getting processed
	 */
	int getResponseCode();

	/**
	 * Sets response code for the request ie getting processed.
	 * @param responseCode response code
	 */
	void setResponseCode(int responseCode);

	/**
	 * Checks if the server can handle range requests, if so, return true otherwise false.
	 * @return true when server can respond to range requests, otherwise false.
	 */
	boolean isAcceptRanges();

	/**
	 * Gets the max cache age to be used for the file ie being retrieved. 
	 * @return cache max age returned to indicate to the browser how long browser can use the cached version
	 */
	long getCacheMaxAge();

}