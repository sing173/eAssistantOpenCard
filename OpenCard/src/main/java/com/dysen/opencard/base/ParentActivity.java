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
    private static final int REQUEST_CODE = 0; // ?????????// ?????????????????????
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
    private static Timer mTimer;//??????1?????????
    private static MyTimerTask mTimerTask;//????????????
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
        // ???????????????????????????
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


    // ?????????????????????
    private void init() {
        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();

        // ????????????????????????????????????
        if (mBlueToothAdapter == null) {
            LogUtils.d("?????????????????????");

            // ???????????????????????????
            return;
        }

        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (!mBlueToothAdapter.isEnabled()) {
            LogUtils.d("????????????????????????");
//            mBlueToothAdapter.enable();

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            // ???????????????onActivityResult????????????
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                LogUtils.d( "?????????????????????");
            }

            if (resultCode == RESULT_CANCELED) {
                LogUtils.d( "?????????????????????");
            }

        } else {
            LogUtils.d( "???????????????");
        }
    }

    // ??????????????????...
    protected void discovery() {
        if (mBlueToothAdapter == null) {
            init();
        }

        mBlueToothAdapter.startDiscovery();
    }

    // ????????????????????????
    // ????????????????????????????????????????????????????????????????????????
    // ?????????????????????0 - 3600 ??????
    // ?????????????????????120??????
    private void enable_discovery() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        // ????????????????????????????????????0~3600??????????????????????????????????????????????????????
        // ???????????????????????????????????????????????????120??????
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
     * ????????????????????????
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
                //???????????????
                oldView = view;
                oldColor = view.getDrawingCacheBackgroundColor();//?????? iew ?????????
                view.setCardBackgroundColor(colorId);

            } else {
                //??????????????????
                //????????????????????? ??????
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
     * ???????????? ?????????
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
            toast("??????????????????????????????");
            return true;
        } else {
            toast("?????????????????????????????????");
        }
        return false;
    }

    /**
     * ?????? View
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
     * ??????item ?????????????????????
     */
    public static void setSelectorItemColor(View view) {

//		LogUtils.d("colorId:"+colorId);
        if (oldView == null) {
            //???????????????
            oldView = view;
            oldColor = view.getDrawingCacheBackgroundColor();//?????? iew ?????????
            view.setBackgroundResource(R.color.common_yellow);
        } else {
            //??????????????????
            //????????????????????? ??????
            oldView.setBackgroundResource(oldColor);
            view.setBackgroundResource(R.color.common_yellow);
            oldView = view;
        }
    }

    /**
     * ??????item ?????????????????????
     */
    public static void setSelectorItemColor(View view, @ColorRes @DrawableRes int colorId) {

//		LogUtils.d("colorId:"+colorId);
        if (oldView == null) {
            //???????????????
            oldView = view;
            oldColor = view.getDrawingCacheBackgroundColor();//?????? iew ?????????
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? R.color.common_yellow : colorId);
        } else {
            //??????????????????
            //????????????????????? ??????
            oldView.setBackgroundResource(oldColor);
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? R.color.common_yellow : colorId);
            oldView = view;
        }
    }

    public static void setSelectorItemColor(View view, @ColorRes @DrawableRes int colorId, @ColorRes @DrawableRes int oldColorId) {

//		LogUtils.d("colorId:"+colorId);
        if (oldView == null) {
            //???????????????
            oldView = view;
            oldColor = oldColorId;//?????? iew ?????????
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? R.color.common_yellow : colorId);
        } else {
            //??????????????????
            //????????????????????? ??????
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
     * ????????????
     *
     * @param cls
     */
    public void gotoNextActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }

    /**
     * ?????????????????????
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
     * Toast????????????
     *
     * @param text
     */
    public void toast(CharSequence text) {
        // 2s??????????????????
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime > 5000) {
            lastTime = currentTime;
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    private LinearLayoutManager mLayoutManager;

    protected RecyclerView setRecyclerView(RecyclerView recyclerView) {

        //??????????????????
        recyclerView.setHasFixedSize(true);
        //??????????????????
        mLayoutManager = new LinearLayoutManager(this);
        //????????????
        mLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        //???????????????
//		recyclerView.addItemDecoration(new TestDecoration(this));
        //???RecyclerView?????????????????????
        recyclerView.setLayoutManager(mLayoutManager);

        return recyclerView;
    }

    protected RecyclerView setRecyclerView(RecyclerView recyclerView, int orientation) {

        //??????????????????
        recyclerView.setHasFixedSize(true);
        //??????????????????
        mLayoutManager = new LinearLayoutManager(context);
        //???????????? OrientationHelper.HORIZONTAL 0 OrientationHelper.VERTICAL 1
        mLayoutManager.setOrientation(orientation);
        //???????????????
//		recyclerView.addItemDecoration(new TestDecoration(this));
        //???RecyclerView?????????????????????
        recyclerView.setLayoutManager(mLayoutManager);

        return recyclerView;
    }

    protected RecyclerView setRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager mLayoutManager) {

        //??????????????????
        recyclerView.setHasFixedSize(true);
        //??????????????????
//		mLayoutManager = new LinearLayoutManager(this);
        //????????????
//		mLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        //???????????????
//		recyclerView.addItemDecoration(new TestDecoration(this));
        //???RecyclerView?????????????????????
        recyclerView.setLayoutManager(mLayoutManager);

        return recyclerView;
    }

    /**
     * ?????????
     *
     * @param context
     * @param str
     * @return
     */
    public DialogAlert ShowDialog(Context context, String str) {
        DialogAlert dialog = new DialogAlert(context);
        dialog.show();
        dialog.setMsg(str);
//		dialog.setMsg("????????????????????????????????????");

        return dialog;
    }

    public DialogAlert ShowDialog(Context context, View view) {
        DialogAlert dialog = new DialogAlert(context);
        dialog.setContentView(view);
        dialog.show();
//		dialog.setMsg("????????????????????????????????????");

        return dialog;
    }

    /**
     * ??????/?????? View ??????
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
     * ???????????????
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
     * ???????????????
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
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//???????????????????????????
        else
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);//???????????????????????????
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);//???????????????????????????????????????
    }

    /**
     * ????????????????????????
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
     * ??????????????????????????????
     *  ??????
     */

    public void  addActivity(Activity activity){
        if(activityList==null){
            activityList=new ArrayList<Activity>();
        }
        activityList.add(activity);
    }

    /**
     * ????????????
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
     * ??????????????????????????????
     *  ??????
     */

    public void exitApp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("??????");
        builder.setMessage("????????????????????????????");
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {

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
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }


    /**
     * ????????????
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,PERMISSIONS,REQUEST_CODE);
    }

    // ???????????????????????????????????????????????????
    public boolean dispatchTouchEvent(MotionEvent event) {
        ConstantValue.mLastActionTime = System.currentTimeMillis();
       // Log.e("hut", "??????????????????=="+Tag+"=="+ConstantValue.mLastActionTime);
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
               // LogUtils.d("hut",  Tag+"??????????????????==="+time);
                // ????????????
//                Looper.prepare();
//                exitDialog();
//                Looper.loop();
            }
        }
    }
    // ???????????? add by hutian 20180507
    protected  static void exitDialog() {
       LogUtils.d("hut", "??????"+Tag+"??????????????????");
        if("MainActivity".equals(Tag)||"CreateCusIdActivity".equals(Tag)){

        String str="????????????????????????????????????????????????????????????";
        SignOutdialog = new DialogAlert(aty);
        SignOutdialog.show();
        SignOutdialog.setMsg(str);

        Button btn = (Button) SignOutdialog.getWindow().findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //??????????????????
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
                ToastUtil.showShortToast(aty,"????????????????????????????????????????????????");
            }else if(msg.what==100){
                ToastUtil.showShortToast(aty,"????????????");
                if(SignOutdialog!=null&&SignOutdialog.isShowing()){
                    SignOutdialog.close();
                }
                goToLoginActivity();

            }else if(msg.what == -3){
                ToastUtil.showShortToast(aty,"????????????????????????");
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

    //????????????
    private  static  void requestSigOut() {
//        if(aty.getClass().getSimpleName().equals("LoginActivity")){
//            return;
//        }
        transCodeList = new ArrayList<>();

//            HB_tellerNo1 ?????????
//            HB_Teller_PW ??????
//            HB_branch1 ?????????
//            HB_terminalId1 ?????????
//            FPI_tx_model ???????????????
//            BkCertType ????????????
//            BkAuthTeller ????????????
        transCodeList.add(ParamUtils.tellerId);
        transCodeList.add("");
        transCodeList.add(ParamUtils.orgId);
        transCodeList.add(ParamUtils.terminalId);
//        transCodeList.add(finger);
        transCodeList.add(ParamUtils.terllerFinger);
        transCodeList.add("0");//0-????????? 1-?????? ???????????????????????????
        transCodeList.add("");
//        if(TextUtils.isEmpty(ParamUtils.terllerFinger)){
//            return;
//        }
        Log.e("hut", Tag+"??????????????????===");
        SocketThread.transCode(aty, SocketThread.tempWithdrawal, transCodeList, handler);
    }
    private Runnable runnable = new Runnable() {
        @Override public void run() {
            long time=System.currentTimeMillis() - ConstantValue.mLastActionTime;
            if (time> ConstantValue.timeout) {
                Log.e("hut", "??????????????????==="+time);
                // ????????????
//                Looper.prepare();
                exitDialog();
 //               Looper.loop();
            }
        }
    };

    // ???????????????????????????
    public  static   void startTimer() {
//        handler.removeCallbacks(runnable);
//        handler.postDelayed(runnable, time);
        mTimer = new Timer();
        mTimerTask = new MyTimerTask();
        // ???????????????????????????????????????????????????
        ConstantValue.mLastActionTime = System.currentTimeMillis();
        Log.e("hut", Tag+"?????????????????????"+ConstantValue.mLastActionTime);
        // ??????1s????????????
        mTimer.schedule(mTimerTask, 0, 1000);

    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    // ??????????????????
    public void stopTimer() {
        if(mTimer!=null) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask.cancel();
        }
        Log.e("hut", "cancel timer");
    }
}
