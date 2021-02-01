package com.namo.dist.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.namo.dist.server.rrprocessors.RRProcessorFactory;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class StaticFileHandler implements HttpHandler {

	private static final Logger logger = Logger.getLogger(StaticFileHandler.class.getName());

	private boolean supportHttpRange = true; // by default supporting http range headers

	private long cacheMaxAge = 604800l; // by default Keeping cache max age as 7 days

	public StaticFileHandler() {
	}

	public StaticFileHandler(boolean supportHttpRange, long cacheMaxAge) {
		this.supportHttpRange = supportHttpRange;
		this.cacheMaxAge = cacheMaxAge;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {

		logger.log(Level.INFO, "Processing a static file request....");
		// TODO this request response processor chaining could be improved
		// Configuring chain of processors
		RequestResponseProcessor firstProcessor = RRProcessorFactory.INSTANCE.getFileExistsRRProcessor();
		RequestResponseProcessor processorChain = firstProcessor.appendNext(RRProcessorFactory.INSTANCE.getETagRRProcessor());
		processorChain = processorChain.appendNext(RRProcessorFactory.INSTANCE.getLastModifiedRRProcessor());
		processorChain = processorChain.appendNext(RRProcessorFactory.INSTANCE.getPreFlightRRProcessor());
		processorChain = processorChain.appendNext(RRProcessorFactory.INSTANCE.getCacheControlRRProcessor());
		processorChain = processorChain.appendNext(RRProcessorFactory.INSTANCE.getFileRRProcessor());

		// Request context which will be passed between processors
		RequestContext reqContext = new RequestContextImpl(supportHttpRange, cacheMaxAge);

		// processing the HttpExchange now
		firstProcessor.process(exchange, reqContext);

		if (reqContext.getResponseCode() < 200 || reqContext.getResponseCode() >= 300) {
			exchange.sendResponseHeaders(reqContext.getResponseCode(), 0);
		}

	}

}
