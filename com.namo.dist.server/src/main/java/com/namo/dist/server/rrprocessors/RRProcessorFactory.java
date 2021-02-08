package com.namo.dist.server.rrprocessors;

import com.namo.dist.server.RequestResponseProcessor;

/**
 * Factory for {@link RequestResponseProcessor}s 
 * TODO improve to use enum and switch for returning appropriate processor.
 *
 */
public class RRProcessorFactory {
	
	public static final RRProcessorFactory INSTANCE = new RRProcessorFactory();
	
	public RequestResponseProcessor getFileExistsRRProcessor() {
		return new FileExistsRRProcessor();
	}
	public RequestResponseProcessor getETagRRProcessor() {
		return new ETagRRProcessor();
	}
	public RequestResponseProcessor getLastModifiedRRProcessor() {
		return new LastModifiedRRProcessor();
	}
	public RequestResponseProcessor getHeadRRProcessor() {
		return new HeadRRProcessor();
	}
	public RequestResponseProcessor getCacheControlRRProcessor() {
		return new CacheControlRRProcessor();
	}
	public RequestResponseProcessor getFileRRProcessor() {
		return new FileRRProcessor();
	}
	
	//private constructor for singleton
	private RRProcessorFactory() {}

}
