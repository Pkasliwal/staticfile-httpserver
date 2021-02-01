package com.namo.dist.server;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

class RequestFileProcessorImpl implements RequestFileProcessor {
	
	private File sourceFile;
	public RequestFileProcessorImpl(File reqFile) {
		sourceFile = reqFile;
	}
	
	@Override
	public File getFile() {
		return sourceFile;
	}
	
	@Override
	public boolean isModifiedAfter(String modifiedSince, DateTimeFormatter dtFormat) {
		Instant modifiedSinceInstant = Instant.from(dtFormat.parse(modifiedSince)).atOffset(ZoneOffset.UTC).toInstant();
		Instant lastModifiedInstant = Instant.ofEpochMilli(sourceFile.lastModified()).atOffset( ZoneOffset.UTC ).toInstant();
		return modifiedSinceInstant.toEpochMilli()<lastModifiedInstant.toEpochMilli();
	}
	@Override
	public String getLastModifiedTime(DateTimeFormatter dtFormat) {
		OffsetDateTime lastModifiedOffsetTime = Instant.ofEpochMilli(sourceFile.lastModified()).atOffset( ZoneOffset.UTC );
		// Required format - <day-name>, <day> <month> <year> <hour>:<minute>:<second> GMT
		return lastModifiedOffsetTime.format(dtFormat);//DateTimeFormatter.RFC_1123_DATE_TIME);
		
	}
	
	@Override
	public String getContentType() throws IOException {
		return Files.probeContentType(sourceFile.toPath());
	}
	@Override
	public Long getFileLengthBytes() {
		return sourceFile.length();
	}
	
	@Override
	public String getETag() {
		return Long.toHexString(sourceFile.lastModified())+"-"+Long.toHexString(getFileLengthBytes());
	}
	
	@Override
	public byte[] readByteRange(long startingOffset, int length) throws IOException
	  {
	      try (RandomAccessFile randomAccessFile = new RandomAccessFile(sourceFile, "r"))
	      {
	          byte[] buffer = new byte[length];
	          randomAccessFile.seek(startingOffset);
	          randomAccessFile.readFully(buffer);

	          return buffer;
	      }
	  }

}
