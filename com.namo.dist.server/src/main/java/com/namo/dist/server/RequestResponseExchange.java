package com.namo.dist.server;

import java.net.URI;

import com.sun.net.httpserver.Headers;

/**
 * Interface provides methods for examining the request from the client, and for building response headers.
 */
public interface RequestResponseExchange {

    /**
     * Get the request URI
     *
     * @return the request URI
     */
    public URI getRequestURI () ;
    
	/**
     * Returns an immutable Map containing the HTTP headers that were
     * included with this request. The keys in this Map will be the header
     * names, while the values will be a List of Strings containing each value
     * that was included (either for a header that was listed several times,
     * or one that accepts a comma-delimited list of values on a single line).
     * In either of these cases, the values for the header name will be
     * presented in the order that they were included in the request.
     * <p>
     * The keys in Map are case-insensitive.
     * @return a read-only Map which can be used to access request headers
     */
    public Headers getRequestHeaders () ;

    /**
     * Returns a mutable Map into which the HTTP response headers can be stored
     * and which will be transmitted as part of this response. The keys in the
     * Map will be the header names, while the values must be a List of Strings
     * containing each value that should be included multiple times
     * (in the order that they should be included).
     * <p>
     * The keys in Map are case-insensitive.
     * @return a writable Map which can be used to set response headers.
     */
    public Headers getResponseHeaders () ;
    
    /**
     * Get the request method
     * @return the request method
     */
    public String getRequestMethod();
}
