package com.namo.dist.server;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Implementation for {@link RequestFileProcessor} as needed for static file handling.
 */
class RequestFileProcessorImpl implements RequestFileProcessor {
	
	private File sourceFile;
	
	
	RequestFileProcessorImpl(File reqFile) {
		sourceFile = reqFile;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFile() {
		return sourceFile;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isModifiedAfter(String modifiedSince, DateTimeFormatter dtFormat) {
		Instant modifiedSinceInstant = Instant.from(dtFormat.parse(modifiedSince)).atOffset(ZoneOffset.UTC).toInstant();
		Instant lastModifiedInstant = Instant.ofEpochMilli(sourceFile.lastModified()).atOffset( ZoneOffset.UTC ).toInstant();
		return modifiedSinceInstant.toEpochMilli()<lastModifiedInstant.toEpochMilli();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastModifiedTime(DateTimeFormatter dtFormat) {
		OffsetDateTime lastModifiedOffsetTime = Instant.ofEpochMilli(sourceFile.lastModified()).atOffset( ZoneOffset.UTC );
		// Required format - <day-name>, <day> <month> <year> <hour>:<minute>:<second> GMT
		return lastModifiedOffsetTime.format(dtFormat);//DateTimeFormatter.RFC_1123_DATE_TIME);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContentType() throws IOException {
		return Files.probeContentType(sourceFile.toPath());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getFileLengthBytes() {
		return sourceFile.length();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getETag() {
		return Long.toHexString(sourceFile.lastModified())+"-"+Long.toHexString(getFileLengthBytes());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] readByteRange(long startingOffset, int length) throws IOException
	  {
		// TODO should add a check if startingOffset+length <= file length
	      try (RandomAccessFile randomAccessFile = new RandomAccessFile(sourceFile, "r"))
	      {
	          byte[] buffer = new byte[length];
	          randomAccessFile.seek(startingOffset);
	          randomAccessFile.readFully(buffer);

	          return buffer;
	      }
	  }

}
