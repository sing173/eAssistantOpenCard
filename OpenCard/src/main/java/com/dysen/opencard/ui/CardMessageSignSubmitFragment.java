package com.dysen.opencard.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dysen.opencard.base.BaseApplication;
import com.dysen.opencard.base.ParentFragment;
import com.dysen.commom_library.utils.CodeFormatUtils;
import com.dysen.commom_library.utils.DatetimeUtil;
import com.dysen.commom_library.utils.DialogUtils;
import com.dysen.commom_library.utils.FormatUtil;
import com.dysen.commom_library.utils.KeyBoardUtils;
import com.dysen.commom_library.utils.StringUtils;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.R;
import com.dysen.opencard.common.FragmentUtils;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.StrUtils;
import com.dysen.opencard.common.costomview.LabelsView;
import com.dysen.opencard.printer.PrinterInfo;
import com.dysen.opencard.http.SocketThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by hutian on 2018/1/29.
 *
 * @卡短信签约
 */
public class CardMessageSignSubmitFragment extends ParentFragment {
    @BindView(R.id.et_sign_account)
    EditText etSignAccount;
    @BindView(R.id.et_sign_cardNumber)
    EditText etSignCardNumber;
    @BindView(R.id.certType)
    Spinner certType;
    @BindView(R.id.et_certID_item)
    EditText etCertIDItem;
    @BindView(R.id.ll_sign_cardType)
    LinearLayout llSignCardType;
    @BindView(R.id.et_coustomerName)
    EditText etCoustomerName;
    @BindView(R.id.et_sign_phone_count)
    EditText etSignPhoneCount;
    @BindView(R.id.ll_sign_phoneNumber)
    LinearLayout llSignPhoneNumber;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_sign_addphoneNumber)
    LinearLayout llSignAddphoneNumber;//添加手机
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.sp_charge_standards)
    Spinner spChargeStandards;
    @BindView(R.id.et_free_month)
    EditText etFreeMonth;
    @BindView(R.id.et_amount_lowerlimit)
    EditText etAmountLowerlimit;
    @BindView(R.id.et_remind_lowerlimit)
    EditText etRemindLowerlimit;
    @BindView(R.id.ll_sign_amount)
    LinearLayout llSignAmount;
    @BindView(R.id.rbt_0)
    RadioButton rbt0;
    @BindView(R.id.rbt_1)
    RadioButton rbt1;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.labels_view)
    LabelsView labelsView;

    Unbinder unbinder;
    private Dialog dialogAddMobileNum;

    ArrayList<String> labels = new ArrayList<>();
    private String mobileNum;
    private Window window;
    private String signAccount, signCardNum, signCardPwd, certTypeStr, chargesStr, certNum, cusName, cardProductName;
    private String transCode, s;
    private List<String> responsList;
    private String duration;//免费月数
    private List<String> transCodeList;
    private String authSeqNo;
    private int PRINT_FUNC;
    private static final int REQUEST_CODE = 0; // 请求码// 所需的全部权限
    private int printTime=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messagesign_submit, container, false);

        unbinder = ButterKnife.bind(this, view);

        initView();
        return view;
    }

    private void initView() {
        //LogUtils.d("hut","签约账号"+signAccount+"=签约卡号"+signCardNum);

        ViewUtils.setText(signAccount, etSignAccount);
        ViewUtils.setText(signCardNum, etSignCardNumber);
        ViewUtils.setText(certNum, etCertIDItem);
        ViewUtils.setText(cusName, etCoustomerName);

        ViewUtils.setText("1.00", etAmountLowerlimit);
        ViewUtils.setText("1.00", etRemindLowerlimit);
        if("IC金卡".equals(cardProductName)){
            ViewUtils.setText("无限", etFreeMonth);
        }
        commonSpinner(getActivity(), new String[]{certTypeStr}, certType);
        String[] charges = getResources().getStringArray(R.array.charges);
        commonSpinner(getActivity(), charges, spChargeStandards);
        spChargeStandards.setSelection(0);
        chargesStr = charges[0];
        transCode(SocketThread.messageSignLimitPeriod);

        //标签的点击监听
        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                //label是被点击的标签，labelText是标签的文字，position是标签的位置。
            }
        });
        //标签的选中监听
        labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                //label是被点击的标签，labelText是标签的文字，isSelect是是否选中，position是标签的位置。
                if (isSelect) {
                    labels.remove(position);
                    addLabel();
                }
            }
        });
        printTime=0;
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
//        if("00".equals(StringUtils.splitString(chargesStr,0))) {
//	        toast(SocketThread.getTransStr(transCode) + s);
//        }
        toast(SocketThread.getTransStr(transCode) + s);
        if (transCode.equals(SocketThread.messageSignLimitPeriod)) {//查询短信签约期限
            if (responsList != null && responsList.size() > 0) {
                duration = responsList.get(0);

                ViewUtils.setText(duration, etFreeMonth);
            }
        } else if (transCode.equals(SocketThread.messageSign)) {
            if (StrUtils.getStateInfo(bytes).equals("交易成功完成")) {//s.contains("已经签约")

                    printInfo(PrinterInfo.PRINT_SIGN_MESSAGE);
            } else if (StrUtils.getStateInfo(bytes).equals("该交易需要授权")) {
                            if("00".equals(StringUtils.splitString(chargesStr,0))){//免费时需要需要授权，其他需要
                                goToAuthorizeActivity();
                            }else{
                                //printInfo(PrinterInfo.PRINT_SIGN_MESSAGE);//直接打印
                                transCode = SocketThread.messageSign;
                                SocketThread.setPackMsgHead(transCode, "1", authSeqNo, "1");
                                SocketThread.transCode(getContext(), transCode, transCodeList, handler);
                            }
//                        }
//                    }
            }else if(StrUtils.getStateInfo(bytes).contains("已经签约")){
                //toast("该账户已经签约！");
            }
        }
    }

    private void goToAuthorizeActivity() {
        Intent intent = new Intent(getContext(), AuthorizeActivity.class);
        authSeqNo = CodeFormatUtils.byte2StrIntercept(bytes, 114, 20);
        intent.putExtra("auth_seqNo", CodeFormatUtils.byte2StrIntercept(bytes, 114, 20));
//                            intent.putExtra("bk_seqNo", respHeadList.get(3));
        intent.putExtra("auth_capability", CodeFormatUtils.byte2StrIntercept(bytes, 154, 3));
        intent.putExtra("auth_errCode", CodeFormatUtils.byte2StrIntercept(bytes, 159, 6));
        intent.putExtra("auth_errMsg", CodeFormatUtils.byte2StrIntercept(bytes, 165, 120));
        intent.putExtra("auth_sealMsg", CodeFormatUtils.byte2StrIntercept(bytes, 285, 120));

        startActivityForResult(intent, AuthorizeActivity.SIGN_MESSAGE);
    }

    /**
     * 打印凭证
     */
    private void printInfo(int index) {
            printTime++;
            this.PRINT_FUNC = index;
            Intent intent = new Intent(getContext(), PrinterInfo.class);
            intent.putExtra(PrinterInfo.FUNC_NAME, PRINT_FUNC);
            if(printTime==1) {
                startActivityForResult(intent, index);
            }else{

            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == AuthorizeActivity.SIGN_MESSAGE) {
            if (resultCode == AuthorizeActivity.AUTH_SUCCESS) {//授权成功再次走签约交易
                transCode = SocketThread.messageSign;
                SocketThread.setPackMsgHead(transCode, "1", authSeqNo, "1");
                SocketThread.transCode(getContext(), transCode, transCodeList, handler);

            } else if (resultCode == AuthorizeActivity.AUTH_FAIL) {
                toast("授权失败");
            }
        }else if (requestCode == PrinterInfo.PRINT_SIGN_MESSAGE){
            if (resultCode == PrinterInfo.PRINT_SUCCESS) {
                ClearUiData();
            }else if (resultCode == PrinterInfo.PRINT_FAIL){
                //printInfo(PrinterInfo.PRINT_SIGN_MESSAGE);
            }
        }
    }

    private void ClearUiData() {

        ViewUtils.setText("", etSignAccount);
        ViewUtils.setText("", etSignCardNumber);
        ViewUtils.setText("", etCertIDItem);
        ViewUtils.setText("", etCoustomerName);

        ViewUtils.setText("", etAmountLowerlimit);
        ViewUtils.setText("", etRemindLowerlimit);
        ViewUtils.setText("",etSignPhoneCount);
        ViewUtils.setText("",etFreeMonth);
        labels.clear();
        labelsView.clearAllSelect();

    }

    private String commonSpinner(final Activity aty, final String[] type, final Spinner spinner) {
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
                } else if (spinner.equals(spChargeStandards)) {
                    chargesStr = type[pos];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        return certTypeStr;
    }

    private void addLabel() {
        ViewUtils.setText(labels.size() + "", etSignPhoneCount);
        labelsView.setLabels(labels); //直接设置一个字符串数组就可以了。
    }

    /**
     * 添加手机号 弹窗
     */
    public void showAddMobileNum() {

        dimBackground(1.0f, 0.5f);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_mobile_num, null);

        dialogAddMobileNum = DialogUtils.showCloseDialog(getContext(), view);
        dialogAddMobileNum.setCanceledOnTouchOutside(false);
        final EditText edtMobileNum = (EditText) dialogAddMobileNum.getWindow().findViewById(R.id.edt_mobile_num);
        Button btnSubmit = (Button) dialogAddMobileNum.getWindow().findViewById(R.id
                .btn_submit);
        LinearLayout relayout = dialogAddMobileNum.getWindow().findViewById(R.id.ll_add_mobile_num);
