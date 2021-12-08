package com.dysen.commom_library.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dysen on 2017/6/30.
 */

public class StringUtils {

    /**
     * 去除字符串里的特殊字符
     * @param str
     * @return
     */
    public static String replaceBlank(String str){
        String dest = "";
        if (str != null){
            Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
            Matcher matcher = pattern.matcher(str);
            dest = matcher.replaceAll("");
        }
        return dest;
    }

    /**
     * 调换 证件时间 顺序
     * @param certDate
     * @return
     */
    public static String setCertDate(String certDate) {
        return certDate.substring(certDate.length() - 2) + certDate.substring(certDate.length() - 4, certDate.length() - 2) + certDate.substring(0, certDate.length() - 4);
    }
    /**
     * 处理身份证类型，账号类型
     * @param
     * @return
     */
    public static String splitString(String certTypeStr,int type) {
        //01:身份证
        String str="";
        if(!TextUtils.isEmpty(certTypeStr)) {
            int index = certTypeStr.indexOf(":");
            String[] a = certTypeStr.split(":");
            LogUtils.i("hut", "index=" + index);
            LogUtils.i("hut", "index1=" + a[0] + "=" + a[1]);
            if (type == 0) { //取：前面
                str = certTypeStr.substring(0, certTypeStr.indexOf(":"));
            } else if (type == 1) {//取后面
               // str = certTypeStr.substring(index + 1, certTypeStr.length());
                str=a[1];
            }
        }
        return str;
    }

}
