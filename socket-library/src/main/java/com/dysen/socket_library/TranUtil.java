package com.dysen.socket_library;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.dysen.socket_library.data.GlobalData;
import com.dysen.socket_library.msg.MsgField;
import com.dysen.socket_library.request.RequestMsg;
import com.dysen.socket_library.request.RequestMsgBody;
import com.dysen.socket_library.request.RequestMsgHead;
import com.dysen.socket_library.request.RequestMsgHead2;
import com.dysen.socket_library.response.ResponseMsg;
import com.dysen.socket_library.response.ResponseMsgBody;
import com.dysen.socket_library.utils.DateUtils;
import com.dysen.socket_library.utils.FileUtils;
import com.dysen.socket_library.utils.LogUtils;
import com.dysen.socket_library.utils.ParamUtils;
import com.dysen.socket_library.utils.SocketManager;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dysen.socket_library.utils.LogUtils.d;

public class TranUtil {

    private static String sn;
    private static String preUUID = "";//当前生成的交易流水
    private static List<String> responsList;
    private static List<String> responsHeader;
    private static List<MsgField> subFieldList;
    private static String myFileName;
    private static RequestMsgHead head;
    private static RequestMsgHead2 head2;
    private static String respMsg;
    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    private ResponseMsgBody res;
    private static List<MsgField> resquestHeaderList;//发送报文头
    private static List<MsgField> responsHeaderList;//接收报文头
    private static String accountType="";
    private static String productType="";

    public static void setSn(String no) {
        sn = no;
    }


    public static RequestMsgHead packMsgHead(String tranCode, String authSpecSign, String
            authSeqNo, String authFlag) {

//    LogUtils.TAG = TranUtil.class.getName();
        LogUtils.i("hut",tranCode+"请求交易流水号============================generateUUID()=============" + generateUUID());

        head = new RequestMsgHead();

        head.RRSpecificNo.setValue(ParamUtils.sn);
        head.HB_Branch_Number.setValue(ParamUtils.orgId);//机构号
        head.HB_Workstation_Number.setValue(ParamUtils.terminalId);//终端号
        head.HB_Teller_Number.setValue(ParamUtils.tellerId);//柜员号
        head.BkPinNode.setValue(ParamUtils.pinNode);//加密节点号
        head.GDTA_SVCNAME.setValue(tranCode);//交易号
        head.HB_FLAG2.setValue("0");//内部标志2，默认为0，AuthErrorFlag为SUP且授权成功赋值为2
        head.HB_FLAG4.setValue("5");//渠道 默认为5
        head.HB_Supervisor_ID.setValue("");//核心授权柜员号码 AuthErrorFlag为SUP时且授权成功时赋授权柜员号
        head.HB_UUID_NO.setValue(generateUUID());
        preUUID = head.HB_UUID_NO.getValue();

        head.AuthSupervisor_ID.setValue("");//授权柜员号
        head.AuthSpeck.setValue("");//为空
        head.AuthAmtFlag.setValue("0");//现转标志 0-现金，1-转账
        head.AuthSpecSign.setValue(authSpecSign);//特殊授权标志 默认为0-需要检查授权  1-不检查授权
        head.AuthFlag.setValue(authFlag);//授权成功标志 授权成功时上送，默认-0，本地授权成功-1 集中授权成功-2
        head.AuthOthSeqNo.setValue("");//远程授权流水号 集中授权平台生成的授权流水号（第一次交易时为空，授权成功需上送流水号）
        head.AuthSeqNo.setValue(authSeqNo);//本地授权流水号 前置返回授权流水号（第一次交易时为空，授权成功需上送流水号）
        head.AuthAmt.setValue("0.00");//交易金额 默认为0.00
        //自助柜面终端标志位 默认值-centerm

        return head;
    }

