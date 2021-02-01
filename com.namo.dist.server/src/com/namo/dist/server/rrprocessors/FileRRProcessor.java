package com.namo.dist.server.rrprocessors;

import java.io.IOException;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseProcessor;
import com.sun.net.httpserver.HttpExchange;

class FileRRProcessor implements RequestResponseProcessor {

	private RequestResponseProcessor nextProcessor;

	@Override
	public void process(HttpExchange exchange, RequestContext ctx) throws IOException {
		if (ctx.getResponseCode() > 0)
			return;
		long startIndex = 0;
		int fullLength = ctx.getRequestedFileProcessor().getFileLengthBytes().intValue();
		int length = fullLength;
		if (fullLength > 1000000 && exchange.getRequestHeaders().containsKey("Range") && !exchange.getRequestHeaders().getFirst("Range").isEmpty()) {
			RangeParser parser = new RangeParser(exchange.getRequestHeaders().getFirst("Range"), fullLength);
			if (parser.parse()) {
				startIndex = parser.getStartIndex();
				length = parser.getLength();
				exchange.getResponseHeaders().set("Content-Range", parser.contentRange());
				exchange.getResponseHeaders().set("Content-Length", String.valueOf(length));
				ctx.setResponseCode(206);
				exchange.sendResponseHeaders(206, length);
				exchange.getResponseBody().write(ctx.getRequestedFileProcessor().readByteRange(startIndex, length));
			} else {
				ctx.setResponseCode(416);
			}
		} else {
			ctx.setResponseCode(200);
			exchange.sendResponseHeaders(200, length);
			exchange.getResponseBody().write(ctx.getRequestedFileProcessor().readByteRange(startIndex, length));
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
