package com.dysen.opencard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dysen.opencard.base.ParentActivity;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.base.BaseApplication;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.http.SocketThread;
import com.dysen.socket_library.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignOutTempActivity extends ParentActivity {

    @BindView(R.id.tv_exit)
    TextView tvExit;
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
    @BindView(R.id.btn_sign_out)
    Button btnSignOut;
    @BindView(R.id.ll_cus_id)
    LinearLayout llCusId;
    private List<String> transCodeList;
    private static String transCode;
    private String finger;
    private List<String> responsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out_temp);
        ButterKnife.bind(this);
        this.setFinishOnTouchOutside(false);
        initView();
    }

    private void initView() {
        aty = this;
       BaseApplication.getInstance().addActivity(aty);
        transCode = SocketThread.tempWithdrawal;
  //      ViewUtils.setViewEnable(aty, btnSignOut, false);
        ViewUtils.setText(ParamUtils.tellerId, edtTellerId);
        ViewUtils.setText(ParamUtils.orgId, edtOrgId);
        ViewUtils.setText(ParamUtils.terminalId, edtTerminalId);
    }

    private List<String> initList(String finger) {
        transCodeList = new ArrayList<>();

//            HB_tellerNo1 柜员号
//            HB_Teller_PW 密码
//            HB_branch1 机构号
//            HB_terminalId1 终端号
//            FPI_tx_model 指纹特征值
//            BkCertType 校验方式
//            BkAuthTeller 授权柜员
        transCodeList.add(ParamUtils.tellerId);
        transCodeList.add("");
        transCodeList.add(ParamUtils.orgId);
        transCodeList.add(ParamUtils.terminalId);
//        transCodeList.add(finger);
        transCodeList.add(ParamUtils.terllerFinger2);
        transCodeList.add("0");//0-指纹， 1-密码 暂时只支持指纹登录
        transCodeList.add("");
       // ViewUtils.setViewEnable(aty, btnSignOut, true);
        btnSignOut.setEnabled(true);
        //默认灰色不可点
        btnSignOut.setBackgroundResource(R.drawable.button_login);
        return transCodeList;
    }

    @OnClick({R.id.btn_read_finger, R.id.btn_sign_out, R.id.tv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_read_finger:
                readInfo(IDCheck.READ_FINGER);
                break;
            case R.id.btn_sign_out:
                aty = this;
                if(TextUtils.isEmpty(ParamUtils.terllerFinger2)){
                    toast("请先确保指纹录取成功");
                    return;
                }
                SocketThread.transCode(this, transCode, transCodeList, handler);
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
                case IDCheck.READ_FINGER://读指纹
                    finger = intent.getStringExtra("finger");
//                    edtTellerFinger.setText(finger);

                    ParamUtils.terllerFinger2 = finger;
                    edtTellerFinger.setText("指纹录取成功！！！");
                    initList(finger);
//                    toast("指纹：" + finger);
                    LogUtils.d("指纹采取：" + finger);
                    break;
                default:
                    break;
            }
    }

    public String s = "";
    public Object obj = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

                parseData(msg);
        }
    };

    private void parseData(Message msg) {

        if (msg.what == -3) {
            s = (String) msg.obj;
        }
        if (msg.what == 100) {
            responsList = (List<String>)msg.obj;
            s = "交易成功";
        }
        toast(SocketThread.getTransStr(transCode)+s);
        if (transCode.equals(SocketThread.tempWithdrawal)) {
            toast("临时签退");
            BaseApplication.exitApps();
        } else if (transCode.equals(SocketThread.forcedWithdrawal)) {
            toast("强制签退");

        }
    }


    public static void setData(String tempWithdrawal) {
        transCode = tempWithdrawal;
    }

}
