package com.namo.dist.server;

public enum ResponseCodes {
	
	SUCCESSFUL(200),PARTIAL(206),CACHED(304), NOT_FOUND(404);
	
	private int responseCode;
	
	private ResponseCodes(int value) {
		responseCode=value;
	}
	
	public int getResponseCode() {
		return responseCode;
	}

}
