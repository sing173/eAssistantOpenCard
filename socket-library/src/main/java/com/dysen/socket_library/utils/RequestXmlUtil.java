package com.dysen.socket_library.utils;

public class RequestXmlUtil {
	public static String fill(String s1, String s2, int len) {
		byte[] b = s1.getBytes();
		if (b.length>=len) {
			return s1;
		}
		StringBuffer bf = new StringBuffer(s1);
		int start = b.length;
		for (int i=start; i<len; i++) {
			bf.append(s2);
		}
		return bf.toString();
	}
}
