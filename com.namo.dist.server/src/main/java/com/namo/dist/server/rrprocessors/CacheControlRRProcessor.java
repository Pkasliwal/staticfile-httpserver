package com.namo.dist.server.rrprocessors;

import java.io.IOException;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseExchange;
import com.namo.dist.server.RequestResponseProcessor;

/**
 * Adds cache-control response header based on the configured cache max age
 * indicating file is good to be cached for this amount of time and won't be
 * needed to be retrieved again during this duration.
 */
class CacheControlRRProcessor implements RequestResponseProcessor {

	private static final String CACHE_CONTROL_RES_HEADER_KEY = "Cache-Control";
	private RequestResponseProcessor nextProcessor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(RequestResponseExchange exchange, RequestContext ctx) throws IOException {
		if (ctx.getResponseCode() > 0)
			return;

		// Content to be cached upto specified max age (default is 7 days but one could
		// define it to be upto 1 year and immutable)
		exchange.getResponseHeaders().set(CACHE_CONTROL_RES_HEADER_KEY, "public,max-age=" + ctx.getCacheMaxAge());
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