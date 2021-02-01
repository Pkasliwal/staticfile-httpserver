package com.namo.dist.server.rrprocessors;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseProcessor;
import com.sun.net.httpserver.HttpExchange;

class FileExistsRRProcessor implements RequestResponseProcessor {

	// Files directory could be defined using environment variable to add
	// flexibility
	private static final String FILES_DIR = "files";

	private RequestResponseProcessor nextProcessor;

	@Override
	public void process(HttpExchange exchange, RequestContext ctx) throws IOException {

		URI reqUri = exchange.getRequestURI();
		String name = new File(reqUri.getPath()).getName();
		File requestFile = new File(FILES_DIR, name);

		if (requestFile.exists()) {
			ctx.setRequestedFile(requestFile);
			exchange.getResponseHeaders().set("Content-Type", ctx.getRequestedFileProcessor().getContentType());
			exchange.getResponseHeaders().set("Content-Length", ctx.getRequestedFileProcessor().getFileLengthBytes().toString());
			exchange.getResponseHeaders().set("Accept-Ranges", ctx.isAcceptRanges()?"bytes":"none");
		} else {
			ctx.setResponseCode(404);
			System.err.println("File not found: " + requestFile.getAbsolutePath());
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
