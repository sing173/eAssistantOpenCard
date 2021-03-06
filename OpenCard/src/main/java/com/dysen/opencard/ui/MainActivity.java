package com.dysen.opencard.ui;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dysen.commom_library.bean.BlueDeviceInfo;
import com.dysen.commom_library.utils.CodeFormatUtils;
import com.dysen.commom_library.utils.DatetimeUtil;
import com.dysen.commom_library.utils.DialogUtils;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.commom_library.utils.PreferencesUtils;
import com.dysen.commom_library.utils.ShowCommonTypeWindow;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.base.BaseApplication;
import com.dysen.opencard.base.ParentActivity;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.StrUtils;
import com.dysen.opencard.http.SocketThread;
import com.dysen.socket_library.utils.ToastUtil;
import com.examples.outputjar.BlueUtils;
import com.examples.outputjar.ClsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends ParentActivity {

    /**/
    private final int OPEN_CARD = 101;
    private final int ACTIVATION_CARD = 102;
    private final int CARD_SIGN = 103;
    private final int MOBILE_BANK_SIGN = 104;
    private final int PRINT_VOUCHER=105;
    @BindView(R.id.txt_back)
    TextView txtBack;
    @BindView(R.id.lay_back)
    LinearLayout layBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_exit)
    TextView txt;
    @BindView(R.id.iv_org_img)
    ImageView ivOrgImg;
    @BindView(R.id.btn_forced_withdrawal)
    Button btnForcedWithdrawal;
    @BindView(R.id.btn_temp_withdrawal)
    Button btnTempWithdrawal;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.rbtn_open_card)
    RadioButton rbtnOpenCard;
    @BindView(R.id.rbtn_activation_card)
    RadioButton rbtnActivationCard;
    @BindView(R.id.rbtn_card_message_sign)
    RadioButton rbtnCardMessageSign;
    @BindView(R.id.rbtn_mobilebank_sign)
    RadioButton rbtnMobileBankSign;
    @BindView(R.id.rbtn_print_voucher)
    RadioButton rbtnPrintVoucher;
    @BindView(R.id.ll_content_left)
    LinearLayout llContentLeft;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.ll_content_right)
    LinearLayout llContentRight;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_org_id)
    TextView tvOrgId;
    @BindView(R.id.tv_teller_name)
    TextView tvTellerName;
    @BindView(R.id.tv_teller_id)
    TextView tvTellerId;
    @BindView(R.id.tv_login_date)
    TextView tvLoginDate;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.choose_device_name)
    TextView chooseDeviceName;
    /*@BindView(R.id.progressBar1)
    ProgressBar broadcastBar;*/
    @BindView(R.id.refresh_img)
    ImageView refresh_img;


    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private OpenCardFragment openCardFragment;//????????????
    private ActivationCardFragment activationCardFragment;//?????????
    private CardMessageSignFragment cardMessageSignFragment;//???????????????
    private MobileBankSignFirstFragment mobileBankSignFirstFragment;//??????????????????
    private PrintVoucherFragment printVoucherFragment;//????????????
    private List<String> transCodeList;
    private List<String> responsList = new ArrayList<>();
    private Dialog dialogAuthorize, dialogWithdrawalTellerId;//??????????????????
    private Dialog dialogWithdrawal;//??????????????????
    private String finger;
    //
    private EditText edtTellerFinger;
    private String transCode;
    private TextView tvCheckFinger, tvReadFinger;
    private Button btnSubmit;
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
    private PreferencesUtils pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initFragment(OPEN_CARD);
    }
    private void initView() {
        aty = this;
        fragmentManager = getSupportFragmentManager();
       BaseApplication.getInstance().addActivity(aty);
        txtTitle.setText(getString(R.string.app_name_cn));
        //txt.setVisibility(View.INVISIBLE);
        txt.setText("????????????");
        if((null!=ParamUtils.tellerId) && (null!=ParamUtils.orgId) && (null!=ParamUtils.terminalId)&&(!"".equals(ParamUtils.tellerId))&&
                (!"".equals(ParamUtils.orgId))&&(!"".equals(ParamUtils.terminalId))){
            tvOrgId.setText(ParamUtils.orgId);
            tvTellerName.setText(ParamUtils.tellerName);
            tvTellerId.setText(ParamUtils.tellerId);
        }else{
            Bundle bundle=getIntent().getExtras();
            tvOrgId.setText(bundle.getString("orgId"));
            tvTellerName.setText(bundle.getString("tellerName"));
            tvTellerId.setText(bundle.getString("tellerId"));
            ParamUtils.orgId=bundle.getString("orgId");
            ParamUtils.tellerName=bundle.getString("tellerName");
            ParamUtils.tellerId=bundle.getString("tellerId");
            ParamUtils.terminalId=bundle.getString("terminaId");
        }

        tvLoginDate.setText(DatetimeUtil.getTodayF());

        backActivity(this, layBack);

        openBle();
        refresh_img.setVisibility(View.VISIBLE);
        pre = new PreferencesUtils(MainActivity.this, "BlueTooth");
        String deviceName = pre.getString("deviceName", "");
        if (deviceName != null) {
            chooseDeviceName.setText(deviceName);
        }

    }
    private void initFragment(int open_card) {
        openCardFragment=new OpenCardFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_content, openCardFragment);
        transaction.commit();
    }

    //????????????????????????fragment
    private void setItem(Fragment fragment) {
        aty = this;
        rbtnActivationCard.setTextColor(aty.getResources().getColor(R.color.gray));
        rbtnOpenCard.setTextColor(aty.getResources().getColor(R.color.gray));
        rbtnCardMessageSign.setTextColor(aty.getResources().getColor(R.color.gray));
        rbtnMobileBankSign.setTextColor(aty.getResources().getColor(R.color.gray));
        rbtnPrintVoucher.setTextColor(aty.getResources().getColor(R.color.gray));
        if (fragment instanceof OpenCardFragment) {
            rbtnOpenCard.setTextColor(aty.getResources().getColor(R.color.white));
        }
        if (fragment instanceof ActivationCardFragment) {
            rbtnActivationCard.setTextColor(aty.getResources().getColor(R.color.white));
        }
        if (fragment instanceof CardMessageSignFragment) {
            rbtnCardMessageSign.setTextColor(aty.getResources().getColor(R.color.white));
        }
        if (fragment instanceof MobileBankSignFirstFragment) {
            rbtnMobileBankSign.setTextColor(aty.getResources().getColor(R.color.white));
        }
        if(fragment instanceof PrintVoucherFragment){
            rbtnPrintVoucher.setTextColor(aty.getResources().getColor(R.color.white));
        }
    }

    //????????????????????????fragment
    private void hideFragments(FragmentTransaction transaction) {

        if (openCardFragment != null) {
            transaction.hide(openCardFragment);
        }
        if (activationCardFragment != null) {
            transaction.hide(activationCardFragment);
        }
        if (cardMessageSignFragment != null) {
            transaction.hide(cardMessageSignFragment);
        }
        if (mobileBankSignFirstFragment != null) {
            transaction.hide(mobileBankSignFirstFragment);
        }
        if(printVoucherFragment!=null){
            transaction.hide(printVoucherFragment);
        }
    }

    //all
    private void setFragmentAll(int index) {
        transaction = fragmentManager.beginTransaction();
//        hideFragments(transaction);

        switch (index) {
            case OPEN_CARD://????????????
//                if (openCardFragment == null) {
//                    openCardFragment = new OpenCardFragment();
//                    transaction.add(R.id.fl_content, openCardFragment);
//                } else {
//                    transaction.show(openCardFragment);
//                }
                //?????????????????????????????????????????????????????????
                openCardFragment = new OpenCardFragment();
                setItem(openCardFragment);
                transaction.replace(R.id.fl_content, openCardFragment);
                break;
            case ACTIVATION_CARD:// ?????????
                //              setItem(activationCardFragment);
//                if (activationCardFragment == null) {
//                    activationCardFragment = new ActivationCardFragment();
//                    transaction.add(R.id.fl_content, activationCardFragment);
//                } else {
//                    transaction.show(activationCardFragment);
//                }
                activationCardFragment = new ActivationCardFragment();
                setItem(openCardFragment);
                transaction.replace(R.id.fl_content, activationCardFragment);
                break;
            case CARD_SIGN:// ???????????????
//                if (cardMessageSignFragment == null) {
//                    cardMessageSignFragment = new CardMessageSignFragment();
////                    transaction.replace(R.id.fl_content, activationCardFragment);
//                    transaction.add(R.id.fl_content, cardMessageSignFragment);
//                } else {
//                    transaction.show(cardMessageSignFragment);
//                }
                setItem(cardMessageSignFragment);
                cardMessageSignFragment = new CardMessageSignFragment();
                transaction.replace(R.id.fl_content, cardMessageSignFragment);
                break;
            case MOBILE_BANK_SIGN:// ??????????????????
                setItem(mobileBankSignFirstFragment);
//                if (mobileBankSignFirstFragment == null) {
//                    mobileBankSignFirstFragment = new MobileBankSignFirstFragment();
////                    transaction.replace(R.id.fl_content, activationCardFragment);
//                    transaction.add(R.id.fl_content, mobileBankSignFirstFragment);
//                } else {
//                    transaction.show(mobileBankSignFirstFragment);
//                }
                mobileBankSignFirstFragment = new MobileBankSignFirstFragment();
                transaction.replace(R.id.fl_content, mobileBankSignFirstFragment);
                break;

            case PRINT_VOUCHER://????????????
                setItem(printVoucherFragment);
                printVoucherFragment=new PrintVoucherFragment();
                transaction.replace(R.id.fl_content,printVoucherFragment);
                break;

        }
        transaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.btn_forced_withdrawal, R.id.btn_temp_withdrawal, R.id.rbtn_open_card, R.id.rbtn_activation_card, R.id.rbtn_card_message_sign, R.id.rbtn_mobilebank_sign,
            R.id.refresh_img, R.id.txt_exit,R.id.choose_device_name,R.id.rbtn_print_voucher})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_forced_withdrawal://????????????
