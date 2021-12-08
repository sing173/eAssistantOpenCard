/*    */ package com.dysen.socket_library.data;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class GlobalData
{
	  public static boolean DEBUG_NETWORK = false;

	  public static boolean DEBUG_DEVICE = false;

	  public static String SEPARATOR = System.getProperty("file.separator");

	  public static String PROJECT_PATH = System.getProperty("user.dir") + SEPARATOR;

	  public static String XML_PATH = PROJECT_PATH + "xml" + SEPARATOR;

	  public static String LIB_PATH = PROJECT_PATH + "lib" + SEPARATOR;

	  public static String LIB_PATH_GWI = PROJECT_PATH + "lib" + SEPARATOR + "gwi" + SEPARATOR;

	  public static String RET_SUCCESS = "00000";

	  public static String RET_SUCCESS2="0000";

	  public static String RET_NOT_FOUND = "HB0188";

	  public static String RET_LUP = "LUP";

	  public static String RET_RUP = "RUP";

	  public static String[] CREDENTIAL_TYPE = { "01：居民身份证", "02：临时身份证", "03：护照", "04：户口簿", "05：军人身份证", "06：武装警察身份证", "07：港澳台居民往来内地通行证", "08：外交人员身份证", "09：外国人居留许可证", "10：边民出入境通行证", "11：个人其他证件" };

	  public static String[] SUB_CARD_TYPE = { "1-Ⅰ类户", "2-Ⅱ类户" };

	  public static String[] PRODUCT_TYPE = { "1101-个人活期结算账户", "1132-个人卡零存整取存款账户", "1152-个人定期一卡通存款账户", "1203-单位专用账户" };

	  public static String[] PRODUCT_TYPE_1101 = { "0101-人民币个人普通卡活期结算账户" };

	  public static String[] PRODUCT_TYPE_1132 = { "0112-人民币个人卡零存整取1年期存款账户", "0136-人民币个人卡零存整取3年期存款账户", "0160-人民币个人卡零存整取5年期存款账户" };

	  public static String[] PRODUCT_TYPE_1152 = { "0001-个人定期一卡通存款账户" };

	  public static String[] PRODUCT_TYPE_1203 = { "0101-单位银行卡专用账户" };

	  public static String[] JOB_CODE = { "01：高级管理者", "02：专业技术人员", "03：国家机关、党群组织、企业、事业单位负责人", "04：半专业人员", "05：办事人员和有关人员", "06：军人", "07：技术工人", "08：建筑贸易", "09：司机", "10：工厂工人", "11：保安", "12：军队—应征入伍的士兵", "13：销售", "14：自由职业者/个体户", "15：商业、服务业人员", "16：非技术工人", "17：退休", "18：家庭主妇", "19：学生", "20：待业", "23：不便分类的其他从业人员", "21：农、林、牧、渔、水利业生产人员", "22：生产、运输设备操作人员及有关人员", "23：不便分类的其他人员" };

	  public static String[] CHARGES_STANDARD = { "00：不收费", "01：1.00元/月", "02：2.00元/月", "12：12元/月" };

	  public static String[] CANCEL_OPERATE = { "00：注销网银", "01：注销手机", "11：注销网银、手机" };

	  public static String[] COMM = { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6" };

	  public static String[] PINPAD_TYPE = { "国光", "南天" };
	  public static String[] MANUFACTURER = { "升腾", "长城" };

	  public static Map<String, String> AUTH_WAY = new HashMap() { private static final long serialVersionUID = 1L; } ;

	  public static String[] ACCOUNT_TYPE = { "000：活期", "001：定期", "002：定活两便", "003：零存整取", "004：通知存款", "005：协定存款", "006：教育储蓄", "999：其他" };

	  public static String[] CERT_TYPE = { "01：卡", "02：折" };
	}

/* Location:           F:\鐢靛瓙閾惰寮�鍙戝洟闃焅鍓崱e鍔╂墜\HBNX_BusinessSystem\HBNX_BusinessSystem.jar
 * Qualified Name:     com.centerm.data.GlobalData
 * JD-Core Version:    0.5.4
 */