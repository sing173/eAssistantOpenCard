package com.dysen.socket_library;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.dysen.socket_library.customview.LoadingDailog;
import com.dysen.socket_library.utils.ParamUtils;

import java.util.List;

/**
 * Created by dysen on 1/19/2018.
 */

public class SocketThread extends Thread {

    public static String signIn = "991052";//991052柜员签到交易
    public static String signOut = "991053";//991053柜员签退交易
    public static String tempWithdrawal = "991055";//991055柜员临时签退交易
    public static String forcedWithdrawal = "991056";//991056柜员强制签退交易
    public static String localAuthorization = "991034";//991034本地授权交易
    public static String selectCus = "997350";//997350根据证件号及户名查询客户信息
    public static String createCus = "992001";//992001个人客户信息基本创建
    public static String openCard = "997204";//997204客户开卡
    public static String selectPwd = "997340";//997340卡密码查询
    public static String changePwd = "997341";//997341卡密码修改
    public static String cardType = "997440";//997440卡类型查询
    public static String cardPwdSelect = "997340";//997340卡密码查询
    public static String bankSignOpenSelect = "N00100";// 电子银行签约开通查询
    public static String bankNoSign = "N00110";//N00100 (未签约)电子银行签约
    public static String bankSignInfoSearch = "N00130";//N00130  电子渠道签约个人信息查询
    public static String bankSignAccountSearch = "N00170";//N00170  电子渠道账户信息查询
    public static String dzqdwh =  "N00180";//  N00180电子渠道账户维护
    public static String dzqdzx =  "N00150";//N00150 电子渠道注销
    public static String messageSend =  "640002";//  640002 验证短信发送
    public static String messageVerification =  "640003";//640003 短信验证
    public static String qdzx =  "N00150";//N00150 电子渠道注销
    public static String messageSign = "990011";//990011卡/账户短信签约
    public static String messageSignLimitPeriod =  "991399";//991399凭卡号查询短信签约限免期限
    public static String VerificationPassword =  "B37445";//B37445卡密码校验
    public static String updateKey =  "991059";//991059获取更新秘钥
    public static String tellerInfoSearch="311070";//柜员详细信息查询

    private static LoadingDailog dailog;//自定义Dailog
    private static String[] transStr = new String[]{
              "991052 柜员签到交易"
            , "991053 柜员签退交易"
            , "991055 柜员临时签退交易"
            , "991056 柜员强制签退交易"
            , "991034 本地授权交易"
            , "997350 根据证件号及户名查询客户信息"
            , "992001 个人客户信息基本创建"
            , "997204 客户开卡"
            , "997340 卡密码查询"
            , "997341 卡密码修改"
            , "997440 卡类型查询"
            , "997340 卡密码查询"
            , "N00100 电子银行签约开通查询"
            , "N00110 电子银行签约"
            , "N00130 电子渠道签约个人查询"
            , "N00170 电子渠道账户信息查询"
            , "N00180 电子渠道账户维护"
            , "640002 验证短信发送"
            , "640003 短信验证"
            , "B37445 卡密码校验"
            , "N00150 电子渠道注销"
            , "990011 卡/账户短信签约"
            , "991399 凭卡号查询短信签约限免期限"
            , "991059 下载更新秘钥"
            , "311070 柜员详细信息查询"};

    public static String getTransStr(String transCode){
        String result="";
        for (int i=0;i<transStr.length;i++){
            if (transStr[i].contains(transCode)){
                result=transStr[i];
            }
        }
        return result;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    //更新UI操作
//                    dailog=(LoadingDailog)msg.obj;
//                    if(dailog!=null||dailog.isShowing()){
//                        dailog.dismiss();
//                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 交易接口
     *
     * @param context
     * @param transCodeList
     */
    public static void transCode(final Context context, final String transCode, final
    List<String> transCodeList, final Handler
                                         handler) {
        //主线程才能调Dailog
        showLoadingDailog(context);
        new Thread() {
            @Override
            public void run() {
                super.run();
                 Looper.prepare();
                TranUtil.getNotice(context, transCode, transCodeList, handler, dailog);
            }
        }.start();

    }

    public static void setSn(String androidId) {
        TranUtil.setSn(androidId);
        ParamUtils.sn = androidId;
        ParamUtils.pinNode = androidId;
    }

    public static void setPackMsgHead(String tranCode, String authSpecSign, String authSeqNo, String authFlag) {
//          TranUtil.packMsgHead(tranCode, authFlag, authSeqNo);
        ParamUtils.authSpecSign = authSpecSign;
        ParamUtils.authSeqNo = authSeqNo;
        ParamUtils.authFlag = authFlag;
    }

    private static void showLoadingDailog(final Context context) {
        final LoadingDailog.Builder builder = new LoadingDailog.Builder(context);
        builder.setMessage("玩命加载中...");
        builder.setCancelable(false);
        dailog = builder.create();
        dailog.show();

    }

    private static void closeLoadingDailog(Context context, Dialog dialog) {
        if (dailog != null || dailog.isShowing()) {
            dailog.dismiss();
        }
    }
}
