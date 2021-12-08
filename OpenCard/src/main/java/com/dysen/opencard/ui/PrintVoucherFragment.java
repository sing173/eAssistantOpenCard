package com.dysen.opencard.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dysen.commom_library.bean.PrintVoucherInfo;
import com.dysen.commom_library.utils.DialogUtils;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.commom_library.utils.ToastUtil;
import com.dysen.commom_library.views.LoadingDailog;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.base.ParentFragment;
import com.dysen.opencard.common.DataBaseManager;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.bean.ChangePassword;
import com.dysen.opencard.common.bean.CreateAccount;
import com.dysen.opencard.common.bean.MobileBankSign;
import com.dysen.opencard.common.bean.OpenCard;
import com.dysen.opencard.common.bean.SignMessage;
import com.dysen.opencard.printer.PrinterInfo;
import com.dysen.socket_library.utils.DateUtils;

import org.kymjs.kjframe.KJDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/20.
 */

public class PrintVoucherFragment extends ParentFragment {
	@BindView(R.id.btn_print_voucher_createId)
	Button printCreateidBt;
	@BindView(R.id.btn_print_voucher_opencard)
	Button printOpencardBt;
	@BindView(R.id.btn_print_voucher_activationcard)
	Button printActivationcardBt;
	@BindView(R.id.btn_print_voucher_cardmessagesign)
	Button printCardmessagesignBt;
	@BindView(R.id.btn_print_voucher_mobilebanksign)
	Button printMobilebanksignBt;
	@BindView(R.id.print_voucher_listview)
	ListView printVoucherLv;

