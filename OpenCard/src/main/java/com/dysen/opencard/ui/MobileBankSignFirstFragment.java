package com.dysen.opencard.ui;

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

import com.dysen.commom_library.utils.DialogUtils;
import com.dysen.commom_library.utils.SocketUtil;
import com.dysen.commom_library.utils.StringUtils;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.base.ParentFragment;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.http.SocketThread;
import com.dysen.opencard.intfaceImpl.SprinerHelper;
import com.dysen.opencard.itfcace.SprinerInterface;
import com.dysen.socket_library.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by hutian on 2018/3/5.
 *
 * @手机银行签约第一个页面
 */


public class MobileBankSignFirstFragment extends ParentFragment implements SprinerInterface{

	Unbinder unbinder;
	@BindView(R.id.certType)
	Spinner certType;
	@BindView(R.id.et_certID_item)
	EditText etCertIDItem;
	@BindView(R.id.btn_readCert)
	Button btnReadCert;
	@BindView(R.id.et_sign_userName)
	EditText etSignUserName;
	@BindView(R.id.ll_loginPassword)
	LinearLayout llLoginPassword;
	@BindView(R.id.btn_submit)
	Button btnSubmit;
	@BindView(R.id.btn_back)
	Button btnBack;


	private Context context;
	private MobileBankSignFragment mobileBankSignFragment;
	private String certId;
	private String userName;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	public String certTypeStr;
	private int certTypeIndex = 0;
	private String transCode;
	private Object obj=null;
	private int msgType;
	private List<String> responsList;
	private String respState;//交易返回状态
	private ArrayList<String> transList;
	private String BkCustNo;
	private String OpenWyFlag;
	private String OpenBankFlag;
	private String WyRzWay;
	private String BankRzWay;
	private String [] fragmentData=new String[8];
	private boolean isFastClick;//判断按钮是否重复点击


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mobilebank_first, container, false);

		unbinder = ButterKnife.bind(this, view);
		context = getActivity();
		initView();
		return view;

	}

	private void initView() {
		fragmentManager = getFragmentManager();
//		ViewUtils.setText(context, "421124199104187030", etCertIDItem);
//		ViewUtils.setText(context, "胡田", etSignUserName);
		final String[] cert_type = getResources().getStringArray(R.array.cert_type);
		SprinerHelper.setSprinerCallBack(this);
		SprinerHelper.showCommonSpinner(context, cert_type, certType,this);
		//commonSpinner(context, cert_type, certType);
		/*etCertIDItem.setText("420104199311214015");
		etSignUserName.setText("陈晟");*/
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == -2) {
				toast("交易超时，请重试或检查网络！！！");
			}
			if (msg.obj != null) {
				obj = msg.obj;
				msgType=msg.what;
				parseData(obj);
			}
		}
	};

	private void parseData(Object obj) {
		if(msgType==100){
			responsList = (List<String>) obj;
			LogUtils.d("hut","搜索查询客户信息"+responsList.toString());
			respState="交易成功";
			toast(respState);
		}else if(msgType==-3){
            respState=obj.toString(); //返回错误码
			toast(respState);
			return;
		}
		if(transCode.equals(SocketThread.selectCus)){
			if (responsList != null && responsList.size() == 2) {

				ParamUtils.cusNum  = responsList.get(0);//客户号
				ParamUtils.cusName = responsList.get(1);//客户姓名
				if(TextUtils.isEmpty(responsList.get(0))){
					toast("返回该核心客户号为空");
					return;
				}
				transCode=SocketThread.bankSignOpenSelect;
				responsList.clear();
				//查询客户电子银行开户签约状态
					transList=new ArrayList<String>();
					transList.add(StringUtils.splitString(certTypeStr,0));//01:身份证  发送后台取01
					transList.add(certId);
					transList.add(userName);

				SocketThread.transCode(context,transCode,transList, handler);
			} else {//没有客户号，创建客户信息(开户)
				LogUtils.d("hut","搜索查询客户信息失败==");
			}
		}else if(transCode.equals(SocketThread.bankSignOpenSelect)){// 电子银行签约开通状态查询
			if (responsList != null && responsList.size() == 5) {
				BkCustNo = responsList.get(0);
				OpenWyFlag = responsList.get(1);
				OpenBankFlag = responsList.get(2);
				WyRzWay = responsList.get(3);
				BankRzWay = responsList.get(4);
				//交易查询成功切换
				swithcFragment();
			}
		}
	}

	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	/**
	 * 读数据
	 */
	private void readInfo(int index) {
			Intent intent = new Intent(context, IDCheck.class);
			intent.putExtra(IDCheck.FUNC_NAME, index);
			startActivityForResult(intent, index);

	}

	@OnClick({R.id.btn_readCert, R.id.btn_submit, R.id.btn_back})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_readCert:
				isFastClick=SocketUtil.isFastTimeClick(R.id.btn_readCert);
				if(!isFastClick) {
					readInfo(IDCheck.READ_CERT);
				}
				break;
			case R.id.btn_submit:
				isFastClick= SocketUtil.isFastTimeClick(R.id.btn_submit);
				if(!isFastClick){
					submitData();
				}
				break;
			case R.id.btn_back:
				DialogUtils.ShowPromptDailog(getActivity(), new DialogUtils.OnButtonClick() {
					@Override
					public void buttonClick(int id) {
						clearData();
					}
				});
				break;
		}
	}

	/**
	 * 提交数据，切换Fragment
	 */
	private void submitData() {
		certId = ViewUtils.getText(etCertIDItem);
		userName = ViewUtils.getText(etSignUserName);
		//certType = ViewUtils.getText(certType);
		if (TextUtils.isEmpty(certId)) {
			toast("身份证不能为空");
			return;
		}
		requestSelcetCusId();
	}

	/**
	 * 查询客户信息
	 */
	private void requestSelcetCusId() {

		transCode = SocketThread.selectCus;
		transList=new ArrayList<String>();
		transList.add(certId);
		transList.add(StringUtils.splitString(certTypeStr,0));//01:身份证  发送后台取01
		transList.add(userName);
		SocketThread.transCode(context,transCode,transList, handler);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == IDCheck.READ_CERT) {
			certId = intent.getStringExtra("cert_id");
			userName= intent.getStringExtra("cert_name");
			if (!TextUtils.isEmpty(certId)) {
				ViewUtils.setText(certId, etCertIDItem);
			}else if(!TextUtils.isEmpty(userName)){
				ViewUtils.setText(userName, etSignUserName);
			}
			if(!TextUtils.isEmpty(userName)){
				ViewUtils.setText(userName, etSignUserName);
			}
		}
	}

	private void swithcFragment() {
		ParamUtils.certId=certId;
		ParamUtils.cusName=userName;
		ParamUtils.certType=certTypeStr;
		Bundle bd=new Bundle();
		fragmentData[0]=certTypeStr;//
		fragmentData[1]=certId;//证件
		fragmentData[2]=userName;
		fragmentData[3]=BkCustNo;//网银客户号
		fragmentData[4]=OpenWyFlag;//是否开通网银状态 0->未开通 1--开通
		fragmentData[5]=OpenBankFlag;//是否开通手机银行 0->未开通 1--开通
		fragmentData[6]=WyRzWay;
		fragmentData[7]=BankRzWay;

        bd.putStringArray("fragmentData",fragmentData);
		transaction = fragmentManager.beginTransaction();
		if (mobileBankSignFragment == null) {
			mobileBankSignFragment = new MobileBankSignFragment();
//                    transaction.replace(R.id.fl_content, openCardFragment);
			mobileBankSignFragment.setArguments(bd);
			transaction.replace(R.id.fl_content, mobileBankSignFragment);
		} else {
			transaction.show(mobileBankSignFragment);
		}
		transaction.commit();
	}

	private void clearData() {
		ViewUtils.setText("", etCertIDItem);
		ViewUtils.setText("", etSignUserName);
	}


	@Override
	public void itemSelectClick(View view, String type, int pos) {
		certTypeStr=type;
	}
}
