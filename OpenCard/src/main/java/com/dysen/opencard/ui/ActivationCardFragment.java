package com.dysen.opencard.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dysen.commom_library.utils.CodeFormatUtils;
import com.dysen.commom_library.utils.DialogUtils;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.commom_library.utils.SocketUtil;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.base.ParentFragment;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.StrUtils;
import com.dysen.opencard.http.SocketThread;
import com.dysen.opencard.printer.PrinterInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by hutian on 2018/1/26.
 * 卡激活
 */
public class ActivationCardFragment extends ParentFragment implements View.OnClickListener{

	Unbinder unbinder;
	@BindView(R.id.cardNumber)
	EditText cardNumber;
	@BindView(R.id.read_card)
	Button readCard;
	@BindView(R.id.btn_search)
	Button btnSearch;
	@BindView(R.id.ll_cus_id)
	LinearLayout llCusId;
	@BindView(R.id.et_customer_Name)
	EditText etCustomerName;
	@BindView(R.id.et_card_product_name)
	EditText etCardProductName;
	@BindView(R.id.ll_sign_id)
	LinearLayout llSignId;
	@BindView(R.id.tv_password)
	TextView tvPassword;
	@BindView(R.id.card_password)
	EditText cardPassword;
	@BindView(R.id.read_card_password)
	Button readCardPassword;
	@BindView(R.id.tv_comfirepassword)
	TextView tvComfirepassword;
	@BindView(R.id.card_password2)
	EditText cardPassword2;
	@BindView(R.id.read_card_password2)
	Button readCardPassword2;
	@BindView(R.id.tv_comfirepassword2)
	TextView tvComfirepassword2;
	@BindView(R.id.card_comfirepassword)
	EditText cardComfirepassword;
	@BindView(R.id.read_card_comfirepassword)
	Button readCardComfirepassword;
	@BindView(R.id.tbR_password)
	TableRow tbRPassword;
	@BindView(R.id.submit)
	Button submit;
	@BindView(R.id.cancel)
	Button cancel;


	private BluetoothAdapter mBlueToothAdapter;
	private int index;
	Activity aty;
	private String cardId, cardPwd, cardOldPwd,
			magCardId;
	private String idCardNumber;
	private String oldPassword, newPassword;
	private List<String> updatePasswordList;
	private ArrayList<String> transList;
	private List<String> responsList;
	private boolean isRespons = false;

