package com.dysen.opencard.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dysen.commom_library.utils.DialogUtils;
import com.dysen.commom_library.utils.PermissionUtils;
import com.dysen.commom_library.utils.SocketUtil;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.CreateCusIdActivity;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.base.ParentFragment;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.http.SocketThread;
import com.dysen.opencard.printer.PrinterInfo;
import com.dysen.socket_library.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 银行开卡
 */
public class OpenCardFragment extends ParentFragment {


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
    @BindView(R.id.read_cert)
    Button readCert;
    @BindView(R.id.seach)
    Button seach;
    @BindView(R.id.ll_cus_id)
    LinearLayout llCusId;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_cus_id_info)
    TextView tvCusIdInfo;
    @BindView(R.id.tv_create_person_cus_id)
    Button tvCreatePersonCusId;
    @BindView(R.id.ll_no_cus_id_info)
    LinearLayout llNoCusIdInfo;
    @BindView(R.id.customerNum)
    EditText customerNum;
    @BindView(R.id.account_type)
    Spinner accountType;
    @BindView(R.id.cardNumber)
    EditText cardNumber;
    @BindView(R.id.card_password)
    EditText cardPassword;
    @BindView(R.id.card_password2)
    EditText cardPassword2;
    @BindView(R.id.read_card)
    Button readCard;
    @BindView(R.id.read_card_password)
    Button readCardPassword;
    @BindView(R.id.card_type_product)
    EditText cardTypeProduct;
    @BindView(R.id.account_type_product)
    Spinner accountTypeProduct;
    @BindView(R.id.product_child)
    Spinner productChild;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.ll_cus_id_info)
    LinearLayout llCusIdInfo;
    @BindView(R.id.read_card_password2)
    Button readCardPassword2;

    Activity aty;
    private int index;
    String[] product_child0;
    String[] product_child1;
    String[] product_child2;
    String[] product_child3;
    String[] product_child4;
    private String[] account_type_product;
    private String[] account_type;

    private String certName, certId, sex, beginDate, endDate, address,cardId, cardPwd, magCardId, finger;
    private List<String> transCodeList;
    private List<String> responsList;
    private String certTypeId;
    private String certTypeStr, productStr, productChildStr, accountTypeStr;
    Unbinder unbinder;
    private String transCode;
    private boolean isFastClick=false;//判断按钮是否重复点击
    private int buttonId=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_card, container, false);

        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        tvCreatePersonCusId.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//        tvCreatPersonCusId.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        aty = getActivity();

//        customerNum.setText("0000080000004428");
        tvCreatePersonCusId.setEnabled(false);
        final String[] cert_type = getResources().getStringArray(R.array.cert_type);
        account_type_product = getResources().getStringArray(R.array.account_type_product);//账户类型
        account_type = getResources().getStringArray(R.array.account_type);
        product_child0 = getResources().getStringArray(R.array.product_child0);
        product_child1 = getResources().getStringArray(R.array.product_child1);
        product_child2 = getResources().getStringArray(R.array.product_child2);
        product_child3 = getResources().getStringArray(R.array.product_child3);
        product_child4 = getResources().getStringArray(R.array.product_child4);

        commonSpinner(aty, cert_type, certType);

        ParamUtils.certType = certTypeStr;
