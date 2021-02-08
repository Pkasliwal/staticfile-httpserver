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

	public RangeParser(String rangeStr, long fullLength) {
		this.rangeStr = rangeStr;
		this.length = fullLength;
		this.fullLength = fullLength;
	}

	public long getStartIndex() {
		return startIndex;
	}

	public long getLength() {
		return length;
	}

	public String getRangeStr() {
		return rangeStr;
	}

	public String contentRange() {
		return rangeStr.replace("=", " ") + "/" + fullLength;
	}

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
					length = lastIndex - startIndex +1;
					validStr = true;
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}

		if (!validStr || startIndex >= fullLength || lastIndex >= fullLength || startIndex < 0 || startIndex > lastIndex) {
			return false;
		}
		return true;
	}
}
