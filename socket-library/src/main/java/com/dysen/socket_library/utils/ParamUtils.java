package com.dysen.socket_library.utils;

import com.dysen.socket_library.msg.MsgField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dysen on 2018/1/16.
 *
 * @Info  参数 配置工具类
 */

public class ParamUtils {
    public static String serverIp = "192.168.1.90";  //测试环境
    public static String serverPort = "8088";
//    public static String serverIp = "186.16.6.163";//生产环境
//    public static String serverPort = "8088";
    public static String sn = "";
    public static String pinNode = "";
    public static String tellerId = "";
    public static String orgId = "";
    public static String terminalId = "";
    public static String tellerName = "";
    public static String authSpecSign = "0";//默认为0-需要检查授权  1-不检查授权
    public static String authSeqNo = "";  //集中授权平台生成的授权流水号（第一次交易时为空，授权成功需上送流水号）
    public static List<String> responsHeader = new ArrayList<>();
    public static List<String> responsList = new ArrayList<>();
    public static List<MsgField> subFieldList=new ArrayList<MsgField>();
    public static byte[] respMsgByte;
    public static String authFlag = "0";//默认-0，本地授权成功-1集中授权成功-2

}
