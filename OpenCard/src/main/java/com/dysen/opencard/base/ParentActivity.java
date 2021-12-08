package com.dysen.opencard.base;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.commom_library.R;
import com.dysen.commom_library.utils.DialogAlert;
import com.dysen.commom_library.utils.FormatUtil;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.commom_library.views.StatusBarUtil;
import com.dysen.opencard.common.ConstantValue;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.http.SocketThread;
import com.dysen.socket_library.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import q.rorbin.badgeview.QBadgeView;


public class ParentActivity extends FragmentActivity {

    public static Context context;
    public static Activity aty;
    private static DialogAlert SignOutdialog;
    protected int curPage = 1;
    private static final int REQUEST_CODE = 0; // 请求码// 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_APN_SETTINGS
    };

    protected BluetoothAdapter mBlueToothAdapter;
    private BluetoothManager blueToothManager;
    public int mIndex;
    private String certTypeIndex;

    private List<Activity> activityList = new ArrayList<>();
    private final int REQUEST_ENABLE_BT = 0xa01;
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 0xb01;
    private static Timer mTimer;//每个1秒执行
    private static MyTimerTask mTimerTask;//计时任务
    private static List<String> transCodeList;
    private long time = 1000 *1;
    private static String  Tag="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aty = this ;
        context = getApplicationContext();
        StatusBarUtil.setColor(this, Color.parseColor("#5f94d8"), 0);
