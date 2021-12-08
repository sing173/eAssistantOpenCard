package com.dysen.opencard.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dysen.opencard.base.ParentFragment;
import com.dysen.commom_library.utils.CodeFormatUtils;
import com.dysen.commom_library.utils.DialogAlert;
import com.dysen.commom_library.utils.DialogUtils;
import com.dysen.commom_library.utils.FormatUtil;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.commom_library.utils.SocketUtil;
import com.dysen.commom_library.utils.StringUtils;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.StrUtils;
import com.dysen.opencard.intfaceImpl.SprinerHelper;
import com.dysen.opencard.itfcace.SprinerInterface;
import com.dysen.opencard.printer.PrinterInfo;
import com.dysen.opencard.http.SocketThread;
import com.dysen.socket_library.msg.MsgField;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by hutian on 2018/3/1.
 *
 * @手机银行签约提交
 */


public class MobileBankSignFragment extends ParentFragment implements SprinerInterface {

	Unbinder unbinder;
	@BindView(R.id.et_coustomerName)
	EditText etCoustomerName;
	@BindView(R.id.et_englishName)
	EditText etEnglishName;
	@BindView(R.id.ll_sign_id)
	LinearLayout llSignId;
	@BindView(R.id.certType)
	EditText certType;
	@BindView(R.id.et_certID_item)
	EditText etCertIDItem;
	@BindView(R.id.ll_sign_cardType)
	LinearLayout llSignCardType;
	@BindView(R.id.et_telephone)
	EditText etTelephone;
	@BindView(R.id.btn_telephone)
	Button btnTelephone;
	@BindView(R.id.et_email)
	EditText etEmail;
	@BindView(R.id.et_contact_address)
	EditText etContactAddress;
	@BindView(R.id.et_postal_code)
	EditText etPostalCode;
	@BindView(R.id.et_sign_phone)
	EditText etSignPhone;
	@BindView(R.id.et_customer_manager_no)
	EditText etCustomerManagerNo;
	@BindView(R.id.et_customer_manager_name)
	EditText etCustomerManagerName;
	@BindView(R.id.ll_sign_amount)
	LinearLayout llSignAmount;
	@BindView(R.id.btn_add_account)
	Button btnAddAccount;
	@BindView(R.id.et_open_status)
	EditText etOpenStatus;
	@BindView(R.id.tv_qudao)
	TextView tvQudao;
	@BindView(R.id.ll_add_account_content)
	LinearLayout llAddAccountContent;
	@BindView(R.id.tv_account)
	TextView tvAccount;
	@BindView(R.id.tv_account_type)
	TextView tvAccountType;
	@BindView(R.id.tv_net_point)
	TextView tvNetPoint;
	@BindView(R.id.tv_dbxe)
	TextView tvDbxe;
	@BindView(R.id.tv_rljxe)
	TextView tvRljxe;
	@BindView(R.id.tv_rljbs)
	TextView tvRljbs;
	@BindView(R.id.tv_nljxe)
	TextView tvNljxe;
	@BindView(R.id.tv_delete)
	TextView tvDelete;
	@BindView(R.id.et_loginPassword)
	EditText etLoginPassword;
	@BindView(R.id.btn_loginPassword)
	Button btnLoginPassword;
	@BindView(R.id.et_transPassword)
	EditText etTransPassword;
	@BindView(R.id.btn_transPassword)
	Button btnTransPassword;
	@BindView(R.id.ll_loginPassword)
	LinearLayout llLoginPassword;
	@BindView(R.id.btn_submit)
	Button btnSubmit;
	@BindView(R.id.btn_back)
	Button btnBack;

