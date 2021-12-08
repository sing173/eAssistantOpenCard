package com.dysen.opencard.printer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.print.sdk.PrinterConstants;
import com.android.print.sdk.PrinterInstance;
import com.dysen.commom_library.utils.DatetimeUtil;
import com.dysen.commom_library.utils.SocketUtil;
import com.dysen.commom_library.views.UberProgressView;
import com.dysen.opencard.R;
import com.dysen.opencard.base.ParentActivity;
import com.dysen.opencard.common.DataBaseManager;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.bean.ChangePassword;
import com.dysen.opencard.common.bean.CreateAccount;
import com.dysen.opencard.common.bean.MobileBankSign;
import com.dysen.opencard.common.bean.OpenCard;
import com.dysen.opencard.common.bean.SignMessage;
import com.dysen.print.BluetoothDeviceList;
import com.dysen.print.BluetoothOperation;
import com.dysen.print.IPrinterOpertion;
import com.dysen.utils.PrintUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrinterInfo extends ParentActivity {

	public static final String FUNC_NAME = "func_name";

	public static final int PRINT_SUCCESS = 11;
	public static final int PRINT_FAIL = 12;
	public static final int PRINT_ERR = 200;//打印凭证失败
	public static final int PRINT_OPEN_CUS = 201;//开户打印凭证
	public static final int PRINT_OPEN_CARD = 202;//开卡打印凭证
	public static final int PRINT_ACTIVATION_CARD = 203;//卡激活打印凭证֤
	public static final int PRINT_ACTIVATION_CARD1 = 204;//
	public static final int PRINT_SIGN_MESSAGE = 205;//短信签约
	public static final int PRINT_SIGN_MOBILE_BANK = 206;//手机银行签约
	public static final int PRINT_Voucher = 207;//凭单补打

	//一．	整体声明
	protected static IPrinterOpertion myOpertion;


	public static final int CONNECT_DEVICE = 1;
	public static final int ENABLE_BT = 2;
	@BindView(R.id.txt_back)
	TextView txtBack;
	@BindView(R.id.lay_back)
	LinearLayout layBack;
	@BindView(R.id.choose_device_name)
	TextView chooseDeviceName;
	@BindView(R.id.refresh_img)
	ImageView refreshImg;
	@BindView(R.id.lay_blue)
	LinearLayout layBlue;
	@BindView(R.id.txt_title)
	TextView txtTitle;
	@BindView(R.id.txt_exit)
	TextView txtExit;
	@BindView(R.id.tv_hint)
	TextView tvHint;
	@BindView(R.id.uber)
	UberProgressView uber;
	@BindView(R.id.fl_idcard)
	FrameLayout flIdcard;
	@BindView(R.id.btn_connect)
	Button btnConnect;
	@BindView(R.id.btn_back)
	Button btnBack;

	private String bt_mac;
	private String bt_name;
	private int funcType;
	private boolean printeState;
	PrinterInstance mPrinter;
	private boolean isFastClick;
	private DataBaseManager liteOrm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printer);
		ButterKnife.bind(this);

		initView();
	}

	private void initView() {
        liteOrm=DataBaseManager.getInstance();
		aty = this;
		txtTitle.setText(getString(R.string.print_info));
		backActivity(this, layBack, -1);
		funcType = getIntent().getIntExtra(FUNC_NAME, 0);
		int pintType = getIntent().getIntExtra("PRINT_Voucher", 0);
		if (pintType == PrinterInfo.PRINT_Voucher) {//凭单补打印入口
			ParamUtils.print_voucher_from_type = 2;
		} else {
			ParamUtils.print_voucher_from_type = 0;
		}
		if (funcType == PRINT_OPEN_CUS) {
			ParamUtils.print_voucher_type = 1;
		} else if (funcType == PRINT_OPEN_CARD) {
			ParamUtils.print_voucher_type = 2;
		} else if (funcType == PRINT_ACTIVATION_CARD) {
			ParamUtils.print_voucher_type = 4;
		} else if (funcType == PRINT_SIGN_MESSAGE) {
			ParamUtils.print_voucher_type = 3;
		} else if (funcType == PRINT_SIGN_MOBILE_BANK) {
			ParamUtils.print_voucher_type = 5;
		}
		//Oncreate声明
		myOpertion = new BluetoothOperation(aty, mHandler);
		//调用蓝牙连接方法
		BlueToothConnectTool.openConn(false, aty, mHandler, myOpertion);
		//add by butian 优化 2018/06-22
		if(pintType==0) { //不是补打入库保存，无需每次重复保存
			SavePrinterInfoToDB(funcType);
		}else {// 补打 已经曾在数据库中 查询
			QueryPrinterInfoFromDB(funcType);
		}
	}
    // add by huitian 2018-06-26 查询数据库是否有保存数据，及记录次数
	private void QueryPrinterInfoFromDB(int funcType) {
		if (funcType == PRINT_OPEN_CUS) { //根据客户号更新数据库这条记录打印次数
			//1.先查询上一次数据库更新打印次数记录
			List<CreateAccount> createAccountList=liteOrm.getQueryByWhere(CreateAccount.class,"custId",new String[]{ParamUtils.cusNum});
			if(createAccountList!=null&&createAccountList.size()>0) {
				int count = createAccountList.get(0).getPrintTime();
				ParamUtils.print_voucher_time=count+1;//补打计数
				//再更新数据库次数
				liteOrm.updateByWhere(CreateAccount.class, "custId", ParamUtils.cusNum, "printTime", ParamUtils.print_voucher_time);
			}
		} else if (funcType == PRINT_OPEN_CARD) {
			//1.根据卡号唯一先查询上一次数据库更新打印次数记录
			List<OpenCard> openCardList=liteOrm.getQueryByWhere(OpenCard.class,"ICCardNo",new String[]{ParamUtils.cardNum});
			if(openCardList!=null&&openCardList.size()>0) {
				int count = openCardList.get(0).getPrintTime();
				ParamUtils.print_voucher_time=count+1;//补打计数
				//再更新数据库次数  卡号唯一标识
				liteOrm.updateByWhere(OpenCard.class, "ICCardNo", ParamUtils.cardNum, "printTime", ParamUtils.print_voucher_time);
			}
		}else if (funcType == PRINT_SIGN_MESSAGE) {
			//1.根据卡号唯一先查询上一次数据库更新打印次数记录
			List<SignMessage> signMessageList=liteOrm.getQueryByWhere(SignMessage.class,"signCardNum",new String[]{ParamUtils.msg_cardNum});
			if(signMessageList!=null&&signMessageList.size()>0) {
				int count = signMessageList.get(0).getPrintTime();
				ParamUtils.print_voucher_time=count+1;//补打计数
				//再更新数据库次数
				liteOrm.updateByWhere(SignMessage.class, "signCardNum", ParamUtils.msg_cardNum, "printTime", ParamUtils.print_voucher_time);
			}
		} else if (funcType == PRINT_ACTIVATION_CARD) {
			//1.根据卡号唯一先查询上一次数据库更新打印次数记录
			List<ChangePassword> changePasswordList=liteOrm.getQueryByWhere(ChangePassword.class,"cardNo",new String[]{ParamUtils.cardNum});
			if(changePasswordList!=null&&changePasswordList.size()>0) {
				int count = changePasswordList.get(0).getPrintTime();
				ParamUtils.print_voucher_time=count+1;//补打计数
				//再更新数据库次数
				liteOrm.updateByWhere(ChangePassword.class, "cardNo", ParamUtils.cardNum, "printTime", ParamUtils.print_voucher_time);
			}
		}  else if (funcType == PRINT_SIGN_MOBILE_BANK) {
			//1.根据卡号唯一先查询上一次数据库更新打印次数记录
			//cardNo
			List<MobileBankSign> mobileBankSignList=liteOrm.getQueryByWhere(MobileBankSign.class,"certNo",new String[]{ParamUtils.cardNum});
			if(mobileBankSignList!=null&&mobileBankSignList.size()>0) {
				int count = mobileBankSignList.get(0).getPrintTime();
				ParamUtils.print_voucher_time=count+1;//补打计数 从新复值
				//再更新数据库次数
				liteOrm.updateByWhere(MobileBankSign.class, "certNo", ParamUtils.cardNum, "printTime", ParamUtils.print_voucher_time);
			}
		}
	}
     //保存打印信息到数据库
	private void SavePrinterInfoToDB(int funcType) {
		if (funcType == PRINT_OPEN_CUS) {
			CreateAccount createAccount=new CreateAccount();
			createAccount.setTransDate(DatetimeUtil.getToday2());
			createAccount.setCustId(ParamUtils.cusNum);
			createAccount.setCustName(ParamUtils.certName);
			createAccount.setCertType(ParamUtils.certType);
			createAccount.setCertNo(ParamUtils.certId);
			createAccount.setPhoneNo(ParamUtils.telPhone);
			createAccount.setBranchNo(ParamUtils.orgId);
			createAccount.setTerminalId(ParamUtils.terminalId);
			createAccount.setUserId(ParamUtils.tellerId);
			createAccount.setPrintType(1);
			createAccount.setPrintTime(0);//默认从0开始
			liteOrm.save(createAccount);
			ParamUtils.print_voucher_type = 1;
		} else if (funcType == PRINT_OPEN_CARD) {
			OpenCard openCard=new OpenCard();
			openCard.setTransDate(DatetimeUtil.getToday2());
			openCard.setCustId(ParamUtils.cusNum);
			openCard.setCustName(ParamUtils.certName);
			openCard.setICCardNo(ParamUtils.cardNum);
			openCard.setAccountNo(ParamUtils.accountNum);
			openCard.setCardName(ParamUtils.cardType);
			openCard.setBranchNo(ParamUtils.orgId);
			openCard.setTerminalId(ParamUtils.terminalId);
			openCard.setUserId(ParamUtils.tellerId);
			openCard.setPrintType(2);
			openCard.setPrintTime(0);//默认从0开始
			liteOrm.save(openCard);
			ParamUtils.print_voucher_type = 2;
		}else if (funcType == PRINT_SIGN_MESSAGE) {
			SignMessage signMessage=new SignMessage();
			signMessage.setTransDate(DatetimeUtil.getToday2());
			signMessage.setSignCardNum(ParamUtils.msg_signAccount);
			signMessage.setCertType(ParamUtils.msg_certType);
			signMessage.setCertNo(ParamUtils.msg_certId);
			signMessage.setProductType(ParamUtils.msg_cardProductName);
			signMessage.setCustName(ParamUtils.msg_certName);
			signMessage.setChargeCardNo(ParamUtils.msg_cardNum);
			//signMessage.setCertType(ParamUtils.msg_signAccount);//扣费卡号
			String phone="";
			if(ParamUtils.msg_mobileNums.size()>0) {//如果有多个手机号码
				for(int i=0;i<ParamUtils.msg_mobileNums.size();i++){
					 phone=phone+ParamUtils.msg_mobileNums.get(i);
				}
				signMessage.setMobileNums(phone);
			}
			signMessage.setChargetandards(ParamUtils.msg_charges);
			signMessage.setBalanceChargeLowerlimit(ParamUtils.msg_amountLowerLimit);
			signMessage.setBalanceRemindLowerlimit(ParamUtils.msg_remindLowerlimit);
			signMessage.setIsShowFree(ParamUtils.msg_isShowFree);
			signMessage.setBranchNo(ParamUtils.orgId);
			signMessage.setTerminalId(ParamUtils.terminalId);
			signMessage.setUserId(ParamUtils.tellerId);
			signMessage.setPrintType(3);
			signMessage.setPrintTime(0);//默认从0开始
			liteOrm.save(signMessage);
			ParamUtils.print_voucher_type = 3;
		} else if (funcType == PRINT_ACTIVATION_CARD) {
			ChangePassword changePassword=new ChangePassword();
			changePassword.setTransDate(DatetimeUtil.getToday2());
			changePassword.setCustId(ParamUtils.cusNum);
			changePassword.setCustName(ParamUtils.cusName);
			changePassword.setCardNo(ParamUtils.cardNum);
			changePassword.setCardProductName(ParamUtils.cardProductName);
			changePassword.setBranchNo(ParamUtils.orgId);
			changePassword.setTerminalId(ParamUtils.terminalId);
			changePassword.setUserId(ParamUtils.tellerId);
			changePassword.setPrintType(4);
			changePassword.setPrintTime(0);//默认从0开始
			liteOrm.save(changePassword);
			ParamUtils.print_voucher_type = 4;
		}  else if (funcType == PRINT_SIGN_MOBILE_BANK) {
			MobileBankSign mobileBankSign=new MobileBankSign();
			mobileBankSign.setTransDate(DatetimeUtil.getToday2());
			mobileBankSign.setCustName(ParamUtils.cusName);
			mobileBankSign.setCertType(ParamUtils.certType);
			//用于保存凭单打印次数
			mobileBankSign.setCardNo(ParamUtils.certId);
			mobileBankSign.setCertNo(ParamUtils.certId);
			mobileBankSign.setSignaccount(ParamUtils.cardNum);
			mobileBankSign.setServiceProject(ParamUtils.serviceProject);
			mobileBankSign.setBranchNo(ParamUtils.orgId);
			mobileBankSign.setTerminalId(ParamUtils.terminalId);
			mobileBankSign.setUserId(ParamUtils.tellerId);//柜员
			mobileBankSign.setPrintType(5);
			mobileBankSign.setPrintTime(0);
			liteOrm.save(mobileBankSign);
			ParamUtils.print_voucher_type = 5;
		}
	}

	/**
	 * 打开蓝牙 并连接
	 *
	 * @return
	 */
	protected boolean openBle() {
		if (mBlueToothAdapter != null && !mBlueToothAdapter.isEnabled()) {
			mBlueToothAdapter.enable();
			toast("opne blue success!");
		}
		SystemClock.sleep(1000);
		if (mBlueToothAdapter.isEnabled()) {
			toast("蓝牙已打开，请操作！");
			return true;
		} else {
			toast("请先打开蓝牙，再操作！");
		}
		return false;
	}


	private boolean isConnected;
	//    Handler接收参数
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			printerConnState(msg.what);
		}
	};

	private void printerConnState(int what) {
		uber.setVisibility(View.INVISIBLE);
		switch (what) {
			case PrinterConstants.Connect.SUCCESS:
				isConnected = true;
				mPrinter = myOpertion.getPrinter();
				toast("蓝牙连接成功！");
				printInfo(funcType);

				break;
			case PrinterConstants.Connect.FAILED:
				isConnected = false;
				toast(aty.getResources().getString(R.string.conn_failed));
				break;
			case PrinterConstants.Connect.CLOSED:
				isConnected = false;
				toast(aty.getResources().getString(R.string.conn_closed));
				break;
			case PrinterConstants.Connect.NODEVICE:
				isConnected = false;
				toast(aty.getResources().getString(R.string.conn_no));
				break;
			default:
				break;
		}
	}