//        certTypeId = certTypeStr.substring(0, 2);//证件类型2个长度
        commonSpinner(aty, account_type, accountType);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (buttonId==R.id.seach) {
            transCode(SocketThread.selectCus);
        }
    }

    int certTypeIndex = 0;

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

                certTypeIndex = pos;
                //  toast(type[pos] + "" + pos);
                if (spinner.equals(certType)) {
                    certTypeStr = type[pos];
                }if (spinner.equals(accountTypeProduct)) {
                    productChild(pos);
                    productStr = type[pos];
                }if (spinner.equals(productChild)) {//产品子类型
                    productChildStr = type[pos];
                }if (spinner.equals(accountType)) {
                    accountTypeProduct(pos);
                    accountTypeStr = type[pos];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        LogUtils.i(certTypeIndex + "--------type--------" + type.length);
    }

    private void accountTypeProduct(int pos) {
        switch (pos) {
            case 0:
                commonSpinner(aty, new
                        String[]{account_type_product[0]}, accountTypeProduct);
                break;
            case 1:
                commonSpinner(aty, new
                        String[]{account_type_product[1]}, accountTypeProduct);
                break;
            case 2:
                commonSpinner(aty, new
                        String[]{account_type_product[2]}, accountTypeProduct);
                break;
            case 3:
                commonSpinner(aty, new
                        String[]{account_type_product[3]}, accountTypeProduct);
                break;
        }
    }

    /**
     * 子产品
     *
     * @param pos
     */
    private void productChild(int pos) {
        LogUtils.i("" + pos);
        switch (pos) {
            case 0: // I-I类账户 --1101-个人活期结算账户
                if("IC金卡".equals(ParamUtils.cardType)){//选择金卡账号
                    //<item>0102-人民币个人VIP卡结算帐户</item>
                    commonSpinner(aty, new String[]{"0102-人民币个人VIP卡结算帐户"}, productChild);
                }else if("IC社保卡".equals(ParamUtils.cardType)){
                    commonSpinner(aty, new String[]{"0105-人民币个人社保卡活期结算帐户"}, productChild);
                }else{//默认标准卡
                    commonSpinner(aty, product_child0, productChild);
                }
                break;
            case 1:
                commonSpinner(aty, product_child1, productChild);
                break;
            case 2:
                commonSpinner(aty, product_child2, productChild);
                break;
            case 3:
                commonSpinner(aty, product_child3, productChild);
                break;
            case 4:
                commonSpinner(aty, product_child3, productChild);
                break;
        }
    }

    @OnClick({R.id.read_cert, R.id.seach, R.id.tv_create_person_cus_id, R.id.read_card, R.id
            .read_card_password, R.id.read_card_password2, R.id.submit, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.read_cert://读身份证
                buttonId=R.id.read_cert;
                readInfo(IDCheck.READ_CERT);
                break;
            case R.id.read_card://读银行卡
                buttonId=R.id.read_card;
                readInfo(IDCheck.READ_CARD);
                break;
            case R.id.read_card_password://读密码
                buttonId=R.id.read_card_password;
                readInfo(IDCheck.READ_PASSWORD);
                break;
            case R.id.read_card_password2://读密码
                buttonId=R.id.read_card_password2;
                readInfo(IDCheck.READ_PASSWORD2);
                break;
            case R.id.seach://搜索
                buttonId=R.id.seach;
                isFastClick= SocketUtil.isFastTimeClick(buttonId);
                if(!isFastClick){
                    transCode(SocketThread.selectCus);
                }
                break;
            case R.id.tv_create_person_cus_id://创建个人客户号
                if(!isFastClick) {
                    createCusId();
                }
                break;
            case R.id.submit://开卡提交
                buttonId=R.id.seach;
                isFastClick=SocketUtil.isFastTimeClick(buttonId);
                if(!isFastClick){
                    cusOpenCard();
                }
                break;
            case R.id.cancel://关闭
                DialogUtils.ShowPromptDailog(aty, new DialogUtils.OnButtonClick() {
                    @Override
                    public void buttonClick(int id) {
                        clearUiData();
                    }
                });
                break;
        }
    }

    private void clearUiData() {
       int count=llCusId.getChildCount();
        ViewUtils.setText("",customerName);
        ViewUtils.setText("",certNumber);
        ViewUtils.setText("",certIssuedStart);
        ViewUtils.setText("",certIssuedEnd);
        rbt0.setChecked(false);
        rbt0.setClickable(false);
        rbt1.setChecked(false);
        rbt1.setClickable(false);
        llCusIdInfo.setVisibility(View.GONE);
    }

    /**
     * 客户开卡
     */
    private void cusOpenCard() {
        transCode = SocketThread.openCard;
        String pwd1 = ViewUtils.getText(cardPassword);
        String pwd2 = ViewUtils.getText(cardPassword2);
        if(!pwd1.equals(pwd2)){
            toast("两次密码必须一致");
            return;
        }
        if ((productStr.length() >= 4 && productChildStr.length() >= 4)) {
            SocketThread.transCode(aty, transCode, Arrays.asList(ViewUtils.getText(customerNum), cardId, pwd2, productStr.substring(0, 4), productChildStr.substring(0, 4), accountTypeStr.substring(0, 1)), handler);
        }
    }
    /**
     * 新开户
     */
    private void createCusId() {
        //开户界面客户信息 自动填充
//        new CreateCusIdActivity().setCertData(certName, certTypeStr, certId, sex, beginDate, endDate);
//        certName=certTypeStr=certId= sex= beginDate= endDate="";

        Intent intent = new Intent(aty, CreateCusIdActivity.class);
        intent.putExtra("cert_name", certName);
        intent.putExtra("cert_type_str", certTypeStr);
        intent.putExtra("cert_id", certId);
        intent.putExtra("cert_sex", sex);
        intent.putExtra("cert_address", address);
//        StringUtils.setCertDate(beginDate);
//        beginDate = StringUtils.setCertDate(beginDate);
//        endDate = StringUtils.setCertDate(endDate);
        LogUtils.i(beginDate + "-----------end----------" + endDate);
        intent.putExtra("cert_begin", beginDate);
        intent.putExtra("cert_end", endDate);
        LogUtils.i("certName=" + certName + "\tcertTypeStr=" + certTypeStr + "\tcertId=" + certId + "\tsex=" + sex + "\tbeginDate=" + beginDate + "\tendDate=" + endDate);
        if (!certName.isEmpty() && !certTypeStr.isEmpty() && !certId.isEmpty() && !sex.isEmpty() && !beginDate.isEmpty() && !endDate.isEmpty())
            startActivityForResult(intent,
                    CreateCusIdActivity.CUS_ID);
        else
            toast("上面参数有误！");
    }

    public Object obj = null;
    private String respState="";
    private byte[] bytes;
    int msgType=0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == -2) {
                toast("交易超时，请重试或检查网络！！！");
            }
                parseData(msg);
        }
    };

    private void parseData(Message msg) {
        responsList = new ArrayList<>();

        if (msg.what == -3) {
            respState = (String) msg.obj;
//            llNoCusIdInfo.setVisibility(View.VISIBLE);
//            llCusIdInfo.setVisibility(View.GONE);
        }
        if (msg.what == 100) {
            responsList = (List<String>)msg.obj;
            respState = "交易成功";
        }
        toast(SocketThread.getTransStr(transCode)+respState);
        //toast("------0------"+transCode);
        if (transCode.equals(SocketThread.selectCus)) {
            if (responsList != null && responsList.size() == 2) {
                LogUtils.d("hut","搜索查询客户信息成功=="+responsList.toString());
                ParamUtils.cusNum  = responsList.get(0);
                ParamUtils.cusName = responsList.get(1);
                ViewUtils.setText(ParamUtils.cusNum, customerNum); //返显客户号
                llCusIdInfo.setVisibility(View.VISIBLE);
                llNoCusIdInfo.setVisibility(View.GONE);
            } else if(respState.equals("没有找到记录")){//没有客户号，创建客户信息(开户)
                LogUtils.d("hut","搜索查询客户信息失败==");
                llNoCusIdInfo.setVisibility(View.VISIBLE);
                llCusIdInfo.setVisibility(View.GONE);
            }
        }else if (transCode.equals(SocketThread.openCard)) {//开卡
            if (responsList != null && responsList.size() == 1) {
                ParamUtils.accountNum = responsList.get(0);
                llCusIdInfo.setVisibility(View.VISIBLE);
                llCusIdInfo.setVisibility(View.VISIBLE);
                llNoCusIdInfo.setVisibility(View.GONE);
                printInfo(PrinterInfo.PRINT_OPEN_CARD);
            }else {//开卡失败
                llNoCusIdInfo.setVisibility(View.GONE);
                llCusIdInfo.setVisibility(View.VISIBLE);
            }
        }else if(transCode.equals(SocketThread.cardType)){//查卡产品类型
            if (responsList != null && responsList.size() == 4) {
                ParamUtils.cardType = responsList.get(0);
                ViewUtils.setText(aty, ParamUtils.cardType, cardTypeProduct);
                if("IC金卡".equals(ParamUtils.cardType)){//选择金卡账号
                            //<item>0102-人民币个人VIP卡结算帐户</item>
                    commonSpinner(aty, new String[]{"1-I类户"}, accountType);
                }else{//默认标准卡 (I -II类账户)
                    commonSpinner(aty, account_type, accountType);
                }
            }
            llNoCusIdInfo.setVisibility(View.GONE);
            llCusIdInfo.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 开卡打印凭证
     */
    private void printInfo(int index) {

            this.index = index;
            Intent intent = new Intent(aty, PrinterInfo.class);
            intent.putExtra(IDCheck.FUNC_NAME, index);
            startActivityForResult(intent, index);

    }

    private List<String> initList(String transCode) {

        transCodeList = new ArrayList<>();

        if (transCode.equals(SocketThread.selectCus)) {
            transCodeList.add(ViewUtils.getText(certNumber));
            transCodeList.add(certTypeStr.substring(0, 2));
            transCodeList.add(ViewUtils.getText(customerName));
        } else if (transCode.equals(SocketThread.openCard)) {
            transCodeList.add(ViewUtils.getText(customerNum));
            transCodeList.add(ViewUtils.getText(cardNumber));
            transCodeList.add(ViewUtils.getText(cardPassword));
            transCodeList.add(productStr.substring(0, 4));
            transCodeList.add(productChildStr.substring(0, 4));
        }
        return transCodeList;
    }

    private void transCode(String transCode) {
        this.transCode = transCode;

        SocketThread.setPackMsgHead(transCode, "1", "", "0");
        if (transCode.equals(SocketThread.selectCus)) {
            if ((!ViewUtils.getText(certNumber).isEmpty() && !ViewUtils.getText(customerName).isEmpty
                    ()) || (!ViewUtils.getText(customerNum).isEmpty() && !ViewUtils.getText(cardNumber).isEmpty
                    () && !ViewUtils.getText(cardPassword).isEmpty())) {
                initList(transCode);
                SocketThread.transCode(aty, transCode, transCodeList, handler);
            } else {
                toast("输入不能为空！！！");
//            llNoCusIdInfo.setVisibility(View.VISIBLE);
//            llCusIdInfo.setVisibility(View.GONE);
            }
        } else if (transCode.equals(SocketThread.openCard)) {

        }
    }

    /**
     * 读身份证
     */
    private void readInfo(int index) {
        isFastClick= SocketUtil.isFastTimeClick(buttonId);
        LogUtils.i("hut","是否重复点击"+isFastClick);
        if(isFastClick){
            LogUtils.i("hut","重复点击啦");
            return;
        }

            this.index = index;
            Intent intent = new Intent(aty, IDCheck.class);
            intent.putExtra(IDCheck.FUNC_NAME, index);
            startActivityForResult(intent, index);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        LogUtils.d("hut","请求码="+requestCode+"结果="+resultCode);
        if (requestCode == index) {
            switch (resultCode) {
                case IDCheck.READ_CERT://读身份证
                    certName = intent.getStringExtra("cert_name");
                    certId = intent.getStringExtra("cert_id");
                    sex = intent.getStringExtra("cert_sex");
                    beginDate = intent.getStringExtra("cert_begin");
                    endDate = intent.getStringExtra("cert_end");
                    address = intent.getStringExtra("cert_address");
                    ParamUtils.certName = certName;
                    ParamUtils.certId = certId;
                    ParamUtils.certType = certTypeStr;
                    ViewUtils.setText(aty, certName, customerName);
                    ViewUtils.setText(aty, certId, certNumber);
                    ViewUtils.setText(aty, beginDate, certIssuedStart);
                    ViewUtils.setText(aty, endDate, certIssuedEnd);
                    LogUtils.d("hut","性别：" + sex);
                    if (sex.equals("女")) {
                        rbt1.setClickable(true);
                        rbt1.setChecked(true);//女
                        rbt1.setEnabled(true);
                    }else if (sex.equals("男")) {
                        rbt0.setClickable(true);
                        rbt0.setEnabled(true);
                        rbt0.setChecked(true);//男
                    }
                    tvCreatePersonCusId.setEnabled(true);
                    break;
                case IDCheck.READ_CARD://读银行卡
                    cardId = intent.getStringExtra("card_id");
                    ParamUtils.cardNum = cardId;
                    ViewUtils.setText(aty, cardId, cardNumber);
                    transCode = SocketThread.cardType;
                    transCodeList.clear();
                    transCodeList.add(cardId);
                    SocketThread.transCode(aty, transCode, transCodeList, handler);
                    break;
                case IDCheck.READ_PASSWORD://读密码
                    cardPwd = intent.getStringExtra("card_pwd");
                    ViewUtils.setText(aty, cardPwd, cardPassword);
                    break;
                case IDCheck.READ_PASSWORD2://读密码
                    cardPwd = intent.getStringExtra("card_pwd");
                    ViewUtils.setText(aty, cardPwd, cardPassword2);
                    break;
                case IDCheck.READ_MAGCARD://读磁条卡
                    magCardId = intent.getStringExtra("mag_card_id");
                    ParamUtils.cardNum = magCardId;
                    toast("磁条卡：" + magCardId);
                    break;
                case IDCheck.READ_FINGER://读指纹
                    finger = intent.getStringExtra("finger");
                    toast("指纹：" + finger);
                    break;
                case -1:
                    toast(intent.getStringExtra("err_code"));
                    break;
                case -2:
                    break;
                case PrinterInfo.PRINT_OPEN_CARD:
                    break;
                case PrinterInfo.PRINT_ERR:
                    break;
                default:
                    break;
            }


        } else if (requestCode == CreateCusIdActivity.CUS_ID) {
            switch (resultCode){
                case CreateCusIdActivity.CUS_NUM://客户号
                    LogUtils.d("hut","客户创建成功");//接受蓝牙打印成功返回结果
                    String cus_num = intent.getStringExtra("cus_num");
//                    cus_num = String.valueOf(Long.parseLong(cus_num));
                    ViewUtils.setText(aty, cus_num, customerNum);
                    llCusIdInfo.setVisibility(View.VISIBLE);
                    llNoCusIdInfo.setVisibility(View.GONE);
                    break;
                case CreateCusIdActivity.CUS_NUM_NO://没有客户号
                    cusOpenCard();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
