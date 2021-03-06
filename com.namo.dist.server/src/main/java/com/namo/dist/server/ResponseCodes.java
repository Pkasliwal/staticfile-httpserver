package com.namo.dist.server;

/**
 * Enum defined for some of the HTTP response codes used as part of this static file server.
 */
public enum ResponseCodes {

	SUCCESSFUL(200), PARTIAL(206), CACHED(304), NOT_FOUND(404), PARTIAL_FAILURE(416), FILE_TOO_BIG(413),
	INTERNAL_SERVER_ERROR(500);

	private int responseCode;

	private ResponseCodes(int value) {
		responseCode = value;
	}

	public int getResponseCode() {
		return responseCode;
	}

}
