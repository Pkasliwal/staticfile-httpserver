package com.namo.dist.server.rrprocessors;

import java.io.IOException;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseExchange;
import com.namo.dist.server.RequestResponseProcessor;

/**
 * Processor to determine if its a HEAD request then terminate with 200
 * response code.
 */
class HeadRRProcessor implements RequestResponseProcessor {

	private static final String HEAD_METHOD = "HEAD";
	private RequestResponseProcessor nextProcessor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(RequestResponseExchange exchange, RequestContext ctx) throws IOException {
		if (ctx.getResponseCode() > 0)
			return;
		// checks if its HEAD request then return with success 200 code and no need to retrieve file contents 
		if (HEAD_METHOD.equals(exchange.getRequestMethod())) {
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