	private Context context;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	private MobileBankSignFirstFragment mobileBankSignFirstFragment;
	private String[] fragmentData = new String[8];
	private String OpenWyFlag;
	private String OpenBankFlag;
	private String respState;//交易返回状态
	private ArrayList<String> transList;
	private String transCode;
	private Object obj;
	private int msgType;
	private String certTypeStr;
	private String certId;
	private String userName;
	private String BkCustNo;//网银客户号
	private List<String> responsList;
	private String openStatus;
	private String BkPCode;//短信关联码
	private EditText qkPassword;
	private EditText signaccount;
	private Spinner accountType;
	private Spinner pinzhenType;
	private EditText dbxe;
	private EditText dayljxe;
	private EditText dayljbs;
	private EditText yearljxe;
	private String[] account_type;
	private String[] pinzhen_type;
	private String signAccountType, signPenType;
	public List<MsgField> subFieldList = new ArrayList<MsgField>();
	private String phoneNumber;
	private boolean isFastClick = false;//判断按钮是否重复点击
	private boolean isHavaAccount = false;//是否已经有添加签约账户
	private int index;
	private String cardPwd;
	private int readPasswordType = 0;
	private String readCardId;
	private boolean VerificationPassword = false;//校验密码
	private Dialog AddAccountdialog;
	private String authSeqNo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mobilebank_messagesign, container, false);

		unbinder = ButterKnife.bind(this, view);
		context = getActivity();
		initView();
		return view;
	}

	private void initView() {
		account_type = getResources().getStringArray(R.array.sign_account_type);
		pinzhen_type = getResources().getStringArray(R.array.pinzhen_type);
		SprinerHelper.setSprinerCallBack(this);
		fragmentManager = getFragmentManager();
		Bundle bd = getArguments();
		if (bd != null) {
			fragmentData = bd.getStringArray("fragmentData");
			LogUtils.d("hut", "获取证件信息=" + fragmentData[0]);
			LogUtils.d("hut", "获取手机银行信息=" + fragmentData[4]);
			LogUtils.d("hut", "获取网上银行信息=" + fragmentData[5]);
		}
		certTypeStr = fragmentData[0];
		certId = fragmentData[1];
		userName = fragmentData[2];
		BkCustNo=fragmentData[3];
		OpenWyFlag = fragmentData[4];
		OpenBankFlag = fragmentData[5];
//		if ("0".equals(OpenBankFlag) && "0".equals(OpenWyFlag)) {
//			openStatus = "手机银行未开通  网上银行未开通";
//			//toast(openStatus);
//		} else if ("1".equals(OpenBankFlag) && "1".equals(OpenWyFlag)) {
//			openStatus = "手机银行已开通  网上银行已开通";
//		} else if ("0".equals(OpenBankFlag) && "1".equals(OpenWyFlag)) {
//			openStatus = "手机银行未开通  网上银行已开通";
//		} else if ("1".equals(OpenBankFlag) && "0".equals(OpenWyFlag)) {
//			openStatus = "手机银行已开通  网上银行未开通";
//		}else if("".equals(OpenBankFlag) && "".equals(OpenWyFlag)){//系统Bug，前置默认“” false 未开通
//			openStatus = "手机银行已开通  网上银行已开通";
//		}
		//update 20180515  业务规则修改，暂时 标志返回空 当做已经开通手机银行，开的网银
		String bankStatus="";
		String wyStatus="";
		if ("1".equals(OpenBankFlag) ) {
			bankStatus = "手机银行已开通";
		}else {
			bankStatus = "手机银行未开通";
		}
		if("1".equals(OpenWyFlag)){
			wyStatus="网上银行已开通";
		}else{
			wyStatus="网上银行未开通";
		}
		openStatus=bankStatus+" "+wyStatus;
		toast(openStatus);
		ViewUtils.setText(userName, etCoustomerName);
		ViewUtils.setText(certId, etCertIDItem);
		ViewUtils.setText(certTypeStr, certType);
		ViewUtils.setText(openStatus, etOpenStatus);
		certTypeStr = StringUtils.splitString(certTypeStr, 0);  //01-身份证 取01
		if ("1".equals(OpenBankFlag)||"1".equals(OpenWyFlag)) {//已经开通手机银行或者网银
			llLoginPassword.setVisibility(View.INVISIBLE);
		}
		requestBankSignInfo();
	}

	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
	private byte[] bytes;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			bytes = ParamUtils.respMsgByte;
			if (msg.what == -2) {
				toast("交易超时，请重试或检查网络！！！");
			}
			if (msg.obj != null) {
				obj = msg.obj;
				msgType = msg.what;
				parseData(obj);
			}
		}
	};

	@OnClick({R.id.btn_telephone, R.id.btn_add_account, R.id.btn_loginPassword, R.id.btn_transPassword, R.id.btn_submit,
			R.id.tv_delete, R.id.btn_back})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_telephone:
				showAddPhoneDailog();//录入手机号码
				break;
			case R.id.btn_add_account:
				if (isHavaAccount) {
					toast("您已经添加一个账户啦！");
					return;
				}
				showAddAccountDailog();
				break;
			case R.id.btn_loginPassword://登录密码
					readPasswordType = 1;
					readInfo(IDCheck.READ_PASSWORD,R.id.btn_loginPassword);
				break;
			case R.id.btn_transPassword://交易密码
					readPasswordType = 2;
					readInfo(IDCheck.READ_PASSWORD,R.id.btn_transPassword);
				break;
			case R.id.btn_submit://签约提交
				//提交前先验证授权

				if(TextUtils.isEmpty(ViewUtils.getText(etTransPassword))){
					toast("交易密码不能为空");
					return;
				}
				if(TextUtils.isEmpty(ViewUtils.getText(etTelephone))){
					toast("手机号码不能为空");
					return;
				}
				if ("0".equals(OpenBankFlag)&&"0".equals(OpenWyFlag)) {//第一次开手机银行或网银
					if (TextUtils.isEmpty(ViewUtils.getText(etLoginPassword))) {
						toast("登录密码不能为空");
						return;
					}
				}
				if("0".equals(OpenBankFlag)||"".equals(OpenBankFlag)){
					requestBankNoSign();
				} else {//已经开通手机银行
					requestBankSign();
				}
				break;
			case R.id.tv_delete://清除
				showClearDailog();
				//clearAccountInfo();
				break;
			case R.id.btn_back:
				DialogUtils.ShowPromptDailog(getActivity(), new DialogUtils.OnButtonClick() {
					@Override
					public void buttonClick(int id) {
						retunFirstFragment();
					}
				});
				break;
		}
	}

	private void parseData(Object obj) {
		if (msgType == 100) {
			responsList = (List<String>) obj;
			LogUtils.d("hut", SocketThread.getTransStr(transCode)+ "成功返回"+responsList.toString());
			respState = "交易成功";
			//toast(respState);
		} else if (msgType == -3) {
			respState = obj.toString(); //返回错误码
			toast(respState);
			//便于测试
			if (transCode.equals(SocketThread.messageVerification)) {// 发送短信验证
				if (responsList != null && responsList.size() == 1) {
					String s = responsList.get(0);
					//toast("size:" + responsList.size() + "------" + s);
				}
				etTelephone.setText(phoneNumber);
			}else {
				return;
			}
		}
		if (transCode.equals(SocketThread.bankSignInfoSearch)) {//查询个人电子银行签约信息
			toast("查询个人电子银行签约信息返回成功" + responsList.size());
			if (responsList != null && responsList.size() == 20) {

				BkCustNo = String.valueOf(Long.parseLong(responsList.get(0)));//网银客户号
				ParamUtils.cusName = responsList.get(1);
				setEditData(responsList);
				responsList.clear();
				requestBankAccount();
			} else {
				toast("查询个人电子银行签约信息失败！");
			}
		//	requestBankAccount();
		} else if (transCode.equals(SocketThread.bankSignAccountSearch)) {// 账户查询
			if (responsList != null && responsList.size() == 3) {
				subFieldList = ParamUtils.subFieldList;
				if (subFieldList != null&&subFieldList.size()>0) {
					//初始化账户数据
					initAccountData(subFieldList);
					LogUtils.i("hut", "查询账户返回信息列表:=" + subFieldList.toString());
				}
			}
		} else if (transCode.equals(SocketThread.VerificationPassword)) {
			if (responsList != null && responsList.size() > 0) {//检验通过
				AddAccount();
				if (AddAccountdialog != null&&AddAccountdialog.isShowing()) {
					AddAccountdialog.dismiss();
				}
			}
		} else if (transCode.equals(SocketThread.messageSend)) {// 发送短信
			if (responsList != null && responsList.size() == 1) {
				BkPCode = responsList.get(0);
			}
		} else if (transCode.equals(SocketThread.messageVerification)) {// 发送短信验证
			if (responsList != null && responsList.size() == 1) {
				String s = responsList.get(0);
				//toast("size:" + responsList.size() + "------" + s);
			}
			etTelephone.setText(phoneNumber);
		} else if (transCode.equals(SocketThread.bankNoSign)) {//开通手机银行签约
			if (responsList != null && responsList.size() >0) {
				String s = responsList.get(0);
				//开通手机银行签约成功
				ParamUtils.cardNum = ViewUtils.getText(tvAccount);
				ParamUtils.serviceProject = "开通手机渠道";
				responsList.clear();
				printInfo(PrinterInfo.PRINT_SIGN_MOBILE_BANK);
				etTelephone.setText(phoneNumber);

			} else if (StrUtils.getStateInfo(bytes).equals("该交易需要授权")) {
				goToAuthorizeActivity();
			}
		}else if(transCode.equals(SocketThread.dzqdwh)){//已经开通 电子渠道维护
			if (StrUtils.getStateInfo(bytes).equals("该交易需要授权")) {
				goToAuthorizeActivity();
			}else {
				String s = responsList.get(0);
				//开通手机银行签约成功
				ParamUtils.cardNum = ViewUtils.getText(tvAccount);
				ParamUtils.serviceProject = "开通手机渠道";
				printInfo(PrinterInfo.PRINT_SIGN_MOBILE_BANK);
			}
		}
	}
	private void goToAuthorizeActivity() {
		Intent intent = new Intent(getContext(), AuthorizeActivity.class);
		authSeqNo = CodeFormatUtils.byte2StrIntercept(bytes, 114, 20);
		try {
			intent.putExtra("transcode", transCode);
			intent.putExtra("auth_seqNo", CodeFormatUtils.byte2StrIntercept(bytes, 114, 20));
//                            intent.putExtra("bk_seqNo", respHeadList.get(3));
			intent.putExtra("auth_capability", CodeFormatUtils.byte2StrIntercept(bytes, 154, 3));
			intent.putExtra("auth_errCode", CodeFormatUtils.byte2StrIntercept(bytes, 159, 6));
			intent.putExtra("auth_errMsg", CodeFormatUtils.byte2StrIntercept(bytes, 165, 120));
			intent.putExtra("auth_sealMsg", CodeFormatUtils.byte2StrIntercept(bytes, 285, 120));
			startActivityForResult(intent, AuthorizeActivity.SIGN_MOBILE_BANK);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 反显初始化账户数据
	 *
	 * @param subFieldList
	 */
	private void initAccountData(List<MsgField> subFieldList) {
		for(int i=0;i<subFieldList.size();i++){
			String str=subFieldList.get(0).getValue();
		}
	}

	/**
	 * 反显客户邮编，联系地址
	 *
	 * @param responsList
	 */
	private void setEditData(List<String> responsList) {
		ViewUtils.setText(responsList.get(4), etEnglishName);
		//ViewUtils.setText(responsList.get(5),etTelephone);
		ViewUtils.setText(responsList.get(6), etEmail);
		ViewUtils.setText(responsList.get(7), etContactAddress);
		ViewUtils.setText(responsList.get(8), etPostalCode);
		ViewUtils.setText(responsList.get(9), etSignPhone);
		ViewUtils.setText(responsList.get(12), etCustomerManagerNo);
		ViewUtils.setText(responsList.get(13), etCustomerManagerName);

		//ViewUtils.setText("hutian", etEnglishName);

	}

	/**
	 * 电子渠道个人信息查询 N00130
	 */
	private void requestBankSignInfo() {
		transCode = SocketThread.bankSignInfoSearch;
		transList=new ArrayList<String>();
		transList.add(certTypeStr);//01:身份证  发送后台取01
		transList.add(certId);
		transList.add(userName);
		SocketThread.transCode(context, transCode, transList, handler);
	}

	/**
	 * 电子渠道账户信息查询 N00170
	 */
	private void requestBankAccount() {
		transCode = SocketThread.bankSignAccountSearch;
		transList = new ArrayList<String>();
		transList.add(certTypeStr);//01:身份证  发送后台取01
		transList.add(certId);
		transList.add(userName);
		SocketThread.transCode(context, transCode, transList, handler);
	}

	/**
	 * 发送手机银行已经签约请求 走N00180
	 */
	private void requestBankSign() {
		transCode = SocketThread.dzqdwh;
		//goToAuthorizeActivity();
		initBankHaveSignData();
		//默认为0-需要检查授权  1-不检查授权
		SocketThread.setPackMsgHead(transCode, "1", "", "0");
		SocketThread.transCode(context, transCode, transList, handler);
	}

	private void initBankHaveSignData() {

		transList = new ArrayList<String>();
		transList.add(BkCustNo);           // BkCustNo     网银客户号
		transList.add(ViewUtils.getText(tvAccount));           // BkAcctNo     账号
		transList.add("0");           // BkType2      操作类型
		transList.add(StringUtils.splitString(signAccountType,0));           // BkType1      账号类型
		transList.add(StringUtils.splitString(signPenType,0));           // BkVchType    凭证类型
		transList.add(ParamUtils.orgId);           // BkListNo2    开户网点号
		transList.add("");           // BkName2      开户网点名称
		transList.add("CNY");           // BkCurrNo     币种
		transList.add("0");           // BkBreakAmt   网银单笔限额
		transList.add("0");           // BkIntrAmt    网银日累计限额
		transList.add(ParamUtils.tellerId);           // BkTeller     柜员编号
		transList.add("");           // BkAuthTeller 授权员编号
		transList.add(ParamUtils.cusNum);           // HB_cutomerNo 核心客户号
		transList.add("0");           // BkDesc5      网银日累计笔数
		transList.add("0");           // BkTaxAmt     网银年累计限额
		transList.add(ViewUtils.getText(dbxe));           // BkAmt        手机单笔限额
		transList.add(ViewUtils.getText(dayljxe));			 //   BkAmt1     手机日累计限额
        transList.add(ViewUtils.getText(dayljbs));           //   BkDesc4    手机日累计笔数
		transList.add(ViewUtils.getText(yearljxe));			 //   BkAmt3     手机年累计限额
}
		/**
	 * 发送手机银行未签约请求 走N00110
	 */
	private void requestBankNoSign() {
		transCode=SocketThread.bankNoSign;
		//goToAuthorizeActivity();
		initBankNoSignData();
		//默认为0-需要检查授权  1-不检查授权
		//LogUtils.d("当前数据集合："+transCode+"=="+transList.size()+"==签约提交数据=="+transList.toString());
		SocketThread.setPackMsgHead(transCode, "1", "", "0");
		SocketThread.transCode(context,transCode,transList, handler);
	}

	private void initBankNoSignData() {
		if(TextUtils.isEmpty(ViewUtils.getText(etEnglishName))){
			toast("英文名不能为空");
			return;
		}
		transList=new ArrayList<String>();
		transList.add(userName);          //BkName1               客户名称
		transList.add(certTypeStr);          //BkType2               证件类型
		transList.add(certId);          //BkCertNo              证件号码
		transList.add(ParamUtils.cusNum);          //HB_cutomerNo          核心客户号
		transList.add(ViewUtils.getText(etEnglishName));          //HB_EngName1           英文名称
		transList.add("0");          //HB_CustomerLevel      客户级别   0
		transList.add("1");          //BkFlag2               是否开通手机银行
		transList.add("0010000000");          //BkDesc3               手机银行认证方式
		transList.add("0");          //BkFlag1               是否开通个人网银
		transList.add("");          //BkDesc2               个人网银认证方式
		transList.add(ViewUtils.getText(etTelephone));          //HB_phoneno            手机号码
		transList.add(ViewUtils.getText(etEmail));          //HB_Address            电子邮箱
		transList.add(ViewUtils.getText(etContactAddress));          //HB_Addr1              联系地址
		transList.add(ViewUtils.getText(etPostalCode));          //HB_PostCode1          邮政编号
		transList.add(ViewUtils.getText(etSignPhone));          //HB_ContactPhone       联系电话
		transList.add(ViewUtils.getText(etLoginPassword));          //HB_DefaultString2     登陆密码
		transList.add(ViewUtils.getText(etTransPassword));          //HB_DefaultString222   交易密码
		transList.add(ParamUtils.tellerId);          //BkTeller              柜员编号
		transList.add("");          //BkAuthTeller          授权员编号
		transList.add(ParamUtils.orgId);          //BkBrchNo              柜员网点机构号
		transList.add(ViewUtils.getText(tvAccount));          //BkAcctNo              账号
		transList.add("");          //BkAcctName            账号别名
		transList.add(ParamUtils.orgId);          //BkListNo2             核心开户网点
		transList.add("");          //BkName2               核心开户网点名称
		transList.add("CNY");          //BkCurrNo              币种
		transList.add(StringUtils.splitString(signAccountType,0));          //BkType1               帐号类型
		transList.add(StringUtils.splitString(signPenType,0));          //BkVchType             凭证类型
		transList.add(ViewUtils.getText(dbxe));          //BkAmt                 手机单笔限额
		transList.add(ViewUtils.getText(dayljxe));          //BkAmt1                手机日累计限额
		transList.add(ViewUtils.getText(dayljbs));          //BkDesc4               手机日累计笔数
		transList.add(ViewUtils.getText(yearljxe));          //BkAmt3                手机年累计限额
		transList.add("0");          //BkBreakAmt            网银单笔限额
		transList.add("0");          //BkIntrAmt             网银日累计限额
		transList.add("0");          //BkDesc5               网银日累计笔数
		transList.add("0");          //BkTaxAmt              网银年累计限额

	}


	/**
	 * //录入手机号码
	 */
	private void showAddPhoneDailog() {
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_phone, null);
		final Dialog dialog = DialogUtils.showCommonDialog(getActivity(), view, 4, 4);
		final EditText et_phone = view.findViewById(R.id.et_sign_phone);
		final EditText et_verificationCode = view.findViewById(R.id.et_check_code);
		TextView tv_exit = view.findViewById(R.id.tv_exit);
		Button btn_send_message = view.findViewById(R.id.btn_send_message);
		Button bt_ok = view.findViewById(R.id.btn_ok);
		//et_phone.setText("13296641225");
		btn_send_message.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				 phoneNumber=ViewUtils.getText(et_phone);
				 String verificationCode=ViewUtils.getText(et_verificationCode);
				boolean isPhone= FormatUtil.isMobileNO(phoneNumber);
				 if (TextUtils.isEmpty(phoneNumber)) {
					toast("输入手机号不能为空");
					return;
				}
				if(!isPhone){
					toast("输入手机号非法");
					return;
				}
				requestSendCode(phoneNumber);
			}
		});
		bt_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				 String phoneNumber=ViewUtils.getText(et_phone);
				 String verificationCode=ViewUtils.getText(et_verificationCode);
				boolean isPhone= FormatUtil.isMobileNO(phoneNumber);
				 if (TextUtils.isEmpty(phoneNumber)) {
					toast("输入手机号不能为空");
					return;
				} else if (TextUtils.isEmpty(et_verificationCode.getText())) {
					toast("验证码不能为空");
					return;
				}
				if(!isPhone){
					toast("输入手机号非法");
					return;
				}
				requestCheckCode(phoneNumber,verificationCode);
				dialog.dismiss();
			}
		});
		tv_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 发送证码走640002
	 */
	private void requestSendCode(String phone) {
		transCode=SocketThread.messageSend;
		transList=new ArrayList<String>();
		transList.add(phone);
		transList.add("移动发卡终端");
		transList.add("手机银行签约 ");//描述
		transList.add("");//备注
		SocketThread.transCode(context,transCode,transList, handler);
	}
	/**
	 * 发送校验验证码走640003
	 * @param phoneNumber
	 * @param verificationCode
	 */
	private void requestCheckCode(String phoneNumber, String verificationCode) {
		transCode=SocketThread.messageVerification;
		transList=new ArrayList<String>();
		transList.add(phoneNumber);
		transList.add(BkPCode);
		transList.add(verificationCode);
		SocketThread.transCode(context,transCode,transList, handler);
	}

	private void retunFirstFragment() {
		transaction = fragmentManager.beginTransaction();
		if (mobileBankSignFirstFragment == null) {
			mobileBankSignFirstFragment = new MobileBankSignFirstFragment();
			transaction.replace(R.id.fl_content, mobileBankSignFirstFragment);
		} else {
			transaction.show(mobileBankSignFirstFragment);
		}
		transaction.commit();
	}

	/**
	 * 添加账号
	 */
	private void showAddAccountDailog() {
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_account, null);

		 AddAccountdialog = DialogUtils.showCommonDialog(getActivity(), view, 8, 6);
		Button btnReadCard = view.findViewById(R.id.btn_read_card);
		Button btnPassword = view.findViewById(R.id.btn_read_password);
		Button btnOk = view.findViewById(R.id.btn_ok);
		TextView tv_exit = view.findViewById(R.id.tv_exit);
		 signaccount = view.findViewById(R.id.et_sign_account);
		 qkPassword = view.findViewById(R.id.et_qkPassword);
		 accountType = view.findViewById(R.id.account_type);
		 pinzhenType = view.findViewById(R.id.pinzhen_Type);
		 dbxe = view.findViewById(R.id.et_dbxe);
		 dayljxe = view.findViewById(R.id.et_day_ljxe);
		 dayljbs = view.findViewById(R.id.et_day_ljbs);
		 yearljxe = view.findViewById(R.id.et_year_ljxe);


