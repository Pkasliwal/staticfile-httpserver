package com.namo.dist.server.rrprocessors;

import java.io.IOException;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseExchange;
import com.namo.dist.server.RequestResponseProcessor;
import com.sun.net.httpserver.HttpExchange;

/**
 * Processor to determine if its a preflight request then terminate with 200 response code.
 */
class PreFlightRRProcessor implements RequestResponseProcessor {

	private static final String PREFLIGHT_HEADER_KEY = "Access-Control-Request-Method";

	private RequestResponseProcessor nextProcessor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(RequestResponseExchange exchange, RequestContext ctx) throws IOException {
		if (ctx.getResponseCode() > 0)
			return;
		// checks if its a preflight request then return with success 200 code
		if (exchange.getRequestHeaders().containsKey(PREFLIGHT_HEADER_KEY)) {
			ctx.setResponseCode(200);
		}
		if (nextProcessor != null) {
			nextProcessor.process(exchange, ctx);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RequestResponseProcessor appendNext(RequestResponseProcessor processor) {
		return nextProcessor = processor;
	}

}
