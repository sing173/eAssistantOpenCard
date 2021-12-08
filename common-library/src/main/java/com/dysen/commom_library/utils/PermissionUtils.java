package com.dysen.commom_library.utils;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by 胡田on 2018/7/6.
 * 系统动态申请权限类
 */

public class PermissionUtils {
	private static final int REQUEST_EXTERANL_STORAGE=1;
	private static String[] PERMISSIONS_STORAGE={
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION
	};
    public static void verrifyStoragePermissions(Activity activity){
    	/*int permission= ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
    	if(permission!= PackageManager.PERMISSION_GRANTED){
    		ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERANL_STORAGE);
	    }*/
    	if(ContextCompat.checkSelfPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
    		ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
		}
		if(ContextCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
		}
		if(ContextCompat.checkSelfPermission(activity,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)!=PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},3);
		}
		if(ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},4);
		}
		if(ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},5);
		}
    }
}
