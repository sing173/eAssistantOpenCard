package com.dysen.opencard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dysen.commom_library.base.ParentActivity;
import com.dysen.commom_library.utils.SocketUtil;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.base.BaseApplication;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.StrUtils;
import com.dysen.opencard.http.SocketThread;
import com.dysen.socket_library.utils.LogUtils;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AuthorizeActivity extends ParentActivity {

    public static final int OPEN_CUS = 101;
    public static final int OPEN_CARD = 102;
    public static final int ACTIVATION_CARD = 103;
    public static final int SIGN_OUT = 104;
    public static final int SIGN_MESSAGE = 105;
    public static final int SIGN_MOBILE_BANK = 106;
    public static final int AUTH_SUCCESS = 11;
    public static final int AUTH_FAIL = 12;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.edt_teller_id)
    EditText edtTellerId;
    @BindView(R.id.btn_read_finger)
    Button btnReadFinger;
    @BindView(R.id.tv_read_finger)
    TextView tvReadFinger;
    @BindView(R.id.tv_check_finger)
    TextView tvCheckFinger;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.tv_teller_level)
    TextView tvTellerLevel;
    @BindView(R.id.tv_authorize_code)
    TextView tvAuthorizeCode;
    @BindView(R.id.tv_authorize_info)
    TextView tvAuthorizeInfo;
