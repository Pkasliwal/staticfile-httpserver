package com.namo.dist.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.namo.dist.server.rrprocessors.RRProcessorFactory;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler for processing a static file retrieval request.
 */
class StaticFileHandler implements HttpHandler {

	private static final Logger logger = Logger.getLogger(StaticFileHandler.class.getName());

	private boolean supportHttpRange = true; // by default supporting http range headers

	private long cacheMaxAge = 604800l; // by default Keeping cache max age as 7 days

	StaticFileHandler() {
	}

	StaticFileHandler(boolean supportHttpRange, long cacheMaxAge) {
		this.supportHttpRange = supportHttpRange;
		this.cacheMaxAge = cacheMaxAge;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {

		logger.log(Level.INFO, "Processing a static file request....");
		
		// TODO this request response processor chaining could be improved
		// Configuring chain of processors
		// First one checks if file exists
		RequestResponseProcessor firstProcessor = RRProcessorFactory.INSTANCE.getFileExistsRRProcessor();
		// Generates and compares Etag headers
		RequestResponseProcessor processorChain = firstProcessor.appendNext(RRProcessorFactory.INSTANCE.getETagRRProcessor());
		// Checks modified timestamp on the file and add the response header accordingly
		processorChain = processorChain.appendNext(RRProcessorFactory.INSTANCE.getLastModifiedRRProcessor());
		// Terminates the pipeline if its a HEAD request with 200 response code
		processorChain = processorChain.appendNext(RRProcessorFactory.INSTANCE.getHeadRRProcessor());
		// Adds cache-control response header based on the configured cache max age
		processorChain = processorChain.appendNext(RRProcessorFactory.INSTANCE.getCacheControlRRProcessor());
		// Performs file access as needed to retrieve the request file/http range.
		processorChain = processorChain.appendNext(RRProcessorFactory.INSTANCE.getFileRRProcessor());

		// Request context which will be passed between processors
		RequestContext reqContext = new RequestContextImpl(supportHttpRange, cacheMaxAge);

		// processing the HttpExchange now
		firstProcessor.process(new RequestResponseExchangeImpl(exchange), reqContext);

		if (reqContext.getResponseCode() >= 200 && reqContext.getResponseCode() < 300 && reqContext.getResponseBytes() != null) {
			exchange.sendResponseHeaders(reqContext.getResponseCode(), reqContext.getResponseBytes().length);
			exchange.getResponseBody().write(reqContext.getResponseBytes());
			exchange.getResponseBody().close();
		} else {
			// -1 for indicating no response body will be written/returned
			exchange.sendResponseHeaders(reqContext.getResponseCode(), 0);
		}
	}

}
