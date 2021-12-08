package com.dysen.socket_library.request;

import com.dysen.socket_library.msg.MsgField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/2.
 */

public class RequestMsgHead2 {
    //系统版本号
    public MsgField BILL_No;
    //请求方系统代码
    public MsgField BILL_Syscode;
    //请求方安全节点号
    public MsgField BkPinNode;
    //交易码
    public MsgField BkTxCode;
    //公共交易名称
    public MsgField BILL_CodeName;
    //全渠道流水号
    public MsgField AuthSeqNo;
    //全渠道时间
    public MsgField BkSysDate;
    //请求方交易日期
    public MsgField Bk10Date1;
    //请求方交易时间戳
    public MsgField BkDTime;
    //请求方流水号
    public MsgField BkUuidNo;
    //渠道号
    public MsgField BkChnlNo;
    //机构号
    public MsgField BILL_NoteBrchName;
    //柜员号
    public MsgField BkTellerNo;
    //授权柜员
    public MsgField BkAuthTeller;
    //复核柜员号
    public MsgField BILL_CheckTeller;
    //对帐分类编号
    public MsgField BkChkNo;
    //尾箱号
    public MsgField BILL_TxNo;
    //需冲正的原交易日期
    public MsgField Bk10Date2;
    //需冲正的原业务流水号
    public MsgField BkOthSeq;
    //终端设备号
    public MsgField BkNodeNo;
    //报文MAC值
    public MsgField TS_F_summry;

    public RequestMsgHead2(){
        this.BILL_No = new MsgField(null,6,3,"BILL_No");
        this.BILL_Syscode = new MsgField(null,12,3,"BILL_Syscode");
        this.BkPinNode=new MsgField(null,20,3,"BkPinNode");
        this.BkTxCode=new MsgField(null,15,3,"BkTxCode");
        this.BILL_CodeName=new MsgField(null,32,3,"BILL_CodeName");
        this.AuthSeqNo=new MsgField(null,20,3,"AuthSeqNo");
        this.BkSysDate=new MsgField(null,6,3,"BkSysDate");
        this.Bk10Date1=new MsgField(null,10,3,"Bk10Date1");
        this.BkDTime=new MsgField(null,20,3,"BkDTime");
        this.BkUuidNo=new MsgField(null,30,3,"BkUuidNo");
        this.BkChnlNo=new MsgField(null,6,3,"BkChnlNo");
        this.BILL_NoteBrchName=new MsgField(null,20,3,"BILL_NoteBrchName");
        this.BkTellerNo=new MsgField(null,10,3,"BkTellerNo");
        this.BkAuthTeller=new MsgField(null,10,3,"BkAuthTeller");
        this.BILL_CheckTeller=new MsgField(null,10,3,"BILL_CheckTeller");
        this.BkChkNo=new MsgField(null,16,3,"BkChkNo");
        this.BILL_TxNo=new MsgField(null,7,3,"BILL_TxNo");
        this.Bk10Date2=new MsgField(null,10,3,"Bk10Date2");
        this.BkOthSeq=new MsgField(null,20,3,"BkOthSeq");
        this.BkNodeNo=new MsgField(null,20,3,"BkNodeNo");
        this.TS_F_summry=new MsgField(null,32,3,"TS_F_summry");
    }

    public List<MsgField> getHeadData() {
        List fieldList = new ArrayList();
        fieldList.add(this.BILL_No);
        fieldList.add(this.BILL_Syscode);
        fieldList.add(this.BkPinNode);
        fieldList.add(this.BkTxCode);
        fieldList.add(this.BILL_CodeName);
        fieldList.add(this.AuthSeqNo);
        fieldList.add(this.BkSysDate);
        fieldList.add(this.Bk10Date1);
        fieldList.add(this.BkDTime);
        fieldList.add(this.BkUuidNo);
        fieldList.add(this.BkChnlNo);
        fieldList.add(this.BILL_NoteBrchName);
        fieldList.add(this.BkTellerNo);
        fieldList.add(this.BkAuthTeller);
        fieldList.add(this.BILL_CheckTeller);
        fieldList.add(this.BkChkNo);
        fieldList.add(this.BILL_TxNo);
        fieldList.add(this.Bk10Date2);
        fieldList.add(this.BkOthSeq);
        fieldList.add(this.BkNodeNo);
        fieldList.add(this.TS_F_summry);

        return fieldList;
    }
}
