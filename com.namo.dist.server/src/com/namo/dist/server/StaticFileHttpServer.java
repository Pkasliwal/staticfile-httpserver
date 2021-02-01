package com.namo.dist.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class StaticFileHttpServer {

	// ThreadPoolSize could be defined using an env variable to add flexibility
	// based on where its deployed and requirements
	private static final ExecutorService executors = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	// Files directory could be defined using environment variable to add
	// flexibility
	private static final String FILES_DIR = "files";
	private static final String STATIC_FILES_PATH = "/staticfiles";
	private static final String SERVER_PORT_STR = System.getenv("SERVER_PORT");
	private static final String LOG_LEVEL_STR = System.getenv("LOG_LEVEL");
	private static int SERVER_PORT = 8500;
	// TODO Logger log level needs to be configured
	private static Level LOG_LEVEL = Level.INFO;
	private static final Logger logger = Logger.getLogger(StaticFileHttpServer.class.getName());

	static {
		if (SERVER_PORT_STR != null) {
			try {
				SERVER_PORT = Integer.parseInt(SERVER_PORT_STR);

			} catch (NumberFormatException ex) {
				logger.log(Level.SEVERE, "SERVER_PORT env variable parsing error", ex);
			}
		}
		if (LOG_LEVEL_STR != null) {
			try {
				LOG_LEVEL = Level.parse(LOG_LEVEL_STR);
			} catch (IllegalArgumentException ex) {
				logger.log(Level.SEVERE, "LOG_LEVEL env variable is not correct", ex);
			}
		}
	}

	// StaticFileHttpServer instance variables
	private HttpServer server = null;
	private StaticFileHandler staticFileHandler = new StaticFileHandler();
	private FileListHandler fileListHandler = new FileListHandler(FILES_DIR);

	public static void main(String[] args) throws IOException {
		StaticFileHttpServer httpServer = new StaticFileHttpServer();
		httpServer.startServer();
	}

	public void startServer() throws IOException {
		// Log port info
		// Log logger level
		server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);

		// For multi threaded web server
		server.setExecutor(executors);
		HttpContext context = server.createContext(STATIC_FILES_PATH);
		context.setHandler(this::handleRequest);
		server.start();
		logger.log(Level.INFO, "Server Started....");
	}

	@Override
	public void finalize() {
		try {
			server.stop(SERVER_PORT);
			logger.log(Level.INFO, "Stopping StaticHttpSever");
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, "Error while stopping server in finalize method", e);
		}
	}

	private void handleRequest(HttpExchange exchange) throws IOException {
		OutputStream out = exchange.getResponseBody();
		try {
			// What about trailing slash?
			if (STATIC_FILES_PATH.equals(exchange.getRequestURI().getPath())) {
				this.fileListHandler.handle(exchange);
			} else if (exchange.getRequestURI().getPath().startsWith(STATIC_FILES_PATH)) {
				this.staticFileHandler.handle(exchange);
			} else {
				exchange.sendResponseHeaders(404, 0);
				out.write("Provided path not allowed.".getBytes());
			}
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "IOException while handling the request" + exchange.getRequestURI().getPath(), ex);
			throw ex;
		} finally {
			out.close();
		}
	}

	public static Level getLogLevel() {
		return LOG_LEVEL;
	}
}
