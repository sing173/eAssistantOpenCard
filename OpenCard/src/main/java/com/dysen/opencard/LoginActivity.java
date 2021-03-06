package com.dysen.opencard;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dysen.commom_library.bean.BlueDeviceInfo;
import com.dysen.commom_library.utils.CodeFormatUtils;
import com.dysen.commom_library.utils.DialogUtils;
import com.dysen.commom_library.utils.FileUtils;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.commom_library.utils.PermissionUtils;
import com.dysen.commom_library.utils.PreferencesUtils;
import com.dysen.commom_library.utils.ShowCommonTypeWindow;
import com.dysen.commom_library.utils.SocketUtil;
import com.dysen.commom_library.views.LoadingDailog;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.base.BaseApplication;
import com.dysen.opencard.base.ParentActivity;
import com.dysen.opencard.common.ConstantValue;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.StrUtils;
import com.dysen.opencard.common.bean.AreaBean;
import com.dysen.opencard.http.SocketThread;
import com.dysen.opencard.printer.PrinterInfo;
import com.dysen.opencard.ui.MainActivity;
import com.examples.outputjar.BlueUtils;
import com.examples.outputjar.ClsUtils;

import org.kymjs.kjframe.KJDB;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends ParentActivity {


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
	@BindView(R.id.iv_org_img)
	ImageView ivOrgImg;
	@BindView(R.id.edt_teller_id)
	EditText edtTellerId;
	@BindView(R.id.edt_org_id)
	EditText edtOrgId;
	@BindView(R.id.edt_terminal_id)
	EditText edtTerminalId;
	@BindView(R.id.edt_teller_finger)
	EditText edtTellerFinger;
	@BindView(R.id.btn_read_finger)
	Button btnReadFinger;
	@BindView(R.id.btn_teller_login)
	Button btnTellerLogin;
	@BindView(R.id.ll_cus_id)
	LinearLayout llCusId;
	@BindView(R.id.btn_parms)
	Button btnParms;
	private int mIndex;
	private String finger;
	private List<String> transCodeList;
	private List<String> responsList;
	private List<String> transCodeListHead;
	private KJDB kjdb;
	private String[] device_type;
	private int DEVICE_GUOGUANG_ID = 1;
	private int DEVICE_SHENSI_ID = 2;
	private int DEVICE_WOQI_ID = 3;
	private String deviceName;
	private List<BlueDeviceInfo> blueDevice = new ArrayList<BlueDeviceInfo>();
	private int searchGuoguang, searchShensi, searchWoqi;
	private int bondedGuoguang, bondedShensi, bondedWoqi;
	private int neededConGuoguang, neededConShensi, neededConWoqi;
	private String tempAddress;
	private ParingReceived pReceiver;
	private String tempPin;
	private LoadingDailog loadingDailog;
	private boolean isFastClick;//??????????????????????????????
	private String transCode;
	private HashMap<String, String> hashMap = new HashMap<String, String>();
	private int deviceType=0;
	private PreferencesUtils pre;
	private MyTextWatcher watcher;

	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ListView pairedListView;
	private Dialog bluePrinterDialog;
	private String bluePrinterName;//???????????????
	private boolean isRepair=false;
	private ArrayList<String> blueList=new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
		PermissionUtils.verrifyStoragePermissions(this);
		if(!isLocationEnabled(LoginActivity.this)){
			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}
		initView();

	}

	@Override
	protected void onRestart() {
		//LogUtils.d("LoginActivityon????????????????????????????????????????????????");
		if(ConstantValue.SignOutTag==1){
			edtTellerId.setText(ParamUtils.tellerId);
			edtOrgId.setText(ParamUtils.orgId);
			edtTerminalId.setText(ParamUtils.terminalId);
			edtTellerFinger.setText("????????????");
		}
		super.onRestart();

	}

	private void initData() {
		Intent intent=getIntent();
		if(intent!=null&&intent.getData()!=null){
			LogUtils.d("????????????????????????????????????????????????????????????"+intent.getStringExtra("tellerId")+"=="+
					intent.getStringExtra("orgId")+"=="+intent.getStringExtra("terminalId"));
			edtTellerId.setText(intent.getStringExtra("tellerId"));
			edtOrgId.setText(intent.getStringExtra("orgId"));
			edtTerminalId.setText(intent.getStringExtra("terminalId"));

		}
	}

	private void initView() {

		kjdb = KJDB.create(this, "country");
		txtTitle.setText(getString(R.string.app_name_cn));
		txtExit.setText(getString(R.string.safe_exit));

		Intent intent=getIntent();
		if(intent!=null&&intent.getData()!=null){
			LogUtils.d("??????????????????????????????????????????????????????"+intent.getStringExtra("tellerId")+"=="+
					intent.getStringExtra("orgId")+"=="+intent.getStringExtra("terminalId"));
			edtTellerId.setText(intent.getStringExtra("tellerId"));
			edtOrgId.setText(intent.getStringExtra("orgId"));

		}

		/*edtTellerId.setText("2000152");
		edtOrgId.setText("10160");
		edtTerminalId.setText("021");*/
		ParamUtils.tellerId = ViewUtils.getText(edtTellerId);
		ParamUtils.orgId = ViewUtils.getText(edtOrgId);
		ParamUtils.terminalId = ViewUtils.getText(edtTerminalId);
		aty = this;
		addActivity(aty);
		BaseApplication.getInstance().addActivity(aty);
		pre = new PreferencesUtils(LoginActivity.this, "BlueTooth");

		pre.putInt("deviceId", 0);
		pre.putString("whetherConnected", "no");
		openBle();
//        discovery();
		refreshImg.setVisibility(View.INVISIBLE);
		ParamUtils.loadingMsg = "???????????????...";
		loadingDailog = DialogUtils.showLoadingDailog(this, ParamUtils.loadingMsg);
		//blueToothManager=(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		//mBlueToothAdapter=blueToothManager.getAdapter();
		//checkOpenBlue(mBlueToothAdapter);
		checkOpenBlue(mBlueToothAdapter);
		getBondedDevices();
		receiverRegister();
		searchDevices();
		watcher=new MyTextWatcher();
		chooseDeviceName.addTextChangedListener(watcher);
	}

	@OnClick({R.id.btn_read_finger, R.id.btn_teller_login, R.id.txt_exit, R.id.choose_device_name, R.id.refresh_img, R.id.btn_parms})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_read_finger://?????????

				deviceName = chooseDeviceName.getText().toString();
				if (deviceName.equals("?????????????????????")) {
					toast("????????????????????????");
					return;
				}
				if (deviceName.contains("??????????????????")) {
					deviceType=DEVICE_WOQI_ID;
					readInfo(IDCheck.READ_FINGER, DEVICE_WOQI_ID);
				} else if (deviceName.contains("??????????????????")) {
					deviceType=DEVICE_SHENSI_ID;
					readInfo(IDCheck.READ_FINGER, DEVICE_SHENSI_ID);
				} else if (deviceName.contains("??????????????????")) {
					deviceType=DEVICE_GUOGUANG_ID;
					readInfo(IDCheck.READ_FINGER, DEVICE_GUOGUANG_ID);
				}
				ParamUtils.deviceFlag=deviceType;
				//readDeviceId();
				break;
			case R.id.btn_teller_login://??????
				isFastClick = SocketUtil.isFastTimeClick(R.id.btn_teller_login);
				if (!isFastClick) {
					if(!isRepair){//????????????????????????????????????????????????
						refreshImg.setVisibility(View.VISIBLE);
						toast("?????????????????????????????????????????????????????????????????????");
						return;
					}else {//??????????????????
						refreshImg.setVisibility(View.INVISIBLE);
						login();
					}
//					login();
				}

