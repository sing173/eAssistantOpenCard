package com.dysen.opencard.backClip;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dysen.opencard.base.ParentActivity;
import com.dysen.commom_library.utils.DialogAlert;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.commom_library.utils.PreferencesUtils;
import com.dysen.commom_library.views.UberProgressView;
import com.dysen.opencard.R;
import com.dysen.opencard.common.ParamUtils;
import com.examples.outputjar.BlueUtils;
import com.examples.outputjar.ClsUtils;
import com.guoguang.hbnx.impl.iMateInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by dysen on 2017/7/24.
 */

public class IDCheck extends ParentActivity {
    public static final String FUNC_NAME = "func_name";
    public static final int READ_CERT = 6;//身份证
    public static final int READ_CARD = 1;//银行卡
    public static final int READ_PASSWORD = 2;//密码
    public static final int READ_MAGCARD = 3;//磁条卡
    public static final int READ_FINGER = 4;//指纹
    public static final int READ_PASSWORD2 = 5;//读取旧密码

    @BindView(R.id.uber)
    UberProgressView uber;
    @BindView(R.id.fl_idcard)
    FrameLayout flIdcard;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.gif)
    GifImageView gifImageView;

    private int searchGuoguang, searchShensi, searchWoqi;
    private int bondedGuoguang, bondedShensi, bondedWoqi;
    private int neededConGuoguang, neededConShensi, neededConWoqi;
    private int deviceId;

    private final int DEVICE_GUOGUANG_ID = 1;
    private final int DEVICE_SHENSI_ID = 2;
    private final int DEVICE_WOQI_ID = 3;
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    private HashMap<String, Bitmap> bitMap = new HashMap<String, Bitmap>();
    private String deviceName;
    private String errCode;
    private PreferencesUtils pre;
    private String deviceAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_check_id);
        ButterKnife.bind(this);
        initView();
        initIntent();
    }

    private void initIntent() {
        Intent intent=getIntent();
        // 如果加载的是gif动图，第一步需要先将gif动图资源转化为GifDrawable
        // 将gif图资源转化为GifDrawable
        GifDrawable gifDrawable = null;
        int resourceId=0;
        if(intent!=null){
            int type=intent.getIntExtra(FUNC_NAME,0);
            try {
                if(type==IDCheck.READ_FINGER){
                    resourceId=R.drawable.read_finger;
                }else if(type==IDCheck.READ_CERT){
                    resourceId=R.drawable.read_id;
                }else if(type==IDCheck.READ_CARD){
                    resourceId=R.drawable.read_ic;
                }else if(type==IDCheck.READ_PASSWORD){
                    resourceId=R.drawable.read_pwd;
                }else if(type==IDCheck.READ_PASSWORD2){//读旧密码
                    resourceId=R.drawable.read_pwd;
                }
                gifDrawable = new GifDrawable(getResources(), resourceId);
                // gif加载一个动态图gif
                gifImageView.setImageDrawable(gifDrawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {

        Intent intent=getIntent();
        deviceId=intent.getIntExtra("flag",0);

        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        openBle();
//        getBondedDevices();
//        receiverRegister();
//        searchDevices();
        pre=new PreferencesUtils(IDCheck.this,"BlueTooth");
        int deviceIdPre=pre.getInt("deviceId",0);
        deviceAddress=pre.getString("deviceAddress","");
        String whetherConnected=pre.getString("whetherConnected","no");
        if(deviceIdPre!=0&&deviceId==0){
            deviceId=deviceIdPre;
        }
        //if(!whetherConnected.equals("no")){
            readInfo();
        //}else {
            //BlueUtils.ConnectDevice(deviceId,IDCheck.this,handler,deviceAddress);
        //}
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

    private void receiverRegister() {
        // TODO Auto-generated method stub
        //注册用以接收到已搜索到的设备设备的receiver
        IntentFilter mFilter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        registerReceiver(mReceiver, mFilter1);
        //注册搜索完成时的receiver
        mFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, mFilter1);

        IntentFilter mFilter2 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
//        registerReceiver(pReceiver, mFilter2);
    }

    private void setButtonEnable() {
        LogUtils.d("searchShensi:" + searchShensi + "\tbondedShensi:" + bondedShensi);
        if (searchGuoguang == 1 && bondedGuoguang == 1) {
            Message msg = new Message();
            msg.what = 1;
            mHandler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 2;
//            mHandler.sendMessage(msg);
        }

        if (searchWoqi == 1 && bondedWoqi == 1) {
            Message msg = new Message();
            msg.what = 3;
            mHandler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 4;
//            mHandler.sendMessage(msg);
        }

        if (searchShensi == 1 && bondedShensi == 1) {
            Message msg = new Message();
            msg.what = 5;
            mHandler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 6;
//            mHandler.sendMessage(msg);
        }
    }

    private void searchDevices() {

        if (mBlueToothAdapter.isDiscovering()) {
            toast("正在扫描设备！");
            mBlueToothAdapter.cancelDiscovery();
        }
        mBlueToothAdapter.startDiscovery();
    }

    int temp;
    private Handler handlerState = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                toast("背夹设备已断开失败！");
            } else if (msg.what == 0) {
                toast("背夹设备已断开！");
            }
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 1) {
                deviceId = DEVICE_GUOGUANG_ID;
            } else if (msg.what == 2) {
                temp = 0;
            } else if (msg.what == 3) {
                deviceId = DEVICE_WOQI_ID;
            } else if (msg.what == 4) {
                temp = 0;
            } else if (msg.what == 5) {
                deviceId = DEVICE_SHENSI_ID;
            } else if (msg.what == 6) {
                temp = 0;
            }

            LogUtils.d(msg.what + "\tdeviceId:" + deviceId);
            if (deviceId > 0) {
                SystemClock.sleep(1000);

                //BlueUtils.ConnectDevice(deviceId, IDCheck.this, handler,deviceAddress);
//                BlueUtils.IdCardInfoGet(deviceId, IDCheck.this, handler, hashMap, bitMap);
            } else {
                LogUtils.d("deviceId error");
            }
        }
    };

    boolean nxFlag;
    private String tempPin;
    private String tempAddress;
    List<String> list = new ArrayList<>();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();

            boolean nxBlueFlag1 = false, nxBlueFlag2 = false;
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                toast("正在扫描设备");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String temp = device.getName();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                    LogUtils.d(temp + "\tdevice===" + device.getType()+"-------------------------------"+device.getBluetoothClass().getMajorDeviceClass());
//                    System.out.println(temp + "\tdevice===" + device.getType()+"-------------------------------"+device.getBluetoothClass().getMajorDeviceClass());
//                }
                if (temp == null) {
                    temp = "";
                }

                switch (deviceId){
                    case DEVICE_WOQI_ID:
                        temp = "P3520";
                        break;
                    case DEVICE_SHENSI_ID:
                        temp = "SS-";
                        break;
                    case DEVICE_GUOGUANG_ID:
                        temp = "GEIT";
                        break;
                }

                if (temp.contains("P3520") || temp.contains("GEIT") || temp.contains("SS-")) {
                    deviceName = temp;
                    list.add(deviceName);
                    mBlueToothAdapter.cancelDiscovery();
                    unregisterReceiver(mReceiver);
                    nxBlueFlag1 = true;
                    //搜索到的不是已经配对的设备设备
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                        nxBlueFlag2 = true;
                    } else {

                        nxBlueFlag2 = false;
                        String address = device.getAddress();
                        tempAddress = address;

                        try {
                            if (temp.contains("P3520")) {
                                searchWoqi = 1;
                                tempPin = "1235";
                                if (neededConWoqi == 1) {
                                    toast("握奇设备已配对！");
                                } else {
//                                getDevices(address);
                                }
                            } else if (temp.contains("GEIT")) {
                                searchGuoguang = 1;
                                tempPin = "1234";
                                if (neededConGuoguang == 1) {
                                    toast("国光设备已配对！");
                                } else {
//                                getDevices(address);
                                }
                            } else if (temp.contains("SS-")) {
                                searchShensi = 1;
                                if (neededConShensi == 1) {
                                    toast("神思设备已配对！");
                                } else {
//                                getDevices(address);
                                }
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        LogUtils.i("---广播---" + temp + "\nneededConWoqi:" + neededConWoqi +
                                "\tneededConGuoguang:" + neededConGuoguang + "\tneededConShensi:" + neededConShensi);
//                setButtonEnable();
                        getBondedDevices();
                    }
                    if (nxBlueFlag2 && nxBlueFlag1) {
                        final DialogAlert dialog = ShowDialog(context, "设备未配对，请去系统里配对设备！");
                        Button btn = (Button) dialog.getWindow().findViewById(R.id.btn_ok);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                                dialog.close();
                                mySetResult(-1);
                            }
                        });
                    }
                } else {
                    LogUtils.d("other devices");
                }

            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

                setProgressBarIndeterminate(false);
                LogUtils.d(nxBlueFlag1 + "scan finish" + list.size());
                if (list.size() == 0) {

                    uber.setVisibility(View.INVISIBLE);
                    LogUtils.d("is devices");
                    if (!nxBlueFlag1) {//非背夹设备
                        nxBlueFlag1 = false;
                        if (!nxBlueFlag1) {
                            final DialogAlert dialog = ShowDialog(context,
                                    "未能找到背夹设备！\n或背夹设备已与其他终端连接,请先断开，再去操作！");
                            Button btn = (Button) dialog.getWindow().findViewById(R.id.btn_ok);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    dialog.close();
                                    mySetResult(-1);
                                }
                            });
                        }
                    }
                }
            } else {
                LogUtils.d("Other");
            }
        }
    };

    /*private void connBle(int deviceId, IDCheck idCheck, Handler handler) {

        BlueUtils.ConnectDevice(deviceId, IDCheck.this, handler);
    }*/

    /*private void readCert(int deviceId, IDCheck idCheck, Handler handler, HashMap<String, String> hashMap, HashMap<String, Bitmap> bitMap) {

        BlueUtils.IdCardInfoGet(deviceId, IDCheck.this, handler, hashMap, bitMap);
    }*/


    /**
     * @param index -1   关闭当前页 不关蓝牙
     *              -2   关闭当前页 关蓝牙
     *              1    读卡成功 返回
     */
    private void mySetResult(int index) {
        //disconn(IDCheck.this, handlerState);
        Intent intent = new Intent();
        uber.setVisibility(View.INVISIBLE);
        switch (index) {
            case READ_CERT://读身份证
                intent.putExtra("cert_name", certName);
                intent.putExtra("cert_id", certId);
            intent.putExtra("cert_sex", sex);
            intent.putExtra("cert_begin", beginDate);
            intent.putExtra("cert_end", endDate);
            intent.putExtra("cert_address", address);
            break;
            case READ_CARD://读银行卡
                intent.putExtra("card_id", cardId);
                break;
            case READ_PASSWORD://读密码
                intent.putExtra("card_pwd", cardPwd);
                break;
            case READ_PASSWORD2://读密码
                intent.putExtra("card_pwd", cardPwd);
                break;
            case READ_MAGCARD://读磁条卡
                intent.putExtra("mag_card_id", magCardId);
                break;
            case READ_FINGER://读指纹
                intent.putExtra("finger", finger);
                break;
            case -1://
                intent.putExtra("err_code", errCode);
                break;
            case -2://
                intent.putExtra("errcode", errCode);
                break;
            default:
                break;
        }
//        if (mBlueToothAdapter.enable()) {
//            mBlueToothAdapter.disable();//关闭设备连接
//        }
        setResult(index, intent);
        finish();
    }

    private BroadcastReceiver pReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            BluetoothDevice btDevice = mBlueToothAdapter.getRemoteDevice(tempAddress);
            try {
                ClsUtils.setPin(btDevice.getClass(), btDevice, tempPin);

                //取消用户输入
//                ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    /**
     * 获取设备适配器中已配对的设备
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void getBondedDevices() {

        Set<BluetoothDevice> devices = mBlueToothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : devices) {

                String temp = bluetoothDevice.getName();
                if (temp.equals(deviceName)) {
                    LogUtils.i("---广播2---" + temp + "\nsearchWoqi:" + searchWoqi +
                            "\tsearchGuoguang:" + searchGuoguang + "\tsearchShensi:" + searchShensi);
                    if (temp.contains("P3520") && searchWoqi == 1) {
                        bondedWoqi = 1;
                        //已配对，不需要重新连接
                        neededConWoqi = 1;
                        toast("握奇设备已配对2！");
                        setButtonEnable();
                    } else if (temp.contains("GEIT") && searchGuoguang == 1) {
                        bondedGuoguang = 1;
                        //已配对，不需要重新连接
                        neededConGuoguang = 1;
                        toast("国光设备已配对2！");
                        setButtonEnable();
                    } else if (temp.contains("SS-") && searchShensi == 1) {
                        bondedShensi = 1;
                        //已配对，不需要重新连接
                        neededConShensi = 1;
                        toast("神思设备已配对2！");
                        setButtonEnable();
                    }
                }
            }
        }
    }

    private String certName, certId, sex, beginDate, endDate,address, cardId, cardPwd,cardOldPwd,
            magCardId, finger;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            LogUtils.d("---msg.what:" + msg.what);
            if (msg.what == 0) {
                //toast(deviceName + "设备连接成功");
                //SystemClock.sleep(500);
                LogUtils.d("传过来的参数：" + getIntent().getIntExtra(FUNC_NAME, 0));

                if (ParamUtils.deviceNum.isEmpty()) {
//                        调用设备模块获取设备号
                   // BlueUtils.GetDeviceId(deviceId, IDCheck.this, handler, hashMap);
                }
                pre.putInt("deviceId",deviceId);
                pre.putString("whetherConnected","yes");
                readInfo();
            } else if (msg.what == 1) {
                errCode = "设备连接失败，错误码:"+hashMap.get("errorMsg");
                toast(errCode);
                //                final DialogAlert dialog = ShowDialog(context, deviceName + "设备连接失败！");
//
//                Button btn = (Button) dialog.getWindow().findViewById(R.id.btn_ok);
//                btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        dialog.close();
//                        mySetResult(-2);
//                    }
//                });
                pre.putString("whetherConnected","no");
                mySetResult(-2);
            } else if (msg.what == 2) {
                toast("证件信息获取成功");
                certName = hashMap.get("name");
                certId = hashMap.get("idNo");
                sex = hashMap.get("sex");
                beginDate = hashMap.get("beginDate");
                endDate = hashMap.get("endDate");
                address = hashMap.get("idAddr");
                LogUtils.d("name:" + hashMap.get("name") + "\tidNo:" + hashMap.get("idNo") + "\taddr:" + hashMap.get("idAddr"));
                mySetResult(6);
            } else if (msg.what == 3) {
                errCode = "证件信息获取失败，错误码:"+hashMap.get("errorMsg");
                toast(errCode);
                mySetResult(-1);
            } else if (msg.what == 4) {
                toast("IC卡信息获取成功");
                cardId = hashMap.get("cardNo");
//                IDCardNo.setText(hashMap.get("cardNo"));
                mySetResult(1);
            } else if (msg.what == 5) {
                errCode = "IC卡信息获取失败，错误码:"+hashMap.get("errorMsg");
                toast(errCode);
                mySetResult(-1);
            } else if (msg.what == 6) {
                toast("磁条卡信息获取成功");
                magCardId = hashMap.get("cardNo");
//                IDCardNo.setText(hashMap.get("cardNo"));
                mySetResult(3);
            } else if (msg.what == 7) {
                errCode = "磁条卡信息获取失败，错误码:"+hashMap.get("errorMsg");
                toast(errCode);
                mySetResult(-1);
            } else if (msg.what == 8) {
                toast("密码信息获取成功");
                cardPwd = hashMap.get("password");
//                passwordText.setText(hashMap.get("password"));
                if (READ_PASSWORD2 == getIntent().getIntExtra(FUNC_NAME, 0)) {
                    cardPwd = hashMap.get("password");
                    mySetResult(5);
                } else if (READ_PASSWORD == getIntent().getIntExtra(FUNC_NAME, 0)) {
                    cardPwd = hashMap.get("password");
                    mySetResult(2);
                }
            } else if (msg.what == 9) {
                errCode = "密码信息获取失败，错误码:"+hashMap.get("errorMsg");
                toast(errCode);
                mySetResult(-1);
            } else if (msg.what == 10) {
             //   toast("指纹信息获取成功");
                finger = hashMap.get("fingerKey");
             //   toast(hashMap.get("fingerKey"));
                mySetResult(4);
            } else if (msg.what == 11) {
                errCode ="指纹信息获取失败，错误码:"+hashMap.get("errorMsg") ;
                toast(errCode);
                mySetResult(-1);
//                BlueUtils.FingerInfoGet(deviceId, IDCheck.this, handler, hashMap);
            } else if (msg.what == 12) {//获取设备信息相关操作
                toast("设备序列号获取成功");
                if (deviceId == 3){//握奇设备试点  设置设备号
                    ParamUtils.deviceNum = hashMap.get("deviceId");
                    LogUtils.d("hut","获取设备ID="+ ParamUtils.deviceNum);
                   // ParamUtils.deviceNum = "1200300000";
                }else {
                    ParamUtils.deviceNum = hashMap.get("deviceId");
                }
            } else if (msg.what == 13) {
                errCode ="设备序列号获取失败，错误码:"+hashMap.get("errorMsg") ;
                toast("设备序列号获取失败");
            }
        }
    };
    private void readInfo() {
        LogUtils.i("======设备号：" + ParamUtils.deviceNum + "======卡号：" + ParamUtils.cardNum);
        switch (getIntent().getIntExtra(FUNC_NAME, 0)) {
            case READ_CERT://读身份证
                BlueUtils.IdCardInfoGet(deviceId, IDCheck.this, handler, hashMap, bitMap);
                break;
            case READ_CARD://读银行卡
                BlueUtils.IcCardInfoGet(deviceId, IDCheck.this, handler, hashMap);
                break;
            case READ_PASSWORD://读密码
                BlueUtils.PasswordInfoGet(deviceId, IDCheck.this, handler, hashMap, ParamUtils.cardNum);
                break;
            case READ_MAGCARD://读磁条卡
                BlueUtils.MagCardInfoGet(deviceId, IDCheck.this, handler, hashMap);
                break;
            case READ_FINGER://读指纹
                BlueUtils.FingerInfoGet(deviceId, IDCheck.this, handler, hashMap);
                break;
            case READ_PASSWORD2://读密码
                BlueUtils.PasswordInfoGet(deviceId, IDCheck.this, handler, hashMap, ParamUtils.cardNum);
                break;
        }
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
//            unregisterReceiver(mReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
//        unregisterReceiver(pReceiver);
//        if (mBlueToothAdapter.enable()) {
//            mBlueToothAdapter.disable();//关闭设备连接
//        }
    }

    iMateInfo imf;

    public void disconn(Context context, final Handler handler) {
        imf = new iMateInfo(context, iMateInfo.Operate.DISCON_BLUETOOTH, new iMateInfo.iMateCallback() {
            @Override
            public void onSucceed(Bundle bundle) {
                String state = bundle.getString(iMateInfo.MESSAGE);
                toast("" + state);
                Message msg1 = new Message();
                msg1.what = 0;
                handler.sendMessage(msg1);
            }

            @Override
            public void onError(Bundle bundle) {
                toast("error");
                Message msg1 = new Message();
                msg1.what = 1;
                handler.sendMessage(msg1);
            }
        });
        imf.startImate();
    }

    @OnClick(R.id.tv_exit)
    public void onViewClicked() {

        errCode = "信息获取失败";
        mySetResult(-1);
    }
}
