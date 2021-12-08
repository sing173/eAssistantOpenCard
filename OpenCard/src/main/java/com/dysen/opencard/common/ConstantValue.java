package com.dysen.opencard.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by hutian on 2018/3/21.
 * 静态常量池
 */

public class ConstantValue {
    public static String Test="Test";
	public static String Product="Product";
	public static String testIp = "192.168.1.90";  //测试环境
	//public static String testIp = "192.168.2.170";  //测试环境
	public static String testPort = "8088";
	public static String serverIp = "186.16.6.163";//生产环境
    public static String serverPort = "8088";
	public static String serverDeviceNum = ""; //生产设备ID 1200300000 --实际正式生产从设备读取不写死
	public static String testDeviceNum = "0100038647";//测试设备ID 0100038647
	public static String CrmTestUrl="http://192.168.2.111:7001/services/";//测试
	public static String CrmServerUrl="http://172.48.12.9:7081/services/";//生产
	public static String TestdeviceNum = "0100038647";//测试环境 0100038647/*/
	public static String ServerdeviceNum = "1200300000"; //生产环境
	public static String terllerFinger = "67=<?>;;>9;1<69;<<48?=4;550:07990>26<7=292<::=?0:72396203>616<?2654=:<;9?9:1<69;<<48?=4;550:07990>26<7=292<::=>4?254619=9=8907=70<806?007=486;588003679?;=6>?:1>0543731<020379422:4:9?47;?2>6392323=4:2432:<9>:8?2;3>702=8078?0?0<75<=<3<558><::429708:5572762:45>39=4:8;<16:=992:436090812<;?4280?0<=9?>::08><1?25126=?7;06<;31?:?01174>;14;;;21;794;:=1=;9>8267<?385;5=92601273098:=4384>332>>=9?11005451=7:2770?441?7>9;6;;25;29:7;6>2>76114<1;9?2:9<82===04>=9?11005451=7:2770?441?7>9;6;;25;29:7;6>2>76114<1;9?2:9<82===082";
	public static String terllerFinger2 = "67=<;7;9??;3<699=>5:>?594718158;1<34=5<080=8;?>2;53184322<737>>0775?;>:;>;;3=489=>5:>?594718158;1<34=5<080=8;?<2=6<;??27247136>26::8401;3>2996:567<>6=;5:=6:27=21:80>6:8:<11:>880909>910>07?>722?91<::=46;49?2=4844511?8930>07;7;62780;;5<?54;349>;747=>0=37:815=752>1:86653>6>75?81><2380>?>46696?61=7;9<=>???16:9>2=89;8;?;07;387:5349<28::8><=13;8<78?6:9:43::=85647131690>53048035839=<2<?51<6>>0?1:5:0265386?>;5>>8?6:9:43::=85647131690>53048035839=<2<?51<6>>0?1:5:0265386?>;5>>8?6:9:43::=85647131690>53048035839=<2<?9=";
//	public static String serverUrl="http://172.48.12.9:7081/services/";
	public static String Logdir= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fkezslog";
	public static String LogTransdir= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fkezslog"+ File.separator+"Trans";
	public static String sn = "";
	public static String pinNode = "";
	public static String tellerId = "";
	public static String orgId = "";
	public static String terminalId = "";
	public static String authSpecSign = "0";//默认为0-需要检查授权  1-不检查授权
	public static String authSeqNo = "";  //集中授权平台生成的授权流水号（第一次交易时为空，授权成功需上送流水号）
	public static byte[] respMsgByte;
	public static String authFlag = "0";//默认-0，本地授权成功-1集中授权成功-2
	public static long timeout=180000;//3分钟 180000
	public static long mLastActionTime;//上次操作时间
	public static int SignOutTag=0;//签退标志，0--正常临时签退，1-后台强制临时签退
}