	private String transCode;
	public byte[] respData = null; //接收的字节
	int msgType = 0;
	private boolean isFastClick;//判断按钮是否重复点击
	private boolean isCheckPassword = false;//是否确认密码
	private String authSeqNo=""; //核心授权柜员号

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_activation_card, container, false);

		unbinder = ButterKnife.bind(this, view);

		initView();
		return view;
	}

	private void initView() {

		aty = getActivity();
		mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
//		ViewUtils.setText(aty, "6224121100033217", cardNumber);
//		ViewUtils.setText(aty, "55A75EEBAFC4C841", cardPassword);
//		ViewUtils.setText(aty, "02770D7E9BAC1104", cardPassword2);

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@OnClick({R.id.read_card,R.id.btn_search, R.id.read_card_password, R.id.read_card_password2, R.id.read_card_comfirepassword, R.id.submit, R.id.cancel})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_search:
				//验证通过，修改密码，先查询,再走修改交易
				requestSelectTransCode(SocketThread.selectPwd);
				break;
			case R.id.read_card://读IC卡
				readInfo(IDCheck.READ_CARD);
				break;
			case R.id.read_card_password:
				readInfo(IDCheck.READ_PASSWORD);
				break;
			case R.id.read_card_password2:
				readInfo(IDCheck.READ_PASSWORD2);
				break;
			case R.id.read_card_comfirepassword://再次确认密码
				isCheckPassword = true;
				readInfo(IDCheck.READ_PASSWORD2);
				break;
			case R.id.submit://提交验证
				isFastClick = SocketUtil.isFastTimeClick();
				if (!isFastClick) {
					cardActhiveSubmitData();
				}
				break;
			case R.id.cancel:
				DialogUtils.ShowPromptDailog(aty, new DialogUtils.OnButtonClick() {
					@Override
					public void buttonClick(int id) {
						clearUiData();
					}
				});
				break;
		}
	}
	/**
	 * 卡激活提交数据
	 */
	private void cardActhiveSubmitData() {
		if(TextUtils.isEmpty(ViewUtils.getText(etCustomerName))){
			toast("请先查询客户信息");
			return;
		}
		idCardNumber = ViewUtils.getText(cardNumber);
		oldPassword = ViewUtils.getText(cardPassword);
		newPassword = ViewUtils.getText(cardPassword2);
		String comfirPassword = ViewUtils.getText(cardComfirepassword);
		LogUtils.i(oldPassword + "===pwd===" + newPassword);
		if (TextUtils.isEmpty(idCardNumber)) {
			toast("卡号不能为空");
			return;
		} else if (TextUtils.isEmpty(oldPassword)) {
			toast("旧密码不能为空");
			return;
		} else if (TextUtils.isEmpty(newPassword)) {
			toast("新密码不能为空");
			return;
		} else if (TextUtils.equals(oldPassword, newPassword)) {
			toast("旧密码与新密码不能一致");
			return;
		}
		if (!TextUtils.equals(comfirPassword, newPassword)) {
			toast("两次输入新密码不一致");
			return;
		}

		//默认为0-需要检查授权  1-不检查授权
		SocketThread.setPackMsgHead(transCode, "0", "", "0");
		requestUpdatePwdCode(SocketThread.changePwd);

	}


	private void requestUpdatePwdCode(String transCode) {

		this.transCode = transCode;

		if (!TextUtils.isEmpty(idCardNumber)) {
			//updatePasswordList = TransParseStringEncodUtil.getResponList(respData, SocketThread.selectPwd);
			updatePasswordList.add(oldPassword);
			updatePasswordList.add(newPassword);
//            updatePasswordList.add("5DD77063E1DD41EB");
//            updatePasswordList.add("5DD77063E1DD41EB");
			SocketThread.transCode(aty, transCode, updatePasswordList, handler);
		} else {
			toast("输入不能为空！！！");
		}
	}

	/**
	 * 改密查询
	 * @param transCode
	 */
	private void requestSelectTransCode(String transCode) {

		this.transCode = transCode;
		idCardNumber=ViewUtils.getText(cardNumber);
		if (transCode.equals(SocketThread.selectPwd)) {
			if (!TextUtils.isEmpty(idCardNumber)) {
				updatePasswordList = new ArrayList<String>();
				updatePasswordList.add(idCardNumber);
				updatePasswordList.add("");
				SocketThread.transCode(aty, transCode, updatePasswordList, handler);
			} else {
				toast("卡号不能为空！！！");
			}
		}
	}

	public String s = "";
	public Object obj = null;
	private String respState;
	private byte[] bytes;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			bytes = ParamUtils.respMsgByte;
			switch (msg.what) {
				case 100:
					respState = StrUtils.respStateInfo(msg.what);
					msgType = 100;
					//成功解析
					parseResponsData(msg.obj);
					break;
				case -3:
					respState = (String) msg.obj;
					msgType = -3;
					parseResponsData(msg.obj);
					break;
				case -2:
					toast("交易超时，请重试或者检查网络！！！");
					break;
			}

		}
	};
	//解析报文返回来的数据
	private void parseResponsData(Object obj) {

		if (msgType == 100) {
			responsList = (List<String>) obj;
		} else if(msgType==-3){//失败没有返回数据不打印
			respState = (String) obj;
		}
		toast(SocketThread.getTransStr(transCode)+respState);
		if (transCode.equals(SocketThread.selectPwd)) {      //查询交易

			if (responsList != null && responsList.size() > 0) {//返回有数据

				LogUtils.i("hut", "查询卡密码成功返回结果=" + responsList.toString());
				ParamUtils.certId = responsList.get(0);//本次交易卡号
				ParamUtils.cardProductName = responsList.get(1);//查询卡产品名称
				ParamUtils.cusNum = responsList.get(2);//查询卡密获取客户号码
				ParamUtils.cusName = responsList.get(3);//查询卡密获取客户名称
                ViewUtils.setText(ParamUtils.cusName,etCustomerName);
				ViewUtils.setText(ParamUtils.cardProductName,etCardProductName);
                if(updatePasswordList!=null&&updatePasswordList.size()>0){//先清空一次
                	updatePasswordList.clear();
                }
				updatePasswordList = responsList;
			}
		} else if (transCode.equals(SocketThread.changePwd)) {
			//无论成功失败，请求后刷新一次页面
			clearUiData();
			//printInfo(PrinterInfo.PRINT_ACTIVATION_CARD);
			if (respState.equals("交易成功")) {//授权成功
				printInfo(PrinterInfo.PRINT_ACTIVATION_CARD);
			} else if (respState.equals("该交易需要授权")) {
				goToAuthorizeActivity();
			}
		}

	}

	/**
	 * 卡激活授权
	 */
	private void goToAuthorizeActivity() {
		Intent intent = new Intent(getContext(), AuthorizeActivity.class);
		authSeqNo = CodeFormatUtils.byte2StrIntercept(bytes, 114, 20);
		intent.putExtra("auth_seqNo", CodeFormatUtils.byte2StrIntercept(bytes, 114, 20));
//                            intent.putExtra("bk_seqNo", respHeadList.get(3));
		intent.putExtra("auth_capability", CodeFormatUtils.byte2StrIntercept(bytes, 154, 3));
		intent.putExtra("auth_errCode", CodeFormatUtils.byte2StrIntercept(bytes, 159, 6));
		intent.putExtra("auth_errMsg", CodeFormatUtils.byte2StrIntercept(bytes, 165, 120));
		intent.putExtra("auth_sealMsg", CodeFormatUtils.byte2StrIntercept(bytes, 285, 120));

		startActivityForResult(intent, AuthorizeActivity.ACTIVATION_CARD);
	}

	/**
	 * 打印凭证
	 */
	private void printInfo(int index) {
		Intent intent = new Intent(aty, PrinterInfo.class);
		intent.putExtra(IDCheck.FUNC_NAME, index);
		startActivityForResult(intent, index);
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

	/**
	 * 读取背夹信息
	 */
	private void readInfo(int index) {
		isFastClick = SocketUtil.isFastTimeClick();
		if (!isFastClick) {
			this.index = index;
			Intent intent = new Intent(aty, IDCheck.class);
			intent.putExtra(IDCheck.FUNC_NAME, index);
			startActivityForResult(intent, index);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == index) {
			switch (resultCode) {
				case IDCheck.READ_CARD://读银行卡
					cardId = intent.getStringExtra("card_id");
					ParamUtils.cardNum = cardId;
					ViewUtils.setText(aty, cardId, cardNumber);
					break;
				case IDCheck.READ_PASSWORD://读密码
					cardPwd = intent.getStringExtra("card_pwd");

					LogUtils.i("旧密码：" + cardPwd);
					ViewUtils.setText(aty, cardPwd, cardPassword);

					break;
				case IDCheck.READ_PASSWORD2:
					cardPwd = intent.getStringExtra("card_pwd");
					LogUtils.i("新密码：" + cardPwd);
					if (isCheckPassword) {//走确认密码
						ViewUtils.setText(aty, cardPwd, cardComfirepassword);//重新确认新密码
					} else {
						ViewUtils.setText(aty, cardPwd, cardPassword2);
					}
					isCheckPassword = false;
					break;
				case IDCheck.READ_MAGCARD://读磁条卡
					magCardId = intent.getStringExtra("mag_card_id");
					ParamUtils.cardNum = magCardId;
					toast("磁条卡：" + magCardId);
					break;
				default:
					break;
			}
		} else if (requestCode == AuthorizeActivity.ACTIVATION_CARD) {//卡激活授权
			if (resultCode == AuthorizeActivity.AUTH_SUCCESS) {//授权成功再次走改密交易
				transCode = SocketThread.changePwd;
				SocketThread.setPackMsgHead(transCode, "1", authSeqNo, "0");
				if(updatePasswordList!=null&&updatePasswordList.size()>0){
					SocketThread.transCode(getContext(), transCode, updatePasswordList, handler);
				}else {
					toast("修改密码失败");
				}

			} else if (resultCode == AuthorizeActivity.AUTH_FAIL) {
				toast("授权失败");
			}
		}
		else if(requestCode == PrinterInfo.PRINT_ACTIVATION_CARD){
			LogUtils.d("hut","卡激活打印");
			  if(resultCode==PrinterInfo.PRINT_SUCCESS){
				  //打印完成
				  clearUiData();
			  }
		}
	}

	/**
	 * 清理界面数据
	 *
	 * @param
	 */
	private void clearUiData() {
		ViewUtils.setText("", etCustomerName);
		ViewUtils.setText("", etCardProductName);
		ViewUtils.setText("", cardNumber);
		ViewUtils.setText("", cardPassword);
		ViewUtils.setText("", cardPassword2);
		ViewUtils.setText("", cardComfirepassword);
	}

	@Override
	public void onClick(View view) {

	}
}