//        StatusBarUtil.setColor(this, Color.parseColor("#ea452f"), 0);
//		StatusBarUtil.setColorDiff(this, Color.parseColor("#ea452f"));

        blueToothManager=(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBlueToothAdapter= blueToothManager.getAdapter();
        }else {
            mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        // 登录成功，开始计时
        Tag=aty.getClass().getSimpleName();
        startTimer();
      //  requestPermissions();
    }
    public String stadus = "";
    public Object obj = null;
    private Handler  hander=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
        }
    };


    // 初始化蓝牙设备
    private void init() {
        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 检查设备是否支持蓝牙设备
        if (mBlueToothAdapter == null) {
            LogUtils.d("设备不支持蓝牙");

            // 不支持蓝牙，退出。
            return;
        }

        // 如果用户的设备没有开启蓝牙，则弹出开启蓝牙设备的对话框，让用户开启蓝牙
        if (!mBlueToothAdapter.isEnabled()) {
            LogUtils.d("请求用户打开蓝牙");
//            mBlueToothAdapter.enable();

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            // 接下去，在onActivityResult回调判断
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                LogUtils.d( "打开蓝牙成功！");
            }

            if (resultCode == RESULT_CANCELED) {
                LogUtils.d( "放弃打开蓝牙！");
            }

        } else {
            LogUtils.d( "蓝牙异常！");
        }
    }

    // 启动蓝牙发现...
    protected void discovery() {
        if (mBlueToothAdapter == null) {
            init();
        }

        mBlueToothAdapter.startDiscovery();
    }

    // 可选方法，非必需
    // 此方法使自身的蓝牙设备可以被其他蓝牙设备扫描到，
    // 注意时间阈值。0 - 3600 秒。
    // 通常设置时间为120秒。
    private void enable_discovery() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        // 第二个参数可设置的范围是0~3600秒，在此时间区间（窗口期）内可被发现
        // 任何不在此区间的值都将被自动设置成120秒。
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);

        startActivity(discoverableIntent);
    }

    public static void backActivity(final Activity activity, LinearLayout layBack) {
        if (layBack != null) {
            layBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    activity.finish();
                }
            });
        }
    }

    public void backActivity(final Activity activity, LinearLayout layBack, final int returnCode) {
        if (layBack != null) {
            layBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setResult(returnCode, new Intent());
                    activity.finish();
                }
            });
        }
    }

    /**
     * 调整窗口的透明度
     * @param from>=0&&from<=1.0f
     * @param to>=0&&to<=1.0f
     *
     * */
    protected void dimBackground(final float from, final float to) {
        final Window window = getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });

        valueAnimator.start();
    }

    protected String commonSpinner(final Activity aty, final String[] type, final Spinner spinner) {
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

                certTypeIndex = type[pos];
                toast(type[pos]+""+pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        return certTypeIndex;
    }

    public static class MyUtils {

        public static void setSelItemColor(CardView view, @ColorInt int colorId) {

//		LogUtils.d("colorId:"+colorId);
            if (oldView == null) {
                //第一次点击
                oldView = view;
                oldColor = view.getDrawingCacheBackgroundColor();//当前 iew 的颜色
                view.setCardBackgroundColor(colorId);

            } else {
                //非第一次点击
                //把上一次点击的 变化
                oldView.setBackgroundResource(oldColor);
                view.setCardBackgroundColor(Color.TRANSPARENT);

                oldView = view;
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
//		StatusBarUtil.setTranslucentDiff(this);

    }

    protected void backActivity(View v) {
        finish();
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

    /**
     * 绑定 View
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T bindView(int id) {
        return (T) findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T bindView(View view, int id) {
        return (T) view.findViewById(id);
    }

    private static View oldView;
    private static TextView oldTextView;
    static int oldColor;

    /**
     * 设置item 被选中时的效果
     */
    public static void setSelectorItemColor(View view) {

//		LogUtils.d("colorId:"+colorId);
        if (oldView == null) {
            //第一次点击
            oldView = view;
            oldColor = view.getDrawingCacheBackgroundColor();//当前 iew 的颜色
            view.setBackgroundResource(R.color.common_yellow);
        } else {
            //非第一次点击
            //把上一次点击的 变化
            oldView.setBackgroundResource(oldColor);
            view.setBackgroundResource(R.color.common_yellow);
            oldView = view;
        }
    }

    /**
     * 设置item 被选中时的效果
     */
    public static void setSelectorItemColor(View view, @ColorRes @DrawableRes int colorId) {

//		LogUtils.d("colorId:"+colorId);
        if (oldView == null) {
            //第一次点击
            oldView = view;
            oldColor = view.getDrawingCacheBackgroundColor();//当前 iew 的颜色
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? R.color.common_yellow : colorId);
        } else {
            //非第一次点击
            //把上一次点击的 变化
            oldView.setBackgroundResource(oldColor);
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? R.color.common_yellow : colorId);
            oldView = view;
        }
    }

    public static void setSelectorItemColor(View view, @ColorRes @DrawableRes int colorId, @ColorRes @DrawableRes int oldColorId) {

//		LogUtils.d("colorId:"+colorId);
        if (oldView == null) {
            //第一次点击
            oldView = view;
            oldColor = oldColorId;//当前 iew 的颜色
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? R.color.common_yellow : colorId);
        } else {
            //非第一次点击
            //把上一次点击的 变化
            oldView.setBackgroundResource(oldColor);
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? R.color.common_yellow : colorId);
            oldView = view;
        }
    }

    private long lastTime = 0;

    protected boolean checkObjValid(Object obj) {
        if (obj != null)
            return true;
        else
            return false;
    }

    protected boolean checkListValid(List list) {
        if (list != null && list.size() > 0)
            return true;
        else
            return false;
    }

    /**
     * 页面跳转
     *
     * @param cls
     */
    public void gotoNextActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }

    /**
     * 页面跳转传参数
     *
     * @param cls
     * @param bundle
     */
    public void gotoNextActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Toast消息提示
     *
     * @param text
     */
    public void toast(CharSequence text) {
        // 2s内只提示一次
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime > 5000) {
            lastTime = currentTime;
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    private LinearLayoutManager mLayoutManager;

    protected RecyclerView setRecyclerView(RecyclerView recyclerView) {

        //设置固定大小
        recyclerView.setHasFixedSize(true);
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(this);
        //垂直方向
        mLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        //添加分割线
//		recyclerView.addItemDecoration(new TestDecoration(this));
        //给RecyclerView设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);

        return recyclerView;
    }

    protected RecyclerView setRecyclerView(RecyclerView recyclerView, int orientation) {

        //设置固定大小
        recyclerView.setHasFixedSize(true);
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(context);
        //垂直方向 OrientationHelper.HORIZONTAL 0 OrientationHelper.VERTICAL 1
        mLayoutManager.setOrientation(orientation);
        //添加分割线
//		recyclerView.addItemDecoration(new TestDecoration(this));
        //给RecyclerView设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);

        return recyclerView;
    }

    protected RecyclerView setRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager mLayoutManager) {

        //设置固定大小
        recyclerView.setHasFixedSize(true);
        //创建线性布局
//		mLayoutManager = new LinearLayoutManager(this);
        //垂直方向
//		mLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        //添加分割线
//		recyclerView.addItemDecoration(new TestDecoration(this));
        //给RecyclerView设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);

        return recyclerView;
    }

    /**
     * 提示框
     *
     * @param context
     * @param str
     * @return
     */
    public DialogAlert ShowDialog(Context context, String str) {
        DialogAlert dialog = new DialogAlert(context);
        dialog.show();
        dialog.setMsg(str);
//		dialog.setMsg("抱歉，查询条件不能为空！");

        return dialog;
    }

    public DialogAlert ShowDialog(Context context, View view) {
        DialogAlert dialog = new DialogAlert(context);
        dialog.setContentView(view);
        dialog.show();
//		dialog.setMsg("抱歉，查询条件不能为空！");

        return dialog;
    }

    /**
     * 显示/隐藏 View 设值
     *
     * @param view
     * @param text
     * @param bl
     */
    public void setTextHide(View view, String text, boolean bl) {
        TextView mTextView = null;
        Button btn = null;
        if (bl)
            view.setVisibility(View.INVISIBLE);
        else {
            view.setVisibility(View.VISIBLE);
            if (view instanceof TextView)
                mTextView.setText(text);
            else if (view instanceof Button)
                btn.setText(text);
        }
    }

    /**
     * 弹出软键盘
     */
    private void showKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    protected void mySetResult(int resultCode, Intent intent) {
        setResult(resultCode, intent);
        this.finish();
    }

    /**
     * 关闭软键盘
     */
    protected void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void clsOpenKeyboard(EditText editText, boolean flag){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!flag)
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//开启或者关闭软键盘
        else
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);//开启或者关闭软键盘
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);//弹出软键盘时，焦点定位在输
    }

    /**
     * 屏蔽一系列实体键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                return true;
//			 case KeyEvent.KEYCODE_BACK:
//			 return true;
            case KeyEvent.KEYCODE_CALL:
                return true;
            case KeyEvent.KEYCODE_SYM:
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                return true;
            case KeyEvent.KEYCODE_STAR:
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void setBadgeView(Context context, View view, String text) {

        QBadgeView badge = new QBadgeView(context);
        badge.bindTarget(view);
        badge.setBadgeTextSize(16, true);
        if (FormatUtil.isNumeric(text))
            badge.setBadgeNumber(Integer.parseInt(text));
        else
            badge.setBadgeText(text);
    }

    public void syncScroll(final RecyclerView leftList, final RecyclerView rightList) {
        leftList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    // note: scrollBy() not trigger OnScrollListener
                    // This is a known issue. It is caused by the fact that RecyclerView does not know how LayoutManager will handle the scroll or if it will handle it at all.
                    rightList.scrollBy(dx, dy);
                }
            }
        });

        rightList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    leftList.scrollBy(dx, dy);
                }
            }
        });
    }
    /**
     * 完完全全退出应用程序
     *  弹框
     */

    public void  addActivity(Activity activity){
        if(activityList==null){
            activityList=new ArrayList<Activity>();
        }
        activityList.add(activity);
    }

    /**
     * 直接退出
     */
    protected void exitApps(){
        if (activityList.size() > 0) {
            for (Activity activity : activityList) {
                LogUtils.i(activity.getClass().getName()+"========activityList========="+activityList.size());
                if (!activity.getClass().getName().equals("LoginActivity"))
                    activity.finish();
            }
//            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 完完全全退出应用程序
     *  弹框
     */

    public void exitApp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("提示");
        builder.setMessage("您确定要退出程序吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (activityList.size() > 0) {
                    for (Activity activity : activityList) {

                        activity.finish();
                    }

                    android.os.Process.killProcess(android.os.Process.myPid());

                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }


    /**
     * 请求权限
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,PERMISSIONS,REQUEST_CODE);
    }

    // 每当用户接触了屏幕，都会执行此方法
    public boolean dispatchTouchEvent(MotionEvent event) {
        ConstantValue.mLastActionTime = System.currentTimeMillis();
       // Log.e("hut", "当前触摸时间=="+Tag+"=="+ConstantValue.mLastActionTime);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                handler.removeCallbacks(runnable);
//            break;
//            case MotionEvent.ACTION_UP:
//                startTimer();
//                break;
//        }
        return super.dispatchTouchEvent(event);
    }


    public static  class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            long time=System.currentTimeMillis() - ConstantValue.mLastActionTime;
            if (time> ConstantValue.timeout) {
               // LogUtils.d("hut",  Tag+"相差间隔时间==="+time);
                // 退出登录
//                Looper.prepare();
//                exitDialog();
//                Looper.loop();
            }
        }
    }
    // 退出登录 add by hutian 20180507
    protected  static void exitDialog() {
       LogUtils.d("hut", "当前"+Tag+"弹出超时提示");
        if("MainActivity".equals(Tag)||"CreateCusIdActivity".equals(Tag)){

        String str="很抱歉，你已经操作超时，请退出重新登录！";
        SignOutdialog = new DialogAlert(aty);
        SignOutdialog.show();
        SignOutdialog.setMsg(str);

        Button btn = (Button) SignOutdialog.getWindow().findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送签退请求
                requestSigOut();
//                SignOutdialog.dismiss();
//                goToLoginActivity();

            }
        });
        }
    }
    public Object objectdata = null;
    public  static  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == -2) {
                ToastUtil.showShortToast(aty,"交易超时，请重试或检查网络！！！");
            }else if(msg.what==100){
                ToastUtil.showShortToast(aty,"签退成功");
                if(SignOutdialog!=null&&SignOutdialog.isShowing()){
                    SignOutdialog.close();
                }
                goToLoginActivity();

            }else if(msg.what == -3){
                ToastUtil.showShortToast(aty,"签退失败，请重试");
            }
        }
    };

    private static  void goToLoginActivity() {
//        Intent intent=new Intent(aty, LoginActivity.class);
//        intent.putExtra("tellerId",ParamUtils.tellerId);
//        intent.putExtra("orgId",ParamUtils.orgId);
//        intent.putExtra("terminalId",ParamUtils.terminalId);
//        aty.startActivity(intent);
        ConstantValue.SignOutTag=1;
        BaseApplication.exitApps();
    }

    //后台签退
    private  static  void requestSigOut() {
//        if(aty.getClass().getSimpleName().equals("LoginActivity")){
//            return;
//        }
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
        transCodeList.add(ParamUtils.terllerFinger);
        transCodeList.add("0");//0-指纹， 1-密码 暂时只支持指纹登录
        transCodeList.add("");
//        if(TextUtils.isEmpty(ParamUtils.terllerFinger)){
//            return;
//        }
        Log.e("hut", Tag+"执行签退交易===");
        SocketThread.transCode(aty, SocketThread.tempWithdrawal, transCodeList, handler);
    }
    private Runnable runnable = new Runnable() {
        @Override public void run() {
            long time=System.currentTimeMillis() - ConstantValue.mLastActionTime;
            if (time> ConstantValue.timeout) {
                Log.e("hut", "相差间隔时间==="+time);
                // 退出登录
//                Looper.prepare();
                exitDialog();
 //               Looper.loop();
            }
        }
    };

    // 登录成功，开始计时
    public  static   void startTimer() {
//        handler.removeCallbacks(runnable);
//        handler.postDelayed(runnable, time);
        mTimer = new Timer();
        mTimerTask = new MyTimerTask();
        // 初始化上次操作时间为登录成功的时间
        ConstantValue.mLastActionTime = System.currentTimeMillis();
        Log.e("hut", Tag+"计时开始。。。"+ConstantValue.mLastActionTime);
        // 每过1s检查一次
        mTimer.schedule(mTimerTask, 0, 1000);

    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    // 停止计时任务
    public void stopTimer() {
        if(mTimer!=null) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask.cancel();
        }
        Log.e("hut", "cancel timer");
    }
}