    public static RequestMsgHead2 packMsgHead2(String tellerNo,String tranCode){
        head2=new RequestMsgHead2();

        head2.BILL_No.setValue("1.0");
        head2.BILL_Syscode.setValue("000001");
        head2.BkPinNode.setValue("");
        head2.BkTxCode.setValue(tranCode);
        head2.BILL_CodeName.setValue("");
        head2.AuthSeqNo.setValue("20180731001002003004");
        head2.BkSysDate.setValue("101010");
        head2.Bk10Date1.setValue("20180731");
        head2.BkDTime.setValue("20180731101010");
        head2.BkUuidNo.setValue("201807310001000200030004000511");
        head2.BkChnlNo.setValue("040301");
        head2.BILL_NoteBrchName.setValue(ParamUtils.orgId);
        head2.BkTellerNo.setValue(ParamUtils.tellerId);
        head2.BkAuthTeller.setValue(ParamUtils.tellerId);
        head2.BILL_CheckTeller.setValue(ParamUtils.tellerId);
        head2.BkChkNo.setValue("");
        head2.BILL_TxNo.setValue("");
        head2.Bk10Date2.setValue("");
        head2.BkOthSeq.setValue("");
        head2.BkNodeNo.setValue("");
        head2.TS_F_summry.setValue("");

        return head2;
    }

