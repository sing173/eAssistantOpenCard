package com.dysen.opencard.common;

import com.dysen.opencard.common.bean.AreaBean;
import com.dysen.socket_library.msg.MsgField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dysen on 2018/1/16.
 *
 * @Info  参数 配置工具类
 */

public class ParamUtils {
    /*public static String serverIp = "192.168.3.243";  //测试环境
    public static String serverPort = "13012";*/
    public static String serverIp = "192.168.1.90";  //测试环境
    public static String serverPort = "8088";
    /*public static String serverIp = "186.16.6.163";//生产环境
    public static String serverPort = "8088";*/
    /*public static String serverIp2="192.168.3.243";
    public static String serverPort2="13012";*/
    public static String serverIp2="192.168.3.243";
    public static String serverPort2="13012";

    public static String CrmUrl="";

    public static String loadingMsg = "玩命加载中...";
    public static String tellerId = "";//柜员号
    public static String tellerName = "";
    public static String tellerIdAuthorize = "";
    public static String orgId = "";//机构号
    public static String terminalId = "";
    public static String transCode="";

    public static String sn = "";
    public static String pinNode = "";
    public static String authSpecSign = "0";//默认为0-需要检查授权  1-不检查授权
    public static String authSeqNo = "";  //集中授权平台生成的授权流水号（第一次交易时为空，授权成功需上送流水号）
    public static List<String> responsHeader = new ArrayList<>();
    public static List<String> responsList = new ArrayList<>();
    public static List<MsgField> subFieldList=new ArrayList<MsgField>();
    public static byte[] respMsgByte;
    public static String authFlag = "0";//默认-0，本地授权成功-1集中授权成功-2

    //public static String terllerFinger = "677701067=8?3;7?1:9=>83=450>5534=121?81163>=?<:8:=2;5>8;?3;8>38267974>:7=55;4:1>1;9=>83=450>5534=121?81163>=?<:8:=2;5=88?;94;97=>79;979286>166=<:10>2=1<23>09=830145;;>57<?682::??416249?<3?;16=4>;36??0<>099>702186>5<6>38=419;2;4>11425<>;:<4;;<97:78>1740=:5;6=2=657765;77693=65025?088<398?91<><35=<:>20316560>693463>752>4?::5:836:189687=3=65025?088<398?91<><35=<:>20316560>693463>752>4?::5:836:189687=3=65025?088<398?91<><35=<:>20316560>693463>752>4?::5:836:189687=3=65025?088<398?91<><35=<:>20316560>693463>752>4?::5:8?:";
    public static String terllerFinger="67=8?0?7;1?=8:=79014:11709565;<5527:9;8><>96?1:<?;7?<:7<623=30:>3911?0>5:5?=9:<79014:11709565;<5527:9;8><>96?1;<>;1623?;>385<>0>;4;84>0553<5;5;63>1>8965<67<7?9:5382246629??1>38;2=175:47=32:356<7>81?616?90231190=8;954:>7=3<;880731<16904<?9;;918?>64;594;01<6:>=>;=?>>;290<1360>451>7?9:6:;35:28:6;7>3>66015<0;8?3:8<92<=<05><9>10015550=6:3760>451>7?9:6:;35:28:6;7>3>66015<0;8?3:8<92<=<05><9>10015550=6:3760>451>7?9:6:;35:28:6;7>3>66015<0;8?3:8<92<=<05><9>10015550=6:3760>451>7?9:6:;35:28:6;7>3>66015<0;8?3:8<92<=<092                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ";
    public static String terllerFinger2 = "67<<>>>9:??780==8:0>;;0=134<41=?48608194=48<>;;6>165=06678272:;4230;>:??;?>780==8:0>;;0=134<41=?48608194=48<>;:2;?28934?7<<44>8>6=06;6<=?;=:3711616:5<8=06696::?35==;8>2<>5>7=43924??42=74048>4?<66>9?>12>06380:=9:>816<?=2=39>801>390:?>97?=:98=83=19846<<;8>403210:1=<12>7839??<78<=7;653:37:93>16?7>2:2?:9=<09713:6100>515<<2557=9<89<991?6:;?<78<=7;653:37:93>16?7>2:2?:9=<09713:6100>515<<2557=9<89<991?6:;?<78<=7;653:37:93>16?7>2:2?:9=<09713:6100>515<<2557=9<89<991?6:;?<78<=7;653:37:93>16?7>2:2?:9=<09713:6100>515<0>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ";
    //生产环境指纹

