package com.dysen.opencard.common;

import com.dysen.commom_library.utils.LogUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by dysen on 1/31/2018.
 */

public class StrUtils {

    /**
     * 获取交易状态码
     * @param bytes
     * @return
     */
    public static String getTransStateeCode(byte[] bytes) {

        try {
            return new String(bytes, 14, 20, "gbk").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 	00000：交易成功
     * *	PUB_xxx：前置错误交易失败
     * 	其它：贷记卡错误交易失败
     * 	HB0188：没有找到客户记录
     * @param bytes
     * @return
     */
    public static String getState(byte[] bytes){

        String code = getTransStateeCode(bytes), str = "";
        if (code.equals("00000")) {
            str = ParamUtils.TRANS_SUCCESS;
        }else if (code.contains("PUB_xxx")) {
            str = ParamUtils.TRANS_PUB;
        }if (code.equals("其它")) {
            str = ParamUtils.TRANS_OTHER;
        }if (code.equals("HB0188")) {
            str = ParamUtils.TRANS_HB;
        }
        ParamUtils.transState = str;
        return str;
    }

    public static String getState(String code){

        String str = "";
        if (code.equals("00000")) {
            str = ParamUtils.TRANS_SUCCESS;
        }else if (code.contains("PUB_xxx")) {
            str = ParamUtils.TRANS_PUB;
        }if (code.equals("其它")) {
            str = ParamUtils.TRANS_OTHER;
        }if (code.equals("HB0188")) {
            str = ParamUtils.TRANS_HB;
        }
        ParamUtils.transState = str;
        return str;
    }

    public static String getStateInfo(byte[] bytes){

        LogUtils.d("bytes.len======2222=========="+bytes.length);
        try {
            return new String(bytes, 34, 80, "gbk").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String respStateInfo(int what) {
       String s = "";
        switch (what){
            case -1:
                s = "组装报文异常";
            break;
            case -2:
                s = "交易超时";
            break;
            case -3:
                s = "交易失败";
            break;
            case -100:
                s = "网络异常";
                break;
            case 100:
                s = "交易成功";
            break;
        }
        return s;
    }
}
