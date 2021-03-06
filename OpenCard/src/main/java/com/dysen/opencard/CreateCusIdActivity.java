package com.dysen.opencard;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dysen.opencard.base.ParentActivity;
import com.dysen.commom_library.bean.BlueDeviceInfo;
import com.dysen.commom_library.bean.CommonBean;
import com.dysen.commom_library.utils.CodeFormatUtils;
import com.dysen.commom_library.utils.DialogUtils;
import com.dysen.commom_library.utils.FileUtils;
import com.dysen.commom_library.utils.FormatUtil;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.commom_library.utils.PreferencesUtils;
import com.dysen.commom_library.utils.ShowCommonTypeWindow;
import com.dysen.commom_library.utils.SocketUtil;
import com.dysen.commom_library.utils.StringUtils;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.base.BaseApplication;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.StrUtils;
import com.dysen.opencard.common.bean.AreaBean;
import com.dysen.opencard.common.bean.CityBean;
import com.dysen.opencard.common.db.AreaDao;
import com.dysen.opencard.http.SocketThread;
import com.dysen.opencard.printer.PrinterInfo;
import com.dysen.opencard.ui.AuthorizeActivity;
import com.dysen.socket_library.utils.ToastUtil;
import com.examples.outputjar.ClsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateCusIdActivity extends ParentActivity {

    public static final int CUS_ID = 10001;
    @BindView(R.id.txt_back)
    TextView txtBack;
    @BindView(R.id.lay_back)
    LinearLayout layBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_exit)
    TextView txt;
    @BindView(R.id.customerName)
    EditText customerName;
    @BindView(R.id.iv_ocr)
    ImageView ivOcr;
    @BindView(R.id.rbt_0)
    RadioButton rbt0;
    @BindView(R.id.rbt_1)
    RadioButton rbt1;
    @BindView(R.id.certType)
    Spinner certType;
    @BindView(R.id.certNumber)
    EditText certNumber;
    @BindView(R.id.cert_issued_start)
    EditText certIssuedStart;
    @BindView(R.id.cert_issued_end)
    EditText certIssuedEnd;
    @BindView(R.id.country)
    Spinner country;
    @BindView(R.id.province)
    Spinner province;
    @BindView(R.id.county)
    Spinner county;
    @BindView(R.id.cus_add_info)
    EditText cusAddInfo;
    @BindView(R.id.edt_telephone)
    EditText edtTelephone;
    @BindView(R.id.edt_postal_code)
    EditText edtPostalCode;
    @BindView(R.id.professional_code)
    Spinner professionalCode;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.ll_cus_id)
    LinearLayout llCusId;
    @BindView(R.id.choose_device_name)
    TextView chooseDeviceName;
    /*@BindView(R.id.progressBar1)
    ProgressBar broadcastBar;*/
    @BindView(R.id.refresh_img)
    ImageView refresh_img;

    private Dialog dialog;
    private String finger;
    List<CityBean> cityBeans;

    final public static int CUS_NUM = 101, CUS_NUM_NO = 102;
    private static List<AreaBean> listProvince = new ArrayList<AreaBean>();// ???
    private List<AreaBean> listCity = new ArrayList<AreaBean>();// ???
    private List<AreaBean> listArea = new ArrayList<AreaBean>();// ???
    private List<AreaBean> listCounty = new ArrayList<AreaBean>();// ???
    private AreaDao dao;
    private int indexHuBei;
    private String[] chars;
    private List<String> list;
    private List<String> lists;
    private List<String> transCodeList;
    private String certName, certTypeStr, certId, sex, beginDate, endDate,address;
    private String countryStr;//??????
    private String provinceStr;//???
    private String countyStr;//???
    private String professionalCodeStr;//????????????
    private List<String> responsList = new ArrayList<>();
    private List<String> respHeadList = new ArrayList<>();
    private String transCode;
    private int index;
    private String authSeqNo;
    private int deviceId;
    private int DEVICE_GUOGUANG_ID = 1;
    private int DEVICE_SHENSI_ID = 2;
    private int DEVICE_WOQI_ID = 3;
    private String deviceName;
    private List<BlueDeviceInfo> blueDevice=new ArrayList<BlueDeviceInfo>();
    private int searchGuoguang,searchShensi,searchWoqi;
    private int bondedGuoguang,bondedShensi,bondedWoqi;
    private int neededConGuoguang,neededConShensi,neededConWoqi;
    private String tempAddress;
    private ParingReceived pReceiver;
    private String tempPin;
    private PreferencesUtils pre;
    private boolean isFastClick;//??????????????????????????????

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cus_id);
        ButterKnife.bind(this);
        BaseApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        aty = this;
        txtBack.setText(R.string.open_card);
        backActivity(this, layBack, -1);
        setValue();
        cusAddInfo.setText(address);