//        KeyBoardUtils.closeKeybord(edtTellerId, dialogWithdrawalTellerId.getWindow().getContext());
        dialogAddMobileNum.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        window = dialogAddMobileNum.getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        int minHeight = (int) (display.getHeight() * 0.16);              //使用这种方式更改了dialog的框高
// int minWidth = (int) (display.getWidth()*0.4);             //没有效果
        relayout.setMinimumHeight(minHeight);

        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (display.getWidth() * 0.4);                     //使用这种方式更改了dialog的框宽
        window.setAttributes(params);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(labels!=null &&labels.size()>10){
                    toast("你已经超过手机号码最多10个限制");
                    return;
                }
                mobileNum = ViewUtils.getText(edtMobileNum);
                if (!mobileNum.isEmpty() && FormatUtil.isMobileNO(mobileNum)) {

                    dimBackground(0.5f, 1.0f);
                    KeyBoardUtils.closeKeybord(edtMobileNum, window.getContext());
                    dialogAddMobileNum.dismiss();
                    labels.add(ViewUtils.getText(edtMobileNum));
                    addLabel();
                } else {
                    toast("请输入合法的手机号");
                }
//                dialogWithdrawalTellerId.dismiss();
            }
        });

        dialogAddMobileNum.getWindow().findViewById(R.id
                .tv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dimBackground(0.5f, 1.0f);
                KeyBoardUtils.closeKeybord(edtMobileNum, window.getContext());
                dialogAddMobileNum.dismiss();
            }
        });
    }

    @OnClick({R.id.img_add, R.id.btn_submit, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_add:
                showAddMobileNum();
                break;
            case R.id.btn_submit:
                transCode(SocketThread.messageSign);
                break;
            case R.id.btn_back:
                DialogUtils.ShowPromptDailog(getActivity(), new DialogUtils.OnButtonClick() {
                    @Override
                    public void buttonClick(int id) {
                        retunFragment();
                    }
                });

                break;
        }
    }

    private void retunFragment() {
        CardMessageSignFragment cardMessageSignFragment=new CardMessageSignFragment();
        FragmentUtils .switchFragment(getActivity(),getActivity().getSupportFragmentManager(),cardMessageSignFragment,null);
    }

    private void transCode(String transCode) {
        this.transCode = transCode;
        if (transCode.equals(SocketThread.messageSignLimitPeriod)) {
            SocketThread.transCode(getContext(), transCode, Arrays.asList(new String[]{ViewUtils.getText(etSignCardNumber)}), handler);
        } else if (transCode.equals(SocketThread.messageSign)) {
            if (labels.size() <= 0) {
                toast("请添加手机号，在做操作！");
                return;
            }
            initList();
            //默认为0-需要检查授权  1-不检查授权
            SocketThread.setPackMsgHead(transCode, "0", "", "0");
            SocketThread.transCode(getContext(), transCode, transCodeList, handler);
        }
    }

    private List<String> initList() {
        saveData();//保存打印时需要的参数
        transCodeList = new ArrayList<>();
        transCodeList.add(signCardNum);      // 卡号
        transCodeList.add(signCardPwd);       // 密码
        transCodeList.add(signAccount);       // 账号
        transCodeList.add(certTypeStr.substring(0, 2));       // 证件类型
        transCodeList.add(certNum);       // 证件号码
        transCodeList.add(cusName);       // 客户名称
        transCodeList.add(DatetimeUtil.getTodayDate());       // 签约日期
        transCodeList.add(ViewUtils.getText(etSignPhoneCount));       // 签约手机个数
        transCodeList.add(getMobileNumFormat());       // 签约手机号   (18971642933 ------- 0118971642933    1)
        transCodeList.add(chargesStr.substring(0, 2));       // 收费标准
        transCodeList.add("" + getAmount(chargesStr.substring(0, 2), Integer.valueOf(ViewUtils.getText(etSignPhoneCount))));       // 收费金额
        transCodeList.add(duration);       // 免费月份
        transCodeList.add(ViewUtils.getText(etAmountLowerlimit));       // 金额变动下限
        transCodeList.add(ViewUtils.getText(etRemindLowerlimit));       // 余额提醒下线
        if (rbt0.isChecked())
            transCodeList.add("Y");
        if (rbt1.isChecked())
            transCodeList.add("N");       // 是否显示余额
        return transCodeList;
    }

    private void saveData() {
        ParamUtils.msg_cardNum = signCardNum;
        ParamUtils.msg_certType = certTypeStr.substring(2);
        ParamUtils.msg_certId = certNum;
        ParamUtils.msg_cardProductName = cardProductName;
        ParamUtils.msg_certName = cusName;
        ParamUtils.msg_signAccount = signAccount;
        ParamUtils.msg_mobileNums = labels;
//        ParamUtils.msg_charges = chargesStr;
        ParamUtils.msg_charges = chargesStr.substring(2);
        ParamUtils.msg_amountLowerLimit = ViewUtils.getText(etAmountLowerlimit);
        ParamUtils.msg_remindLowerlimit = ViewUtils.getText(etRemindLowerlimit);
        ParamUtils.msg_isShowFree = rbt0.isChecked() ? "Y" : "N";
//        ParamUtils.msg_isShowFree = rbt0.isChecked() ? "是" : "否";

    }

    /**
     * 修改手机号码拼接格式
     * @return
     */
    private String getMobileNumFormat() {
        String str = "";
        for (int i = 0; i < labels.size(); i++) {
            str += labels.get(i) + "    1";
        }

        if (labels.size() < 10) {
            str = "0" + labels.size() + str;
        } else if (labels.size() >= 10) {
            str = labels.size() + str;
        }
        return str;
    }

    /**
     * 计算收费金额
     * 手机号码个数 × 收费标准
     *
     * @param charges
     * @param mobileNumCount
     * @return
     */
    private Double getAmount(String charges, int mobileNumCount) {
        double d = 0.0;
        if (charges.equals("00")) {
            d = 0;
        } else if (charges.equals("01")) {
            d = mobileNumCount * 1;
        } else if (charges.equals("02")) {
            d = mobileNumCount * 2;
        } else if (charges.equals("03")) {
            d = mobileNumCount * 12;
        }
        return d;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setData(String signAccount, String signCardNum, String signCardPwd, String certTypeStr, String certNum, String cusName, String cardProductName) {

        this.signAccount = signAccount;
        this.signCardNum = signCardNum;
        this.signCardPwd = signCardPwd;
        this.certTypeStr = certTypeStr;
        this.certNum = certNum;
        this.cusName = cusName;
        this.cardProductName = cardProductName;

    }
}