//    @BindView(R.id.tv_)
//    TextView tv;
    @BindView(R.id.tv_activation_authorize)
    TextView tvActivationAuthorize;
    private String finger;
    private List<String> transCodeList;
    private String transCode;
    private boolean flagFinger;
    private String authSeqNo ,authCapability,authErrCode,authErrMsg  ,authSealMsg ;
    private List<String> listBody;
    private boolean isFastClick;//判断按钮是否重复点击

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out_forced);
        ButterKnife.bind(this);
        this.setFinishOnTouchOutside(false);
        initView();
    }

    private void initView() {
        aty = this;
        BaseApplication.getInstance().addActivity(aty);
        authSeqNo = getIntent().getStringExtra("auth_seqNo");
        authCapability = getIntent().getStringExtra("auth_capability");
        authErrCode = getIntent().getStringExtra("auth_errCode");
        authErrMsg = getIntent().getStringExtra("auth_errMsg");
        authSealMsg = getIntent().getStringExtra("auth_sealMsg");

        ViewUtils.setText(aty, "授权柜员级别最低为"+authCapability+"级", tvTellerLevel);
        ViewUtils.setText(aty, getString(R.string.authorize_code)+authErrCode, tvAuthorizeCode);
        ViewUtils.setText(aty, getString(R.string.authorize_info)+authErrMsg, tvAuthorizeInfo);
        ViewUtils.setText(aty, getString(R.string.activation_authorize)+authSealMsg, tvActivationAuthorize);
       // ViewUtils.setText("2009642",edtTellerId);
        /*String tempTransCode=getIntent().getStringExtra("transcode");
                //N00110
        if(null==tempTransCode){
        transCode = SocketThread.localAuthorization;
        SocketThread.setPackMsgHead(transCode, "0", authSeqNo, "0");
        }else {
            if (!tempTransCode.equals(SocketThread.bankNoSign) && !tempTransCode.equals(SocketThread.dzqdwh)) {*/
                transCode = SocketThread.localAuthorization;
                SocketThread.setPackMsgHead(transCode, "0", authSeqNo, "0");
            /*} else {
                transCode = SocketThread.fingerCheck;
                ParamUtils.transCode = transCode;
                SocketThread.setPackMsgHead(transCode, "0", authSeqNo, "0");
            }
        }*/
    }

    @OnClick({R.id.btn_read_finger, R.id.submit, R.id.tv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_read_finger:
                readInfo(IDCheck.READ_FINGER);
                break;
            case R.id.submit:
                if (TextUtils.isEmpty(ViewUtils.getText(edtTellerId))) {
                    toast("授权柜员号不能为空");
                    return;
                } else if (!flagFinger) {
                    toast("指纹失败,请重新录指纹!");
                    return;
                }
                isFastClick= SocketUtil.isFastTimeClick(R.id.submit);
                if (!isFastClick) {
                    SocketThread.transCode(AuthorizeActivity.this, transCode, transCodeList, handler);
                    tvCheckFinger.setVisibility(View.VISIBLE);
                 }
                break;
            case R.id.tv_exit:
                finish();
                break;
        }
    }

    /**
     * 读数据
     */
    private void readInfo(int index) {
            this.mIndex = index;
            Intent intent = new Intent(this, IDCheck.class);
            LogUtils.d(mIndex + "-----------------------mIndex----------------" + index);
            intent.putExtra(IDCheck.FUNC_NAME, index);
            startActivityForResult(intent, index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == mIndex)
            switch (resultCode) {
                case IDCheck.READ_FINGER://读指纹
                    finger = intent.getStringExtra("finger");

                    ParamUtils.terllerFinger2 = finger;
                    tvReadFinger.setVisibility(View.VISIBLE);

                    if(!transCode.equals(SocketThread.fingerCheck)) {
                        initList(finger);
                    }else{
                        initList2();
                    }
                    flagFinger = true;
                    LogUtils.d("指纹采取：" + finger);
                    break;
                default:
                    break;
            }
    }

    private List<String> initList(String finger) {
        transCodeList = new ArrayList<>();

        //授权柜员号	AuthSupervisor_ID
        //机构号	HB_Authorization_Branch
        //空	HB_Authorization_Option
        //空	BkFlag1
        //指纹特征值	FPI_tx_model

        transCodeList.add(ViewUtils.getText(edtTellerId));
        transCodeList.add(ParamUtils.orgId);
        transCodeList.add("1");
        transCodeList.add("0");
        transCodeList.add(ParamUtils.terllerFinger2);

        ViewUtils.setViewEnable(aty, submit, true);
        return transCodeList;
    }

    @Deprecated
    private List<String> initList2(){
        transCodeList=new ArrayList<>();
        transCodeList.add(ViewUtils.getText(edtTellerId));
        transCodeList.add(ParamUtils.terllerFinger2);
        transCodeList.add("");

        return transCodeList;
    }

    @Deprecated
    private List<String> initList3(){
        transCodeList=new ArrayList<>();
        transCodeList.add(ViewUtils.getText(edtTellerId));

        return transCodeList;
    }

    public String respState = "";
    public Object obj = null;
    private byte[] bytes;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == -2) {
                toast("交易超时，请重试或检查网络！！！");
            }else if(msg.what == -3){
                //toast("交易超时，请重试或检查网络！！！");
                toast(msg.obj+"");
            }else {
                bytes = ParamUtils.respMsgByte;
                if (msg.obj != null) {
                    parseData(msg);
                }
            }
        }
    };

    private void parseData(Message msg) {

        if (msg.what == -3) {
            respState = (String) msg.obj;
        }
        if (msg.what == 100) {
            listBody = (List<String>)msg.obj;
            respState = "交易成功";
        }
        //toast(SocketThread.getTransStr(transCode)+respState);

        if (transCode.equals(SocketThread.forcedWithdrawal)) {
           this.finish();
        } else if (transCode.equals(SocketThread.localAuthorization)) {

            if (StrUtils.getStateInfo(bytes).equals(getString(R.string.transState))) {
                mySetResult(AUTH_SUCCESS, new Intent());
            }else {
                mySetResult(AUTH_FAIL, new Intent());
            }
        }else if(transCode.equals(SocketThread.fingerCheck)){
            if(respState.equals("交易成功")){
                String s = "";
                transCode = SocketThread.tellerInfoSearch;
                SocketThread.setPackMsgHead(transCode, "0", authSeqNo, "0");
                initList3();
                SocketThread.transCode(AuthorizeActivity.this, transCode, transCodeList, handler);
            }else{
                toast("指纹校验失败！");
            }
        }else if(transCode.equals(SocketThread.tellerInfoSearch)) {
            if (null != listBody&&listBody.size()>0) {
                String val = listBody.get(0);
                if (Integer.parseInt(val) >= 8) {
                    toast("授权成功！");
                    LogUtils.i("------授权成功！---");
                    mySetResult(AUTH_SUCCESS, new Intent());
                }else{
                    toast("用户级别受限，授权失败！");
                    mySetResult(AUTH_FAIL, new Intent());
                }
            }else{
                toast("用户级别解析失败！");
            }
        }
    }
}

