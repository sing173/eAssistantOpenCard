package com.dysen.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.print.sdk.PrinterConstants.Command;
import com.android.print.sdk.PrinterInstance;
import com.android.print.sdk.Table;
import com.dysen.ble_lib.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PrintUtils {
	static SimpleDateFormat simdf=new SimpleDateFormat("yyyy年MM月dd日");
	static Calendar cal=Calendar.getInstance();
	static String currentDate=simdf.format(cal.getTime());
	public static void printText(Resources resources, PrinterInstance mPrinter) {
		mPrinter.init();
		// mPrinter.printText(resources.getString(R.string.str_text));//打印文本效果展示：
		// mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);// 换2行

		boolean taxt = true;
		if (taxt) {
			mPrinter.setFont(0, 0, 0, 0);
			mPrinter.setPrinter(Command.ALIGN, 0);// 局右
			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 5);// 换2行
			mPrinter.printText("                        鲁 能");// 文字居左效果演示abc123：
			mPrinter.setPrinter(Command.PRINT_AND_NEWLINE);
			mPrinter.printText("                     82566743");
			mPrinter.setPrinter(Command.PRINT_AND_NEWLINE);
			mPrinter.printText("                      AT-4534");
			mPrinter.setPrinter(Command.PRINT_AND_NEWLINE);
			mPrinter.printText("                       000000");
			mPrinter.setPrinter(Command.PRINT_AND_NEWLINE);
			mPrinter.printText("                   2016-05-18");// 日期
			mPrinter.setPrinter(Command.PRINT_AND_NEWLINE);
			mPrinter.printText("                  21:11-21:40");// 上车时间 -下车时间
			mPrinter.setPrinter(Command.PRINT_AND_NEWLINE);
			mPrinter.printText("                         2.25");// 单价
			mPrinter.setPrinter(Command.PRINT_AND_NEWLINE);
			mPrinter.printText("                         12.7");// 里程
			mPrinter.setPrinter(Command.PRINT_AND_NEWLINE);
			mPrinter.printText("                     00:03:27");// 等待时间
			mPrinter.setPrinter(Command.PRINT_AND_NEWLINE);
			mPrinter.printText("                       $27.90");// 价格
			
			mPrinter.setPrinter(Command.PRINT_AND_NEWLINE);
			mPrinter.setPrinter(Command.PRINT_AND_NEWLINE);
			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);// 换2行
		} else {

			mPrinter.setPrinter(Command.ALIGN, 1);// 居中 
			mPrinter.printText(resources.getString(R.string.str_text_center));
			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);// 换2行

			mPrinter.setPrinter(Command.ALIGN, 2);// 局右
			mPrinter.printText(resources.getString(R.string.str_text_right));
			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3); // 换3行

			mPrinter.setPrinter(Command.ALIGN, 0);
			mPrinter.setFont(0, 0, 1, 0);// 加粗
			mPrinter.printText(resources.getString(R.string.str_text_strong));
			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2); // 换2行

			mPrinter.setFont(0, 0, 0, 1);// 下划线
			mPrinter.sendByteData(new byte[] { (byte) 0x1C, (byte) 0x21,
					(byte) 0x80 });
			mPrinter.printText(resources.getString(R.string.str_text_underline));
			mPrinter.sendByteData(new byte[] { (byte) 0x1C, (byte) 0x21,
					(byte) 0x00 });
			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2); // 换2行

			mPrinter.setFont(0, 0, 0, 0);
			mPrinter.printText(resources.getString(R.string.str_text_height));
			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
			for (int i = 0; i < 4; i++) {
				mPrinter.setFont(i, i, 0, 0);
				mPrinter.printText((i + 1)
						+ resources.getString(R.string.times));

			}
			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 1);
			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

			for (int i = 0; i < 4; i++) {

				mPrinter.setFont(i, i, 0, 0);
				mPrinter.printText(resources.getString(R.string.bigger)
						+ (i + 1) + resources.getString(R.string.bigger1));
				mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

			}

			mPrinter.setFont(0, 0, 0, 0);
			mPrinter.setPrinter(Command.ALIGN, 0);
			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
		}

	}
	
	public static boolean printInfoCreate(Resources resources, PrinterInstance mPrinter,String custId,
										  String custName,String certType,String certNo,String phoneNo,String branchNo,
										  String terminalId,String userId,int voucherFrom,int voucherTime){
		mPrinter.init();

		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		//mPrinter.printText(resources.getString(R.string.str_note));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

		StringBuffer sb = new StringBuffer();

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(1, 1);
		Bitmap bitmap1 = BitmapFactory.decodeResource(resources,
				R.drawable.logo_login);
		mPrinter.printImage(bitmap1);

		if(voucherFrom==2){
			mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);// 居中
			mPrinter.setCharacterMultiple(0, 0);
			mPrinter.printText("   (补打凭单)\n\n");
		}
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		// 字号使用默认
		mPrinter.setCharacterMultiple(0, 0);
		sb.append("个人客户基本信息创建回执\n");
		sb.append("交易日期："+currentDate+"\n");
		sb.append("客户号："+custId+"\n");
		sb.append("客户名称："+custName+"\n");
		sb.append("证件类型："+certType+"\n");
		sb.append("证件号码："+certNo+"\n");
		sb.append("手机号码："+phoneNo+"\n");
		sb.append("交易网点："+branchNo+"\n");
		sb.append("终端编号："+terminalId+"\n");
		sb.append("操作柜员号："+userId+"\n");

		if(voucherFrom==2){
			//sb.append("打印次数:"+voucherTime+"\n");
		}

		sb.append("经核对，以上打印内容本人无异议。\n");
		sb.append("客户签名：\n\n");
		sb.append("农信社（农商行）存根\n");

		mPrinter.printText(sb.toString());

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);

		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
		return true;
	}
	public static boolean printSignMessage(Resources resources, PrinterInstance mPrinter, String signCardNum,
											   String certType, String certNo, String productType, String custName, String signAccount, List<String> mobileNums,
											   String chargetandards,String balanceChargeLowerlimit, String balanceRemindLowerlimit, String isShowFree,  String branchNo,
											   String terminalId, String userId,int voucherFrom,int voucherTime){
		mPrinter.init();

		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		//mPrinter.printText(resources.getString(R.string.str_note));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

		StringBuffer sb = new StringBuffer();

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(1, 1);
		Bitmap bitmap1 = BitmapFactory.decodeResource(resources,
				R.drawable.logo_login);

		mPrinter.printImage(bitmap1);
		if(voucherFrom==2){
			mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);// 居中
			mPrinter.setCharacterMultiple(0, 0);
			mPrinter.printText("   (补打凭单)\n\n");
		}

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		// 字号使用默认
		mPrinter.setCharacterMultiple(0, 0);
		sb.append("短信银行服务创建回执\n");
		sb.append("交易日期："+currentDate+"\n");
		sb.append("签约卡号："+signCardNum+"\n");
		sb.append("证件类型："+certType+"\n");
		sb.append("证件号码："+certNo+"\n");
		sb.append("产品类型："+productType+"\n");
		sb.append("客户名称："+custName+"\n");
		sb.append("扣费卡号："+signAccount+"\n");
		for (int i = 0; i < mobileNums.size(); i++) {

		sb.append("手机号码"+(i+1)+"："+mobileNums.get(i)+"\n");
		}
		sb.append("收费标准："+chargetandards+"\n");
		sb.append("余额变动下限设置："+balanceChargeLowerlimit+"\n");
		sb.append("余额警告下限设置："+balanceRemindLowerlimit+"\n");
		sb.append("是否显示账户余额："+isShowFree+"\n");

		sb.append("交易网点："+branchNo+"\n");
		sb.append("终端编号："+terminalId+"\n");
		sb.append("操作柜员号："+userId+"\n");
		if(voucherFrom==2){
			//sb.append("打印次数:"+voucherTime+"\n");
		}
		sb.append("经核对，以上打印内容本人无异议。\n");
		sb.append("客户签名：\n\n");
		sb.append("农信社（农商行）存根");

		mPrinter.printText(sb.toString());

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);

		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
		return true;
	}
	public static boolean printOpenCardService(Resources resources, PrinterInstance mPrinter,
			int printType,String custId,String custName,String ICCardNo,String accountNo
			,String cardName,String branchNo,String terminalId,String userId,int voucherFrom,int voucherTime){
		mPrinter.init();
		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		//mPrinter.printText(resources.getString(R.string.str_note));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
		StringBuffer sb = new StringBuffer();
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(1, 1);
		Bitmap bitmap1 = BitmapFactory.decodeResource(resources,
				R.drawable.logo_login);
		mPrinter.printImage(bitmap1);
		if(voucherFrom==2){
			mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);// 居中
			mPrinter.setCharacterMultiple(0, 0);
			mPrinter.printText("   (补打凭单)\n\n");
		}
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		// 字号使用默认
		mPrinter.setCharacterMultiple(0, 0);
		sb.append("开卡服务回执\n");
		sb.append("交易日期："+currentDate+"\n");
		sb.append("客户号："+custId+"\n");
		sb.append("客户名称:"+custName+"\n");
		sb.append("卡号："+ICCardNo+"\n");
		sb.append("账号："+accountNo+"\n");
		sb.append("卡产品名称："+cardName+"\n");
		sb.append("交易网点："+branchNo+"\n");
		sb.append("终端编号："+terminalId+"\n");
		sb.append("操作柜员号："+userId+"\n");
		if(voucherFrom==2){
			//sb.append("打印次数:"+voucherTime+"\n");
		}
		if(printType==1){
			//农商行存根
			sb.append("经核对，以上打印内容本人无异议。\n");
			sb.append("客户签名：\n\n");
			sb.append("农信社（农商行）存根");
		}else{
			//持卡人存根
			sb.append("\n\n");
			sb.append("持卡人存根");
		}
		mPrinter.printText(sb.toString());

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);

		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
		return true;
	}
	
	public static boolean printShortNoteService(Resources resources, PrinterInstance mPrinter,
			int printType,String signCardNo,String certType,String certNo,String chargeCardNo
			,String productType,String custName,String phoneNo,String chargeStandard,
			Double limitChangeLowest,Double limitWarnLowest,String whetherShowLeft,String branchNo,
			String terminalId,String userId,int voucherFrom,int voucherTime){
		mPrinter.init();
		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		//mPrinter.printText(resources.getString(R.string.str_note));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
		StringBuffer sb = new StringBuffer();
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(1, 1);
		Bitmap bitmap1 = BitmapFactory.decodeResource(resources,
				R.drawable.logo_login);
		mPrinter.printImage(bitmap1);
		if(voucherFrom==2){
			mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);// 居中
			mPrinter.setCharacterMultiple(0, 0);
			mPrinter.printText("   (补打凭单)\n\n");
		}
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		// 字号使用默认
		mPrinter.setCharacterMultiple(0, 0);
		sb.append("短信银行服务回执\n");
		sb.append("交易日期："+currentDate+"\n");
		sb.append("签约卡号："+signCardNo+"\n");
		sb.append("证件类型："+certType+"\n");
		sb.append("证件号码："+certNo+"\n");
		sb.append("产品类型："+productType+"\n");
		sb.append("客户名称："+custName+"\n");
		sb.append("扣费卡号："+chargeCardNo+"\n");
		sb.append("手机号码："+phoneNo+"\n");
		sb.append("收费标准："+chargeStandard+"\n");
		sb.append("余额变动下限设置："+limitChangeLowest+"\n");
		sb.append("余额告警下限设置："+limitWarnLowest+"\n");
		sb.append("是否显示账户余额："+whetherShowLeft+"\n");
		sb.append("交易网点："+branchNo+"\n");
		sb.append("终端编号："+terminalId+"\n");
		sb.append("操作柜员号："+userId+"\n");
		if(voucherFrom==2){
			//sb.append("打印次数:"+voucherTime+"\n");
		}
		if(printType==1){
			//农商行存根
			sb.append("经核对，以上打印内容本人无异议。\n");
			sb.append("客户签名：\n\n");
			sb.append("农信社（农商行）存根");
		}else{
			//持卡人存根
			sb.append("\n\n");
			sb.append("持卡人存根");
		}
		mPrinter.printText(sb.toString());

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);

		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
		return true;
	}

	public static boolean printChangePassword(Resources resources, PrinterInstance mPrinter,String custId,
			String custName,String cardNo,String cardName,String branchNo,String terminalId,String userId,int voucherFrom,int voucherTime){
		mPrinter.init();
		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		//mPrinter.printText(resources.getString(R.string.str_note));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
		StringBuffer sb = new StringBuffer();
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(1, 1);
		Bitmap bitmap1 = BitmapFactory.decodeResource(resources,
				R.drawable.logo_login);
		mPrinter.printImage(bitmap1);
		if(voucherFrom==2){
			mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);// 居中
			mPrinter.setCharacterMultiple(0, 0);
			mPrinter.printText("   (补打凭单)\n\n");
		}
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		// 字号使用默认
		mPrinter.setCharacterMultiple(0, 0);
		sb.append("卡密码修改服务回执\n");
		sb.append("交易日期："+currentDate+"\n");
		sb.append("客户号："+custId+"\n");
		sb.append("客户名称:"+custName+"\n");
		sb.append("卡号："+cardNo+"\n");
		sb.append("卡产品名称："+cardName+"\n");
		sb.append("交易网点："+branchNo+"\n");
		sb.append("终端编号："+terminalId+"\n");
		sb.append("操作柜员号："+userId+"\n");
		if(voucherFrom==2){
			//sb.append("打印次数:"+voucherTime+"\n");
		}
		sb.append("经核对，以上打印内容本人无异议。\n");
		sb.append("客户签名：\n\n");
		sb.append("农信社（农商行）存根\n");

		mPrinter.printText(sb.toString());

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);

		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
		return true;
	}
	public static boolean printOpenMobileBankService(Resources resources, PrinterInstance mPrinter, String custName, String certType,
													 String certNo, String signaccount, String serviceProject, String branchNo, String terminalId,String tellerId,int voucherFrom,int voucherTime){
		mPrinter.init();
		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		//mPrinter.printText(resources.getString(R.string.str_note));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
		StringBuffer sb = new StringBuffer();
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(1, 1);
		Bitmap bitmap1 = BitmapFactory.decodeResource(resources,
				R.drawable.logo_login);
		mPrinter.printImage(bitmap1);
		if(voucherFrom==2){
			mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);// 居中
			mPrinter.setCharacterMultiple(0, 0);
			mPrinter.printText("   (补打凭单)\n\n");
		}
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		// 字号使用默认
		mPrinter.setCharacterMultiple(0, 0);
		sb.append("电子渠道签约服务回执\n");
		sb.append("交易日期："+currentDate+"\n");
		sb.append("客户名称:"+custName+"\n");
		sb.append("证件类型："+certType+"\n");
		sb.append("证件号码："+certNo+"\n");
		sb.append("签约账号："+signaccount+"\n");
		sb.append("申请服务项目："+serviceProject+"\n");
		sb.append("交易网点："+branchNo+"\n");
		sb.append("终端编号："+terminalId+"\n");
		sb.append("操作柜员号："+tellerId+"\n");
		if(voucherFrom==2){
			//sb.append("打印次数:"+voucherTime+"\n");
		}
		sb.append("经核对，以上打印内容本人无异议。\n");
		sb.append("客户签名：\n\n");
		sb.append("农信社（农商行）存根\n");

		mPrinter.printText(sb.toString());

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);

		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
		return true;
	}
	
	public static void printNote(Resources resources, PrinterInstance mPrinter,
			boolean is58mm) {
		mPrinter.init();

		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.printText(resources.getString(R.string.str_note));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

		StringBuffer sb = new StringBuffer();

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(1, 1);
		mPrinter.printText(resources.getString(R.string.shop_company_title)
				+ "\n");
		
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		// 字号使用默认
		mPrinter.setCharacterMultiple(0, 0);
		sb.append(resources.getString(R.string.shop_num) + "574001\n");
		sb.append(resources.getString(R.string.shop_receipt_num)
				+ "S00003169\n");
		sb.append(resources.getString(R.string.shop_cashier_num)
				+ "s004_s004\n");

		sb.append(resources.getString(R.string.shop_receipt_date)
				+ "2012-06-17\n");
		sb.append(resources.getString(R.string.shop_print_time)
				+ "2012-06-17 13:37:24\n");
		mPrinter.printText(sb.toString()); // 打印

		printTable1(resources, mPrinter, is58mm); // 打印表格

		sb = new StringBuffer();
		if (is58mm) {
			sb.append(resources.getString(R.string.shop_goods_number)
					+ "                6.00\n");
			sb.append(resources.getString(R.string.shop_goods_total_price)
					+ "                35.00\n");
			sb.append(resources.getString(R.string.shop_payment)
					+ "                100.00\n");
			sb.append(resources.getString(R.string.shop_change)
					+ "                65.00\n");
		} else {
			sb.append(resources.getString(R.string.shop_goods_number)
					+ "                                6.00\n");
			sb.append(resources.getString(R.string.shop_goods_total_price)
					+ "                                35.00\n");
			sb.append(resources.getString(R.string.shop_payment)
					+ "                                100.00\n");
			sb.append(resources.getString(R.string.shop_change)
					+ "                                65.00\n");
		}

		sb.append(resources.getString(R.string.shop_company_name) + "\n");
		sb.append(resources.getString(R.string.shop_company_site)
				+ "www.jiangsuxxxx.com\n");
		sb.append(resources.getString(R.string.shop_company_address) + "\n");
		sb.append(resources.getString(R.string.shop_company_tel)
				+ "0574-12345678\n");
		sb.append(resources.getString(R.string.shop_Service_Line)
				+ "4008-123-456 \n");
		if (is58mm) {
			sb.append("==============================\n");
		} else {
			sb.append("==============================================\n");
		}
		mPrinter.printText(sb.toString());

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(0, 1);
		mPrinter.printText(resources.getString(R.string.shop_thanks) + "\n");
		mPrinter.printText(resources.getString(R.string.shop_demo) + "\n\n\n");

		mPrinter.setFont(0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

	}

	/*
	 * public static void printTable(Resources resources, PrinterInstance
	 * mPrinter, boolean is58mm) { mPrinter.init();
	 * 
	 * mPrinter.setFont(0, 0, 0, 0); mPrinter.setPrinter(Command.ALIGN,
	 * Command.ALIGN_LEFT); mPrinter.printText("打印表格效果演示：");
	 * mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
	 * 
	 * // getTable方法:参数1,以特定符号分隔的列名; 2,列名分隔符; // 3,各列所占字符宽度,中文2个,英文1个.
	 * 默认字体总共不要超过48 // 表格超出部分会另起一行打印.若想手动换行,可加\n.
	 * mPrinter.setCharacterMultiple(0, 0); String column =
	 * resources.getString(R.string.note_title); Table table; if (is58mm) {
	 * table = new Table(column, ";", new int[] { 14, 6, 6, 6 }); } else { table
	 * = new Table(column, ";", new int[] { 16, 8, 8, 12 }); }
	 * 
	 * table.setColumnAlignRight(true); table.addRow("1," +
	 * resources.getString(R.string.coffee) + ";2.00;5.00;10.00");
	 * table.addRow("2," + resources.getString(R.string.tableware) +
	 * ";2.00;5.00;10.00"); table.addRow("3," +
	 * resources.getString(R.string.frog) + ";1.00;68.00;68.00");
	 * table.addRow("4," + resources.getString(R.string.cucumber) +
	 * ";1.00;4.00;4.00"); table.addRow("5," +
	 * resources.getString(R.string.peanuts) + "; 1.00;5.00;5.00");
	 * table.addRow("6," + resources.getString(R.string.rice) +
	 * ";1.00;2.00;2.00"); mPrinter.printTable(table);
	 * 
	 * mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3); }
	 */

	public static void printTable1(Resources resources,
			PrinterInstance mPrinter, boolean is58mm) {
		mPrinter.init();
		String column = resources.getString(R.string.note_title);
		Table table;
		if (is58mm) {
			table = new Table(column, ";", new int[] { 14, 6, 6, 6 });
		} else {
			table = new Table(column, ";", new int[] { 18, 10, 10, 12 });
		}
		table.addRow("" + resources.getString(R.string.bags) + ";10.00;1;10.00");
		table.addRow("" + resources.getString(R.string.hook) + ";5.00;2;10.00");
		table.addRow("" + resources.getString(R.string.umbrella)
				+ ";5.00;3;15.00");
		mPrinter.printTable(table);
	}

}
