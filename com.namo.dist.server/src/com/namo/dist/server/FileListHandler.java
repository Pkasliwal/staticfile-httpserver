package com.namo.dist.server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class FileListHandler implements HttpHandler {

	private static final Logger logger = Logger.getLogger(FileListHandler.class.getName());
	private String filesDir;

	FileListHandler(String filesDirectory) {
		filesDir = filesDirectory;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		listFiles(exchange);
	}

	// TODO Need to change response to json
	// System outs need to be changed to logger
	private void listFiles(HttpExchange exchange) throws IOException {
		OutputStream out = exchange.getResponseBody();
		exchange.getResponseHeaders().add("Content-Type", "text/html");
		File path = new File(filesDir);
		if (path.exists()) {
			logger.log(Level.INFO,"Directory path: " + path.getAbsolutePath());
			File[] files = path.listFiles((File f) -> {
				return f.isFile();
			});
			System.out.println("number of files" + files.length);
			StringBuffer namesBuffer = new StringBuffer();
			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
				namesBuffer.append(files[fileIndex].getName());
				namesBuffer.append("\n");
			}
			byte[] outBytes = namesBuffer.toString().getBytes();
			exchange.sendResponseHeaders(200, outBytes.length);
			out.write(outBytes);
		} else {
			System.err.println("File not found: " + path.getAbsolutePath());

			exchange.sendResponseHeaders(404, 0);
			out.write("404 File not found.".getBytes());
		}

	}
}
