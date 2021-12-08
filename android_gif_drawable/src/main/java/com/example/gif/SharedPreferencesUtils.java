package com.example.gif;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences的一个工具类 同样调用getParam就能获取到保存在手机里面的数据
 * 
 * @author 李长亭
 * 
 */
public class SharedPreferencesUtils {
	/**
	 * 保存在手机里面的文件名
	 */
	private static final String FILE_NAME = "share_dat";

	// public static String FILE_USER = "share" + User.userID;

	/**
	 * 保存数据的方法
	 * 
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void setParam(Context context, String key, String st) {

		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, st);
		editor.commit();
	}

	public static void setNum(Context context, String key, int num) {

		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, num);
		editor.commit();
	}

	/**
	 * 保存long型的数据比如：时间戳
	 * 
	 * @param context
	 *            上下文对象
	 * @param key
	 *            键
	 * @param num
	 *            键所对应的值
	 */

	public static void setData(Context context, String key, Long num) {

		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong(key, num);
		editor.commit();
	}

	/**
	 * 
	 * @param context
	 *            上下文对象
	 * @param key
	 *            用户id
	 * @param num
	 *            笑话id
	 */
	public static void setUser(Context context, String adress, String key,
			String num) {

		SharedPreferences sp = context.getSharedPreferences(adress,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, num);
		editor.commit();
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static String getParam(Context context, String key, String st) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, st);
	}

	public static int getNum(Context context, String key, int num) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(key, num);
	}

	public static long getData(Context context, String key, Long num) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getLong(key, num);
	}

	/**
	 * 
	 * @param context
	 *            上下文对象
	 * @param key
	 *            笑话id
	 * @param num
	 *            得到的用户点击的踩或赞
	 */
	public static String getUser(Context context, String adress, String key,
			String num) {
		SharedPreferences sp = context.getSharedPreferences(adress,
				Context.MODE_PRIVATE);
		return sp.getString(key, num);
	}
}
