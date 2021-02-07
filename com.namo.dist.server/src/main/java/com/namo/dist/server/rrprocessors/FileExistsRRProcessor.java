package com.namo.dist.server.rrprocessors;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseExchange;
import com.namo.dist.server.RequestResponseProcessor;
import com.namo.dist.server.ResponseCodes;

/**
 * Processor to verify if file exists and append required response headers.
 */
class FileExistsRRProcessor implements RequestResponseProcessor {

	// Files directory could be defined using environment variable to add
	// flexibility
	private static final String FILES_DIR = "files";
	private static final Logger logger = Logger.getLogger(FileExistsRRProcessor.class.getName());
	
	static final String CONTENT_LENGTH = "Content-Length";
	static final String ACCEPT_RANGE = "Accept-Ranges";
	static final String CONTENT_TYPE = "Content-Type";
	static final String[] EXPECTED_HEADERS = {CONTENT_LENGTH, ACCEPT_RANGE, CONTENT_TYPE};
	
	private RequestResponseProcessor nextProcessor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(RequestResponseExchange exchange, RequestContext ctx) throws IOException {

		URI reqUri = exchange.getRequestURI();
		String name = new File(reqUri.getPath()).getName();
		File requestFile = new File(FILES_DIR, name);

		if (requestFile.exists()) {
			ctx.setRequestedFile(requestFile);
			// Adding basic response headers for the requested file
			exchange.getResponseHeaders().set(CONTENT_TYPE, ctx.getRequestedFileProcessor().getContentType());
			exchange.getResponseHeaders().set(CONTENT_LENGTH,
					ctx.getRequestedFileProcessor().getFileLengthBytes().toString());
			exchange.getResponseHeaders().set(ACCEPT_RANGE, ctx.isAcceptRanges() ? "bytes" : "none");
		} else {
			ctx.setResponseCode(ResponseCodes.NOT_FOUND.getResponseCode());
			logger.log(Level.SEVERE, "File not found: " + requestFile.getAbsolutePath());
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
