package com.dysen.opencard.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dysen.opencard.R;

/**
 * Created by hutian on 2018/3/19.
 *Fragment 切换工具类
 */

public class FragmentUtils {
      private static Context context;
    private static FragmentTransaction transaction;
    private static FragmentManager fragmentManager;

      public void FragmentUtils(Context ct){
          context=ct;
      }
    public static void switchFragment(Context context, FragmentManager fragmentManager,Fragment source ,Bundle bd){
        transaction = fragmentManager.beginTransaction();
        if(bd!=null){
            source.setArguments(bd);
        }
        transaction.replace(R.id.fl_content, source);
        transaction.commitAllowingStateLoss();

    }
}
