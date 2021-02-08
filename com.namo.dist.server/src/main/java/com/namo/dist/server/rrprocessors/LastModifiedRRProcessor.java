package com.namo.dist.server.rrprocessors;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseExchange;
import com.namo.dist.server.RequestResponseProcessor;

/**
 * Processor to check last modified timestamp on the file and add the response
 * header accordingly. Also if request header contains cached file last
 * modification timestamp, then compre the cached to current time stamp and if
 * cached one happens to be up-to-date then returns 304 response.
 */
class LastModifiedRRProcessor implements RequestResponseProcessor {

	static final String LASTMODIFIED_RES_HEADER_KEY = "Last-Modified";
	static final String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since";
	private RequestResponseProcessor nextProcessor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(RequestResponseExchange exchange, RequestContext ctx) throws IOException {
		if (ctx.getResponseCode() > 0)
			return;

		String lastModifiedStr = ctx.getRequestedFileProcessor()
				.getLastModifiedTime(DateTimeFormatter.RFC_1123_DATE_TIME);
		// setting last modified date in required format on response header
		exchange.getResponseHeaders().set(LASTMODIFIED_RES_HEADER_KEY, lastModifiedStr);

		if (exchange.getRequestHeaders().containsKey(IF_MODIFIED_SINCE_HEADER)
				&& !exchange.getRequestHeaders().get(IF_MODIFIED_SINCE_HEADER).isEmpty()) {
			String modifiedSinceStr = exchange.getRequestHeaders().getFirst(IF_MODIFIED_SINCE_HEADER);

			// Checking if the last modified date on file is after IF_MODIFIED_SINCE_HEADER,
			// if not then browsers cache copy is still good, hence return 304 response
			// code.
			if (!ctx.getRequestedFileProcessor().isModifiedAfter(modifiedSinceStr,
					DateTimeFormatter.RFC_1123_DATE_TIME)) {
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