    //public static String terllerFinger ="67<<:?;:>:1275>2;5?346302>313<>2751=?<:9>9?1968;=<18:=5;45=:=7891>=435=:9:3<555270;=0<;;:5?:?769?>=63722623:5=207<9?>830999?=:2>:5<2>08?<96:<5<2993?0><2=04883431171146?39671320896:<<10>372>83<<46?4;3=43?5=4<6;>7257;:8312<=1<7?370278;><;3;3=;69;3:=359<39=0=85?60=36520<?<8:184>32=83;:3>62485:<5<21<71<>;=>:7=139=3335>=91:=0;<0>7:?>=62;1?75==?;245?580<<?6>>=8<84;12682=625:114:2;<>3>>70>7<?2>3;7;2344194><:7?<9=788851;8<:4455010482?7225:114:2;<>3>>70>7<?2>3;7;2344194><:7?<9=788851;8<:4455010482?7225:114:2;<>3>>;< 67=8?;:<?<1473><;;7?<:3<227=70>>7951;0:5>5;==:87=054>15749161;85123:=;0>4>?6919:<?4;>:5<421=108>1931=0<585==;:<3=4:317???7<502=63>:6430<;94;::89?;1;;?770<5?03>794655<0743?4093:50=0?30;6:=40<?84147;1=>2::;8=;?:79;:841:3676683?048?::0;68?7;7=856045;<3<2>;97<96?7470=4;==2321=1>7<<31>0174?86?7=4:1=?>:6280?705;855<948:6?64292<2:49:=5371660?130923?9?83<=0?26146?020732437:53<=24<26:1<=91>5595?<945187656>3?7>04<5;?:2>135:28:6;7>3>66015<0;8?3:8<92<=<05><9>10015550=6:3760>451>7?9:6:;35:28:6;7>3>66015<0;8?3:8<92<=<092";
    public static List<AreaBean> countryList = new ArrayList<>();
    public static String[] countrys = new String[]{};//地址 国家
    public static String transStateCode = "";//交易状态码
    public static String transState = "";//交易状态
    //交易标志
    public static String TRANS_SUCCESS = "交易成功";
    public static String TRANS_PUB = "前置错误交易失败";
    public static String TRANS_OTHER = "贷记卡错误交易失败";
    public static String TRANS_HB = "没有找到客户记录";
    public static String cusNum = "";
    public static String cusName = "";
    public static String cardNum = "";
    public static String accountNum = ""; //主帐号
    public static String cardType = "";//卡类型名称
    public static String certId = "";
    public static String certType = "";
    public static String telPhone = "";

    public static String certName = "";//身份证名称
    public static String cardProductName = "";//卡产品名称
    public static String serviceProject="开通手机渠道";

    public static String deviceNum = "";// 正式生产


    public static int deviceFlag =0 ;//连接设备标志

    public static String msg_cardNum;
    public static String msg_certType;
    public static String msg_certId;
    public static String msg_cardProductName;
    public static String msg_certName;
    public static String msg_signAccount;
    public static List<String> msg_mobileNums = new ArrayList<>();
    public static String msg_charges;
    public static String msg_amountLowerLimit="";
    public static String msg_remindLowerlimit="";
    public static String msg_isShowFree="";

    public static int print_voucher_type;
    //打印来源，正常打印或凭单补打
    public static int print_voucher_from_type=0;
    //凭单保存日期
    public static String print_voucher_date;
    //凭单打印次数
    public static int print_voucher_time=0;
}
