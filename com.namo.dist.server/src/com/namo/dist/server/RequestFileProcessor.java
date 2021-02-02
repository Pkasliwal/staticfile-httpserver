package com.namo.dist.server;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * This processor supports request response processors to provide information to be populated on different response headers.
 */
public interface RequestFileProcessor {

	/**
	 * Gets file instance for the request.
	 * @return requested file instance.
	 */
	File getFile();

	/**
	 * Checks if file has been modified since the provided modifiedSince date.
	 * @param modifiedSince date to be compared with the last modified timestamp on the requested file.
	 * @param dtFormat date time format for provided date
	 * @return true if the requested file's last modified time is after the provided time/date.
	 */
	boolean isModifiedAfter(String modifiedSince, DateTimeFormatter dtFormat);

	/**
	 * Gets requested file's last modified time in required date time format.
	 * @param dtFormat date time format
	 * @return last modified time in required date time format
	 */
	String getLastModifiedTime(DateTimeFormatter dtFormat);

	/**
	 * Gets the requested file's content/mime type.
	 * @return requested file's content/mime type.
	 * @throws IOException exception retrieving file's content type
	 */
	String getContentType() throws IOException;

	/**
	 * Gets file size in bytes.
	 * @return file size in bytes.
	 */
	Long getFileLengthBytes();

	/**
	 * Calculates file's Etag hex(modifiedTime)-hex(sizeInBytes).
	 * @return file's ETag
	 */
	String getETag();

	/**
	 * Reads requested file byte range.
	 * @param startingOffset starting offset for the byte range
	 * @param length number of bytes to be read
	 * @return requested byte array
	 * @throws IOException exception thrown when there is an issue while retrieving file bytes.
	 */
	byte[] readByteRange(long startingOffset, int length) throws IOException;

}