//		ViewUtils.setText("622285284562",signaccount);
//		ViewUtils.setText("000000",qkPassword);
//		ViewUtils.setText("1000",dbxe);
//		ViewUtils.setText("1000",dayljxe);
//		ViewUtils.setText("3",dayljbs);
//		ViewUtils.setText("10000",yearljxe);

		SprinerHelper.showCommonSpinner(context,account_type,accountType,this);
		SprinerHelper.showCommonSpinner(context,pinzhen_type,pinzhenType,this);

		btnReadCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				readInfo(IDCheck.READ_CARD,R.id.btn_read_card);
			}
		});
		btnPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
					readPasswordType = 3;
					readInfo(IDCheck.READ_PASSWORD,R.id.btn_read_password);
			}
		});
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean checkInputOk=checkAccountData(signaccount,qkPassword,accountType,pinzhenType,dbxe,dayljxe,dayljbs,yearljxe);
				if(checkInputOk){//发送密码校验接口
					requestVerivacitionPassword();
					//AddAccount();
					//dialog.dismiss();
				}
			}
		});
		tv_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				getActivity().closeKeyboard();
				AddAccountdialog.dismiss();
			}
		});

	}

	private boolean checkAccountData(EditText signaccount, EditText qkPassword, Spinner accountType, Spinner pinzhenType, EditText dbxe, EditText dayljxe, EditText ljbs, EditText yearljxe) {
		if(TextUtils.isEmpty(ViewUtils.getText(signaccount))){
			toast("签约账号不能为空");
			return false;
		}else if(TextUtils.isEmpty(ViewUtils.getText(qkPassword))){
			toast("取款密码不能为空");
			return false;
		}else if(TextUtils.isEmpty(ViewUtils.getText(signaccount))){
			toast("签约账号不能为空");
			return false;
		}else if(TextUtils.isEmpty(ViewUtils.getText(signaccount))){
			toast("单笔限额不能为空");
			return false;
		}else if(TextUtils.isEmpty(ViewUtils.getText(signaccount))){
			toast("日累计限额不能为空");
			return false;
		}else if(TextUtils.isEmpty(ViewUtils.getText(signaccount))){
			toast("日累计笔数不能为空");
			return false;
		}else if(TextUtils.isEmpty(ViewUtils.getText(signaccount))){
			toast("年累计限额不能为空");
			return false;
		}
		LogUtils.d("hut","取款密码:="+ViewUtils.getText(qkPassword));

		return true;
	}

	/**
	 * 发送密码校验接口 走B37445
	 */
	private void requestVerivacitionPassword() {
		transCode = SocketThread.VerificationPassword;
		addVerivactionPasswordData();
		SocketThread.transCode(context,transCode,transList, handler);
	}

	private void addVerivactionPasswordData() {
		transList=new ArrayList<String>();
		transList.add(ViewUtils.getText(signaccount));       //HB_Pan             卡号
		transList.add("N");       //HB_Action          二磁校验标识
		transList.add("N");       //HB_Track2          Track2
		transList.add("Y");       //HB_ShowButton      验密标志
		transList.add(ViewUtils.getText(qkPassword));       //HB_PIN             密码
		transList.add("N");       //HB_CardFlag        卡状态校验标识
		transList.add("E");       //HB_TXN_SIGN        功能标识
		transList.add("");       //HB_acc_type        账户类型
		transList.add("");       //HB_AccoutSubType   账户子类
		transList.add("0");       //HB_IND             可读IC卡标识(即校验ARQC标识)
		transList.add("");       //HB_IC_CARD_CTR     应用主账号序列号
		transList.add("");       //HB_DefaultString3  55域长度
		transList.add("");       //HB_filler6         IC卡芯片信息、对应银联55域数据

	}

	/**
	 * 清除账号信息
	 */
	private void showClearDailog() {
			final DialogAlert dialog = ShowDialog(context,
					"是否删除添加的账号信息？");
			Button btn = (Button) dialog.getWindow().findViewById(R.id.btn_ok);
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
                    clearAccountInfo();
					dialog.close();
				}
			});
		}

	/**
	 * 清理账户
	 */
	private void clearAccountInfo() {
		int count=llAddAccountContent.getChildCount();
		for(int i=0;i<count-1;i++){
			TextView tv_context= (TextView) llAddAccountContent.getChildAt(i);
			tv_context.setText("");
		}
		isHavaAccount=false;
	}

	/**
	 * 添加账号验证
	 */
	private void AddAccount() {
		ViewUtils.setText("手机",tvQudao);
		ViewUtils.setText(ViewUtils.getText(signaccount),tvAccount);
		ViewUtils.setText(ViewUtils.getText(StringUtils.splitString(signAccountType,1)),tvAccountType);
		ViewUtils.setText(ViewUtils.getText(dbxe),tvDbxe);
		ViewUtils.setText(ViewUtils.getText(dayljxe),tvRljxe);
		ViewUtils.setText(ViewUtils.getText(dayljbs),tvRljbs);
		ViewUtils.setText(ViewUtils.getText(yearljxe),tvNljxe);
		isHavaAccount=true;//已经有账户蜡


	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == AuthorizeActivity.SIGN_MOBILE_BANK) {
			if (resultCode == AuthorizeActivity.AUTH_SUCCESS) {//授权成功再次走签约交易
				//transCode = SocketThread.bankNoSign;
				LogUtils.d("当前交易"+SocketThread.getTransStr(transCode));
				LogUtils.i("---授权成功，提交---");
				if("0".equals(OpenBankFlag)||"".equals(OpenBankFlag)){
					initBankNoSignData();
				} else {//已经开通手机银行
					initBankHaveSignData();
				}
				SocketThread.setPackMsgHead(transCode, "1", authSeqNo, "0");
				SocketThread.transCode(context,transCode,transList, handler);

			} else if (resultCode == AuthorizeActivity.AUTH_FAIL) {
				toast("授权失败");
			}
		}else if (resultCode == IDCheck.READ_CARD) {
			readCardId = intent.getStringExtra("card_id");
			ParamUtils.cardNum=readCardId;
			if (!TextUtils.isEmpty(readCardId)) {
				ViewUtils.setText(readCardId, signaccount);
			}
		}else if(resultCode == IDCheck.READ_PASSWORD){
			String pwd = intent.getStringExtra("card_pwd");
			if (!TextUtils.isEmpty(pwd)) {
				//根据调用位置不同给不同view赋值
				if(readPasswordType==1){
					ViewUtils.setText(pwd, etLoginPassword);
				}else if(readPasswordType==2){
					ViewUtils.setText(pwd, etTransPassword);
				}else if(readPasswordType==3) {
					ViewUtils.setText(pwd, qkPassword);
				}
			}
		}else if(resultCode == PrinterInfo.PRINT_SIGN_MOBILE_BANK){//打印手机银行签约凭条
			clearUiData();
		}
	}

	private void clearUiData(){
		ViewUtils.setText("",etCoustomerName);
		ViewUtils.setText("",etEnglishName);
		ViewUtils.setText("",etCertIDItem);
		ViewUtils.setText("",etLoginPassword);
		ViewUtils.setText("",etTransPassword);
		clearAccountInfo();
	}

	@Override
	public void itemSelectClick(View view, String type, int pos) {
		int id=view.getId();
		String ids= (String) view.getTag();
		switch (id){
			case R.id.account_type:
				signAccountType=type;
				break;
			case R.id.pinzhen_Type:
				signPenType=type;
				break;
		}
		LogUtils.i("hut","选择账号类型是="+signAccountType);
		//toast(type);
	}

	private void readInfo(int index,int buttonId) {
		isFastClick= SocketUtil.isFastTimeClick(buttonId);
		LogUtils.i("hut","是否重复点击"+isFastClick);
		if(!isFastClick){
			this.index = index;
			Intent intent = new Intent(context, IDCheck.class);
			intent.putExtra(IDCheck.FUNC_NAME, index);
			startActivityForResult(intent, index);
		}

	}
	/**
	 * 打印凭证
	 */
	private void printInfo(int index) {

		//this.PRINT_FUNC = index;
		Intent intent = new Intent(getContext(), PrinterInfo.class);
		intent.putExtra(PrinterInfo.FUNC_NAME, index);
		startActivityForResult(intent, index);
	}

}
