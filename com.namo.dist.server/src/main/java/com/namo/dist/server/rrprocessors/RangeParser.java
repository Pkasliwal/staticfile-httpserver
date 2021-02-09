package com.namo.dist.server.rrprocessors;

/**
 * Parser for HttpRange request header to extract the request start offset and
 * length of the requested range.
 */
class RangeParser {

	private long startIndex = 0;
	private long length;
	private String rangeStr;
	private long fullLength;

	/**
	 * Constructs parser for the given range header and complete file length.
	 * 
	 * @param rangeStr   requested range header
	 * @param fullLength file length
	 */
	RangeParser(String rangeStr, long fullLength) {
		this.rangeStr = rangeStr;
		this.length = fullLength;
		this.fullLength = fullLength;
	}

	/**
	 * Gets starting offset from where to retrieve file bytes.
	 * 
	 * @return starting offset
	 */
	public long getStartIndex() {
		return startIndex;
	}

	/**
	 * Gets length in bytes for the partial contents to be retrieved.
	 * 
	 * @return partial content length in bytes.
	 */
	public long getLength() {
		return length;
	}

	/**
	 * Range header value that gets parsed to extract the file index range to be
	 * retrieved.
	 * 
	 * @return range header value
	 */
	public String getRangeStr() {
		return rangeStr;
	}

	/**
	 * Gets Content-range response header value to be populated, formatted as "bytes
	 * <range-start>-<range-end>/<complete file length>"
	 * 
	 * @return Content-range response header value to be populated.
	 */
	public String contentRange() {
		return rangeStr.replace("=", " ") + "/" + fullLength;
	}

	/**
	 * Parses the provided range header value to extract start offset, length of the
	 * range etc. One needs to parse the range header first before using other
	 * getters on this class.
	 * 
	 * @return Parses the provided range header value, returns true for successful
	 *         parsing otherwise false when range header is not in supported format
	 *         or is constructed with incorrect index range.
	 */
	public boolean parse() {
		// Just parsing for one scenario for now, needs to be enhanced for other 3 cases
		// <unit>=<range-start>-<range-end>
		long lastIndex = 0;
		boolean validStr = false;
		String[] comps = rangeStr.split("=");
		if (comps.length > 1) {
			String[] subComps = comps[1].split("-");
			if (subComps.length > 1) {
				try {
					startIndex = Integer.valueOf(subComps[0].trim());
					lastIndex = Integer.valueOf(subComps[1].trim());
					length = lastIndex - startIndex + 1;
					validStr = true;
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}

		if (!validStr || startIndex >= fullLength || lastIndex >= fullLength || startIndex < 0
				|| startIndex > lastIndex) {
			return false;
		}
		return true;
	}
}