//                transCode(SocketThread.forcedWithdrawal);
                showWithdrawalTellerId();
                break;
            case R.id.btn_temp_withdrawal://????????????
                SignOutTempActivity.setData(SocketThread.tempWithdrawal);
                gotoNextActivity(SignOutTempActivity.class);
//                transCode(SocketThread.tempWithdrawal);
                break;
            case R.id.rbtn_open_card://??????
                setFragmentAll(OPEN_CARD);
                break;
            case R.id.rbtn_activation_card://?????????
                setFragmentAll(ACTIVATION_CARD);
                break;
            case R.id.rbtn_card_message_sign://???????????????
                setFragmentAll(CARD_SIGN);
                break;
            case R.id.rbtn_print_voucher:
                setFragmentAll(PRINT_VOUCHER);
                break;
            case R.id.refresh_img:
                //broadcastBar.setVisibility(View.VISIBLE);
                getBondedDevices();
                receiverRegister();
                searchDevices();
                break;
            case R.id.choose_device_name:
                ShowCommonTypeWindow.showCommonTypeWindow(MainActivity.this, blueDevice, chooseDeviceName, 1920, 1200);
                break;
            case R.id.txt_exit://??????????????????
                downLoadKey();
                break;
            case R.id.rbtn_mobilebank_sign://??????????????????
                setFragmentAll(MOBILE_BANK_SIGN);
                break;
        }
    }

    /**
     * ??????????????????
     */
    private void downLoadKey() {
        transCodeList = new ArrayList<>();
        transCodeList.add("02");
        transCodeList.add("HOST");
        transCodeList.add("");
        transCode = SocketThread.updateKey;
        SocketThread.transCode(MainActivity.this, transCode, transCodeList, handler);
    }

    public String respState = "";//???????????????????????????
    public Object obj = null;
    private byte[] bytes;
    private List<String> respHeadList = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bytes = ParamUtils.respMsgByte;
            if (msg.what == -2) {
                toast("????????????????????????????????????????????????");
            } else if(msg.what==14){
                LogUtils.d("????????????????????????");
                ToastUtil.showShortToast(aty,"?????????????????????????????????");
            }else if(msg.what==15){
                ToastUtil.showShortToast(aty,"????????????????????????,???????????????????????????");
            }
            if(msg.what==100||msg.what==-3){
                parseData(msg);
            }
        }
    };

    private void parseData(Message msg) {
        if (msg.what == -3) {
            respState = (String) msg.obj;
        }else if (msg.what == 100) {
            responsList = (List<String>)msg.obj;
            respState = "????????????";
        }
        toast(SocketThread.getTransStr(transCode)+respState);

        respHeadList = ParamUtils.responsHeader;

        if (transCode.equals(SocketThread.tempWithdrawal)) {
            this.finish();
        } else if (transCode.equals(SocketThread.localAuthorization)) {

            transCodeList = new ArrayList<>();
            transCodeList.add(ParamUtils.tellerId);
            SocketThread.transCode(aty, SocketThread.forcedWithdrawal, transCodeList, handler);
        } else if (transCode.equals(SocketThread.forcedWithdrawal)) {
            LogUtils.d("resp_head=" + respHeadList);
            LogUtils.d("StrUtils.getStateInfo(bytes)=" + StrUtils.getStateInfo(bytes));
            if (StrUtils.getStateInfo(bytes).equals("??????????????????")) {
                toast("????????????");
            } else if (StrUtils.getStateInfo(bytes).equals("?????????????????????")) {
                Intent intent = new Intent(aty, AuthorizeActivity.class);
//                    if (bytes!= null && bytes.length > 0) {
//                        if ( CodeFormatUtils.byte2StrIntercept(bytes, 134, 5).equals("LUP")){

                intent.putExtra("auth_seqNo", CodeFormatUtils.byte2StrIntercept(bytes, 114, 20));
//                            intent.putExtra("bk_seqNo", respHeadList.get(3));
                intent.putExtra("auth_capability", CodeFormatUtils.byte2StrIntercept(bytes, 154, 3));
                intent.putExtra("auth_errCode", CodeFormatUtils.byte2StrIntercept(bytes, 159, 6));
                intent.putExtra("auth_errMsg", CodeFormatUtils.byte2StrIntercept(bytes, 165, 120));
                intent.putExtra("auth_sealMsg", CodeFormatUtils.byte2StrIntercept(bytes, 285, 120));
                startActivityForResult(intent, AuthorizeActivity.SIGN_OUT);
//                        }
//                    }
            }

        }else if(transCode.equals(SocketThread.updateKey)){
              if(responsList!=null &&responsList.size()==3){
                  String keyName=responsList.get(0);
                  String keyValue=responsList.get(1);
                  LogUtils.d("????????????????????????="+keyName);
                  responsList.clear();
                 BlueUtils.putWorkKey(ParamUtils.deviceFlag,keyName.substring(0,32),handler,aty);
              }
        }
//        gotoNextActivity(SignOutForcedActivity.class);
    }

    private void transCode(String transCode) {
        this.transCode = transCode;
        if (transCode.equals(SocketThread.tempWithdrawal)) {
            showWithdrawal();
        } else if (transCode.equals(SocketThread.forcedWithdrawal)) {
            showWithdrawalTellerId();
        }

//        if (!ParamUtils.tellerId.isEmpty() && !ParamUtils.orgId.isEmpty() &&
//                !ParamUtils.terminalId.isEmpty()) {
//
//            SocketThread.transCode(aty, transCode, transCodeList, handler);
//        } else {
//            toast("???????????????????????????");
//        }
    }

    /**
     * ??????????????? ??????
     */
    public void showWithdrawalTellerId() {

        aty = MainActivity.this;
        dimBackground(1.0f, 0.5f);
        View view = LayoutInflater.from(aty).inflate(R.layout.dialog_withdrawal_teller_id, null);

        dialogWithdrawalTellerId = DialogUtils.showCloseDialog(aty, view);
        dialogWithdrawalTellerId.setCanceledOnTouchOutside(false);
        final EditText edtTellerId = (EditText) dialogWithdrawalTellerId.getWindow().findViewById(R.id.edt_teller_id);
        Button btnSubmit = (Button) dialogWithdrawalTellerId.getWindow().findViewById(R.id
                .btn_submit);
//        KeyBoardUtils.closeKeybord(edtTellerId, dialogWithdrawalTellerId.getWindow().getContext());
        dialogWithdrawalTellerId.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Window window = dialogWithdrawalTellerId.getWindow();

        LinearLayout relayout = window.findViewById(R.id.ll_withdrawal_teller_id);
        Display display =window.getWindowManager().getDefaultDisplay();
        int minHeight = (int) (display.getHeight()*0.16);              //???????????????????????????dialog?????????
// int minWidth = (int) (display.getWidth()*0.4);             //????????????
        relayout.setMinimumHeight(minHeight);

        WindowManager.LayoutParams params = window.getAttributes() ;
        params.width =(int) (display.getWidth()*0.4);                     //???????????????????????????dialog?????????
        window.setAttributes(params);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ViewUtils.getText(edtTellerId).isEmpty()) {

                    dialogWithdrawalTellerId.dismiss();
                    dimBackground(0.5f, 1.0f);
                    transCodeList = new ArrayList<>();
//                    transCodeList.add("9999933");
                    transCodeList.add(ViewUtils.getText(edtTellerId));
                    transCode = SocketThread.forcedWithdrawal;
                    //?????????0-??????????????????  1-???????????????
                    SocketThread.setPackMsgHead(transCode, "0", "", "0");
                    SocketThread.transCode(aty, transCode, transCodeList, handler);
//                    gotoNextActivity(SignOutForcedActivity.class);
//                showAuthorize();
                } else {
                    toast("??????????????????");
                }
