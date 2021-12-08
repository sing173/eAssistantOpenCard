package com.dysen.opencard.base;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.dysen.commom_library.utils.FileUtils;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.opencard.common.ConstantValue;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.intfaceImpl.CrashHandler;
import com.dysen.opencard.intfaceImpl.LogCatManagerHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Application 基类
 * Created by hutian on 2018/2/25.
 */

public class BaseApplication extends Application {
     private static BaseApplication instance;
    private  static Context appContext;
   private static List<Activity> allActivities ;
//	private static Stack<Activity> allActivities;
	protected BluetoothAdapter mBlueToothAdapter;
	public int mIndex;
	private String certTypeIndex;
	private BluetoothManager blueToothManager;
	private static final int REQUEST_CODE = 0; // 请求码// 所需的全部权限
	static final String[] PERMISSIONS = new String[]{
			Manifest.permission.RECORD_AUDIO,
			Manifest.permission.MODIFY_AUDIO_SETTINGS,
			Manifest.permission.BLUETOOTH,
			Manifest.permission.BLUETOOTH_ADMIN,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};
	private String filePath;
	public static String Modle= ConstantValue.Test; //开发测试模式环境切换

	  public static  BaseApplication getInstance(){
	  	 if(instance==null){
	  	 	instance=new BaseApplication();
	     }
		  return instance;
	    }
	  public static  Context getAppContext(){

			return appContext;
		}
	   public  void  addActivity(Activity activity){
	  	if(allActivities==null){
	  		allActivities=new ArrayList<Activity>();
	     }
	     LogUtils.d("新增活动类名"+activity.getComponentName().getClassName());
	         allActivities.add(activity);
	   }

	/**
	 * 直接退出
	 */
	public static void exitApps(){
		if (allActivities.size() > 0) {
			for (Activity activity : allActivities) {
				String name=activity.getClass().getSimpleName();
				if (!name.equals("LoginActivity")){
					activity.finish();
				}
			}
//            android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	/**
	 * 完完全全退出应用程序
	 *  弹框
	 */

	public void exitApp() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("提示");
		builder.setMessage("您确定要退出程序吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (allActivities.size() > 0) {
					for (Activity activity : allActivities) {

						activity.finish();
					}
					android.os.Process.killProcess(android.os.Process.myPid());

				}

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		builder.show();
	}

	@Override
	public void onCreate() {

	   	instance=this;
	   	appContext=instance.getApplicationContext();
		blueToothManager=(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			mBlueToothAdapter=blueToothManager.getAdapter();
		}
		//记录操作日志
		LogCatManagerHelper logCatManagerHelper=LogCatManagerHelper.getInstance();
		logCatManagerHelper.startLogcatManager(appContext);
		if(Modle.equals(ConstantValue.Product)) {
			//记录崩溃日志
			CrashHandler crashHandler = new CrashHandler(appContext, "FkezsOpenCard");
			Thread.setDefaultUncaughtExceptionHandler(crashHandler);
		}else {//测试环境根据自己需要是否记录异常日志，或者直接控制台调试
			CrashHandler crashHandler = new CrashHandler(appContext, "FkezsOpenCard");
			Thread.setDefaultUncaughtExceptionHandler(crashHandler);
		}
		LogUtils.d("hut","记录日志======");
		initModle();
		//定期检查清理日志
		clearLog();
		super.onCreate();
	}

	/**
	 * 检查 联网交易环境  add by hutian  20180518
	 */
	private void initModle() {
		if(Modle.equals(ConstantValue.Test)){//测试环境
			ParamUtils.serverIp=ConstantValue.testIp;
			ParamUtils.serverPort=ConstantValue.testPort;
			ParamUtils.deviceNum=ConstantValue.testDeviceNum;
			ParamUtils.CrmUrl=ConstantValue.CrmTestUrl;
			ParamUtils.terllerFinger=ConstantValue.terllerFinger;  //测试方便开发指纹写死
			ParamUtils.terllerFinger2=ConstantValue.terllerFinger2;
		}else if(Modle.equals(ConstantValue.Product)){
			ParamUtils.serverIp=ConstantValue.serverIp;
			ParamUtils.serverPort=ConstantValue.serverPort;
			ParamUtils.deviceNum=ConstantValue.serverDeviceNum;
			ParamUtils.CrmUrl=ConstantValue.CrmServerUrl;
		}
	}

	private void clearLog() {
		//清理操作日志和异常报错日志
		Date currentDate=new Date();
		FileUtils.deleteLogFilebyDate(appContext,currentDate, -7);
	}

	/**
	 * 请求权限
	 *//*
	private void requestPermissions() {
		ActivityCompat.requestPermissions((Activity) appContext,PERMISSIONS,REQUEST_CODE);
	}*/

	/**
	 * 关闭Activity
	 */
	public static void finishActivity(){
		try {

			if (allActivities.size() > 0) {
				for (Activity activity : allActivities) {
					String name=activity.getClass().getSimpleName();
					activity.finish();
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}


}

