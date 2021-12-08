package com.dysen.socket_library.utils;

import com.dysen.socket_library.SocketThread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hutian on 2018/1/31.
 *
 * 交易转换接口，转换字符编码
 */

public class TransParseStringEncodUtil {
    public static byte trans_997340[];
    public static byte trans_997341[];
    public static ArrayList<String> responseListTemp=new ArrayList<String>();

    /**
     * byte数组截取转换成字符串
     * @param bytes     byte数组
     * @param index     下标
     * @param offset    截取长度
     * @return
     */
    public static String byte2StrIntercept(byte[] bytes, int index, int offset) {

        try {
            return new String(bytes, index, offset, "gbk").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getUtfToGbk(String str) {
        String gbk="";
        try {
            byte[] bytesUtf=str.getBytes("utf-8");
            byte[] bytesGBK=str.getBytes("GBK");
            String unicode=new String(bytesUtf,"ISO-8859-1");
            byte[] unicodebyte=str.getBytes("ISO-8859-1");
             gbk=new String(bytesGBK,"gbk");
            String utf=new String(bytesUtf,"utf-8");

            LogUtils.d("hut","请求激活字节：="+unicode.toString());
            LogUtils.d("hut","请求gbk：="+gbk.toString());
            LogUtils.d("hut","请求UTF：="+utf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return gbk;
    }
    public static List<String> getResponList (byte bytes[],String transCode){
        ArrayList<String> responseList=new ArrayList<String>();
        if (transCode.equals(SocketThread.signIn)) {//签名

        } else if (transCode.equals(SocketThread.signOut)) {//签退

        } else if (transCode.equals(SocketThread.tempWithdrawal)) {

        } else if (transCode.equals(SocketThread.forcedWithdrawal)) {

        } else if (transCode.equals(SocketThread.localAuthorization)) {

        } else if (transCode.equals(SocketThread.selectCus)) {//查询客户信息
//            responseList.add(byte2StrIntercept(bytes, 158, 19));//客户号
//            responseList.add(byte2StrIntercept(bytes,177, 40)); //客户姓名
        } else if (transCode.equals(SocketThread.createCus)) {

        } else if (transCode.equals(SocketThread.openCard)) {

        } else if (transCode.equals(SocketThread.selectPwd)) {//查询卡密码
            try {
                responseList.add(byte2StrIntercept(bytes, 158, 19));//卡号
                responseList.add(byte2StrIntercept(bytes,177, 40)); //卡产品
                responseList.add(byte2StrIntercept(bytes,217, 17));
                responseList.add(byte2StrIntercept(bytes,234, 40));
                responseList.add(byte2StrIntercept(bytes,274, 120));
                responseList.add(byte2StrIntercept(bytes,394, 20));
                responseList.add(byte2StrIntercept(bytes,414, 20));
                responseList.add(byte2StrIntercept(bytes,434, 20));
                responseList.add(byte2StrIntercept(bytes,454, 8));
                responseList.add(byte2StrIntercept(bytes,462,	26));
                responseList.add(byte2StrIntercept(bytes,488,	26));
                responseList.add(byte2StrIntercept(bytes,514,	8));
                responseList.add(byte2StrIntercept(bytes,522,	40));
                responseList.add(byte2StrIntercept(bytes,562,	1));
                responseList.add(byte2StrIntercept(bytes,563,	5));
               // responseList.clear();
                responseListTemp.clear();
                String gbk="";
                for(int i=0;i<responseList.size();i++){//转换GBK
                    try {
                        byte[] bytesGbk=responseList.get(i).toString().getBytes("gbk");
                        gbk=new String(bytesGbk,"gbk");
                        responseListTemp.add(gbk);
                        //LogUtils.d("hut",transCode+"交易报文返回字节解析GBK的结果="+responseList.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (transCode.equals(SocketThread.changePwd)) {

        }

        LogUtils.d("hut",transCode+"交易报文返回字节解析GBK的结果="+responseListTemp.toString());
        return  responseListTemp;
    }
    public static String getGbkToUtfString(String gbk){
        String str="";
        char[] chars=gbk.toCharArray();
        byte[] fullByte=new byte[3*chars.length];
        for (int i=0;i<chars.length;i++){
            String binary=Integer.toBinaryString(chars[i]);
            StringBuffer sb=new StringBuffer();
            int len=16-binary.length();
            for(int j=0;j<len;j++){
                sb.append("0");
            }
            sb.append(binary);
            //增加24位3个字节
            sb.insert(0,"1110");
            sb.insert(8,"10");
            sb.insert(16,"10");
            fullByte[i*3]=Integer.valueOf(sb.substring(0,8),2).byteValue();
            fullByte[i*3+1]=Integer.valueOf(sb.substring(8,16),2).byteValue();
            fullByte[i*3+2]=Integer.valueOf(sb.substring(16,24),2).byteValue();

        }
        try {
            str=new String(fullByte, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  str;
    }
    public static String getEncodeToUtfString(String gbk) throws UnsupportedEncodingException {
        String  iso = new String(gbk.getBytes("UTF-8"), "ISO-8859-1");
        String str="";
        for (byte b:iso.getBytes("ISO-8859-1")){
            //LogUtils.d("hut字节",+b+"");
        }
        str=new String(iso.getBytes("UTF-8"), "ISO-8859-1");
        return  str;
    }
}