	Unbinder unbinder;
	Activity aty;
	@BindView(R.id.print_voucher_tv1_date)
	TextView printVoucherTvDate;
	@BindView(R.id.print_voucher_tv2_custId)
	TextView printVoucherTvCustId;
	@BindView(R.id.print_voucher_tv3_custName)
	TextView printVoucherTvCustName;
	@BindView(R.id.print_voucher_tv4_certType)
	TextView printVoucherTvCertType;
	@BindView(R.id.print_voucher_tv5_certId)
	TextView printVoucherTvCertId;
	@BindView(R.id.print_voucher_tv6_cardNo)
	TextView printVoucherTvCardNo;
	@BindView(R.id.print_voucher_tv7_accountNo)
	TextView printVoucherTvAccountNo;
	@BindView(R.id.print_voucher_tv8_phoneNo)
	TextView printVoucherTvPhoneNo;
	@BindView(R.id.print_voucher_tv9_cardProductName)
	TextView printVoucherTvCardProductName;
	@BindView(R.id.print_voucher_tv10_chargeStandard)
	TextView printVoucherTvChargeStandard;
	@BindView(R.id.print_voucher_tv11_limitChangeLowest)
	TextView printVoucherTvLimitChangeLowest;
	@BindView(R.id.print_voucher_tv12_limitWarnLowest)
	TextView printVoucherTvLimitWarnLowest;
	@BindView(R.id.print_voucher_tv13_whetherShowLeft)
	TextView printVoucherTvWhetherShowLeft;
	@BindView(R.id.print_voucher_tv14_serviceProject)
	TextView printVoucherTvServiceProject;
	@BindView(R.id.print_voucher_tv15_branchNo)
	TextView printVoucherTvBranchNo;
	@BindView(R.id.print_voucher_tv16_terminalId)
	TextView printVoucherTvTerminalId;
	@BindView(R.id.print_voucher_tv17_tellerId)
	TextView printVoucherTvTellerId;
	@BindView(R.id.print_voucher_tv18_PrintTime)
	TextView printVoucherTvPrintTime;
	@BindView(R.id.print_voucher_tv19_printFlag)
	TextView printVoucherTvPrintFlag;
	private int index;
	private KJDB db;
	private List<PrintVoucherInfo> allList = new ArrayList<PrintVoucherInfo>();
	private List<PrintVoucherInfo> tempList = new ArrayList<PrintVoucherInfo>();
	private PrintVoucherInfoAdapter voucherInfoAdapter;
	private DataBaseManager liteOrm;
	private List<CreateAccount> createAccountList = new ArrayList<CreateAccount>();
	private List<OpenCard> openCardList = new ArrayList<OpenCard>();
	private List<ChangePassword> changePasswordList = new ArrayList<ChangePassword>();
	private List<SignMessage> signMessageList = new ArrayList<SignMessage>();
	private List<MobileBankSign> mobileBankSignList = new ArrayList<MobileBankSign>();
	boolean isQueryDataOk = false;
	private List<PrintVoucherInfo> tempcreateAccountList = new ArrayList<PrintVoucherInfo>();
	private List<PrintVoucherInfo> tempopenCardList = new ArrayList<PrintVoucherInfo>();
	private List<PrintVoucherInfo> tempchangePasswordList = new ArrayList<PrintVoucherInfo>();
	private List<PrintVoucherInfo> tempsignMessageList = new ArrayList<PrintVoucherInfo>();
	private List<PrintVoucherInfo> tempmobileBankSignList = new ArrayList<PrintVoucherInfo>();
	private LoadingDailog loadingDailog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_print_voucher, container, false);

		unbinder = ButterKnife.bind(this, view);

		initView();

		return view;
	}

	private void initView() {
		aty = getActivity();
		initViewFirst();
	}

	private void initViewFirst() {
		liteOrm = DataBaseManager.getInstance();
		db = KJDB.create(getActivity(), "voucherDb");
		printVoucherTvCardNo.setVisibility(View.GONE);
		printVoucherTvAccountNo.setVisibility(View.GONE);
		printVoucherTvCardProductName.setVisibility(View.GONE);
		printVoucherTvChargeStandard.setVisibility(View.GONE);
		printVoucherTvLimitChangeLowest.setVisibility(View.GONE);
		printVoucherTvLimitWarnLowest.setVisibility(View.GONE);
		printVoucherTvWhetherShowLeft.setVisibility(View.GONE);
		printVoucherTvServiceProject.setVisibility(View.GONE);
		allList = db.findAll(PrintVoucherInfo.class);
		ParamUtils.loadingMsg = "玩命查询中...";
		loadingDailog = DialogUtils.showLoadingDailog(aty, ParamUtils.loadingMsg);

		//1.删除超时相关记录(3天)
		String date= DateUtils.compare_date(-3);
		DeleteAllDataFromDB(date);
		printCreateidBt.setTextColor(aty.getResources().getColor(R.color.white));
		printCreateidBt.setBackground(getResources().getDrawable(R.drawable.common_button_selector));
		//QueryAllDataFromDB();
		new Thread(new Runnable() {
			//            @Override
			public void run() {
				isQueryDataOk = QueryAllDataFromDB();
			}
		}).start();

	}

	private void DeleteAllDataFromDB(String date) {
		liteOrm.deleteWhere(CreateAccount.class,"transDate",date);
		liteOrm.deleteWhere(OpenCard.class,"transDate",date);
		liteOrm.deleteWhere(ChangePassword.class,"transDate",date);
		liteOrm.deleteWhere(SignMessage.class,"transDate",date);
		liteOrm.deleteWhere(MobileBankSign.class,"transDate",date);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 100:
					//默认加载开户数据 //只能合并 大表，避免增加多个adpter 一个布局写死维护有点麻烦,整合要重新封装数据
					if(tempcreateAccountList != null && tempcreateAccountList.size() != 0) {
						voucherInfoAdapter = new PrintVoucherInfoAdapter(tempcreateAccountList);
						printVoucherLv.setAdapter(voucherInfoAdapter);
					}

					DialogUtils.closeLoadingDailog(aty, loadingDailog);
					break;
				case -100:
					ToastUtil.ShortToast(aty, "查询失败，暂无数据");
					DialogUtils.closeLoadingDailog(aty, loadingDailog);
					break;
			}
		}
	};

	/**
	 * 得到开户数据重新封装
	 */
	private void getCreateAccountData() {
		tempcreateAccountList.clear();
		if (createAccountList != null && createAccountList.size() > 0) {
			LogUtils.d("数据库查询结果" + createAccountList.toString());
			//再更新数据库次数

			for (int i = 0; i < createAccountList.size(); i++) {
				PrintVoucherInfo info = new PrintVoucherInfo();
				info.setSaveDate(createAccountList.get(i).getTransDate());
				info.setCustId(createAccountList.get(i).getCustId());
				info.setCustName(createAccountList.get(i).getCustName());
				info.setCertType(createAccountList.get(i).getCertType());
				info.setCertNo(createAccountList.get(i).getCertNo());
				info.setPhoneNo(createAccountList.get(i).getPhoneNo());
				info.setBranchNo(createAccountList.get(i).getBranchNo());
				info.setTerminalId(createAccountList.get(i).getTerminalId());
				info.setTellerId(createAccountList.get(i).getUserId());//柜员号
				info.setPrintVoucherType(createAccountList.get(i).getPrintType());
				info.setPrintTime(createAccountList.get(i).getPrintTime());
				tempcreateAccountList.add(info);
			}

		}
	}

	/**
	 * 得到开卡数据重新封装
	 */
	private void getOpenCardData() {
		tempopenCardList.clear();
		if (openCardList != null && openCardList.size() > 0) {
			LogUtils.d("数据库查询结果" + openCardList.toString());
			//再更新数据库次数

			for (int i = 0; i < openCardList.size(); i++) {
				PrintVoucherInfo info = new PrintVoucherInfo();
				info.setSaveDate(openCardList.get(i).getTransDate());
				info.setCustId(openCardList.get(i).getCustId());
				info.setCustName(openCardList.get(i).getCustName());
				info.setCardNo(openCardList.get(i).getICCardNo());
				info.setAccountNo(openCardList.get(i).getAccountNo());
				info.setCardName(openCardList.get(i).getCardName());
				info.setBranchNo(openCardList.get(i).getBranchNo());
				info.setTerminalId(openCardList.get(i).getTerminalId());
				info.setTellerId(openCardList.get(i).getUserId());//柜员号
				info.setPrintVoucherType(openCardList.get(i).getPrintType());
				info.setPrintTime(openCardList.get(i).getPrintTime());
				tempopenCardList.add(info);
			}
		}
	}

	/**
	 * 得到卡短信签约数据重新封装
	 */
	private void getCardSignMessageData() {
		tempmobileBankSignList.clear();
		if (signMessageList != null && signMessageList.size() >0) {
			LogUtils.d("数据库查询结果" + signMessageList.toString());

			for (int i = 0; i < signMessageList.size(); i++) {
				PrintVoucherInfo info = new PrintVoucherInfo();
				info.setSaveDate(signMessageList.get(i).getTransDate());
				info.setCardNo(signMessageList.get(i).getSignCardNum());//签约卡号
				info.setCertType(signMessageList.get(i).getCertType());
				info.setCertNo(signMessageList.get(i).getCertNo());
				info.setCustName(signMessageList.get(i).getCustName());
				info.setAccountNo(signMessageList.get(i).getChargeCardNo());//扣费账户
				info.setPhoneNo(signMessageList.get(i).getMobileNums());
				info.setChargeStandard(signMessageList.get(i).getChargetandards());
				info.setLimitChangeLowest(signMessageList.get(i).getBalanceChargeLowerlimit());//余额变动下限设置
				info.setLimitWarnLowest(signMessageList.get(i).getBalanceRemindLowerlimit());//余额告警下限设置
				info.setWhetherShowLeft(signMessageList.get(i).getIsShowFree());//是否显示账户余额
				info.setBranchNo(signMessageList.get(i).getBranchNo());
				info.setTerminalId(signMessageList.get(i).getTerminalId());
				info.setTellerId(signMessageList.get(i).getUserId());//柜员号
				info.setPrintVoucherType(signMessageList.get(i).getPrintType());
				info.setPrintTime(signMessageList.get(i).getPrintTime());
				tempmobileBankSignList.add(info);
			}
		}
	}

	/**
	 * 得到开密码修改数据重新封装
	 */
	private void getChangePasswordData() {
		tempchangePasswordList.clear();
		if (changePasswordList != null && changePasswordList.size() > 0) {
			LogUtils.d("数据库查询结果" + changePasswordList.toString());
			//再更新数据库次数

			for (int i = 0; i < changePasswordList.size(); i++) {
				PrintVoucherInfo info = new PrintVoucherInfo();
				info.setSaveDate(changePasswordList.get(i).getTransDate());
				info.setCustId(changePasswordList.get(i).getCustId());
				info.setCustName(changePasswordList.get(i).getCustName());
				info.setCardNo(changePasswordList.get(i).getCardNo());
				info.setCardName(changePasswordList.get(i).getCardProductName());
				info.setBranchNo(changePasswordList.get(i).getBranchNo());
				info.setTerminalId(changePasswordList.get(i).getTerminalId());
				info.setTellerId(changePasswordList.get(i).getUserId());//柜员号
				info.setPrintVoucherType(changePasswordList.get(i).getPrintType());
				info.setPrintTime(changePasswordList.get(i).getPrintTime());
				tempchangePasswordList.add(info);
			}
		}
	}

	/**
	 * 得到手机银行签约数据重新封装
	 */
	private void getMobileBankSignData() {
		tempsignMessageList.clear();
		if (mobileBankSignList != null && mobileBankSignList.size() > 0) {
			LogUtils.d("数据库查询结果" + mobileBankSignList.toString());
			//再更新数据库次数*9
			for (int i = 0; i < mobileBankSignList.size(); i++) {
				PrintVoucherInfo info = new PrintVoucherInfo();
				info.setSaveDate(mobileBankSignList.get(i).getTransDate());
				info.setCustName(mobileBankSignList.get(i).getCustName());
				info.setCertType(mobileBankSignList.get(i).getCertType());
				info.setCertNo(mobileBankSignList.get(i).getCertNo());
				info.setSignaccount(mobileBankSignList.get(i).getSignaccount());
				info.setServiceProject(mobileBankSignList.get(i).getServiceProject());
				info.setBranchNo(mobileBankSignList.get(i).getBranchNo());
				info.setTerminalId(mobileBankSignList.get(i).getTerminalId());
				info.setTellerId(mobileBankSignList.get(i).getUserId());//柜员号
				info.setPrintVoucherType(mobileBankSignList.get(i).getPrintType());
				info.setPrintTime(mobileBankSignList.get(i).getPrintTime());
				tempsignMessageList.add(info);
			}
		}
	}

	// add by huitian 2018-06-27 查询数据库是否有保存数据，及记录次数
	public boolean QueryAllDataFromDB() {
//		ParamUtils.cusNum = "8888888";
//		ParamUtils.certName = "胡田";
//		ParamUtils.cardNum = "6222010245693265";
//		ParamUtils.accountNum = "62221212121212";
		try {
			createAccountList = liteOrm.getQueryAll(CreateAccount.class);
			if (createAccountList != null && createAccountList.size() >0) {
				getCreateAccountData();
			}
			openCardList = liteOrm.getQueryAll(OpenCard.class);
			if (openCardList != null && openCardList.size() > 0) {
				getOpenCardData();
			}
			signMessageList = liteOrm.getQueryAll(SignMessage.class);
			if (signMessageList != null && signMessageList.size() > 0) {
				getCardSignMessageData();
			}
			//1.根据卡号唯一先查询上一次数据库更新打印次数记录
			changePasswordList =liteOrm.getQueryAll(ChangePassword.class);
			if (changePasswordList != null && changePasswordList.size() > 0) {
				getChangePasswordData();
			}
			//1.根据卡号唯一先查询上一次数据库更新打印次数记录
			mobileBankSignList = liteOrm.getQueryAll(MobileBankSign.class);
			if (mobileBankSignList != null && mobileBankSignList.size() >0) {
				getMobileBankSignData();
			}
			isQueryDataOk = true;
		} catch (Exception ex) {
			isQueryDataOk = false;
			ex.printStackTrace();
		}

		Message msg = new Message();
		if (isQueryDataOk) {
			msg.what = 100;
			msg.obj = "查询成功";

		} else {
			msg.what = -100;
			msg.obj = "查询失败";
		}
		handler.sendMessageDelayed(msg, 2000);
		return isQueryDataOk;

	}

    /*@Override
    public void onResume() {
        super.onResume();
        initViewFirst();
    }*/


	@OnClick({R.id.btn_print_voucher_createId, R.id.btn_print_voucher_opencard, R.id.btn_print_voucher_activationcard, R.id.btn_print_voucher_cardmessagesign, R.id.btn_print_voucher_mobilebanksign})
	public void onViewClicked(View view) {
		initBackGround();
		int checkId=view.getId();
		switch (view.getId()) {
			case R.id.btn_print_voucher_createId://查询开户信息
				showCheckedInfo(1,checkId);
				break;
			case R.id.btn_print_voucher_opencard://查询开卡信息
				showCheckedInfo(2,checkId);
				break;
			case R.id.btn_print_voucher_activationcard://查询卡激活信息
				showCheckedInfo(3,checkId);
				break;
			case R.id.btn_print_voucher_cardmessagesign://查询卡短信签约信息
				showCheckedInfo(4,checkId);
				break;
			case R.id.btn_print_voucher_mobilebanksign://查询手机银行签约信息
				showCheckedInfo(5,checkId);
				break;
            /*case R.id.print_voucher_print:
                printInfo(index);
                break;*/
		}
	}

	/**
	 *  控制切换展示内容，优化代码结构
	 * @param type
	 * @param id
	 */
	private void showCheckedInfo(int type,int id ) {

		if(1==type){
			printCreateidBt.setTextColor(aty.getResources().getColor(R.color.white));
			printCreateidBt.setBackground(getResources().getDrawable(R.drawable.common_button_selector));
			printVoucherTvCustId.setVisibility(View.VISIBLE);
			printVoucherTvPhoneNo.setVisibility(View.VISIBLE);
			printVoucherTvCertType.setVisibility(View.VISIBLE);
			printVoucherTvCardNo.setVisibility(View.VISIBLE);
			printVoucherTvAccountNo.setVisibility(View.GONE);
			printVoucherTvChargeStandard.setVisibility(View.GONE);
			printVoucherTvLimitChangeLowest.setVisibility(View.GONE);
			printVoucherTvLimitWarnLowest.setVisibility(View.GONE);
			printVoucherTvWhetherShowLeft.setVisibility(View.GONE);
			printVoucherTvCardProductName.setVisibility(View.GONE);
			printVoucherTvServiceProject.setVisibility(View.GONE);
			index = PrinterInfo.PRINT_OPEN_CUS;
			tempList=tempcreateAccountList;
		}else if(2==type){//开卡
			printOpencardBt.setTextColor(aty.getResources().getColor(R.color.white));
			printOpencardBt.setBackground(aty.getResources().getDrawable(R.drawable.common_button_selector));
			printVoucherTvCustId.setVisibility(View.VISIBLE);
			printVoucherTvAccountNo.setVisibility(View.VISIBLE);
			printVoucherTvCardNo.setVisibility(View.VISIBLE);
			printVoucherTvCardProductName.setVisibility(View.VISIBLE);
			printVoucherTvCertId.setVisibility(View.GONE);
			printVoucherTvCertType.setVisibility(View.GONE);
			printVoucherTvPhoneNo.setVisibility(View.GONE);
			printVoucherTvChargeStandard.setVisibility(View.GONE);
			printVoucherTvLimitChangeLowest.setVisibility(View.GONE);
			printVoucherTvLimitWarnLowest.setVisibility(View.GONE);
			printVoucherTvWhetherShowLeft.setVisibility(View.GONE);
			printVoucherTvServiceProject.setVisibility(View.GONE);
			index = PrinterInfo.PRINT_OPEN_CARD;
			tempList=tempopenCardList;
		}else if(3==type){//卡改密
			printActivationcardBt.setTextColor(aty.getResources().getColor(R.color.white));
			printActivationcardBt.setBackground(aty.getResources().getDrawable(R.drawable.common_button_selector));
			printVoucherTvCustId.setVisibility(View.VISIBLE);
			printVoucherTvAccountNo.setVisibility(View.GONE);
			printVoucherTvCardProductName.setVisibility(View.VISIBLE);
			printVoucherTvCertId.setVisibility(View.GONE);
			printVoucherTvCertType.setVisibility(View.GONE);
			printVoucherTvPhoneNo.setVisibility(View.GONE);
			printVoucherTvChargeStandard.setVisibility(View.GONE);
			printVoucherTvLimitChangeLowest.setVisibility(View.GONE);
			printVoucherTvLimitWarnLowest.setVisibility(View.GONE);
			printVoucherTvWhetherShowLeft.setVisibility(View.GONE);
			printVoucherTvServiceProject.setVisibility(View.GONE);
			index = PrinterInfo.PRINT_ACTIVATION_CARD;
			tempList=tempchangePasswordList;
		}else if(4==type){ //短信签约
			printCardmessagesignBt.setTextColor(aty.getResources().getColor(R.color.white));
			printCardmessagesignBt.setBackground(aty.getResources().getDrawable(R.drawable.common_button_selector));
			printVoucherTvCustId.setVisibility(View.GONE);
			printVoucherTvCardNo.setVisibility(View.VISIBLE);
			printVoucherTvCardNo.setText("签约账号");
			printVoucherTvAccountNo.setVisibility(View.VISIBLE);
			printVoucherTvAccountNo.setText("扣费卡号");
			printVoucherTvCardProductName.setVisibility(View.VISIBLE);
			printVoucherTvCardProductName.setText("联系方式");
			printVoucherTvPhoneNo.setVisibility(View.GONE);
			printVoucherTvChargeStandard.setVisibility(View.VISIBLE);
			printVoucherTvLimitChangeLowest.setVisibility(View.VISIBLE);
			printVoucherTvLimitWarnLowest.setVisibility(View.VISIBLE);
			printVoucherTvWhetherShowLeft.setVisibility(View.VISIBLE);
			index = PrinterInfo.PRINT_SIGN_MESSAGE;
			//tempList=tempsignMessageList;
			tempList=tempmobileBankSignList;
		}else if(5==type){
			printVoucherTvCardNo.setText("签约账号");
			printMobilebanksignBt.setTextColor(aty.getResources().getColor(R.color.white));
			printMobilebanksignBt.setBackground(aty.getResources().getDrawable(R.drawable.common_button_selector));
			printVoucherTvCustId.setVisibility(View.GONE);
			printVoucherTvAccountNo.setVisibility(View.GONE);
			printVoucherTvPhoneNo.setVisibility(View.GONE);
			printVoucherTvCardProductName.setVisibility(View.GONE);
			printVoucherTvChargeStandard.setVisibility(View.GONE);
			printVoucherTvLimitChangeLowest.setVisibility(View.GONE);
			printVoucherTvLimitWarnLowest.setVisibility(View.GONE);
			printVoucherTvWhetherShowLeft.setVisibility(View.GONE);
			printVoucherTvCertId.setVisibility(View.VISIBLE);
			printVoucherTvCertType.setVisibility(View.VISIBLE);
			printVoucherTvServiceProject.setVisibility(View.VISIBLE);
			index = PrinterInfo.PRINT_SIGN_MOBILE_BANK;
			//tempList=tempmobileBankSignList;
			tempList=tempsignMessageList;
		}
		voucherInfoAdapter = new PrintVoucherInfoAdapter(tempList);
		printVoucherLv.setAdapter(voucherInfoAdapter);
		voucherInfoAdapter.notifyDataSetChanged();//及时刷新一下
	}
    //初始化切换标题背景
	private void initBackGround() {
		printCreateidBt.setTextColor(aty.getResources().getColor(R.color.gray));
		printCreateidBt.setBackground(aty.getResources().getDrawable(R.drawable.read_button_selector));
		printOpencardBt.setTextColor(aty.getResources().getColor(R.color.gray));
		printOpencardBt.setBackground(getResources().getDrawable(R.drawable.read_button_selector));
		printCardmessagesignBt.setTextColor(aty.getResources().getColor(R.color.gray));
		printCardmessagesignBt.setBackground(aty.getResources().getDrawable(R.drawable.read_button_selector));
		printActivationcardBt.setTextColor(aty.getResources().getColor(R.color.gray));
		printActivationcardBt.setBackground(aty.getResources().getDrawable(R.drawable.read_button_selector));
		printMobilebanksignBt.setTextColor(aty.getResources().getColor(R.color.gray));
		printMobilebanksignBt.setBackground(aty.getResources().getDrawable(R.drawable.read_button_selector));
	}
	/**
	 * 开卡打印凭证 PrinterInfo.PRINT_OPEN_CARD
	 */
	private void printInfo(int index) {

		this.index = index;
		Intent intent = new Intent(aty, PrinterInfo.class);
		intent.putExtra(IDCheck.FUNC_NAME, index);
		intent.putExtra("PRINT_Voucher", PrinterInfo.PRINT_Voucher);
		startActivityForResult(intent, index);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		new Thread(new Runnable() {
			//            @Override
			public void run() {
				isQueryDataOk = QueryAllDataFromDB();
			}
		}).start();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	private class PrintVoucherInfoAdapter extends BaseAdapter {
		private List<PrintVoucherInfo> list;

		public PrintVoucherInfoAdapter(List<PrintVoucherInfo> list) {
			// TODO Auto-generated constructor stub
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (null == convertView) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_print_voucher_lv, null);
				holder.date= (TextView) convertView.findViewById(R.id.item_voucher_tv_date);
				holder.custId = (TextView) convertView.findViewById(R.id.item_voucher_custId);
				holder.custName = (TextView) convertView.findViewById(R.id.item_voucher_custName);
				holder.certType = (TextView) convertView.findViewById(R.id.item_voucher_certType);
				holder.certNo = (TextView) convertView.findViewById(R.id.item_voucher_certNo);
				holder.cardNo = (TextView) convertView.findViewById(R.id.item_voucher_cardNo);
				holder.accountNo = (TextView) convertView.findViewById(R.id.item_voucher_accountNo);
				holder.phoneNo = (TextView) convertView.findViewById(R.id.item_voucher_phoneNo);
				holder.branchNo = (TextView) convertView.findViewById(R.id.item_voucher_branchNo);
				holder.terminalId = (TextView) convertView.findViewById(R.id.item_voucher_terminalId);
				holder.tellerId = (TextView) convertView.findViewById(R.id.item_voucher_tellerId);
				holder.cardName = (TextView) convertView.findViewById(R.id.item_voucher_cardName);
				holder.chargeStandard = (TextView) convertView.findViewById(R.id.item_voucher_chargeStandard);
				holder.whetherShowLeft = (TextView) convertView.findViewById(R.id.item_voucher_whetherShowLeft);
				holder.limitChangeLowest = (TextView) convertView.findViewById(R.id.item_voucher_limitChangeLowest);
				holder.limitWarnLowest = (TextView) convertView.findViewById(R.id.item_voucher_limitWarnLowest);
				holder.serviceProject = (TextView) convertView.findViewById(R.id.item_voucher_serviceProject);
				holder.printTime = (TextView) convertView.findViewById(R.id.item_voucher_printTime);
				holder.printVoucher = (Button) convertView.findViewById(R.id.item_voucher_printVoucher_Bt);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			//先设置textView不可见

			holder.custId.setVisibility(View.GONE);
			holder.certType.setVisibility(View.GONE);
			holder.certNo.setVisibility(View.GONE);
			holder.phoneNo.setVisibility(View.GONE);
			holder.accountNo.setVisibility(View.GONE);
			holder.cardName.setVisibility(View.GONE);
			holder.chargeStandard.setVisibility(View.GONE);
			holder.whetherShowLeft.setVisibility(View.GONE);
			holder.limitChangeLowest.setVisibility(View.GONE);
			holder.limitWarnLowest.setVisibility(View.GONE);
			holder.serviceProject.setVisibility(View.GONE);
              //update by hutian 2018-06-26  简化
			if (!"".equals(list.get(position).getPrintVoucherType())) {
				if (list.get(position).getPrintVoucherType() == 1) {
					//开户  为空值 时布局也要要显示占位
					holder.date.setText(list.get(position).getSaveDate());
					holder.custId.setVisibility(View.VISIBLE);
					holder.cardNo.setVisibility(View.GONE);
					holder.custName.setVisibility(View.VISIBLE);
					holder.certType.setVisibility(View.VISIBLE);
					holder.certNo.setVisibility(View.VISIBLE);
					holder.phoneNo.setVisibility(View.VISIBLE);
					if (null != list.get(position).getUserId() && !"".equals(list.get(position).getSaveDate())) {
						holder.tellerId.setText(list.get(position).getTellerId());
					}
					//String custId, String custName, String certType, String certNo, String phoneNo, String branchNo, String terminalId, String userId
					if (null != list.get(position).getCustId() && !"".equals(list.get(position).getCustId())) {
						holder.custId.setText(list.get(position).getCustId());
					}
					if (null != list.get(position).getCustName() && !"".equals(list.get(position).getCustName())) {
						holder.custName.setText(list.get(position).getCustName());
					}
					if (null != list.get(position).getCertType() && !"".equals(list.get(position).getCertType())) {
						holder.certType.setText(list.get(position).getCertType());
					}
					if (null != list.get(position).getCertNo() && !"".equals(list.get(position).getCertNo())) {
						holder.certNo.setText(list.get(position).getCertNo());
					}
					if (null != list.get(position).getPhoneNo() && !"".equals(list.get(position).getPhoneNo())) {
						holder.phoneNo.setText(list.get(position).getPhoneNo());
					}
					if (null != list.get(position).getBranchNo() && !"".equals(list.get(position).getBranchNo())) {
						holder.branchNo.setText(list.get(position).getBranchNo());
					}
					if (null != list.get(position).getTerminalId() && !"".equals(list.get(position).getTerminalId())) {
						holder.terminalId.setText(list.get(position).getTerminalId());
					}
					if (null != list.get(position).getTellerId() && !"".equals(list.get(position).getTellerId())) {
						holder.tellerId.setText(list.get(position).getTellerId());
					}

					if (!"".equals(list.get(position).getPrintTime())) {
						holder.printTime.setText("补打"+list.get(position).getPrintTime()+"次");
					}
				} else if (list.get(position).getPrintVoucherType() == 2) {
					//开卡
					holder.date.setText(list.get(position).getSaveDate());
					holder.custId.setVisibility(View.VISIBLE);
					holder.accountNo.setVisibility(View.VISIBLE);
					holder.cardName.setVisibility(View.VISIBLE);
					//int printType, String custId, String custName, String ICCardNo, String accountNo, String cardName, String branchNo, String terminalId, String userId
					if (null != list.get(position).getCustId() && !"".equals(list.get(position).getCustId())) {
						holder.custId.setText(list.get(position).getCustId());
					}
					if (null != list.get(position).getCustName() && !"".equals(list.get(position).getCustName())) {
						holder.custName.setText(list.get(position).getCustName());
					}
					if (null != list.get(position).getCardNo() && !"".equals(list.get(position).getCardNo())) {
						holder.cardNo.setText(list.get(position).getCardNo());
					}
					if (null != list.get(position).getAccountNo() && !"".equals(list.get(position).getAccountNo())) {
						holder.accountNo.setText(list.get(position).getAccountNo());
					}
					if (null != list.get(position).getCardName() && !"".equals(list.get(position).getCardName())) {
						holder.cardName.setText(list.get(position).getCardName());
					}
					if (null != list.get(position).getBranchNo() && !"".equals(list.get(position).getBranchNo())) {
						holder.branchNo.setText(list.get(position).getBranchNo());
					}
					if (null != list.get(position).getTerminalId() && !"".equals(list.get(position).getTerminalId())) {
						holder.terminalId.setText(list.get(position).getTerminalId());
					}
					if (null != list.get(position).getTellerId() && !"".equals(list.get(position).getTellerId())) {
						holder.tellerId.setText(list.get(position).getTellerId());
					}
					if (!"".equals(list.get(position).getPrintTime())) {
						holder.printTime.setText("已补打"+list.get(position).getPrintTime()+"次");
					}
				} else if (list.get(position).getPrintVoucherType() == 3) {
					//短信
					holder.date.setText(list.get(position).getSaveDate());
					holder.accountNo.setVisibility(View.VISIBLE);
					holder.certType.setVisibility(View.VISIBLE);
					holder.certNo.setVisibility(View.VISIBLE);
					holder.cardName.setVisibility(View.VISIBLE);
					holder.phoneNo.setVisibility(View.VISIBLE);
					holder.chargeStandard.setVisibility(View.VISIBLE);
					holder.limitChangeLowest.setVisibility(View.VISIBLE);
					holder.limitWarnLowest.setVisibility(View.VISIBLE);
					holder.whetherShowLeft.setVisibility(View.VISIBLE);
					//签约卡号

					if (!"".equals(list.get(position).getCardNo())) {
						holder.cardNo.setText(list.get(position).getCardNo());
					}
					//
					if (null != list.get(position).getAccountNo()) {
						holder.accountNo.setText(list.get(position).getAccountNo());
					}
					if (null != list.get(position).getCertType() && !"".equals(list.get(position).getCertType())) {
						holder.certType.setText(list.get(position).getCertType());
					}

					if (null != list.get(position).getCertNo() && !"".equals(list.get(position).getCertNo())) {
						holder.certNo.setText(list.get(position).getCertNo());
					}
					if (null != list.get(position).getCardName() && !"".equals(list.get(position).getCardName())) {
						holder.cardName.setText(list.get(position).getCardName());
					}
					if (null != list.get(position).getCustName() && !"".equals(list.get(position).getCustName())) {
						holder.custName.setText(list.get(position).getCustName());
					}
					if (null != list.get(position).getPhoneNo() && !"".equals(list.get(position).getPhoneNo())) {
						holder.phoneNo.setText(list.get(position).getPhoneNo());
					}
					if (null != list.get(position).getChargeStandard() && !"".equals(list.get(position).getChargeStandard())) {
						//展示收费标准
						holder.cardName.setText(list.get(position).getChargeStandard());
					}
					//if (null != list.get(position).getLimitChangeLowest() && !"".equals(list.get(position).getLimitChangeLowest())) {
						//显示警告下限
						holder.limitChangeLowest.setText(list.get(position).getLimitChangeLowest() + "1");
					//}
					//if (null != list.get(position).getLimitWarnLowest() && !"".equals(list.get(position).getLimitWarnLowest())) {
						holder.limitWarnLowest.setText(list.get(position).getLimitWarnLowest() + "2");
					//}
					//if (null != list.get(position).getWhetherShowLeft() && !"".equals(list.get(position).getWhetherShowLeft())) {
						holder.whetherShowLeft.setText(list.get(position).getWhetherShowLeft());
					//}
					if (null != list.get(position).getBranchNo() && !"".equals(list.get(position).getBranchNo())) {
						holder.branchNo.setText(list.get(position).getBranchNo());
					}
					if (null != list.get(position).getTerminalId() && !"".equals(list.get(position).getTerminalId())) {
						holder.terminalId.setText(list.get(position).getTerminalId());
					}
					if (null != list.get(position).getTellerId() && !"".equals(list.get(position).getTellerId())) {
						holder.tellerId.setText(list.get(position).getTellerId());
					}
					if (!"".equals(list.get(position).getPrintTime())) {
						holder.printTime.setText("补打"+list.get(position).getPrintTime()+"次");
					}
				} else if (list.get(position).getPrintVoucherType() == 4) {
					//改密
					holder.custId.setVisibility(View.VISIBLE);
					holder.cardNo.setVisibility(View.VISIBLE);
					holder.cardName.setVisibility(View.VISIBLE);
					holder.date.setText(list.get(position).getSaveDate());
					//String custId, String custName, String cardNo, String cardName, String branchNo, String terminalId, String userId
					if (null != list.get(position).getCustId() && !"".equals(list.get(position).getCustId())) {
						holder.custId.setText(list.get(position).getCustId());
					}
					if (null != list.get(position).getCustName() && !"".equals(list.get(position).getCustName())) {
						holder.custName.setText(list.get(position).getCustName());
					}
					if (null != list.get(position).getCardName() && !"".equals(list.get(position).getCardName())) {
						holder.cardName.setText(list.get(position).getCardName());
					}
					if (null != list.get(position).getCardNo() && !"".equals(list.get(position).getCardNo())) {
						holder.cardNo.setText(list.get(position).getCardNo());
					}
					if (null != list.get(position).getBranchNo() && !"".equals(list.get(position).getBranchNo())) {
						holder.branchNo.setText(list.get(position).getBranchNo());
					}
					if (null != list.get(position).getTerminalId() && !"".equals(list.get(position).getTerminalId())) {
						holder.terminalId.setText(list.get(position).getTerminalId());
					}
					if (null != list.get(position).getTellerId() && !"".equals(list.get(position).getTellerId())) {
						holder.tellerId.setText(list.get(position).getTellerId());
					}
					if (!"".equals(list.get(position).getPrintTime())) {
						holder.printTime.setText("补打"+list.get(position).getPrintTime()+"次");
					}
				} else if (list.get(position).getPrintVoucherType() == 5) {
					//手机银行
					holder.date.setText(list.get(position).getSaveDate());
					holder.serviceProject.setVisibility(View.VISIBLE);
					holder.certType.setVisibility(View.VISIBLE);
					holder.certNo.setVisibility(View.VISIBLE);
					holder.cardNo.setVisibility(View.VISIBLE);
					//String custName, String certType,String certNo, String signaccount, String serviceProject, String branchNo, String terminalId,String tellerId
					if (null != list.get(position).getCustName() && !"".equals(list.get(position).getCustName())) {
						holder.custName.setText(list.get(position).getCustName());
					}
					if (null != list.get(position).getCertType() && !"".equals(list.get(position).getCertType())) {
						holder.certType.setText(list.get(position).getCertType());
					}
					if (null != list.get(position).getCertNo() && !"".equals(list.get(position).getCertNo())) {
						holder.certNo.setText(list.get(position).getCertNo());
					}
					if (null != list.get(position).getSignaccount() && !"".equals(list.get(position).getSignaccount())) {
						holder.cardNo.setText(list.get(position).getSignaccount());
					}
					if (null != list.get(position).getServiceProject() && !"".equals(list.get(position).getServiceProject())) {
						holder.serviceProject.setText(list.get(position).getServiceProject());
					}
					if (null != list.get(position).getBranchNo() && !"".equals(list.get(position).getBranchNo())) {
						holder.branchNo.setText(list.get(position).getBranchNo());
					}
					if (null != list.get(position).getTerminalId() && !"".equals(list.get(position).getTerminalId())) {
						holder.terminalId.setText(list.get(position).getTerminalId());
					}
					if (null != list.get(position).getTellerId() && !"".equals(list.get(position).getTellerId())) {
						holder.tellerId.setText(list.get(position).getTellerId());
					}
					if (!"".equals(list.get(position).getPrintTime())) {
						holder.printTime.setText("补打"+list.get(position).getPrintTime()+"次");
					}
				}

				holder.printVoucher.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (list.get(position).getPrintVoucherType() == 1) {
							ParamUtils.cusNum = list.get(position).getCustId();
							ParamUtils.certName = list.get(position).getCustName();
							ParamUtils.certType = list.get(position).getCertType();
							ParamUtils.certId = list.get(position).getCertNo();
							ParamUtils.telPhone = list.get(position).getPhoneNo();
							ParamUtils.orgId = list.get(position).getBranchNo();
							ParamUtils.terminalId = list.get(position).getTerminalId();
							ParamUtils.tellerId = list.get(position).getTellerId();
							ParamUtils.print_voucher_time = list.get(position).getPrintTime();
							ParamUtils.print_voucher_date = list.get(position).getSaveDate();
							index = PrinterInfo.PRINT_OPEN_CUS;
							ParamUtils.print_voucher_type = 1;

						} else if (list.get(position).getPrintVoucherType() == 2) {
							ParamUtils.cusNum = list.get(position).getCustId();
							ParamUtils.certName = list.get(position).getCustName();
							ParamUtils.cardNum = list.get(position).getCardNo();
							ParamUtils.accountNum = list.get(position).getAccountNo();
							ParamUtils.cardType = list.get(position).getCardName();
							ParamUtils.orgId = list.get(position).getBranchNo();
							ParamUtils.terminalId = list.get(position).getTerminalId();
							ParamUtils.tellerId = list.get(position).getTellerId();
							ParamUtils.print_voucher_time = list.get(position).getPrintTime();
							ParamUtils.print_voucher_date = list.get(position).getSaveDate();
							index = PrinterInfo.PRINT_OPEN_CARD;
							ParamUtils.print_voucher_type = 2;
						}  else if (list.get(position).getPrintVoucherType() == 3) {
							ParamUtils.msg_cardNum = list.get(position).getCardNo();
							ParamUtils.msg_certType = list.get(position).getCertType();
							ParamUtils.msg_certId = list.get(position).getCertNo();
							ParamUtils.msg_cardProductName = list.get(position).getCardName();
							ParamUtils.msg_certName = list.get(position).getCustName();
							ParamUtils.msg_signAccount = list.get(position).getAccountNo();
							ParamUtils.msg_mobileNums.add(list.get(position).getPhoneNo());
							ParamUtils.msg_charges = list.get(position).getChargeStandard();
							ParamUtils.msg_amountLowerLimit = list.get(position).getLimitChangeLowest() + "";
							ParamUtils.msg_remindLowerlimit = list.get(position).getLimitWarnLowest() + "";
							ParamUtils.msg_isShowFree = list.get(position).getWhetherShowLeft();
							ParamUtils.orgId = list.get(position).getBranchNo();
							ParamUtils.terminalId = list.get(position).getTerminalId();
							ParamUtils.tellerId = list.get(position).getTellerId();
							ParamUtils.print_voucher_time = list.get(position).getPrintTime();
							ParamUtils.print_voucher_date = list.get(position).getSaveDate();
							index = PrinterInfo.PRINT_SIGN_MESSAGE;
							ParamUtils.print_voucher_type = 3;
						}else if (list.get(position).getPrintVoucherType() == 4) {//改密码
							//getCustId(),getCustName(),getCardName(),getCardNo(),getBranchNo(),getTerminalId(),getUserId()
							ParamUtils.cusNum = list.get(position).getCustId();
							ParamUtils.cusName = list.get(position).getCustName();
							ParamUtils.cardNum = list.get(position).getCardNo();
							ParamUtils.cardProductName = list.get(position).getCardName();
							ParamUtils.orgId = list.get(position).getBranchNo();
							ParamUtils.terminalId = list.get(position).getTerminalId();
							ParamUtils.tellerId = list.get(position).getTellerId();
							ParamUtils.print_voucher_time = list.get(position).getPrintTime();
							ParamUtils.print_voucher_date = list.get(position).getSaveDate();
							index = PrinterInfo.PRINT_ACTIVATION_CARD;
							ParamUtils.print_voucher_type = 4;
						} else if (list.get(position).getPrintVoucherType() == 5) {
							ParamUtils.cusName = list.get(position).getCustName();
							ParamUtils.certType = list.get(position).getCertType();
							ParamUtils.certId = list.get(position).getCertNo();
							ParamUtils.cardNum = list.get(position).getCertNo();
							ParamUtils.serviceProject = list.get(position).getServiceProject();
							ParamUtils.orgId = list.get(position).getBranchNo();
							ParamUtils.terminalId = list.get(position).getTerminalId();
							ParamUtils.tellerId = list.get(position).getTellerId();
                            ParamUtils.print_voucher_time=list.get(position).getPrintTime();
                            ParamUtils.print_voucher_date=list.get(position).getSaveDate();
							index = PrinterInfo.PRINT_SIGN_MOBILE_BANK;
							ParamUtils.print_voucher_type = 5;
						}
						//ParamUtils.print_voucher_from_type = 2;
						printInfo(index);
					}
				});
			}

			return convertView;
		}

		class ViewHolder {
			TextView date;
			TextView custId;
			TextView custName;
			TextView certType;
			TextView certNo;
			TextView phoneNo;
			TextView branchNo;
			TextView terminalId;
			TextView tellerId;
			TextView accountNo;
			TextView cardName;
			TextView chargeStandard;
			TextView whetherShowLeft;
			TextView limitChangeLowest;
			TextView limitWarnLowest;
			TextView cardNo;
			TextView serviceProject;
			TextView printTime;
			Button printVoucher;
		}
	}
}
