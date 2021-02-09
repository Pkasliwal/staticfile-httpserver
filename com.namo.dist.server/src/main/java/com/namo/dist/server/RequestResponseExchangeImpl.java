package com.namo.dist.server;

import java.net.URI;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

/**
 * Class encapsulating a {@link HttpExchange} instance and provides methods for examining the exchange's request from the client, and for building response headers.
 */
class RequestResponseExchangeImpl implements RequestResponseExchange {

	private HttpExchange exchange;
	
	RequestResponseExchangeImpl(HttpExchange exchange) {
		this.exchange = exchange;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public URI getRequestURI() {
		return exchange.getRequestURI();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Headers getRequestHeaders() {
		return exchange.getRequestHeaders();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Headers getResponseHeaders() {
		return exchange.getResponseHeaders();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRequestMethod() {
		return exchange.getRequestMethod();
	}

}
