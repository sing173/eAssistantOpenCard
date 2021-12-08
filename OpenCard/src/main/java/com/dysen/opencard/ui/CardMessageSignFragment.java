
package com.dysen.opencard.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.dysen.commom_library.utils.DialogUtils;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.commom_library.utils.SocketUtil;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.base.ParentFragment;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.http.SocketThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.dysen.commom_library.base.ParentActivity.aty;

/**
 * Created by hutian on 2018/1/29.
 *
 * @卡短信签约首页
 */


public class CardMessageSignFragment extends ParentFragment {

	Unbinder unbinder;
	@BindView(R.id.et_sign_account)
	EditText etSignAccount;
	@BindView(R.id.read_card)
	Button readCard;
	@BindView(R.id.et_sign_cardNumber)
	EditText etSignCardNumber;
	@BindView(R.id.btn_read_cardNumber)
	Button btnReadCardNumber;
	@BindView(R.id.et_card_password)
	EditText etCardPassword;
	@BindView(R.id.btn_read_password)
	Button btnReadPassword;
	@BindView(R.id.btn_search)
	Button btnSearch;
	@BindView(R.id.ll_cus_id)
	LinearLayout llCusId;
	@BindView(R.id.et_sign_userName)
	EditText etSignUserName;
	@BindView(R.id.certType)
	Spinner certType;
	@BindView(R.id.et_certID_item)
	EditText etCertIDItem;
	@BindView(R.id.btn_readCert)
	Button btnReadCert;
	@BindView(R.id.btn_ok)
	Button btnOk;
	@BindView(R.id.btn_close)
	Button btnClose;
	@BindView(R.id.ll_serach_result)
	LinearLayout llSerachResult;

	private boolean isShow=false;
	private String transCode;
	private String certName, certId, sex, beginDate, endDate, cardId, cardPwd, magCardId, finger;

