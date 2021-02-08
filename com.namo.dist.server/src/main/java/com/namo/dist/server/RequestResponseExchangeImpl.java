package com.namo.dist.server;

import java.net.URI;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

class RequestResponseExchangeImpl implements RequestResponseExchange {

	private HttpExchange exchange;
	
	RequestResponseExchangeImpl(HttpExchange exchange) {
		this.exchange = exchange;
	}
	
	@Override
	public URI getRequestURI() {
		return exchange.getRequestURI();
	}

	@Override
	public Headers getRequestHeaders() {
		return exchange.getRequestHeaders();
	}

	@Override
	public Headers getResponseHeaders() {
		return exchange.getResponseHeaders();
	}

	@Override
	public String getRequestMethod() {
		return exchange.getRequestMethod();
	}

}
