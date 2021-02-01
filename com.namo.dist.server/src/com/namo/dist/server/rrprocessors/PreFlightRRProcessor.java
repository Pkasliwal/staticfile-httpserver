package com.namo.dist.server.rrprocessors;

import java.io.IOException;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseProcessor;
import com.sun.net.httpserver.HttpExchange;

class PreFlightRRProcessor implements RequestResponseProcessor {

	private static final String PREFLIGHT_HEADER_KEY = "Access-Control-Request-Method";

	private RequestResponseProcessor nextProcessor;

	@Override
	public void process(HttpExchange exchange, RequestContext ctx) throws IOException {
		if (ctx.getResponseCode() > 0)
			return;

		if (exchange.getRequestHeaders().containsKey(PREFLIGHT_HEADER_KEY)) {
			ctx.setResponseCode(200);
		}
		if (nextProcessor != null) {
			nextProcessor.process(exchange, ctx);
		}

	}

	@Override
	public RequestResponseProcessor appendNext(RequestResponseProcessor processor) {
		return nextProcessor = processor;
	}

}
