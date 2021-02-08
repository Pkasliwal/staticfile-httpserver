package com.namo.dist.server.rrprocessors;

import java.io.IOException;

import com.namo.dist.server.RequestContext;
import com.namo.dist.server.RequestResponseExchange;
import com.namo.dist.server.RequestResponseProcessor;
import com.namo.dist.server.ResponseCodes;

/**
 * Performs file access as needed to retrieve the request file/http range bytes.
 */
class FileRRProcessor implements RequestResponseProcessor {

	private RequestResponseProcessor nextProcessor;
	private static int BYTE_LEN_1MB = 1000000;
	private static int BYTE_LEN_5MB = 5000000;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(RequestResponseExchange exchange, RequestContext ctx) throws IOException {
		if (ctx.getResponseCode() > 0)
			return;
		long startIndex = 0;
		long fullLength = ctx.getRequestedFileProcessor().getFileLengthBytes();
		long length = fullLength;

		// Supports range request when file size is > 1MB and range headers are being
		// requested.
		// Server can choose to respond with complete file even when a range is
		// requested as defined in the protocol
		if (fullLength > BYTE_LEN_1MB && exchange.getRequestHeaders().containsKey("Range")
				&& !exchange.getRequestHeaders().getFirst("Range").isEmpty()) {

			// RangeParser parses the header information to extract the requested range to
			// be read from file.
			RangeParser parser = new RangeParser(exchange.getRequestHeaders().getFirst("Range"), fullLength);
			if (parser.parse()) {
				startIndex = parser.getStartIndex();
				length = parser.getLength();
				if (length > BYTE_LEN_5MB) {
					// Requested range is too big
					ctx.setResponseCode(ResponseCodes.FILE_TOO_BIG.getResponseCode());
				} else {
					exchange.getResponseHeaders().set("Content-Range", parser.contentRange());
					exchange.getResponseHeaders().set("Content-Length", String.valueOf(length));

					// When returning partial range, success response code is 206
					ctx.setResponseCode(ResponseCodes.PARTIAL.getResponseCode());
					ctx.setResponseBytes(ctx.getRequestedFileProcessor().readByteRange(startIndex, (int)length));
				}
			} else {
				// Failure code for partial range retrieval is 416
				ctx.setResponseCode(ResponseCodes.PARTIAL_FAILURE.getResponseCode());
			}
		} else {
			// case when retrieving the complete file
			if (fullLength > BYTE_LEN_5MB) {
				ctx.setResponseCode(ResponseCodes.FILE_TOO_BIG.getResponseCode());
			} else {
				ctx.setResponseCode(ResponseCodes.SUCCESSFUL.getResponseCode());
				ctx.setResponseBytes(ctx.getRequestedFileProcessor().readByteRange(startIndex, (int)length));
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
