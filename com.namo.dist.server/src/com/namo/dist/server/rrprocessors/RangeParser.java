package com.namo.dist.server.rrprocessors;

/**
 * Parser for HttpRange request header to extract the request start offset and
 * length of the requested range.
 */
class RangeParser {

	private int startIndex = 0;
	private int length;
	private String rangeStr;
	private int fullLength;

	public RangeParser(String rangeStr, int fullLength) {
		this.rangeStr = rangeStr;
		this.length = fullLength;
		this.fullLength = fullLength;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getLength() {
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
		int lastIndex = 0;
		boolean validStr = false;
		String[] comps = rangeStr.split("=");
		if (comps.length > 1) {
			String[] subComps = comps[1].split("-");
			if (subComps.length > 1) {
				try {
					startIndex = Integer.valueOf(subComps[0].trim());
					lastIndex = Integer.valueOf(subComps[1].trim());
					validStr = true;
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}

		if (!validStr || startIndex >= length || lastIndex >= length) {
			return false;
		}
		return true;
	}
}