//        edtPostalCode.setText("438700");
//        edtTelephone.setText("18971642933");
        String[] cert_type = getResources().getStringArray(R.array.cert_type);
        String[] professional_code = getResources().getStringArray(R.array.professional_code);
//        commonSpinner(aty, cert_type, certType);
        commonSpinners(aty, professional_code, professionalCode);
        professionalCode.setSelection(23);
//        HttpThread.sendRequestGet("http://192.168.112.163:8080/city.json", handler);
        dao = new AreaDao(aty);

//        selectProvince(1);//????????????
//        selectProvince2();
        selectCountry();//????????????
        openBle();
        refresh_img.setVisibility(View.VISIBLE);
        pre=new PreferencesUtils(CreateCusIdActivity.this,"BlueTooth");
        String deviceName=pre.getString("deviceName","");
        if(deviceName!=null){
            chooseDeviceName.setText(deviceName);
        }
    }

    private void selectProvince2() {
        List<CommonBean.CountryBean> countArrayList = FileUtils.getCountArrayList(
                FileUtils.readFromAssets(aty, "province.txt"), 3);
    }


    public void setValue() {

        certName = getIntent().getStringExtra("cert_name");
        certTypeStr = getIntent().getStringExtra("cert_type_str");
        certId = getIntent().getStringExtra("cert_id");
        sex = getIntent().getStringExtra("cert_sex");
        beginDate = getIntent().getStringExtra("cert_begin");
        endDate = getIntent().getStringExtra("cert_end");
        address = getIntent().getStringExtra("cert_address");

        ViewUtils.setText(aty, certName, customerName);
        ViewUtils.setText(aty, certId, certNumber);
        ViewUtils.setText(aty, beginDate, certIssuedStart);
        ViewUtils.setText(aty, endDate, certIssuedEnd);
           //???????????????DDMMYYYY ?????? ??????????????? ?????????=?????????add by hutian 2018-07-31
        try {
            beginDate = StringUtils.setCertDate(beginDate);
            if ("??????".equals(endDate)) {//????????????2099-12-31
                endDate = "31122099"; //DDMMYYYY
            } else {
                endDate = StringUtils.setCertDate(endDate);
            }
        }catch(Exception e){
            toast("?????????????????????????????????");
        }
        commonSpinners(aty, new String[]{certTypeStr}, certType);//????????????????????????
        LogUtils.i("=====================" + sex);
        if (sex.equals("???"))
            rbt1.setChecked(true);//???
        else if (sex.equals("???"))
            rbt0.setChecked(true);//???
    }

    private void selectCountry() {
     /*   List<CommonBean.CountryBean> countArrayList = FileUtils.getCountArrayList(
                FileUtils.readFromAssets(aty, "country.txt"), 2);
        String[] str = new String[countArrayList.size()];*/

   /*     for (int i = 0; i < countArrayList.size(); i++) {
            str[i] = countArrayList.get(i).getId().replace("\"", "")+":"+countArrayList.get(i)
                    .getName().replace("\"", "");
        }*/
//        String[] str = FileUtils.getArryString(FileUtils.readFromAssets(aty,"country.txt"),2);
        String[] str = ParamUtils.countrys;
        commonSpinners(aty, str, country);
    }

    //????????????
    private void selectProvince(int index) {
        listProvince.clear();
        if (index == 0) {
            listProvince = dao.getAllMessage("000000");

        } else {
            listProvince.add(new AreaBean("0", "??????"));
        }
        if (!listProvince.isEmpty()) {
            list = new ArrayList<>();
            for (int i = 0, size = listProvince.size(); i < size; i++) {
//                if (listProvince.get(i).getName().equals("?????????")) {
//                    indexHuBei = i;
//                    list.add(0, listProvince.get(i).getName());
//                }else
                list.add(i, listProvince.get(i).getName());
            }
        }
        chars = (String[]) list.toArray(new String[list.size()]);
        commonSpinners(aty, chars, province);
    }

    private void selectCounty(int index) {
        listCity.clear();
        if (listProvince.get(0).getName().equals("??????")) {
            listCity.add(new AreaBean("0", "??????"));
        } else {
            LogUtils.d(indexHuBei + "*****************************" + index);
//            if (index == 0)
//                listCity = dao.getAllMessage(listProvince.get(indexHuBei).getId());
//            else
            listCity = dao.getAllMessage(listProvince.get(index).getId());
        }
        lists = new ArrayList<>();
        for (int i = 0; i < listCity.size(); i++) {
//            if (listCity.get(i).getName().equals("?????????")) {
//                list.add(0, listCity.get(i).getName());
//            }else
            lists.add(i, listCity.get(i).getName());

        }
        chars = (String[]) lists.toArray(new String[lists.size()]);
        commonSpinners(aty, chars, county);
    }

    int certTypeIndex = 0;

    private void commonSpinners(final Activity aty, final String[] type, final Spinner spinner) {
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
                certTypeIndex = pos;
               // toast(type[pos] + "" + pos);
                if (spinner.equals(country)) {
                    countryStr = type[pos];
                    selectProvince(pos);
                }
                if (spinner.equals(province)) {
                    provinceStr = type[pos];
                    selectCounty(pos);//???????????????
                }
                if (spinner.equals(county)) {
                    countyStr = type[pos];
                }
                if (spinner.equals(professionalCode)) {
                    professionalCodeStr = type[pos];
                    String code=StringUtils.splitString(professionalCodeStr,0);
                    ToastUtil.showLongToast(aty,"?????????????????????"+code);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    public String s = "";
    public Object obj = null;
    private byte[] bytes;
    private String respState;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == -2) {
                toast("????????????????????????????????????????????????");
            }
            bytes = ParamUtils.respMsgByte;
            if(msg.obj!=null) {
                parseData(msg);
            }
        }
    };

    private void parseData(Message msg) {
        if (msg.what == -3) {
            s = (String) msg.obj;
        }
        if (msg.what == 100) {
            responsList = (List<String>)msg.obj;
            s = "????????????";
        }
        //toast(SocketThread.getTransStr(transCode)+s);
            respHeadList = ParamUtils.responsHeader;

            if (transCode.equals(SocketThread.createCus)) {
                if (StrUtils.getStateInfo(bytes).equals("??????????????????")) {
                    if (responsList != null && responsList.size() == 1) {
                        ParamUtils.cusNum = responsList.get(0); //???????????????
                        responsList.clear();
                        printInfo(PrinterInfo.PRINT_OPEN_CUS);
                    }
                } else if (StrUtils.getStateInfo(bytes).equals("?????????????????????")) {
                    Intent intent = new Intent(aty, AuthorizeActivity.class);
//                    if (bytes!= null && bytes.length > 0) {
//                        if ( CodeFormatUtils.byte2StrIntercept(bytes, 134, 5).equals("LUP")){
                    authSeqNo = CodeFormatUtils.byte2StrIntercept(bytes, 114, 20);
                    intent.putExtra("auth_seqNo", CodeFormatUtils.byte2StrIntercept(bytes, 114, 20));
//                            intent.putExtra("bk_seqNo", respHeadList.get(3));
                    intent.putExtra("auth_capability", CodeFormatUtils.byte2StrIntercept(bytes, 154, 3));
                    intent.putExtra("auth_errCode", CodeFormatUtils.byte2StrIntercept(bytes, 159, 6));
                    intent.putExtra("auth_errMsg", CodeFormatUtils.byte2StrIntercept(bytes, 165, 120));
                    intent.putExtra("auth_sealMsg", CodeFormatUtils.byte2StrIntercept(bytes, 285, 120));

                    startActivityForResult(intent, AuthorizeActivity.OPEN_CUS);
//                        }
//                    }
                }
            }
    }

    /**
     * ????????????
     */
    private void printInfo(int index) {
            this.index = index;
        //    Intent intent = new Intent(aty, PrinterInfo.class);
        Intent intent = new Intent(aty, PrinterInfo.class);
            intent.putExtra(PrinterInfo.FUNC_NAME, index);
            startActivityForResult(intent, index);
    }

    @OnClick({R.id.submit, R.id.cancel,R.id.refresh_img,R.id.choose_device_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit:
                isFastClick= SocketUtil.isFastTimeClick();
                if(!isFastClick) {
                    createCusId();
                }
                break;
            case R.id.cancel:
                DialogUtils.ShowPromptDailog(aty, new DialogUtils.OnButtonClick() {
                    @Override
                    public void buttonClick(int id) {
                        mySetResult(CUS_NUM_NO, new Intent());
                    }
                });
                break;
            case R.id.refresh_img:
                //broadcastBar.setVisibility(View.VISIBLE);
                getBondedDevices();
                receiverRegister();
                searchDevices();
                break;
            case R.id.choose_device_name:
                ShowCommonTypeWindow.showCommonTypeWindow(CreateCusIdActivity.this,blueDevice,chooseDeviceName,1920,1200);
        }
    }

    /**
     * ??????
     */
    private void createCusId() {
        if (FormatUtil.isMobileNO(ViewUtils.getText(edtTelephone))) {
            ParamUtils.telPhone = ViewUtils.getText(edtTelephone);
            initList();
            transCode = SocketThread.createCus;
            //?????????0-??????????????????  1-???????????????
            SocketThread.setPackMsgHead(transCode, "0", "", "0");
            SocketThread.transCode(CreateCusIdActivity.this, transCode, transCodeList, handler);
        } else {
            toast("????????????????????????");
        }
    }

    public void show() {

        View view = LayoutInflater.from(aty).inflate(R.layout.dialog_authorize, null);

        dialog = DialogUtils.showCloseDialog(aty, view);
        Button btnSubmit = (Button) this.dialog.getWindow().findViewById(R.id.submit);
        EditText edtTellerId = (EditText) this.dialog.getWindow().findViewById(R.id.edt_teller_id);
        Button btnReadFinger = (Button) this.dialog.getWindow().findViewById(R.id.btn_read_finger);
        final TextView tvReadFinger = (TextView) this.dialog.getWindow().findViewById(R.id.tv_read_finger);
        Button submit = (Button) this.dialog.getWindow().findViewById(R.id.submit);
        TextView tvTellerLevel = (TextView) this.dialog.getWindow().findViewById(R.id.tv_teller_level);
        TextView tvAuthorizeCode = (TextView) this.dialog.getWindow().findViewById(R.id.tv_authorize_code);
        TextView tvAuthorizeInfo = (TextView) this.dialog.getWindow().findViewById(R.id.tv_authorize_info);
        TextView tv = (TextView) this.dialog.getWindow().findViewById(R.id.tv_);
        TextView tvActivationAuthorize = (TextView) this.dialog.getWindow().findViewById(R.id.tv_activation_authorize);
        clsOpenKeyboard(edtTellerId, false);
        btnReadFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre=new PreferencesUtils(CreateCusIdActivity.this,"BlueTooth");
                deviceId=pre.getInt("deviceId",0);
                readInfo(IDCheck.READ_FINGER,deviceId);
                tvReadFinger.setVisibility(View.VISIBLE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toast("?????????????????????");
//                initList();
                transCode = SocketThread.localAuthorization;
                SocketThread.transCode(aty, transCode, transCodeList, handler);
                dialog.dismiss();
            }
        });
    }

    private List<String> initList() {

        transCodeList = new ArrayList<>();

        transCodeList.add("01");                            // HB_CustType         ????????????         ?????????-01   ????????????
        transCodeList.add("101");                                   // HB_CustSubtype      ???????????????        ?????????-101  ??????
        transCodeList.add(certTypeStr);                             // HB_IDType           ????????????         ?????????-01   ?????????
//        transCodeList.add(certId);                                  // HB_IDNum            ????????????
        transCodeList.add(ViewUtils.getText(certNumber));                                  // HB_IDNum            ????????????
        transCodeList.add("12");                                    // HB_LanguageCode     ????????????         ?????????-12   ??????
        transCodeList.add(beginDate);                               // HB_IDIssueDate      ??????????????????       DDMMYYYY
        transCodeList.add(endDate);                                 // HB_IDExpiryDate     ???????????????        DDMMYYYY
        transCodeList.add(certName);                                // HB_LastName         ??????
        transCodeList.add(ViewUtils.getText(edtTelephone));         // HB_Mobile           ????????????
        transCodeList.add(ViewUtils.getText(cusAddInfo));           // HB_HomeAddr1        ????????????
        transCodeList.add(countyStr);                               // HB_HomeAddr2        ???/???          ???
        transCodeList.add(provinceStr);                             // HB_HomeAddr3        ???/???
        transCodeList.add(countryStr);                              // HB_HomeAddr4        ??????
        transCodeList.add(ViewUtils.getText(edtPostalCode));        // HB_PostCode1        ????????????
        transCodeList.add("CN");                                    // HB_Nationality      ??????           ?????????-CN ??????
        transCodeList.add(sex);                                     // HB_SexCode          ??????           1??????   0??????
        transCodeList.add("1");                                     // HB_IsResident       ??????/?????????       ?????????1     1???  2???
        transCodeList.add(StringUtils.splitString(professionalCodeStr,0));                     // HB_OccupationCode   ????????????
        return transCodeList;
    }

    /**
     * ?????????
     */
    private void readInfo(int index,int flag) {
        if (openBle()) {
            this.mIndex = index;
            Intent intent = new Intent(this, IDCheck.class);
            LogUtils.d(mIndex + "-----------------------mIndex----------------" + index);
            intent.putExtra(IDCheck.FUNC_NAME, index);
            intent.putExtra("flag",flag);
            startActivityForResult(intent, index);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        LogUtils.i("hut","????????????????????????="+requestCode+" ????????????????????????="+resultCode);
        if (requestCode == mIndex)
            switch (resultCode) {
                case IDCheck.READ_FINGER://?????????
                    finger = intent.getStringExtra("finger");
                    toast("?????????" + finger);
                    break;
                default:
                    break;
            }
        if (requestCode == index) {
            if (resultCode == PrinterInfo.PRINT_SUCCESS) {//?????????????????????

                intent.putExtra("cus_num", ParamUtils.cusNum);
                mySetResult(CUS_NUM, intent);
            }else if (resultCode == PrinterInfo.PRINT_ERR){
//                printInfo(PrinterInfo.PRINT_OPEN_CUS);
//                intent.putExtra("err_code", "??????????????????");
//                mySetResult(-1, intent);
            }
        }
        if (requestCode == AuthorizeActivity.OPEN_CUS){
            if (resultCode == AuthorizeActivity.AUTH_SUCCESS){ //???????????????????????????????????????
                transCode = SocketThread.createCus;
                SocketThread.setPackMsgHead(transCode, "1", authSeqNo, "1");
                SocketThread.transCode(CreateCusIdActivity.this, transCode, transCodeList, handler);

            }else if (resultCode == AuthorizeActivity.AUTH_FAIL){
                toast("????????????");
            }
        }
    }

    private void getBondedDevices(){
        Set<BluetoothDevice> devices=mBlueToothAdapter.getBondedDevices();
        if(devices.size()>0){
            for(BluetoothDevice bluetoothDevice:devices){
                String temp=bluetoothDevice.getName();
                //Log.i("---", "---??????2---"+temp);
                if(temp.contains("P3520")||temp.contains("W9310")){
                    bondedWoqi=1;
                    //?????????????????????????????????
                    neededConWoqi=1;
                }else if(temp.contains("GEIT")||temp.contains("rk312")){
                    bondedGuoguang=1;
                    //?????????????????????????????????
                    neededConGuoguang=1;
                }else if(temp.contains("SS-")){
                    bondedShensi=1;
                    //?????????????????????????????????
                    neededConShensi=1;
                }
            }
            //setButtonEnable();
        }
    }

    private void receiverRegister() {
        // TODO Auto-generated method stub
        //???????????????????????????????????????????????????receiver
        IntentFilter mFilter1=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, mFilter1);
        //????????????????????????receiver
        mFilter1=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, mFilter1);

        pReceiver=new ParingReceived();
        IntentFilter mFilter2=new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(pReceiver, mFilter2);
    }

    private BroadcastReceiver mReceiver=new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action=intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                final BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //?????????????????????????????????????????????
                String temp=device.getName();
                String address=device.getAddress();
                int deviceType=device.getBluetoothClass().getMajorDeviceClass();
                tempAddress=address;
                boolean canAdd=true;
                if(deviceType==1536){

                }else {
                    try {
                        BlueDeviceInfo deviceInfo=new BlueDeviceInfo();
                        if (temp.contains("P3520")||temp.contains("W9310")) {
                            tempPin = "1235";
                            searchWoqi = 1;
                            if (neededConWoqi == 1) {
                                toast("????????????????????????");
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
                                toast("????????????????????????");
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
                                toast("????????????????????????");
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
                //broadcastBar.setVisibility(View.INVISIBLE);
                if(blueDevice.size()==0) {
                    toast("????????????????????????????????????????????????");
                    //refresh_img.setVisibility(View.VISIBLE);
                }else{
                    //refresh_img.setVisibility(View.INVISIBLE);
                }
                //}
            }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                setProgressBarIndeterminate(false);
            }
        }
    };

    private class ParingReceived extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            BluetoothDevice btDevice=mBlueToothAdapter.getRemoteDevice(tempAddress);
            try {
//??????jar??????????????????????????????
                ClsUtils.setPin(btDevice.getClass(), btDevice, tempPin);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void searchDevices(){
        if(mBlueToothAdapter.isDiscovering()){
            mBlueToothAdapter.cancelDiscovery();
        }
        mBlueToothAdapter.startDiscovery();
    }

    private void getDevices(String address){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.checkBluetoothAddress(address)) {//????????????????????????????????????
            toast("????????????????????????");
        }else{
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
                }else {
                    toast("?????????????????????");
                }
            }else{
                toast("???????????????????????????");
            }
        }
    }
}