//                dialogWithdrawalTellerId.dismiss();
            }
        });

        dialogWithdrawalTellerId.getWindow().findViewById(R.id
                .tv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dimBackground(0.5f, 1.0f);
                dialogWithdrawalTellerId.dismiss();
            }
        });
    }

    /**
     * ??????????????????
     */
    public void showAuthorize() {

        View view = LayoutInflater.from(aty).inflate(R.layout.dialog_authorize, null);

        dialogAuthorize = DialogUtils.showCloseDialog(aty, view);
        btnSubmit = (Button) this.dialogAuthorize.getWindow().findViewById(R.id.submit);
        ViewUtils.setViewEnable(aty, btnSubmit, false);
        final EditText edtTellerId = (EditText) this.dialogAuthorize.getWindow().findViewById(R.id.edt_teller_id);
        Button btnReadFinger = (Button) this.dialogAuthorize.getWindow().findViewById(R.id.btn_read_finger);
        Button submit = (Button) this.dialogAuthorize.getWindow().findViewById(R.id.submit);
        tvCheckFinger = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id
                .tv_check_finger);
        tvReadFinger = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id
                .tv_read_finger);
        TextView tvTellerLevel = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id.tv_teller_level);
        TextView tvAuthorizeCode = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id.tv_authorize_code);
        TextView tvAuthorizeInfo = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id.tv_authorize_info);
        TextView tv = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id.tv_);
        TextView tvActivationAuthorize = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id
                .tv_activation_authorize);
        clsOpenKeyboard(edtTellerId, false);
        transCodeList = new ArrayList<>();
        transCodeList.add(ViewUtils.getText(edtTellerId));
        transCodeList.add(ParamUtils.orgId);
        transCodeList.add("1");
        transCodeList.add("0");
        btnReadFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                readInfo(IDCheck.READ_FINGER);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toast("?????????????????????");
                SocketThread.transCode(aty, SocketThread.localAuthorization, transCodeList, handler);
            }
        });
    }

    /**
     * ??????????????????
     */
    public void showWithdrawal() {
        View view = LayoutInflater.from(aty).inflate(R.layout.dialog_sign_out, null);

        dialogWithdrawal = DialogUtils.showCloseDialog(aty, view);
        EditText edtTellerId = (EditText) dialogWithdrawal.getWindow().findViewById(R.id
                .edt_teller_id);
        EditText edtOrgId = (EditText) dialogWithdrawal.getWindow().findViewById(R.id
                .edt_org_id);
        EditText edtTerminalId = (EditText) dialogWithdrawal.getWindow().findViewById(R.id
                .edt_terminal_id);
        edtTellerFinger = (EditText) dialogWithdrawal.getWindow().findViewById(R.id
                .edt_teller_finger);
        Button btnReadFinger = (Button) dialogWithdrawal.getWindow().findViewById(R.id
                .btn_read_finger);
        clsOpenKeyboard(edtTellerId, false);
        if (!ParamUtils.tellerId.isEmpty() && !ParamUtils.orgId.isEmpty() &&
                !ParamUtils.terminalId.isEmpty()) {
            ViewUtils.setText(ParamUtils.tellerId, edtTellerId);
            ViewUtils.setText(ParamUtils.orgId, edtOrgId);
            ViewUtils.setText(ParamUtils.terminalId, edtTerminalId);

            transCodeList = new ArrayList<>();
            transCodeList.add(ViewUtils.getText(edtTellerId));
            transCodeList.add("");
            transCodeList.add(ViewUtils.getText(edtOrgId));
            transCodeList.add(ViewUtils.getText(edtTerminalId));

        } else {
            toast("???????????????????????????");
        }
        btnReadFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                readInfo(IDCheck.READ_FINGER);
            }
        });
    }

    /**
     * ?????????
     */
    private void readInfo(int index) {
        if (openBle()) {
            this.mIndex = index;
            Intent intent = new Intent(this, IDCheck.class);
            LogUtils.d(mIndex + "-----------------------mIndex----------------" + index);
            intent.putExtra(IDCheck.FUNC_NAME, index);
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
                    ParamUtils.terllerFinger2 = finger;
//                    edtTellerFinger.setText(finger);
                    if (transCode.equals(SocketThread.tempWithdrawal)) {//991055????????????????????????
//                        transCodeList.add(finger);
                        transCodeList.add(ParamUtils.terllerFinger);
                        transCodeList.add("0");
                        transCodeList.add("");
//                        dialogWithdrawal.dismiss();
                        SocketThread.transCode(aty, transCode, transCodeList, handler);
                    } else if (transCode.equals(SocketThread.forcedWithdrawal)) {//991056????????????????????????
                        tvReadFinger.setVisibility(View.VISIBLE);
//                        transCodeList.add(finger);
                        transCodeList.add(ParamUtils.terllerFinger2);
                        ViewUtils.setViewEnable(aty, btnSubmit, true);
                    }
//                    toast("?????????" + finger);
                    LogUtils.d("???????????????" + finger);
                    break;
                case AuthorizeActivity.SIGN_OUT:
                    toast("???????????????");
                    break;
                default:
                    break;
            }
    }

    /**
     * ?????????????????????
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getBondedDevices() {
        Set<BluetoothDevice> devices = mBlueToothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : devices) {
                String temp = bluetoothDevice.getName();
                //Log.i("---", "---??????2---"+temp);
                if (temp.contains("P3520")) {
                    bondedWoqi = 1;
                    //?????????????????????????????????
                    neededConWoqi = 1;
                } else if (temp.contains("GEIT")) {
                    bondedGuoguang = 1;
                    //?????????????????????????????????
                    neededConGuoguang = 1;
                } else if (temp.contains("SS-")) {
                    bondedShensi = 1;
                    //?????????????????????????????????
                    neededConShensi = 1;
                }
            }
            //setButtonEnable();
        }
    }

    private void receiverRegister() {
        // TODO Auto-generated method stub
        //???????????????????????????????????????????????????receiver
        IntentFilter mFilter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, mFilter1);
        //????????????????????????receiver
        mFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, mFilter1);

        pReceiver = new ParingReceived();
        IntentFilter mFilter2 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
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

    private class ParingReceived extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
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
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        long mLastActionTime1 = System.currentTimeMillis();
//        Log.e("hut", "??????MainActivity????????????=="+mLastActionTime1);
//        return super.dispatchTouchEvent(ev);
//    }
}
