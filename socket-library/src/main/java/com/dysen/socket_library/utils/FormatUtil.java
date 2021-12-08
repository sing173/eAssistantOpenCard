package com.dysen.socket_library.utils;

public class FormatUtil {
	public static String byteToHexString(byte byteData, boolean needPad)
	  {
	    String hex = Integer.toHexString(byteData & 0xFF).toUpperCase();
	    if ((needPad == true) && 
	      (hex.length() < 2))
	    {
	      hex = "0" + hex;
	    }

	    return hex;
	  }
}
