package org.hsu.hsuapp.util;

public class StringUtil {

	public static String delFirstZero(String content) {
		return content.replaceFirst("^0*", "");
	}
	
}
