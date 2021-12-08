/**
 * 作者:胡田
 * 日期: 2015 
 * 时间: 2015年8月17日下午2:31:04
 * 开发工具:Android Studio
 * 开发语言:Java/Android
 * 版本:移动展厅1.0
 * 公司:文思海辉信息技术有限公司
 */
package com.dysen.socket_library.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 描述：日期处理工具类 作者:hutian 日期: 2015年 时间: 2015年8月17日下午2:31:04 开发工具:Android Studio 开发语言:Eclipse ADT 版本:移动展厅1.0 公司:文思海辉信息技术有限公司
 */
public class DateUtils {
	public static String getDateCN() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
		String date = format.format(new Date(System.currentTimeMillis()));
		return date;// 2012年10月03日 23:41:31
	}

	public static String getDateEN() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date1 = format1.format(new Date(System.currentTimeMillis()));
		return date1;// 2012-10-03 23:41:31
	}
	/** 得到当前日期 */
	public static String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String date = format.format(new Date());
		return date;
	}

	/** 计算两个时间的差值，返回天 */
	public static long timeDistance(long time1, long time2) {
		long time = time2 - time1;
		return time / (1000 * 60 * 60 * 24);
	}

	public static String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String date = format.format(new Date(System.currentTimeMillis()));
		return date;
	}

	public static String getDateJy(Date date) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(date);
		return date1;// 2012-10-03
	}

	/**
	 * 判断是否为合法的日期时间字符串
	 * 
	 * @param str_input
	 * @param str_input
	 * @return boolean;符合为true,不符合为false
	 */
	public static boolean isDate(String str_input, String rDateFormat) {
		if (!isNull(str_input)) {
			SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
			formatter.setLenient(false);
			try {
				formatter.format(formatter.parse(str_input));
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isNull(String str) {
		if (str == null)
			return true;
		else
			return false;
	}

	/**
	 * 获取当前系统时间
	 * 
	 * @return
	 */
	public static String getDate(int type) {
		String date = "";
		switch (type) {
		case 0:
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式
			date = df.format(new Date());
			break;
		case 1:
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd"); // 只显示当前日期
			date = df1.format(new Date());
			break;
		case 2:
			SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss"); // 只显示当前时间
			date = df2.format(new Date());
			break;
		case 3:
			SimpleDateFormat df3 = new SimpleDateFormat("yyyyMMdd"); // 只显示当前时间
			date = df3.format(new Date());
			break;
		}
		return date;
	}

	/**
	 * 将分钟数转成 时：分格式
	 */
	public static String getTimeByMinute(int minute) {
		String strTime = "";
		int hs = minute * 60 * 1000;
		strTime = formatLongToTimeStr(hs);
		strTime = fillZero(strTime);
		return strTime;
	}

	/**
	 * 格式化小时分钟
	 * 
	 * @param hs
	 * @return
	 */
	public static String formatLongToTimeStr(int hs) {
		int hour = 0;
		int minute = 0;
		int second = 0;
		second = hs / 1000;
		if (second > 60) {
			minute = second / 60;
			second = second % 60;
		}

		if (minute > 60) {
			hour = minute / 60;
			minute = minute % 60;
		}

		String strtime = hour + ":" + minute;
		return strtime;
	}

	/**
	 * 小时，分钟不足两位，用0补齐
	 */
	public static String fillZero(String time) {
		String newTime = "";
		String[] timeStr = time.split(":");
		if (timeStr.length > 1) {
			String hour = timeStr[0];
			String minute = timeStr[1];
			if (Integer.parseInt(hour) < 10) {
				hour = "0" + hour;
			}
			if (Integer.parseInt(minute) < 10) {
				minute = "0" + minute;
			}
			newTime = hour + ":" + minute;
		}
		return newTime;
	}

	/**
	 * 判断两个日期间隔 大小
	 */
	public static int compareDate(String DATE) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = df.format(new Date());
		try {
			Date dt1 = df.parse(DATE);
			Date now = df.parse(nowDate);
			if (dt1.getTime() > now.getTime()) {
				return 1;
			} else if (dt1.getTime() < now.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 判断两个日期间隔 是否大于一天
	 */
	public static boolean compare_date1(String DATE) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = df.format(new Date());
		try {
			Date dt1 = df.parse(DATE);
			Date now = df.parse(nowDate);
			long daysBetween = (now.getTime() - dt1.getTime() + 1000000) / (3600 * 24 * 1000);
			if (daysBetween > 1) {
				return false;
			} else if (daysBetween <= 1) {
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return true;
	}
	/**
	 * 判断当前日期前后几天日期 -1 前一天，1 往后一天
	 */
	public static String compare_date(int  diff) {
		String date="";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH,diff);
		Date newDate=calendar.getTime();
		date=df.format(newDate);
		return date;
	}
	/**
	 * 判断给定时间在否在给定两个时间之间
	 * 
	 * @param star
	 * @param end
	 * @return
	 */
	public static boolean compare_2times(String star, String end) {
		SimpleDateFormat localTime = new SimpleDateFormat("HH:mm:ss");
		try {
			Date sDate = localTime.parse(star);
			Date eDate = localTime.parse(end);
			// 当前时间
			Calendar calendar = Calendar.getInstance();
			String todayStr = localTime.format(calendar.getTime());
			Date today = localTime.parse(todayStr);

			long sTime = sDate.getTime();
			long time = today.getTime();
			long eTime = eDate.getTime();
			if (time >= sTime && time <= eTime) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
		}

		return false;
	}

	/**
	 * 判断是否为工作日
	 * 
	 * @return
	 */
	public static boolean isWorkDay() {
		/** 定义日期对象 */
		Calendar c = Calendar.getInstance();
		Date today = c.getTime();
		// 判断是周几
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int weekDay = cal.get(Calendar.DAY_OF_WEEK);

		if (weekDay == 1 || weekDay == 7) {
			return false;
		} else {
			return true;
		}
	}

	/** yyyy-mm-dd -> yyyy-mm */
	public static String formatToYYYY_MM(String str) {
		return str.substring(0, 7);
	}

	/** yyyymmdd -> yyyymm */
	public static String toYYYYMM(String str) {
		return str.substring(0, 6);
	}

	/** yyyy-mm-dd -> yyyymm */
	public static String formatToYYYYMM(String str) {
		return str.substring(0, 7).replace("-", "");
	}

	/** yyyymm -->yyyy-mm-dd */
	public static String formatToYYYY_MM_DD(String str) {
		return str.substring(0, 7).replace("-", "");
	}

	/** yyyyMMdd ->> yyyyy年MM月dd日 */
	public static String formatToChineseDate(String source) {
		if (source == null || source.length() != 8) {
			return "";
		}
		return source.substring(0, 4) + "年" + source.substring(4, 6) + "月" + source.substring(6, 8) + "日";
	}

	public static String getDateYMDHMS() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String date1 = format1.format(new Date(System.currentTimeMillis()));
		return date1;
	}

	public static String convertYYYYYYDDHHMMSS(long millis) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String date1 = format1.format(new Date(millis));
		return date1;
	}

	/**
	 * 将时间字符串转换成想要的格式时间字符串
	 * 
	 * @param datetime
	 * @param fromPattern
	 * @param toPattern
	 * @return
	 */
	public static String formatDate(String datetime, String fromPattern, String toPattern) {
		SimpleDateFormat format1 = new SimpleDateFormat(fromPattern);
		SimpleDateFormat format2 = new SimpleDateFormat(toPattern);
		Date date;
		try {
			date = format1.parse(datetime);
			return format2.format(date);
		} catch (ParseException e) {
			return null;
		}
	}
}