//	@OnClick({R.id.refresh_img, R.id.choose_device_name, R.id.btn_connect, R.id.btn_back})
	@OnClick({R.id.refresh_img, R.id.btn_connect, R.id.btn_back})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.refresh_img:
				BlueToothConnectTool.openConn(false, PrinterInfo.this, mHandler, myOpertion);
				//BlueToothConnectTool.openConn(false, getApplicationContext(), mHandler, myOpertion);
				break;
			case R.id.btn_connect://选择连接
				isFastClick = SocketUtil.isFastTimeClick(R.id.btn_connect);
				if (!isFastClick) {
					//BlueToothConnectTool.openConn(false, aty, mHandler, myOpertion);
				}
				break;
			case R.id.btn_back:
				aty.finish();
				break;
		}
	}
//	public void onViewClicked(View view) {
//		switch (view.getId()) {
//			case R.id.refresh_img:
//				//BlueToothConnectTool.openConn(false, aty, mHandler, myOpertion);
//				break;
//			case R.id.btn_connect://选择连接
//				BlueToothConnectTool.openConn(false, aty, mHandler, myOpertion);
//				break;
//			case R.id.btn_back:
//				aty.finish();
//				break;
//		    /*case R.id.choose_device_name:
//                ShowCommonTypeWindow.showCommonTypeWindow(PrinterInfo.this,blueDevice,chooseDeviceName,1920,1200);*/
//		}
//	}

	/**
	 * 开卡打印凭证
	 *
	 * @param funcType
	 */
	private void printInfo(final int funcType) {
		printeState = false;
		if (PRINT_OPEN_CUS == funcType) {//开户
			printeState = PrintUtils.printInfoCreate(aty.getResources(), mPrinter, ParamUtils.cusNum, ParamUtils.certName, ParamUtils.certType, ParamUtils.certId, ParamUtils.telPhone,
					ParamUtils.orgId, ParamUtils.terminalId, ParamUtils.tellerId, ParamUtils.print_voucher_from_type, ParamUtils.print_voucher_time);
			ParamUtils.print_voucher_type = 1;
		} else if (PRINT_OPEN_CARD == funcType) {//开卡
             /*前两参数保持不变，
             第三个参数为打印类型-1.农信社存根 2.客户存根
             后面依次为/客户号/客户名称/卡号/账户号/卡产品名称/交易网点/终端编号/操作柜员号
        */
//         PrintUtils.printOpenCardService(aty.getResources(), mPrinter, 1, "8008008820", "圣保罗", "6210137280906598", "81010001000027618", "IC金卡", "10160", "109", "2000155");
			printeState = PrintUtils.printOpenCardService(aty.getResources(), mPrinter, 1, ParamUtils.cusNum, ParamUtils.certName, ParamUtils.cardNum,
					ParamUtils.accountNum, ParamUtils.cardType, ParamUtils.orgId, ParamUtils.terminalId, ParamUtils.tellerId, ParamUtils.print_voucher_from_type, ParamUtils.print_voucher_time);
			ParamUtils.print_voucher_type = 2;
		} else if (PRINT_ACTIVATION_CARD == funcType) {//卡激活

			printeState = PrintUtils.printChangePassword(aty.getResources(), mPrinter, ParamUtils.cusNum, ParamUtils.cusName, ParamUtils.cardNum, ParamUtils.cardProductName,
					ParamUtils.orgId, ParamUtils.terminalId, ParamUtils.tellerId, ParamUtils.print_voucher_from_type, ParamUtils.print_voucher_time);
			ParamUtils.print_voucher_type = 4;
		} else if (PRINT_SIGN_MESSAGE == funcType) {
			printeState = PrintUtils.printSignMessage(aty.getResources(), mPrinter, ParamUtils.msg_cardNum, ParamUtils.msg_certType, ParamUtils.msg_certId
					, ParamUtils.msg_cardProductName, ParamUtils.msg_certName, ParamUtils.msg_signAccount, ParamUtils.msg_mobileNums, ParamUtils.msg_charges, ParamUtils.msg_amountLowerLimit
					, ParamUtils.msg_remindLowerlimit, ParamUtils.msg_isShowFree, ParamUtils.orgId, ParamUtils.terminalId, ParamUtils.tellerId, ParamUtils.print_voucher_from_type, ParamUtils.print_voucher_time);
			ParamUtils.print_voucher_type = 3;
		} else if (PRINT_SIGN_MOBILE_BANK == funcType) {
			printeState = PrintUtils.printOpenMobileBankService(aty.getResources(), mPrinter, ParamUtils.cusName, ParamUtils.certType, ParamUtils.certId, ParamUtils.cardNum, ParamUtils.serviceProject,
					ParamUtils.orgId, ParamUtils.terminalId, ParamUtils.tellerId, ParamUtils.print_voucher_from_type, ParamUtils.print_voucher_time);
			ParamUtils.print_voucher_type = 5;
		}
		if (printeState) {
			toast("打印成功");
			mySetResult(PRINT_SUCCESS, new Intent());
		} else {
			toast("打印失败");
			 mySetResult(PRINT_FAIL, new Intent());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {

			case CONNECT_DEVICE:
				if (resultCode == Activity.RESULT_OK) {
					bt_mac = intent.getExtras().getString(BluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
					bt_name = intent.getExtras().getString(BluetoothDeviceList.EXTRA_DEVICE_NAME);
					//dialog.show();
					new Thread(new Runnable() {
						public void run() {
							myOpertion.open(intent);
						}
					}).start();
				}
				break;
			case ENABLE_BT:
				if (resultCode == Activity.RESULT_OK) {
					myOpertion.chooseDevice();
				} else {
					toast(aty.getResources().getString(R.string.bt_not_enabled));
				}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (myOpertion != null) {
			myOpertion.close();
		}

	}
}