    private static String generateUUID() {
        long timemillis = System.currentTimeMillis();
        Date currentDate = new Date(timemillis);
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat serialNo = new SimpleDateFormat("MMddhhmmss");
        String uuid = "019999000099" + date.format(currentDate) + "00" + serialNo.format(currentDate);

        if (preUUID.equals(uuid)) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return generateUUID();
        }
        return uuid;
    }

    /**
     * 发送，接收报文
     *
     * @param tranCode
     * @param head
     * @param body
     * @param context
     * @param handler
     * @return
     */
    public static ResponseMsg startTran(String tranCode, RequestMsgHead head, RequestMsgBody body, Context context, Handler handler) {
        RequestMsg rm = new RequestMsg(head, body);
        SocketManager sm = new SocketManager();
        d("hut",SocketThread.getTransStr(tranCode) + "---------交易提交开始");
        d("hut","请求IP地址：==" + ParamUtils.serverIp);
        d("hut","请求IP端口：=="+         ParamUtils.serverPort);
        d("hut", SocketThread.getTransStr(tranCode) +"---------生成发送报文---------------");
        d("hut","GDTA_ITEMDATA_LENGTH"+"---"+rm.GDTA_ITEMDATA_LENGTH.getLength()+"---"+rm.GDTA_ITEMDATA_LENGTH.getValue());
        d("hut","TRAN_CODE"+"---"+rm.TRAN_CODE.getLength()+"---"+rm.TRAN_CODE.getValue());
        d("hut","GDTA_SVCNAME"+"---"+rm.GDTA_SVCNAME.getLength()+"---"+rm.GDTA_SVCNAME.getValue());
        resquestHeaderList=head.getHeadData();
        for(int i = 0; i < resquestHeaderList.size(); i++){
            d("hut", resquestHeaderList.get(i).getName() + "=========" + resquestHeaderList.get(i)
                    .getValue());
        }
        for (int i = 0; i < body.getBodyData().size(); i++) {

            d("hut",body.getBodyData().get(i).getName() + "=========" + body.getBodyData().get(i)
                    .getValue());
        }
        LogUtils.d("hut","HB_NEW_PWD16_1"+"---"+rm.HB_NEW_PWD16_1.getLength()+"---"+rm.HB_NEW_PWD16_1.getValue());
        d("hut","HB_NEW_PWD16_2"+"---"+rm.HB_NEW_PWD16_2.getLength()+"---"+rm.HB_NEW_PWD16_2.getValue());
        d("hut","HB_NEW_PWD16_3"+"---"+rm.HB_NEW_PWD16_3.getLength()+"---"+rm.HB_NEW_PWD16_3.getValue());
        d("hut","GDTA_MAC"+"---"+rm.GDTA_MAC.getLength()+"---"+rm.GDTA_MAC.getValue());
        byte[] retMsg;
        try {
            int timeout = 30;
//      retMsg = sm.transmitMsg("192.168.1.90",
//              Integer.valueOf("8088").intValue(), timeout * 10000, rm.getMsg());

            byte[] requestByte=rm.getMsg();
            String content="--------------------------------------本次交易开始--------------------------------"+"\n"+
                    "交易号:"+tranCode+"\n"+
                    "网点名称：="+ParamUtils.orgId+"\n"+
                    "柜员号：="+ParamUtils.tellerId+"\n"+
                    "柜员姓名：="+ParamUtils.tellerName+"\n"+
                    "交易名称：="+SocketThread.getTransStr(tranCode)+"\n"+
                    "账户类型：="+accountType+"\n"+
                    "卡产品类型：="+productType+"\n"+
                    "交易时间：="+ DateUtils.getDateYMDHMS()+"\n"+
                    "交易流水号：="+preUUID+"\n"+
                    "--------------------------------------本次交易结束--------------------------------"+"\n\n";

            FileUtils.writeTransLog( tranCode  +"_"+"requset", content+"生成请求报文"+"\n"+new String(requestByte, "gbk"));
            String str=RequestMsg.requesgbkStr;
            retMsg = sm.transmitMsg(ParamUtils.serverIp,
                    Integer.valueOf(ParamUtils.serverPort).intValue(), timeout * 1000, requestByte,str);
        } catch (UnknownHostException e) {
            handler.sendEmptyMessage(-101);
            e.printStackTrace();
            LogUtils.e(e+"SocketManager 传输报文异常1"+e.getMessage());
            //ToastUtil.showShortToast(context,"传输报文异常");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("SocketManager 传输报文异常2");

            return null;
        }
        if (retMsg == null) {
            LogUtils.e("SocketManager 返回报文为空");
           // ToastUtil.showShortToast(context,"返回报文为空");
            return null;
        }
        LogUtils.e("生成返回报文体");
        ResponseMsgBody rrmb;
        try {
            rrmb = new ResponseMsgBody(tranCode, context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LogUtils.e("ResponseMsgBody 解析报文异常1");
            return null;
        } catch (DocumentException e) {
            e.printStackTrace();
            LogUtils.e("ResponseMsgBody 解析报文异常2");
            return null;
        }
        try {
            LogUtils.d("hut","应答报文：=="+ byte2Hex(retMsg));
            FileUtils.writeTransLog( tranCode  +"_"+"response", "交易号:" + SocketThread.getTransStr(tranCode) +"\n"+"流水号：="+generateUUID()+"\n"+ "生成应答报文:"+"\n"+new String(retMsg, "gbk"));
            LogUtils.e("hut",retMsg.length+"生成返回报文=" + new String(retMsg, "gbk"));
            ParamUtils.respMsgByte = retMsg;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ResponseMsg result;
        try {
            result = new ResponseMsg(retMsg, rrmb);
            LogUtils.e("应答报文不为空");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("ResponseMsg 解析报文异常:" + e.getMessage());
            return null;
        }
        //ResponseMsg result;
        return result;
    }

    protected static String byte2Hex(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf.toString();
    }

    public static void getNotice(Context context, String transCode, List<String> list, Handler handler, Dialog dialog) {

       // dailog.show();
        //showLoadingDailog(context); 子线程不能调用
        responsList = new ArrayList<>();
        responsHeader = new ArrayList<>();
        subFieldList=new ArrayList<MsgField>();
        responsHeaderList=new ArrayList<MsgField>();
        if (transCode.equals(SocketThread.signIn)) {
            ParamUtils.tellerId = list.get(0);
            ParamUtils.orgId = list.get(2);
            ParamUtils.terminalId = list.get(3);
        }
        LogUtils.i("--------------authSpecSign--------"+ParamUtils.authSpecSign);
        RequestMsgHead head = packMsgHead(transCode, ParamUtils.authSpecSign, ParamUtils.authSeqNo, ParamUtils.authFlag);
        RequestMsgBody body = null;
        try {
            try {
                body = new RequestMsgBody(transCode, context);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            LogUtils.i(transCode + "交易----lis.size=" + list.size() + "\t\tlist=" + list);

            if (transCode.equals(SocketThread.signIn)) {
                if (list != null && list.size() == 7) {
                    body.getMsgField("HB_tellerNo1").setValue(list.get(0));
                    body.getMsgField("HB_Teller_PW").setValue(list.get(1));
                    body.getMsgField("HB_branch1").setValue(list.get(2));
                    body.getMsgField("HB_terminalId1").setValue(list.get(3));
                    body.getMsgField("FPI_tx_model").setValue(list.get(4));
                    body.getMsgField("BkCertType").setValue(list.get(5));
                    body.getMsgField("BkAuthTeller").setValue(list.get(6));
                }
            } else if (transCode.equals(SocketThread.signOut)) {
                if (list != null && list.size() > 0) {
                    body.getMsgField("").setValue(list.get(0));
                }
            } else if(transCode.equals(SocketThread.messageSign)){// 990011卡/账户短信签约
                    body.getMsgField("SMS_Credit_No1").setValue(list.get(0));
                    body.getMsgField("BkPwd").setValue(list.get(1));
                    body.getMsgField("SMS_signacct").setValue(list.get(2));
                    body.getMsgField("SMS_id_type").setValue(list.get(3));
                    body.getMsgField("SMS_id").setValue(list.get(4));
                    body.getMsgField("SMS_custname1").setValue(list.get(5));
                    body.getMsgField("SMS_Sign_Date").setValue(list.get(6));
                    body.getMsgField("SMS_Num").setValue(list.get(7));
                    body.getMsgField("SMS_sign_telphone").setValue(list.get(8));
                    body.getMsgField("SMS_sf_bz").setValue(list.get(9));
                    body.getMsgField("BkAmt").setValue(list.get(10));
                    body.getMsgField("BkAmt1").setValue(list.get(11));
                    body.getMsgField("BkFlg").setValue(list.get(12));

            } else if (transCode.equals(SocketThread.messageSignLimitPeriod)) {//991399凭卡号查询短信签约限免期限
                if (list != null && list.size() > 0) {
                    body.getMsgField("SMS_Credit_No1").setValue(list.get(0));
                }
            } else if (transCode.equals(SocketThread.tempWithdrawal)) {
                if (list != null && list.size() == 7) {
                    body.getMsgField("HB_tellerNo1").setValue(list.get(0));
                    body.getMsgField("HB_Teller_PW").setValue(list.get(1));
                    body.getMsgField("HB_branch1").setValue(list.get(2));
                    body.getMsgField("HB_terminalId1").setValue(list.get(3));
                    body.getMsgField("FPI_tx_model").setValue(list.get(4));
                    body.getMsgField("BkCertType").setValue(list.get(5));
                    body.getMsgField("BkAuthTeller").setValue(list.get(6));
                }
            } else if (transCode.equals(SocketThread.forcedWithdrawal)) {
                if (list != null && list.size() == 1) {
                    body.getMsgField("HB_DefInteger2").setValue(list.get(0));
                }
            } else if (transCode.equals(SocketThread.localAuthorization)) {
                if (list != null && list.size() == 5) {
                    body.getMsgField("AuthSupervisor_ID").setValue(list.get(0));
                    body.getMsgField("HB_Authorization_Branch").setValue(list.get(1));
                    body.getMsgField("HB_Authorization_Option").setValue(list.get(2));
                    body.getMsgField("BkFlag1").setValue(list.get(3));
                    body.getMsgField("FPI_tx_model").setValue(list.get(4));
                }
            } else if (transCode.equals(SocketThread.selectCus)) {
                if (list != null && list.size() == 3) {
                    body.getMsgField("HB_CERT_1").setValue(list.get(0));
                    body.getMsgField("HB_CertType1").setValue(list.get(1));
                    body.getMsgField("HB_GirdName").setValue(list.get(2));
                }
            } else if (transCode.equals(SocketThread.createCus)) {
                if (list != null && list.size() == 18) {
                    body.getMsgField("HB_CustType").setValue(list.get(0));
                    body.getMsgField("HB_CustSubtype").setValue(list.get(1));
                    body.getMsgField("HB_IDType").setValue(list.get(2));
                    body.getMsgField("HB_IDNum").setValue(list.get(3));
                    body.getMsgField("HB_LanguageCode").setValue(list.get(4));
                    body.getMsgField("HB_IDIssueDate").setValue(list.get(5));
                    body.getMsgField("HB_IDExpiryDate").setValue(list.get(6));
                    body.getMsgField("HB_LastName").setValue(list.get(7));
                    body.getMsgField("HB_Mobile").setValue(list.get(8));
                    body.getMsgField("HB_HomeAddr1").setValue(list.get(9));
                    body.getMsgField("HB_HomeAddr2").setValue(list.get(10));
                    body.getMsgField("HB_HomeAddr3").setValue(list.get(11));
                    body.getMsgField("HB_HomeAddr4").setValue(list.get(12));
                    body.getMsgField("HB_PostCode1").setValue(list.get(13));
                    body.getMsgField("HB_Nationality").setValue(list.get(14));
                    body.getMsgField("HB_SexCode").setValue(list.get(15));
                    body.getMsgField("HB_IsResident").setValue(list.get(16));
                    body.getMsgField("HB_OccupationCode").setValue(list.get(17));
                }
            } else if (transCode.equals(SocketThread.openCard)) {
                if (list != null && list.size() == 6) {
                    body.getMsgField("HB_CustNumber").setValue(list.get(0));
                    body.getMsgField("HB_Pan").setValue(list.get(1));
                    body.getMsgField("HB_Pwd").setValue(list.get(2));
                    body.getMsgField("HB_DefInteger1").setValue(list.get(3));
                    body.getMsgField("HB_DefInteger2").setValue(list.get(4));
                    body.getMsgField("HB_Flag").setValue(list.get(5));
                }
            } else if (transCode.equals(SocketThread.selectPwd)) {
                if (list != null && list.size() == 2) {
                    body.getMsgField("HB_Pan").setValue(list.get(0));
                    body.getMsgField("HB_Track2").setValue(list.get(1));
                }
            } else if (transCode.equals(SocketThread.changePwd)) {
                if (list != null && list.size() == 17) {
                    body.getMsgField("HB_CardNumber").setValue(list.get(0));
                    body.getMsgField("HB_CardDesc").setValue(list.get(1));
                    body.getMsgField("HB_customerNo").setValue(list.get(2));
                    body.getMsgField("HB_Name").setValue(list.get(3));
                    body.getMsgField("HB_Address1").setValue(list.get(4));
                    body.getMsgField("HB_Address2").setValue(list.get(5));
                    body.getMsgField("HB_Address3").setValue(list.get(6));
                    body.getMsgField("HB_Address4").setValue(list.get(7));
                    body.getMsgField("HB_postCode").setValue(list.get(8));
                    body.getMsgField("HB_cardName1").setValue(list.get(9));
                    body.getMsgField("HB_cardName2").setValue(list.get(10));
                    body.getMsgField("HB_cardExpiryDate").setValue(list.get(11));
                    body.getMsgField("HB_Bin").setValue(list.get(12));
                    body.getMsgField("HB_issuingIndex").setValue(list.get(13));
                    body.getMsgField("HB_type").setValue(list.get(14));
                    body.getMsgField("HB_oldPIN").setValue(list.get(15));
                    body.getMsgField("HB_firstPIN").setValue(list.get(16));
                }
            } else if (transCode.equals(SocketThread.cardType)) {
                if (list != null && list.size() == 1) {
                    body.getMsgField("HB_Pan").setValue(list.get(0));
                }
            }else if (transCode.equals(SocketThread.cardPwdSelect)) {
                if (list != null && list.size() == 2) {
                    body.getMsgField("HB_Pan").setValue(list.get(0));
                    body.getMsgField("HB_Track2").setValue(list.get(1));
                }
            }else if(transCode.equals(SocketThread.bankSignOpenSelect)){// N00100电子渠道已开通查询
                    body.getMsgField("BkType2").setValue(list.get(0));
                    body.getMsgField("BkCertNo").setValue(list.get(1));
                    body.getMsgField("BkName1").setValue(list.get(2));
            }else if(transCode.equals(SocketThread.bankSignInfoSearch)) { // N00130电子渠道个人信息查询
                    body.getMsgField("BkType2").setValue(list.get(0));
                    body.getMsgField("BkCertNo").setValue(list.get(1));
                    body.getMsgField("BkName1").setValue(list.get(2));
                }else if(transCode.equals(SocketThread.VerificationPassword)){//卡密码校验
                    body.getMsgField("HB_Pan").setValue(list.get(0));
                    body.getMsgField("HB_Action").setValue(list.get(1));
                    body.getMsgField("HB_Track2").setValue(list.get(2));
                    body.getMsgField("HB_ShowButton").setValue(list.get(3));
                    body.getMsgField("HB_PIN").setValue(list.get(4));
                    body.getMsgField("HB_CardFlag").setValue(list.get(5));
                    body.getMsgField("HB_TXN_SIGN").setValue(list.get(6));
                    body.getMsgField("HB_acc_type").setValue(list.get(7));
                    body.getMsgField("HB_AccoutSubType").setValue(list.get(8));
                    body.getMsgField("HB_IND").setValue(list.get(9));
                    body.getMsgField("HB_IC_CARD_CTR").setValue(list.get(10));
                    body.getMsgField("HB_DefaultString3").setValue(list.get(11));
                    body.getMsgField("HB_filler6").setValue(list.get(12));

            }else if(transCode.equals(SocketThread.bankNoSign)){ // N00110电子渠道未签约
                    body.getMsgField("BkName1").setValue(list.get(0));
                    body.getMsgField("BkType2").setValue(list.get(1));
                    body.getMsgField("BkCertNo").setValue(list.get(2));
                    body.getMsgField("HB_cutomerNo").setValue(list.get(3));
                    body.getMsgField("HB_EngName1").setValue(list.get(4));
                    body.getMsgField("HB_CustomerLevel").setValue(list.get(5));
                    body.getMsgField("BkFlag2").setValue(list.get(6));
                    body.getMsgField("BkDesc3").setValue(list.get(7));
                    body.getMsgField("BkFlag1").setValue(list.get(8));
                    body.getMsgField("BkDesc2").setValue(list.get(9));
                    body.getMsgField("HB_phoneno").setValue(list.get(10));
                    body.getMsgField("HB_Address").setValue(list.get(11));
                    body.getMsgField("HB_Addr1").setValue(list.get(12));
                    body.getMsgField("HB_PostCode1").setValue(list.get(13));
                    body.getMsgField("HB_ContactPhone").setValue(list.get(14));
                    body.getMsgField("HB_DefaultString2").setValue(list.get(15));
                    body.getMsgField("HB_DefaultString222").setValue(list.get(16));
                    body.getMsgField("BkTeller").setValue(list.get(17));
                    body.getMsgField("BkAuthTeller").setValue(list.get(18));
                    body.getMsgField("BkBrchNo").setValue(list.get(19));
                    body.getMsgField("BkAcctNo").setValue(list.get(20));
                    body.getMsgField("BkAcctName").setValue(list.get(21));
                    body.getMsgField("BkListNo2").setValue(list.get(22));
                    body.getMsgField("BkName2").setValue(list.get(23));
                    body.getMsgField("BkCurrNo").setValue(list.get(24));
                    body.getMsgField("BkType1").setValue(list.get(25));
                    body.getMsgField("BkVchType").setValue(list.get(26));
                    body.getMsgField("BkAmt").setValue(list.get(27));
                    body.getMsgField("BkAmt1").setValue(list.get(28));
                    body.getMsgField("BkDesc4").setValue(list.get(29));
                    body.getMsgField("BkAmt3").setValue(list.get(30));
                    body.getMsgField("BkBreakAmt").setValue(list.get(31));
                    body.getMsgField("BkIntrAmt").setValue(list.get(32));
                    body.getMsgField("BkDesc5").setValue(list.get(33));
                    body.getMsgField("BkTaxAmt").setValue(list.get(34));

            }else  if(transCode.equals(SocketThread.bankSignAccountSearch)){ //  N00170电子渠道账户信息查询
                    body.getMsgField("BkType2").setValue(list.get(0));
                    body.getMsgField("BkCertNo").setValue(list.get(1));
                    body.getMsgField("BkName1").setValue(list.get(2));
            }else if(transCode.equals(SocketThread.dzqdwh)){//N00180 电子渠道维护
                    body.getMsgField("BkCustNo").setValue(list.get(0));
                    body.getMsgField("BkAcctNo").setValue(list.get(1));
                    body.getMsgField("BkType2").setValue(list.get(2));
                    body.getMsgField("BkType1").setValue(list.get(3));
                    body.getMsgField("BkVchType").setValue(list.get(4));
                    body.getMsgField("BkListNo2").setValue(list.get(5));
                    body.getMsgField("BkName2").setValue(list.get(6));
                    body.getMsgField("BkCurrNo").setValue(list.get(7));
                    body.getMsgField("BkBreakAmt").setValue(list.get(8));
                    body.getMsgField("BkIntrAmt").setValue(list.get(9));
                    body.getMsgField("BkTeller").setValue(list.get(10));
                    body.getMsgField("BkAuthTeller").setValue(list.get(11));
                    body.getMsgField("HB_cutomerNo").setValue(list.get(12));
                    body.getMsgField("BkDesc5").setValue(list.get(13));
                    body.getMsgField("BkTaxAmt").setValue(list.get(14));
                    body.getMsgField("BkAmt").setValue(list.get(15));
                    body.getMsgField("BkAmt1").setValue(list.get(16));
                    body.getMsgField("BkDesc4").setValue(list.get(17));
                    body.getMsgField("BkAmt3").setValue(list.get(18));
            }else if(transCode.equals(SocketThread.messageSend)){//发送短信
                    body.getMsgField("BkDesc1").setValue(list.get(0));
                    body.getMsgField("BkDesc2").setValue(list.get(1));
                    body.getMsgField("BkDetail1").setValue(list.get(2));
                    body.getMsgField("BkDesc3").setValue(list.get(3));
            }else if(transCode.equals(SocketThread.messageVerification)){//发送短信验证
                    body.getMsgField("BkDesc1").setValue(list.get(0));
                    body.getMsgField("BkPCode").setValue(list.get(1));
                    body.getMsgField("BkSerNo").setValue(list.get(2));
            }else if(transCode.equals(SocketThread.updateKey)){//获取更新秘钥
                    body.getMsgField("BkCertType").setValue(list.get(0));
                    body.getMsgField("BkChnlNo").setValue(list.get(1));
                    body.getMsgField("BkDetail1").setValue(list.get(2));
            }else if(transCode.equals(SocketThread.tellerInfoSearch)){//获取柜员详细信息
                    body.getMsgField("MNG_Tlrno").setValue(list.get(0));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        ResponseMsg result = null;
        try {
            result = startTran(transCode, head, body, context, handler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (result == null) {
            handler.sendEmptyMessage(-2);//"交易超时"
            //ToastUtil.showLongToast(context,"交易超时请检查网络");
        }else if(result.ERR_RET.getValue().contains(GlobalData.RET_SUCCESS)) {
            // FileUtils.writeTransLog(transCode +"_"+ "onSuccess", "交易号:" + transCode + "交易执行成功"+"\n");
            responsList.clear();
            //返回交易头数据
            responsHeader.add(result.responseMsgHead.AuthSeqNo.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthErrorType.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthErrorFlag.getFieldValue());
            responsHeader.add(result.responseMsgHead.BkSeqNo.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthCapability.getFieldValue());
            responsHeader.add(result.responseMsgHead.BkNum1.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthErrorCode.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthErrorMessage.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthSealMessage.getFieldValue());
            //优化 add by hutian 2018-03-16
            responsHeaderList = result.responseMsgHead.getHeadData();

            if (transCode.equals(SocketThread.signIn)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_tellerNo").getValue());
                ParamUtils.tellerName=result.responseMsgBody.getMsgField("HB_tellerNo").getValue();
            } else if (transCode.equals(SocketThread.signOut)) {
//                responsList.add(result.responseMsgBody.getMsgField("").getValue());
            } else if (transCode.equals(SocketThread.tempWithdrawal)) {
                //responsList.add("签退成功");
            } else if (transCode.equals(SocketThread.forcedWithdrawal)) {
                //responsList.add("签退成功");
            } else if (transCode.equals(SocketThread.localAuthorization)) {
//                responsList.add(result.responseMsgBody.getMsgField("").getValue());
            } else if (transCode.equals(SocketThread.selectCus)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_cust_no").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_GirdName").getValue());
            } else if (transCode.equals(SocketThread.createCus)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_CustNum").getValue());
            } else if (transCode.equals(SocketThread.openCard)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_accNo").getValue());
            } else if (transCode.equals(SocketThread.selectPwd)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_CardNumber").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_CardDesc").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_customerNo").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Name").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address3").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address4").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_postCode").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardName1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardName2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardExpiryDate").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Bin").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_issuingIndex").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_type").getValue());
            } else if (transCode.equals(SocketThread.cardType)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_cardProduct").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_CustName").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_linkAccount1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_IDNumber").getValue());
            } else if (transCode.equals(SocketThread.cardPwdSelect)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_CardNumber").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_CardDesc").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_customerNo").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Name").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address3").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address4").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_postCode").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardName1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardName2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardExpiryDate").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Bin").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_issuingIndex").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_type").getValue());
            } else if (transCode.equals(SocketThread.bankSignOpenSelect)) {// N00100电子渠道已开通查询
                responsList.add(result.responseMsgBody.getMsgField("BkCustNo").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkFlag1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkFlag2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkDesc2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkDesc3").getValue());
            } else if (transCode.equals(SocketThread.bankSignInfoSearch)) {//N00130电子渠道个人信息查询
                responsList.add(result.responseMsgBody.getMsgField("BkDesc4").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkName1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkType2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkCertNo").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_EngName1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_phoneno").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Addr1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_PostCode1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_ContactPhone").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkDesc2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkListNo1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkListNo2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkName2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkDesc3").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkListNo3").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkListNo4").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkName3").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkFlag1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkFlag2").getValue());
            } else if (transCode.equals(SocketThread.dzqdwh)) { //N00170电子渠道账户信息查询
                responsList.add(result.responseMsgBody.getMsgField("BkCustNo").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkName3").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkSSum").getValue());
                subFieldList = result.responseMsgBody.getMsgField("BkSSum").getSubField();//循环区域值
            } else if (transCode.equals(SocketThread.messageSend)) {//发送短信验证
                responsList.add(result.responseMsgBody.getMsgField("BkPCode").getValue());
            } else if (transCode.equals(SocketThread.VerificationPassword)) {//检验密码
                responsList.add("检验密码成功");
            } else if (transCode.equals(SocketThread.bankNoSign)) {//电子渠道签约返回
                responsList.add("电子银行签约交易执行成功");
                //responsList.add(result.responseMsgBody.getMsgField("BkDesc4").getValue());
            } else if (transCode.equals(SocketThread.messageSignLimitPeriod)) {//991399凭卡号查询短信签约限免期限
                responsList.add(result.responseMsgBody.getMsgField("HB_Duration").getValue());
            }else if(transCode.equals(SocketThread.updateKey)){//991059 工作密钥的获取、更新
                responsList.add(result.responseMsgBody.getMsgField("BkName1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkName2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("BkName3").getValue());
            }
            d("hut", SocketThread.getTransStr(transCode) + "交易执行成功");
            d("hut", transCode + "--------生成返回报文------------"+generateUUID());
            d("hut", "GDTA_ITEMDATA_LENGTH" + "---" + result.GDTA_ITEMDATA_LENGTH.getLength() + "---" + result.GDTA_ITEMDATA_LENGTH.getValue());
            d("hut", "TRAN_CODE" + "---" + result.TRAN_CODE.getLength() + "---" + result.TRAN_CODE.getValue());
            d("hut", "GDTA_SVCNAME" + "---" + result.GDTA_SVCNAME.getLength() + "---" + result.GDTA_SVCNAME.getValue());
            d("hut", "ERR_RET" + "---" + result.ERR_RET.getLength() + "---" + result.ERR_RET.getValue());
            d("hut", "ERR_MSG " + "---" + result.ERR_MSG.getLength() + "---" + result.ERR_MSG.getValue());
            for (int i = 0; i < responsHeaderList.size(); i++) {
                LogUtils.d("hut", responsHeaderList.get(i).getName() + "---" + responsHeaderList.get(i).getLength() + "---" + responsHeaderList.get(i).getValue());
            }
            if (responsList.size()>0&&result.responseMsgBody.getBodyData().size()>0) {
                for (int i = 0; i < responsList.size(); i++) {
                    LogUtils.i("hut", i + 1 + "-" + "---" + result.responseMsgBody.getBodyData().get(i).getName() + "---" + responsList.get(i));
                }
            }
                ParamUtils.responsHeader = responsHeader;
                ParamUtils.responsList = responsList;
                if(transCode.equals(SocketThread.bankSignAccountSearch)){
                   ParamUtils.subFieldList=subFieldList;
                }
                Message message = new Message();
                       message.obj = responsList;
                    message.what=100;
                handler.sendMessage(message);

            } else {
           //处理后台失败信息 add by hutian 20180226
                Message message = new Message();
//                message.obj = respMsg;
                message.obj = result.ERR_MSG.getValue();
                message.what=-3;
                handler.sendMessage(message);
//            FileUtils.writeTransLog( transCode  +"_"+"onFaile", "交易号:" + transCode + "交易执行失败"+"\n"+"流水号：="+generateUUID()+"\n"+result.ERR_MSG.getValue());
                LogUtils.e(transCode + "=错误信息="+result.ERR_MSG.getValue());
               // handler.sendEmptyMessage(-3);//result.ERR_MSG.getValue();
            }
       closeLoadingDailog(context,dialog);
        }

//    private static void showLoadingDailog(final Context context) {
//        if (dailog == null || !dailog.isShowing()) {
//                  final  LoadingDailog.Builder builder=new LoadingDailog.Builder(context);
//                    builder.setMessage("玩命加载中...");
//                    builder.setCancelable(false);
//                    dailog=builder.create();
//                    dailog.show();
//        }
//
//    }
    private static void closeLoadingDailog(Context context,Dialog dialog) {
        if(dialog!=null&& context!=null){
            dialog.dismiss();
            dialog=null;
        }
    }
  /*public static void main(String[] args){
      TranUtil util=new TranUtil();
	  util.getNotice("");
  }*/
}