package com.namo.dist.server.rrprocessors;

import java.net.URI;

import com.namo.dist.server.RequestResponseExchange;
import com.sun.net.httpserver.Headers;

class DummyRequestResponseExchange implements RequestResponseExchange {

	private URI requestUri;
	private Headers reqHeaders;
	private Headers resHeaders;
	
	DummyRequestResponseExchange(URI requestUri) {
		this.requestUri=requestUri;
		reqHeaders = new Headers();
		resHeaders = new Headers();
	}
	
	@Override
	public URI getRequestURI() {
		return requestUri;
	}

	@Override
	public Headers getRequestHeaders() {
		return reqHeaders;
	}

	@Override
	public Headers getResponseHeaders() {
		return resHeaders;
	}

}
