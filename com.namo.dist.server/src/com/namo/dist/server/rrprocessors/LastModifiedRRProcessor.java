package com.namo.dist.server.rrprocessors;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseProcessor;
import com.sun.net.httpserver.HttpExchange;

class LastModifiedRRProcessor implements RequestResponseProcessor {

	private static final String LASTMODIFIED_RES_HEADER_KEY = "Last-Modified";
	private static final String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since";
	private RequestResponseProcessor nextProcessor;

	@Override
	public void process(HttpExchange exchange, RequestContext ctx) throws IOException {
		if (ctx.getResponseCode() > 0)
			return;

		String lastModifiedStr = ctx.getRequestedFileProcessor()
				.getLastModifiedTime(DateTimeFormatter.RFC_1123_DATE_TIME);
		exchange.getResponseHeaders().set(LASTMODIFIED_RES_HEADER_KEY, lastModifiedStr);
		if (exchange.getRequestHeaders().containsKey(IF_MODIFIED_SINCE_HEADER)
				&& !exchange.getRequestHeaders().get(IF_MODIFIED_SINCE_HEADER).isEmpty()) {
			String modifiedSinceStr = exchange.getRequestHeaders().getFirst(IF_MODIFIED_SINCE_HEADER);
			if (!ctx.getRequestedFileProcessor().isModifiedAfter(modifiedSinceStr,
					DateTimeFormatter.RFC_1123_DATE_TIME)) {
				ctx.setResponseCode(304);
			}
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