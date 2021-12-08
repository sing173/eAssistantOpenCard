package com.dysen.opencard.intfaceImpl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by hutian on 2018/7/2.
 * 记录每日异常本地日志  每天报错记录一个异常日志
 */
@SuppressLint({"SimpleDateFormat", "SdCardPath"})
public class CrashHandler implements Thread.UncaughtExceptionHandler{
	public static final String TAG = "CrashHandler";
	private UncaughtExceptionHandler mDefaultHandler;
	private Context mContext;
	private Map<String, String> infos = new HashMap();
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private String appName;

	public CrashHandler(Context context, String appName) {
		this.mContext = context;
		this.appName = appName;
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	public void uncaughtException(Thread thread, Throwable ex) {
		if(!this.handleException(ex) && this.mDefaultHandler != null) {
			this.mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException var4) {
				var4.printStackTrace();
			}

			System.exit(0);
		}

	}

	private boolean handleException(Throwable ex) {
		if(ex == null) {
			return false;
		} else {
			(new Thread() {
				public void run() {
					Looper.prepare();
					Toast.makeText(CrashHandler.this.mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
					Looper.loop();
				}
			}).start();
			this.collectDeviceInfo(this.mContext);
			this.saveCrashInfo2File(ex);
			return ex.getMessage().endsWith("Binary XML file line #21: Error inflating class <unknown>")?false:(ex.getMessage().endsWith("Binary XML file line #21: Error inflating class android.widget.LinearLayout")?false:!ex.getMessage().endsWith("android.database.sqlite.SQLiteCantOpenDatabaseException: unknown error (code 14): Could not open database"));
		}
	}

	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 1);
			if(pi != null) {
				String versionName = pi.versionName == null?"null":pi.versionName;
				String versionCode = String.valueOf(pi.versionCode);
				this.infos.put("versionName", versionName);
				this.infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException var9) {
			var9.printStackTrace();
		}

		Field[] fields = Build.class.getDeclaredFields();
		Field[] var6 = fields;
		int var13 = fields.length;

		for(int var12 = 0; var12 < var13; ++var12) {
			Field field = var6[var12];

			try {
				field.setAccessible(true);
				this.infos.put(field.getName(), field.get((Object)null).toString());
			} catch (Exception var8) {
				var8.printStackTrace();
			}
		}

	}

	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		Iterator var4 = this.infos.entrySet().iterator();

		String result;
		while(var4.hasNext()) {
			Entry<String, String> entry = (Map.Entry)var4.next();
			String key = (String)entry.getKey();
			result = (String)entry.getValue();
			sb.append(key + " = " + result + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);

		for(Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
			cause.printStackTrace(printWriter);
		}

		printWriter.close();
		result = writer.toString();
		sb.append(result);

		try {
			long timestamp = System.currentTimeMillis();
			String time = this.formatter.format(new Date());
			String fileName = "crash-" + time+ ".log";
			if(Environment.getExternalStorageState().equals("mounted")) {
				String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crash/" + this.appName+ "/";
				File dir = new File(path);
				if(!dir.exists()) {
					dir.mkdirs();
				}

				FileOutputStream fos = new FileOutputStream(path+fileName,true);
				fos.write(sb.toString().getBytes());
				fos.close();
			}

			return fileName;
		} catch (Exception var14) {
			var14.printStackTrace();
			return null;
		}
	}
}
