package com.namo.dist.server;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public interface RequestFileProcessor {

	File getFile();

	boolean isModifiedAfter(String modifiedSince, DateTimeFormatter dtFormat);

	String getLastModifiedTime(DateTimeFormatter dtFormat);

	String getContentType() throws IOException;

	Long getFileLengthBytes();

	String getETag();

	byte[] readByteRange(long startingOffset, int length) throws IOException;

}