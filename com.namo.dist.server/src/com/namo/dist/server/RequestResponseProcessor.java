package com.namo.dist.server;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

/**
 * Request response processor interface defining process contract and state/context that gets passed on to next processor. 
 */
public interface RequestResponseProcessor {
	
	/**
	 * Responsible for verifying if request context response code is not set ie 0 then go ahead with the exchange request processing based on the define request headers.
	 * @param exchange exchange instance encapsulating request received and response to be generated.
	 * @param ctx context holding any processing information to be passed on to next processor.
	 * @throws IOException exception thrown when there is an issue processing the file request.
	 */
	void process(HttpExchange exchange, RequestContext ctx) throws IOException;
	
	/**
	 * Configuring the next processor in the pipeline.
	 * @param processor next processor in the pipeline.
	 * @return last processor in the pipeline to be chained next.
	 */
	RequestResponseProcessor appendNext(RequestResponseProcessor processor);

}
