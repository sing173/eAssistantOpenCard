package com.dysen.commom_library.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by dysen on 2018/5/15.
 */

public class ToastUtil {
	private static long lastTime = 0;
	/**
	 * Toast消息提示
	 *
	 * @param text
	 */
	public static void ShortToast(Context context,String text) {
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

	}
	public static void LongToast(Context context,String text) {
			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
}
