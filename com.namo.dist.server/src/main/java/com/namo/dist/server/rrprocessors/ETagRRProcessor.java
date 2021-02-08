package com.namo.dist.server.rrprocessors;

import java.io.IOException;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseExchange;
import com.namo.dist.server.RequestResponseProcessor;

/**
 * Generates and compares Etag headers. For cases when request headers contain
 * "If-Match" header with cached e-tag value, and cached e-tag is same as
 * current e-tag, request is returned with 304 response code indicating client's
 * cached version of file is still good.
 */
class ETagRRProcessor implements RequestResponseProcessor {

	static final String ETAG_RES_HEADER_KEY = "ETag";
	static final String IF_MATCH_HEADER_KEY = "If-Match";
	private RequestResponseProcessor nextProcessor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(RequestResponseExchange exchange, RequestContext ctx) throws IOException {
		if (ctx.getResponseCode() > 0)
			return;

		String eTagValue = ctx.getRequestedFileProcessor().getETag();

		exchange.getResponseHeaders().set(ETAG_RES_HEADER_KEY, eTagValue);

		if (exchange.getRequestHeaders().containsKey(IF_MATCH_HEADER_KEY)) {
			// Given the current calculated etag matches what was sent in request header,
			// then return 304 response code to indicate cached version is still good.
			if (exchange.getRequestHeaders().get(IF_MATCH_HEADER_KEY).contains(eTagValue)) {
				ctx.setResponseCode(304);
			}
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