	private boolean isFastClick=false;//判断按钮是否重复点击
	private int buttonId=0;
	private int index;
	private List<String> responsList;
	private String s;
	private String[] cert_type;
	private String certTypeStr;
	CardMessageSignSubmitFragment cardMessageSignSubmitFragment;
	private FragmentTransaction transaction;
	private String cardProductName, certNameCard, certNumCard, signAccount;
	private boolean checkIdNo=false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cardsign_card, container, false);

		unbinder = ButterKnife.bind(this, view);

		initView();
		return view;
	}

	private void initView() {
		aty = getActivity();
		cert_type = getResources().getStringArray(R.array.cert_type);
		commonSpinner(aty, cert_type, certType);
		ParamUtils.certType = certTypeStr;
	}

	private void commonSpinner(final Activity aty, final String[] type, final Spinner spinner) {
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(aty, android.R.layout
				.simple_spinner_item, type);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//绑定 Adapter到控件
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {

				//  toast(type[pos] + "" + pos);
				if (spinner.equals(certType)) {
					certTypeStr = type[pos];
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == -2) {
				toast("交易超时，请重试或检查网络！！！");
			}
			if(msg.obj!=null){
			parseData(msg);
			}
		}
	};

	private void parseData(Message msg) {
		responsList = new ArrayList<>();

		if (msg.what == -3) {
			s = (String) msg.obj;
		}
		if (msg.what == 100) {
			responsList = (List<String>) msg.obj;
			s = "交易成功";
		}
		toast(SocketThread.getTransStr(transCode)+s);
		if (transCode.equals(SocketThread.cardType)){
			if (responsList != null && responsList.size() > 0){
				cardProductName = responsList.get(0);
				certNameCard = responsList.get(1);
				signAccount = responsList.get(2);
				certNumCard = responsList.get(3);

				ViewUtils.setText(certNameCard, etSignUserName);
				ViewUtils.setText(signAccount, etSignAccount);
			}
		}
	}

	@SuppressLint("NewApi")
	@OnClick({R.id.btn_read_cardNumber,R.id.btn_read_password, R.id.btn_search, R.id.btn_readCert,R.id.btn_ok, R.id.btn_close})
	public void onViewClicked(View view) {
		int id=view.getId();
		switch (id){
			case R.id.btn_read_cardNumber://读银行卡
				buttonId=R.id.btn_read_cardNumber;
				readInfo(IDCheck.READ_CARD);
				break;
			case R.id.btn_read_password://读密码
				buttonId=R.id.read_card_password;
				readInfo(IDCheck.READ_PASSWORD);

				break;
			case R.id.btn_readCert://读身份证
				buttonId=R.id.read_cert;
				readInfo(IDCheck.READ_CERT);

				break;
			case R.id.btn_search:
				isFastClick= SocketUtil.isFastTimeClick(buttonId);
				if(!isFastClick){
//					ViewUtils.setText(aty, "6224121100033217", etSignCardNumber);
					if (!ViewUtils.getText(etCardPassword).isEmpty() || !ViewUtils.getText(etSignCardNumber).isEmpty())
						transCode(SocketThread.cardType);
				}
//					RequestSearchData(isShow);

				break;
			case R.id.btn_ok:
				isFastClick= SocketUtil.isFastTimeClick(buttonId);
				if(!isFastClick){
					if(checkData()){
					startFragment();
					}
				}
				break;
			case R.id.btn_close://关闭
				DialogUtils.ShowPromptDailog(aty, new DialogUtils.OnButtonClick() {
					@Override
					public void buttonClick(int id) {
						clearUiData();
					}
				});
				break;
		}
	}

	private boolean checkData() {
		if(TextUtils.isEmpty(ViewUtils.getText( etSignAccount))){
			toast("签约账户不能为空");
			return false;
		}else if(TextUtils.isEmpty(ViewUtils.getText( etSignUserName))){
			toast("签约卡号不能为空");
			return false;
		}else if(TextUtils.isEmpty(ViewUtils.getText( etCardPassword))){
			toast("卡号密码不能为空");
			return false;
		}else if(TextUtils.isEmpty(ViewUtils.getText( etSignUserName))){
			toast("客户名称不能为空");
			return false;
		}else if(TextUtils.isEmpty(ViewUtils.getText( etCertIDItem))){
			toast("身份证不能为空");
			return false;
		}else if(!checkIdNo){
			toast("查询到的证件号码与读取证件不符,请重新读二代证！");
			return false;
		}
		return true;
	}

	//清除所有数据
	private void clearUiData() {
		ViewUtils.setText("",etSignAccount);
		ViewUtils.setText("",etSignCardNumber);
		ViewUtils.setText("",etCardPassword);
		ViewUtils.setText("",etSignUserName);
		ViewUtils.setText("",etCertIDItem);
	}

	private void startFragment() {
        Bundle bd=new Bundle();
		transaction = getActivity().getSupportFragmentManager().beginTransaction();
		cardMessageSignSubmitFragment = new CardMessageSignSubmitFragment();
		initData();
		transaction.replace(R.id.fl_content, cardMessageSignSubmitFragment);
		transaction.commitAllowingStateLoss();
	}

	private void initData() {
		cardMessageSignSubmitFragment.setData(ViewUtils.getText(etSignAccount), ViewUtils.getText(etSignCardNumber), ViewUtils.getText(etCardPassword), certTypeStr,
				ViewUtils.getText(etCertIDItem), ViewUtils.getText(etSignUserName), cardProductName);
	}

	private void transCode(String transCode) {
		this.transCode = transCode;
		if (transCode.equals(SocketThread.cardType)){
			SocketThread.transCode(getActivity(), transCode, Arrays.asList(new String[]{ViewUtils.getText(etSignCardNumber)}), handler);
		}
	}

	/**
	 * 读身份证
	 */
	private void readInfo(int index) {
		isFastClick= SocketUtil.isFastTimeClick(buttonId);
		com.dysen.socket_library.utils.LogUtils.i("hut","是否重复点击"+isFastClick);
		if(isFastClick){
			com.dysen.socket_library.utils.LogUtils.i("hut","重复点击啦");
			return;
		}
		this.index = index;
		Intent intent = new Intent(getActivity(), IDCheck.class);
		intent.putExtra(IDCheck.FUNC_NAME, index);
		startActivityForResult(intent, index);
	}

	private void RequestSearchData(boolean isShow) {
		if(isShow){
			llSerachResult.setVisibility(View.VISIBLE);
		}else {
			llSerachResult.setVisibility(View.GONE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		LogUtils.d("hut", "请求码=" + requestCode + "结果=" + resultCode);
		if (requestCode == index) {
			switch (resultCode) {
				case IDCheck.READ_CERT://读身份证
					certName = intent.getStringExtra("cert_name");
					certId = intent.getStringExtra("cert_id");
					sex = intent.getStringExtra("cert_sex");
					beginDate = intent.getStringExtra("cert_begin");
					endDate = intent.getStringExtra("cert_end");

					ViewUtils.setText(aty, certId, etCertIDItem);
					ViewUtils.setText(aty,certName,etSignUserName);
	LogUtils.d(certNumCard+"---------certNumCard-------------"+certId);
					if(certId!=null&&certNumCard!=null) {
						if (certNumCard.equals(certId)) {
							btnOk.setEnabled(true);
							checkIdNo=true;
						} else if (!certNumCard.equals(certId)) {
							//btnOk.setEnabled(false);
							checkIdNo=false;
							toast("查询到的证件号码与读取证件不符,请重新读二代证！");
						}
					}
					break;
				case IDCheck.READ_CARD://读银行卡
					cardId = intent.getStringExtra("card_id");
					ParamUtils.cardNum = cardId;
					ViewUtils.setText(aty, cardId, etSignCardNumber);
					break;
				case IDCheck.READ_PASSWORD://读密码
					cardPwd = intent.getStringExtra("card_pwd");
					ViewUtils.setText(aty, cardPwd, etCardPassword);
					break;
			}
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}


