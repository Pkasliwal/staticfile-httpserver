package com.namo.dist.server.rrprocessors;

import com.namo.dist.server.RequestResponseProcessor;

/**
 * Factory for {@link RequestResponseProcessor}s 
 * TODO improve to use enum and switch for returning appropriate processor.
 *
 */
public class RRProcessorFactory {
	
	/**
	 * Provides factory instance.
	 */
	public static final RRProcessorFactory INSTANCE = new RRProcessorFactory();
	
	/**
	 * Provides processor for file exists processing.
	 * @return FileExistsRRProcessor instance.
	 */
	public RequestResponseProcessor getFileExistsRRProcessor() {
		return new FileExistsRRProcessor();
	}
	
	/**
	 * Provides e tag processor
	 * @return ETagRRProcessor instance
	 */
	public RequestResponseProcessor getETagRRProcessor() {
		return new ETagRRProcessor();
	}
	/**
	 * Provides last modified time stamp processor.
	 * @return LastModifiedRRProcessor instance
	 */
	public RequestResponseProcessor getLastModifiedRRProcessor() {
		return new LastModifiedRRProcessor();
	}
	/**
	 * Provides processor to handle HEAD request type.
	 * @return HeadRRProcessor instance
	 */
	public RequestResponseProcessor getHeadRRProcessor() {
		return new HeadRRProcessor();
	}
	/**
	 * Provides processor for cache control header.
	 * @return CacheControlRRProcessor instance
	 */
	public RequestResponseProcessor getCacheControlRRProcessor() {
		return new CacheControlRRProcessor();
	}
	/**
	 * Provides processor for file complete/partial retrieval.
	 * @return FileRRProcessor instance
	 */
	public RequestResponseProcessor getFileRRProcessor() {
		return new FileRRProcessor();
	}
	
	/*
	 * Private constructor for singleton
	 */
	private RRProcessorFactory() {}

}
