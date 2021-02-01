package com.namo.dist.server;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public interface RequestResponseProcessor {
	
	void process(HttpExchange exchange, RequestContext ctx) throws IOException;
	
	RequestResponseProcessor appendNext(RequestResponseProcessor processor);

}