//            gotoNextActivity(MainActivity.class);
				break;

			case R.id.choose_device_name:
			    /*for(int i=0;i<blueDevice.size();i++){
                    for(int j=i+1;i<blueDevice.size();j++){
                        if(blueDevice.get(j).equals(blueDevice.get(i))){
                            blueDevice.remove(blueDevice.get(j));
                            i--;
                        }
                    }
                }*/
			    //if(null==blueDevice){
					/*String s1="??????????????????";
					String s2="88:1B:99:0A:AC:65";
					BlueDeviceInfo b1=new BlueDeviceInfo();
					b1.setBlueDeviceName(s1);
					b1.setBlueDeviceAddress(s2);
			    	blueDevice.add(b1);*/

				//}
				ShowCommonTypeWindow.showCommonTypeWindow(LoginActivity.this, blueDevice, chooseDeviceName, 1920, 1200);
				break;

			case R.id.refresh_img://??????
				DialogUtils.showLoadingDailog(this, ParamUtils.loadingMsg);
				getBondedDevices();
				receiverRegister();
				searchDevices();
				break;
			case R.id.btn_parms://????????????
				//printInfoTest(PrinterInfo.PRINT_OPEN_CUS);
				ShowParamDailog();
				break;
			case R.id.txt_exit://????????????
				exitApp();
				break;
		}
	}

	private class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int flag=0;
            String macAddress=pre.getString("deviceAddress","");
            String temp=charSequence.toString();
            if(temp.contains("??????")){
                flag=BlueUtils.DEVICE_WOQI_ID;
            }else if(temp.contains("??????")){
                flag=BlueUtils.DEVICE_SHENSI_ID;
            }else if(temp.contains("??????")){
                flag=BlueUtils.DEVICE_GUOGUANG_ID;
            }
            //macAddress="88:1B:99:0A:AC:65";
            //flag=2;
            //toast(flag+"---"+macAddress+"---??????");
			//ParamUtils.deviceFlag=flag;
            BlueUtils.ConnectDevice(flag,LoginActivity.this,handler,macAddress);
        }
    }

	/**
	 * ?????????????????????????????? create by hutian 2018-03-21
	 */
	private void ShowParamDailog() {
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_update_params, null);
		final Dialog dialog = DialogUtils.showCommonDialog(this, view);
		dialog.show();
		TextView tv_close = view.findViewById(R.id.tv_exit);
		Button btn_ok = view.findViewById(R.id.btn_submit);
		final EditText etServerIp = view.findViewById(R.id.et_serverIp);
		final EditText etServerPort = view.findViewById(R.id.et_serverPort);
		RadioButton rb0=view.findViewById(R.id.rbt_0);
		RadioButton rb1=view.findViewById(R.id.rbt_1);
		LinearLayout update_params=view.findViewById(R.id.ll_update_params);
		Window window = dialog.getWindow();

		Display display =window.getWindowManager().getDefaultDisplay();
		int minHeight = (int) (display.getHeight()*0.5);
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,minHeight);
         update_params.setLayoutParams(lp);
         rb0.setOnClickListener(new View.OnClickListener() {
	         @Override
	         public void onClick(View view) {
		         ViewUtils.setText("186.16.6.163",etServerIp);
		         ViewUtils.setText("8088",etServerPort);
	         }
         });
		rb1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ViewUtils.setText("192.168.1.90",etServerIp);
				ViewUtils.setText("8088",etServerPort);
			}
		});
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TextUtils.isEmpty(ViewUtils.getText(etServerIp))) {
					toast("IP?????????????????????");
					return;
				} else if (TextUtils.isEmpty(ViewUtils.getText(etServerPort))) {
					toast("IP?????????????????????");
					return;
				}
				if (ViewUtils.getText(etServerIp).equals(ConstantValue.testIp)) {
					ParamUtils.deviceNum = ConstantValue.testDeviceNum;
					ParamUtils.CrmUrl=ConstantValue.CrmTestUrl;
				} else if (ViewUtils.getText(etServerIp).equals(ConstantValue.serverIp)) {
					ParamUtils.deviceNum = ConstantValue.serverDeviceNum;
					ParamUtils.CrmUrl=ConstantValue.CrmServerUrl;
				}
				ParamUtils.serverIp = ViewUtils.getText(etServerIp);
				ParamUtils.serverPort = ViewUtils.getText(etServerPort);
				dialog.dismiss();
			}
		});
		tv_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
	}

	public String s = "";
	public Object obj = null;
	private String respState;
	private byte[] bytes=null;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			 bytes = ParamUtils.respMsgByte;
			if (msg.what == -2) {
				toast("????????????????????????????????????????????????");
			}else if(msg.what ==12){
                String deviceId=hashMap.get("deviceId");
	                LogUtils.d("hut", deviceName+"??????????????????ID===" + deviceId);

	                if(!TextUtils.isEmpty(deviceId)){
		                ParamUtils.deviceNum=deviceId;
	                	ConstantValue.serverDeviceNum=deviceId;
	                }
	               // readInfo(IDCheck.READ_FINGER, deviceType);

			}else if(msg.what==13){
				toast("????????????ID??????,????????????");
			}
			if (msg.obj != null) {
				parseData(msg);
			}
		}
	};

	private void parseData(Message msg) {
		if (msg.what == -3) {
			s = (String) msg.obj;
		}
		if (msg.what == 100) {
			responsList = (List<String>) msg.obj;
			s = "????????????";
		}
		toast(SocketThread.getTransStr(transCode) + s);
		if (bytes != null) {
			try {
				ParamUtils.tellerName = CodeFormatUtils.byte2StrIntercept(bytes, bytes.length - 20, 20);
				ParamUtils.transStateCode = StrUtils.getTransStateeCode(bytes);
//                    new String(bytes, bytes.length-20, 20, "gbk").trim();
				LogUtils.i(new String(bytes, "gbk") + "?????????????????????" + StrUtils.getState(bytes) + "---" + ParamUtils.transState + "------------" + ParamUtils.tellerName);
				//  toast(StrUtils.getState(bytes));
				if (StrUtils.getState(bytes).equals(ParamUtils.TRANS_SUCCESS)) {
					ViewUtils.setText(this, "", edtTellerFinger);
					Bundle bundle=new Bundle();
					bundle.putString("orgId",ViewUtils.getText(edtOrgId));
					bundle.putString("tellerId",ViewUtils.getText(edtTellerId));
					bundle.putString("terminaId",ViewUtils.getText(edtTerminalId));
					bundle.putString("tellerName",ParamUtils.tellerName);
					gotoNextActivity(MainActivity.class,bundle);
					clearUIData();

				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	private void clearUIData() {
		edtTellerId.setText("");
		edtOrgId.setText("");
		edtTerminalId.setText("");
		edtTellerFinger.setText("????????????");
		edtTellerFinger.setCompoundDrawables(null,null,null,null);
	}

	private List<String> initList() {

		transCodeList = new ArrayList<>();

		transCodeList.add(ParamUtils.tellerId);
		transCodeList.add("");
		transCodeList.add(ParamUtils.orgId);
		transCodeList.add(ParamUtils.terminalId);
		transCodeList.add(ParamUtils.terllerFinger);
		transCodeList.add("0");//0-????????? 1-?????? ???????????????????????????
		transCodeList.add("");
		LogUtils.i("---"+ParamUtils.terllerFinger);
		return transCodeList;
	}

	@Override
	protected String commonSpinner(final Activity aty, final String[] type, final Spinner spinner) {
		// ??????Adapter?????????????????????
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(aty, android.R.layout
				.simple_spinner_item, type);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//?????? Adapter?????????
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
			                           int pos, long id) {

				deviceName = type[pos];
				toast(type[pos] + "" + pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});
		return deviceName;
	}

	private void login() {
//        initCountry();
		ParamUtils.tellerId = ViewUtils.getText(edtTellerId);
		ParamUtils.orgId = ViewUtils.getText(edtOrgId);
		ParamUtils.terminalId = ViewUtils.getText(edtTerminalId);
		if(!ViewUtils.getText(edtTellerFinger).equals("??????????????????!")){
			toast("?????????????????????????????????");
			return;
		}else if(TextUtils.isEmpty(ParamUtils.deviceNum)){
			toast("????????????????????????ID?????????");
			return;
		}
		if((null!=ParamUtils.tellerId) && (null!=ParamUtils.orgId) && (null!=ParamUtils.terminalId)&&(!"".equals(ParamUtils.tellerId))&&
				(!"".equals(ParamUtils.orgId))&&(!"".equals(ParamUtils.terminalId))){
		/*if (!ParamUtils.tellerId.isEmpty() && !ParamUtils.orgId.isEmpty() &&
				!ParamUtils.terminalId.isEmpty()) {*/
			initList();
			ParamUtils.countryList = kjdb.findAll(AreaBean.class);
			if (ParamUtils.countrys.length == 0) {

				ParamUtils.countrys = FileUtils.getArryString(FileUtils.readFromAssets(aty, "country.txt"), 2);

			}
//            SocketThread.setSn(GetDeviceSerialNumberUtil.getAndroidId(aty));
			SocketThread.setSn(ParamUtils.deviceNum);
			transCode = SocketThread.signIn;
			SocketThread.transCode(LoginActivity.this, transCode,
					transCodeList,
					handler);
//            SocketThread.smsCheck(LoginActivity.this);
			/*Bundle bundle=new Bundle();
			bundle.putString("orgId",ViewUtils.getText(edtOrgId));
			bundle.putString("tellerId",ViewUtils.getText(edtTellerId));
			bundle.putString("terminaId",ViewUtils.getText(edtTerminalId));
			bundle.putString("tellerName",ParamUtils.tellerName);
			gotoNextActivity(MainActivity.class,bundle);*/
		} else {
			toast("???????????????????????????");
		}
	}

	/**
	 * ????????? ??????  ????????????
	 */
	private void initCountry() {
		String[] str = FileUtils.getArryString(FileUtils.readFromAssets(this, "country.txt"), 2);

		AreaBean areaBean = new AreaBean();
		for (int i = 0; i < str.length; i++) {
			LogUtils.i("---------str------------" + str[i]);
			areaBean.setId(i + "");
			areaBean.setName(str[i]);
			kjdb.save(areaBean);
		}
		ParamUtils.countryList = kjdb.findAll(AreaBean.class);
		ParamUtils.countrys = ParamUtils.countryList.toArray(new String[ParamUtils.countryList.size()]);
	}

	/**
	 * ?????????
	 */
	private void readInfo(int index, int flag) {
		isFastClick = SocketUtil.isFastTimeClick();
//		BlueUtils.GetDeviceId(flag,LoginActivity.this,handler,hashMap);
		if (openBle() && !isFastClick) {
			this.mIndex = index;
			Intent intent = new Intent(this, IDCheck.class);
			LogUtils.d(mIndex + "-----------------------mIndex----------------" + index);
			intent.putExtra(IDCheck.FUNC_NAME, index);
			intent.putExtra("flag", flag);
			startActivityForResult(intent, index);
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == mIndex)
			switch (resultCode) {
				case IDCheck.READ_FINGER://?????????
					finger = intent.getStringExtra("finger");
				   if(!TextUtils.isEmpty(finger)){
				   		readDeviceId();
					   ParamUtils.terllerFinger = finger;
					   readFingerStatus(true);
					   toast("????????????????????????");
					   LogUtils.d("hut", ParamUtils.deviceNum + "???????????????" + finger);
				   }else {
					   toast("????????????????????????");

				   }
					break;
				case -1:
					toast(intent.getStringExtra("err_code"));
					readFingerStatus(false);
					break;
				case -2:
					break;
				default:
					break;
			}
	}

	/**
	 * ?????????????????????
	 */
	private void readDeviceId(){
		if (TextUtils.isEmpty(ParamUtils.deviceNum)) {
//                        ?????????????????????????????????
			BlueUtils.GetDeviceId(deviceType, LoginActivity.this, handler, hashMap);
		}
	}
     //??????????????????
	private void readFingerStatus(boolean status) {
		if(status){
			Drawable drawable=getResources().getDrawable(R.drawable.ic_green_susseces);
			drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
			edtTellerFinger.setText("??????????????????!");
			//edtTellerFinger.setCompoundDrawables(drawable,null,null,null);
		}else {
			Drawable drawable=getResources().getDrawable(R.drawable.ic_red_failed);
			drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
			edtTellerFinger.setText("??????????????????!");
			//edtTellerFinger.setCompoundDrawables(drawable,null,null,null);
		}
	}

	private void getBondedDevices() {
		Set<BluetoothDevice> devices = mBlueToothAdapter.getBondedDevices();
		if (devices.size() > 0) {
			for (BluetoothDevice bluetoothDevice : devices) {
				String temp = bluetoothDevice.getName();
				//Log.i("---", "---??????2---"+temp);
				if(null!=temp && !temp.equals("")) {
					if (temp.contains("P3520") || temp.contains("W9310")) {
						bondedWoqi = 1;
						//?????????????????????????????????
						neededConWoqi = 1;
					} else if (temp.contains("GEIT") || temp.contains("rk312")) {
						bondedGuoguang = 1;
						//?????????????????????????????????
						neededConGuoguang = 1;
					} else if (temp.contains("SS-")) {
						bondedShensi = 1;
						//?????????????????????????????????
						neededConShensi = 1;
					}
				}
			}
			//setButtonEnable();
		}
	}

	private void receiverRegister() {
		//???????????????????????????????????????????????????receiver
		/*IntentFilter filterFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filterFound);
		// ??????????????????
		IntentFilter filterStart = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		registerReceiver(mReceiver, filterStart);
		//????????????????????????receiver
		IntentFilter filterFinish = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filterFinish);*/

		IntentFilter intent=new IntentFilter();
		intent.addAction(BluetoothDevice.ACTION_FOUND);
		intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(pReceiver,intent);

		pReceiver = new ParingReceived();
		IntentFilter mFilter2 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
		registerReceiver(pReceiver, mFilter2);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				//android.bluetooth.adapter.action.DISCOVERY_STARTED
				LogUtils.d("????????????...");
			} else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
				final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//?????????????????????????????????????????????
				String temp = device.getName();
				String address = device.getAddress();
				int deviceType = device.getBluetoothClass().getMajorDeviceClass();
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//					LogUtils.d(temp + "\tdevice===" + device.getType() + "-------------------------------" + device.getBluetoothClass().getMajorDeviceClass());
//					System.out.println(temp + "\tdevice===" + device.getType() + "-------------------------------" + device.getBluetoothClass().getMajorDeviceClass());
//				}
				tempAddress = address;
				boolean canAdd = true;
				if (deviceType == 1536) {//????????????????????????
					int blueSdatus=device.getBondState();
					if(blueSdatus==BluetoothDevice.BOND_NONE ){//????????????????????????
						bluePrinterName = device.getName()+ " ( " +"?????????"+ " )"+ "\n" + device.getAddress();
						//toast("???????????????????????????");
						blueList.clear();
						blueList.add(bluePrinterName);
						isRepair=false;
					}
					else if(blueSdatus==BluetoothDevice.BOND_BONDED ){
						if(!isFastClick) {
							//toast("???????????????????????????");
						}
						bluePrinterName = device.getName()+ " ( " +"?????????"+ " )";
//						blueList.add(bluePrinterName);
						isRepair=true;
					}
				} else {
					try {
						BlueDeviceInfo deviceInfo=new BlueDeviceInfo();
						if (temp.contains("P3520")||temp.contains("W9310")) {
							tempPin = "1235";
							searchWoqi = 1;
							if (neededConWoqi == 1) {
								//toast("????????????????????????");
								String s = "??????????????????--" + temp;
								deviceInfo.setBlueDeviceName(s);
								deviceInfo.setBlueDeviceAddress(tempAddress);
								if (blueDevice.size() == 0) {
									blueDevice.add(deviceInfo);
								} else {
									for (int i = 0; i < blueDevice.size(); i++) {
										if (blueDevice.get(i).getBlueDeviceName().equals(s)) {
											//blueDevice.remove(i);
											//blueDevice.add(s);
											canAdd = false;
										}
									}
									if (canAdd == true) {
										blueDevice.add(deviceInfo);
									}
								}
							} else {
								getDevices(address);
							}
						} else if (temp.contains("GEIT")||temp.contains("rk312")) {
							tempPin = "1234";
							searchGuoguang = 1;
							if (neededConGuoguang == 1) {
								//toast("????????????????????????");
								String s = "??????????????????--" + temp;
								deviceInfo.setBlueDeviceName(s);
								deviceInfo.setBlueDeviceAddress(tempAddress);
								if (blueDevice.size() == 0) {
									blueDevice.add(deviceInfo);
								} else {
									for (int i = 0; i < blueDevice.size(); i++) {
										if (blueDevice.get(i).getBlueDeviceName().equals(s)) {
                                        /*blueDevice.remove(i);
                                        blueDevice.add(s);*/
											canAdd = false;
										}
									}
									if (canAdd == true) {
										blueDevice.add(deviceInfo);
									}
								}
							} else {
								getDevices(address);
							}
						} else if (temp.contains("SS-")) {
							searchShensi = 1;
							if (neededConShensi == 1) {
								//toast("????????????????????????");
								String s = "??????????????????--" + temp;
								deviceInfo.setBlueDeviceName(s);
								deviceInfo.setBlueDeviceAddress(tempAddress);
								if (blueDevice.size() == 0) {
									blueDevice.add(deviceInfo);
								} else {
									for (int i = 0; i < blueDevice.size(); i++) {
										if (blueDevice.get(i).getBlueDeviceName().equals(s)) {
                                        /*blueDevice.remove(i);
                                        blueDevice.add(s);*/
											canAdd = false;
										}
									}
									if (canAdd == true) {
										blueDevice.add(deviceInfo);
									}
								}
							} else {
								getDevices(address);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				getBondedDevices();
				aty = LoginActivity.this;
				DialogUtils.closeLoadingDailog(aty, loadingDailog);
				if (blueDevice.size() == 0) {

					//toast("????????????????????????????????????????????????");
					refreshImg.setVisibility(View.VISIBLE);
				} else {
					refreshImg.setVisibility(View.INVISIBLE);
				}
				//}
			} else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
				LogUtils.d("????????????.");

				DialogUtils.closeLoadingDailog(aty, loadingDailog);
				setProgressBarIndeterminate(false);
			}
		}
	};

	//???????????????????????????????????????????????????
	private void checkOpenBlue(BluetoothAdapter mBlueToothAdapter) {
		// TODO Auto-generated method stub
		if(null==mBlueToothAdapter||!mBlueToothAdapter.isEnabled()){
			Intent BtIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(BtIntent);
		}
	}

	private class ParingReceived extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			LogUtils.i("---tempAddress---"+tempAddress);
			BluetoothDevice btDevice = mBlueToothAdapter.getRemoteDevice(tempAddress);
			try {
//??????jar??????????????????????????????
				ClsUtils.setPin(btDevice.getClass(), btDevice, tempPin);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void searchDevices() {
		if (mBlueToothAdapter.isDiscovering()) {
			mBlueToothAdapter.cancelDiscovery();
		}
		mBlueToothAdapter.startDiscovery();
		mBlueToothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
			@Override
			public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
				//?????????????????????????????????????????????
				String temp = bluetoothDevice.getName();
				String address = bluetoothDevice.getAddress();
				int deviceType = bluetoothDevice.getBluetoothClass().getMajorDeviceClass();
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//					LogUtils.d(temp + "\tdevice===" + bluetoothDevice.getType() + "-------------------------------" + bluetoothDevice.getBluetoothClass().getMajorDeviceClass());
//					System.out.println(temp + "\tdevice===" + bluetoothDevice.getType() + "-------------------------------" + bluetoothDevice.getBluetoothClass().getMajorDeviceClass());
//				}
				tempAddress = address;
				boolean canAdd = true;
				if (deviceType == 1536) {//????????????????????????
					int blueSdatus=bluetoothDevice.getBondState();
					if(blueSdatus==BluetoothDevice.BOND_NONE ){//????????????????????????
						bluePrinterName = bluetoothDevice.getName()+ " ( " +"?????????"+ " )"+ "\n" + bluetoothDevice.getAddress();
						//toast("???????????????????????????");
						blueList.clear();
						blueList.add(bluePrinterName);
						isRepair=false;
					}
					else if(blueSdatus==BluetoothDevice.BOND_BONDED ){
						//toast("???????????????????????????");
						bluePrinterName = bluetoothDevice.getName()+ " ( " +"?????????"+ " )";
//						blueList.add(bluePrinterName);
						isRepair=true;
					}
				} else {
					try {
						if(temp != null) {
							BlueDeviceInfo deviceInfo=new BlueDeviceInfo();
							if (temp.contains("P3520")||temp.contains("W9310")) {
								tempPin = "1235";
								searchWoqi = 1;
								if (neededConWoqi == 1) {
									//toast("????????????????????????");
									String s = "??????????????????--" + temp;
									deviceInfo.setBlueDeviceName(s);
									deviceInfo.setBlueDeviceAddress(tempAddress);
									if (blueDevice.size() == 0) {
										blueDevice.add(deviceInfo);
									} else {
										for (int j = 0; j < blueDevice.size(); j++) {
											if (blueDevice.get(j).getBlueDeviceName().equals(s)) {
												//blueDevice.remove(i);
												//blueDevice.add(s);
												canAdd = false;
											}
										}
										if (canAdd == true) {
											blueDevice.add(deviceInfo);
										}
									}
								} else {
									getDevices(address);
								}
							} else if (temp.contains("GEIT")||temp.contains("rk312")) {
								tempPin = "1234";
								searchGuoguang = 1;
								if (neededConGuoguang == 1) {
									//toast("????????????????????????");
									String s = "??????????????????--" + temp;
									deviceInfo.setBlueDeviceName(s);
									deviceInfo.setBlueDeviceAddress(tempAddress);
									if (blueDevice.size() == 0) {
										blueDevice.add(deviceInfo);
									} else {
										for (int j = 0; j < blueDevice.size(); j++) {
											if (blueDevice.get(j).getBlueDeviceName().equals(s)) {
                                        /*blueDevice.remove(i);
                                        blueDevice.add(s);*/
												canAdd = false;
											}
										}
										if (canAdd == true) {
											blueDevice.add(deviceInfo);
										}
									}
								} else {
									getDevices(address);
								}
							} else if (temp.contains("SS-")) {
								searchShensi = 1;
								if (neededConShensi == 1) {
									//toast("????????????????????????");
									String s = "??????????????????--" + temp;
									deviceInfo.setBlueDeviceName(s);
									deviceInfo.setBlueDeviceAddress(tempAddress);
									if (blueDevice.size() == 0) {
										blueDevice.add(deviceInfo);
									} else {
										for (int j = 0; j < blueDevice.size(); j++) {
											if (blueDevice.get(j).getBlueDeviceName().equals(s)) {
                                        /*blueDevice.remove(i);
                                        blueDevice.add(s);*/
												canAdd = false;
											}
										}
										if (canAdd == true) {
											blueDevice.add(deviceInfo);
										}
									}
								} else {
									getDevices(address);
								}
							}
						}

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				getBondedDevices();
				aty = LoginActivity.this;
				DialogUtils.closeLoadingDailog(aty, loadingDailog);
				if (blueDevice.size() == 0) {
					toast("????????????????????????????????????????????????");
					refreshImg.setVisibility(View.VISIBLE);
				} else {
					refreshImg.setVisibility(View.INVISIBLE);
				}
				//}
			}
		});
	}

	private void getDevices(String address) {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!bluetoothAdapter.checkBluetoothAddress(address)) {//????????????????????????????????????
			toast("????????????????????????");
		} else {
			BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
			if (device != null) {
				Boolean returnValue = null;
				try {
					returnValue = ClsUtils.createBond(BluetoothDevice.class, device);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (returnValue) {//??????????????????,????????????????????????????????????????????????
					toast("?????????????????????");
				} else {
					toast("?????????????????????");
				}
			} else {
				toast("???????????????????????????");
			}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(mReceiver);
			unregisterReceiver(pReceiver);
		}catch (Exception e){
		}
	}
	/**
	 * ????????????????????? create by hutian 2018-05-17
	 */
	private void ShowCheckBluetoothDailog() {
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_check_bluetooth, null);
		bluePrinterDialog = DialogUtils.showCommonDialog(this, view);
		bluePrinterDialog.show();
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
				com.dysen.ble_lib.R.layout.device_item);
		mPairedDevicesArrayAdapter.clear();
		mPairedDevicesArrayAdapter.addAll(blueList);

		pairedListView = view.findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

	}
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3)
		{

			String info = ((TextView) v).getText().toString();
			System.out.println("message:"+info);
			String address = info.substring(info.length() - 17);
			String name=info.substring(0,info.length() - 17);
			System.out.println("name:"+name);

			RepairBluePrinter(address, false,name);
		}
	};
	/**
	 * ????????????????????? create by hutian 2018-05-17
	 */
	private void RepairBluePrinter(String address, boolean re_pair, String name)
	{
		boolean pairSucess=false;
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!bluetoothAdapter.checkBluetoothAddress(address)) {//????????????????????????????????????
			toast("?????????????????????????????????");
		} else {
			BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
			if (device != null) {
				try {
					pairSucess = ClsUtils.createBond(BluetoothDevice.class, device);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (pairSucess) {//??????????????????,????????????????????????????????????????????????
					if(bluePrinterDialog!=null&&bluePrinterDialog.isShowing()){
						bluePrinterDialog.dismiss();
						bluePrinterDialog=null;
					}
					isRepair=true;
					toast("??????????????????????????????");
				} else {
					isRepair=false;
					toast("??????????????????????????????");
				}
			} else {
				toast("???????????????????????????");
			}
		}
	}
	/**
	 * ??????????????????
	 */
	private void printInfoTest(int index) {
		ParamUtils.cusNum="8888888";
		ParamUtils.certName="??????";
		ParamUtils.cusName="?????????";
		ParamUtils.cardNum="6222888885693265";
		ParamUtils.accountNum="62221212121212";
		ParamUtils.certType="?????????";
		ParamUtils.certId="421124199004157030";
		ParamUtils.cardProductName="IC?????????";
		ParamUtils.cardType="?????????";
		ParamUtils.telPhone="13632979742";
		ParamUtils.msg_cardNum  ="6222010245555555";
		ParamUtils.msg_certType = "?????????";
		ParamUtils.msg_certId = "421124199004155252";
		ParamUtils.msg_cardProductName = "IC??????";
		ParamUtils.msg_certName = "??????";
		ParamUtils.serviceProject="??????????????????";
		ParamUtils.orgId="021";
		ParamUtils.terminalId="57220";
		ParamUtils.tellerId="9999933";
		Intent intent = new Intent(aty, PrinterInfo.class);
		intent.putExtra(IDCheck.FUNC_NAME, index);
		startActivityForResult(intent, index);
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param context ?????????
	 * @return true????????????false????????????
	 */
	public static boolean isLocationEnabled(Context context) {
		int locationMode = 0;
		String locationProviders;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
			} catch (Settings.SettingNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			return locationMode != Settings.Secure.LOCATION_MODE_OFF;
		} else {
			locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			return !TextUtils.isEmpty(locationProviders);
		}
	}
}
