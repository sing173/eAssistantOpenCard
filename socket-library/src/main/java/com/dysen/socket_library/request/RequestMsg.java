/*     */
package com.dysen.socket_library.request;

import com.dysen.socket_library.SocketThread;
import com.dysen.socket_library.msg.MsgField;
import com.dysen.socket_library.utils.FormatUtil;
import com.dysen.socket_library.utils.LogUtils;
import com.dysen.socket_library.utils.ValidUtil;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/*     */
/*     */
/*     */
/*     */ public class RequestMsg
        /*     */ {
    /*  14 */   private static final byte[] MAC_KEY = {0, 0, 0, 0, 0, 0, 0, 0};
    /*     */
    /*  16 */   public MsgField GDTA_ITEMDATA_LENGTH = new MsgField(null, 6, 1, "GDTA_ITEMDATA_LENGTH");
    /*  17 */   public MsgField TRAN_CODE = new MsgField("00", 2, 3, "TRAN_CODE");
    /*  18 */   public MsgField GDTA_SVCNAME = new MsgField(null, 6, 3, "GDTA_SVCNAME");
    public MsgField GDTA_BLANK = new MsgField(" ", 1, 3, "BLANK");
    /*     */   private List<MsgField> GDTA_ITEMDATA_HEAD;
    /*     */   private List<MsgField> GDTA_ITEMDATA_BODY;
    /*  21 */   private String GDTA_ITEMDATA = "";
    /*  22 */   public MsgField HB_NEW_PWD16_1 = new MsgField("0000000000000000", 16, 3, "HB_NEW_PWD16_1");
    /*  23 */   public MsgField HB_NEW_PWD16_2 = new MsgField("0000000000000000", 16, 3, "HB_NEW_PWD16_2");
    /*  24 */   public MsgField HB_NEW_PWD16_3 = new MsgField("0000000000000000", 16, 3, "HB_NEW_PWD16_3");
    /*  25 */   public MsgField GDTA_MAC = new MsgField(null, 16, 3, "GDTA_MAC");
    public static String requesgbkStr = "";
    private String bm = "gbk";

    /*     */
    /*     */
    public RequestMsg(RequestMsgHead head, RequestMsgBody body)
    /*     */ {
        /*  34 */
        this.GDTA_ITEMDATA_HEAD = head.getHeadData();
        /*  35 */
        this.GDTA_ITEMDATA_BODY = body.getBodyData();
        /*  36 */
        for (int i = 0; i < this.GDTA_ITEMDATA_HEAD.size(); ++i)
            /*     */ {
            /*  38 */
            this.GDTA_ITEMDATA += ((MsgField) this.GDTA_ITEMDATA_HEAD.get(i)).getFieldValue();
            /*     */
        }
        /*  40 */
        for (int i = 0; i < this.GDTA_ITEMDATA_BODY.size(); ++i)
            /*     */ {
            /*  42 */
            this.GDTA_ITEMDATA += ((MsgField) this.GDTA_ITEMDATA_BODY.get(i)).getFieldValue();
            /*     */
        }
        /*     */
        /*  45 */
        String msgCode = body.MSG_CODE;
        /*  46 */
        this.GDTA_SVCNAME.setValue(msgCode);
        /*     */
        /*  48 */
        byte[] mac = new byte[0];
        mac = ValidUtil.MacCode(this.GDTA_ITEMDATA.getBytes(), MAC_KEY);

        /*  49 */
        String hexMac = "";
        /*  50 */
        for (int i = 0; i < mac.length; ++i)
            /*     */ {
            /*  52 */
            hexMac = hexMac + FormatUtil.byteToHexString(mac[i], true);
            /*     */
        }
        /*     */
        /*  55 */
        this.GDTA_MAC.setValue("1111111111111111");
        /*     */
        int ItemDataLength = 0;
        try {
            ItemDataLength = this.GDTA_ITEMDATA.getBytes(bm).length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /*  57 */
        int length = this.TRAN_CODE.getLength() + this.GDTA_SVCNAME.getLength() + ItemDataLength + this.HB_NEW_PWD16_1.getLength() + this.HB_NEW_PWD16_2.getLength() + this.HB_NEW_PWD16_3.getLength() + this.GDTA_MAC.getLength();
        /*  58 */
        this.GDTA_ITEMDATA_LENGTH.setValue(String.valueOf(length));
        /*     */
    }

    /*     */
    public RequestMsg(RequestMsgHead2 head2, RequestMsgBody body, String s) {
        this.GDTA_ITEMDATA_HEAD = head2.getHeadData();
        this.GDTA_ITEMDATA_BODY = body.getBodyData();
        for (int i = 0; i < this.GDTA_ITEMDATA_HEAD.size(); ++i) {
            this.GDTA_ITEMDATA += ((MsgField) this.GDTA_ITEMDATA_HEAD.get(i)).getFieldValue();
        }
        for (int i = 0; i < this.GDTA_ITEMDATA_BODY.size(); ++i) {
            this.GDTA_ITEMDATA += ((MsgField) this.GDTA_ITEMDATA_BODY.get(i)).getFieldValue();
        }

        String msgCode = body.MSG_CODE;
        this.GDTA_SVCNAME.setValue(msgCode);


        byte[] mac = new byte[0];
        mac = ValidUtil.MacCode(this.GDTA_ITEMDATA.getBytes(), MAC_KEY);

        String hexMac = "";
        for (int i = 0; i < mac.length; ++i) {
            hexMac = hexMac + FormatUtil.byteToHexString(mac[i], true);
        }

        this.GDTA_MAC.setValue("1111111111111111");
        int ItemDataLength = 0;
        try {
            ItemDataLength = this.GDTA_ITEMDATA.getBytes(bm).length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (this.GDTA_SVCNAME.getFieldValue().equals("311077") || this.GDTA_SVCNAME.getFieldValue().equals("311070")) {
            int length = this.GDTA_SVCNAME.getLength() + ItemDataLength;
            this.GDTA_ITEMDATA_LENGTH.setValue(String.valueOf(length - 4));
        } else {
            int length = this.TRAN_CODE.getLength() + this.GDTA_SVCNAME.getLength() + ItemDataLength + this.HB_NEW_PWD16_1.getLength() + this.HB_NEW_PWD16_2.getLength() + this.HB_NEW_PWD16_3.getLength() + this.GDTA_MAC.getLength();
            this.GDTA_ITEMDATA_LENGTH.setValue(String.valueOf(length));
        }

    }

    /*     */
    public byte[] getMsg()
    /*     */ {
        /*  74 */
        byte[] msg = new byte[this.GDTA_ITEMDATA_LENGTH.getLength() + this.TRAN_CODE
/*  68 */.getLength() + this.GDTA_SVCNAME
/*  69 */.getLength() + this.GDTA_ITEMDATA
/*  70 */.getBytes().length + this.HB_NEW_PWD16_1
/*  71 */.getLength() + this.HB_NEW_PWD16_2
/*  72 */.getLength() + this.HB_NEW_PWD16_3
/*  73 */.getLength() + this.GDTA_MAC
/*  74 */.getLength()];
        /*  75 */
        int pos = 0;
        /*     */
        /*  77 */
        System.arraycopy(this.GDTA_ITEMDATA_LENGTH.getFieldValue().getBytes(), 0, msg, pos, this.GDTA_ITEMDATA_LENGTH.getLength());
        pos += this.GDTA_ITEMDATA_LENGTH.getLength();
        if (this.GDTA_SVCNAME.getFieldValue().equals("311077") || this.GDTA_SVCNAME.getFieldValue().equals("311070")) {

        } else {
            System.arraycopy(this.TRAN_CODE.getFieldValue().getBytes(), 0, msg, pos, this.TRAN_CODE.getLength());
            pos += this.TRAN_CODE.getLength();
        }
        System.arraycopy(this.GDTA_SVCNAME.getFieldValue().getBytes(), 0, msg, pos, this.GDTA_SVCNAME.getLength());
        pos += this.GDTA_SVCNAME.getLength();

        if (this.GDTA_SVCNAME.getFieldValue().equals("311077") || this.GDTA_SVCNAME.getFieldValue().equals("311070")) {
            System.arraycopy(this.GDTA_BLANK.getFieldValue().getBytes(), 0, msg, pos, 1);
            pos += 1;
        } else {

        }
        try { //封装报文体 设置编码 add by hutian 20180306
            System.arraycopy(this.GDTA_ITEMDATA.getBytes(bm), 0, msg, pos, this.GDTA_ITEMDATA.getBytes(bm).length);
            pos += this.GDTA_ITEMDATA.getBytes().length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (this.GDTA_SVCNAME.getFieldValue().equals("311077") || this.GDTA_SVCNAME.getFieldValue().equals("311070")) {

        } else {
		/*  84
/*  85 */
            System.arraycopy(this.HB_NEW_PWD16_1.getFieldValue().getBytes(), 0, msg, pos, this.HB_NEW_PWD16_1.getLength());
            /*  86 */
            pos += this.HB_NEW_PWD16_1.getLength();
            /*  87 */
            System.arraycopy(this.HB_NEW_PWD16_2.getFieldValue().getBytes(), 0, msg, pos, this.HB_NEW_PWD16_2.getLength());
            /*  88 */
            pos += this.HB_NEW_PWD16_2.getLength();
            /*  89 */
            System.arraycopy(this.HB_NEW_PWD16_3.getFieldValue().getBytes(), 0, msg, pos, this.HB_NEW_PWD16_3.getLength());
            /*  90 */
            pos += this.HB_NEW_PWD16_3.getLength();
            /*  91 */
            System.arraycopy(this.GDTA_MAC.getFieldValue().getBytes(), 0, msg, pos, this.GDTA_MAC.getLength());
        }
        /*     */
        /*  93 */
        logMsgInfo();
        /*  78 */
        try {
            String t = new String(msg, "gbk");

            requesgbkStr = new String(msg, "gbk");
            byte[] gbkbyte = requesgbkStr.getBytes("gbk");
            LogUtils.d("hut", "请求原字节长度：==" + msg.length + "请求gbk字节长度：==" + gbkbyte.length);
            LogUtils.d("hut", "发送报文：==" + t);
            LogUtils.d("hut", "请求报文：==" + Arrays.toString(msg).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /*  94 */
        return msg;
        /*     */
    }

    /*     */
    /*     */
    private void logMsgInfo()
    /*     */ {
        /* 102 */
        this.GDTA_ITEMDATA_LENGTH.logFieldInfo();
        /* 103 */
        this.TRAN_CODE.logFieldInfo();
        /* 104 */
        this.GDTA_SVCNAME.logFieldInfo();
        /* 105 */
        for (int i = 0; i < this.GDTA_ITEMDATA_HEAD.size(); ++i)
            /*     */ {
            /* 107 */
            ((MsgField) this.GDTA_ITEMDATA_HEAD.get(i)).logFieldInfo();
            /*     */
        }
        /* 109 */
        for (int i = 0; i < this.GDTA_ITEMDATA_BODY.size(); ++i)
            /*     */ {
            /* 111 */
            ((MsgField) this.GDTA_ITEMDATA_BODY.get(i)).logFieldInfo(i + 1);
            /*     */
        }
        /* 113 */
        this.HB_NEW_PWD16_1.logFieldInfo();
        /* 114 */
        this.HB_NEW_PWD16_2.logFieldInfo();
        /* 115 */
        this.HB_NEW_PWD16_3.logFieldInfo();
        /* 116 */
        this.GDTA_MAC.logFieldInfo();
        /*     */
    }
    /*     */
}

