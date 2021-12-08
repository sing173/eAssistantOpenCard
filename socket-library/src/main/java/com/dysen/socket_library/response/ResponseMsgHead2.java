package com.dysen.socket_library.response;

import com.dysen.socket_library.msg.MsgField;
import com.dysen.socket_library.utils.ParamUtils;

import java.util.ArrayList;
import java.util.List;

;

/*     */
/*     */ 
/*     */ public class ResponseMsgHead2
/*     */ {
/*     */   public MsgField BILL_NO;
/*     */   public MsgField BkAreaNo;
/*     */   public MsgField BkPCode;
/*     */   public MsgField BkOthRetCode;
/*     */   public MsgField BkOthRetMsg;
/*     */   public MsgField BkTxCode;
/*     */   public MsgField BILL_TxDate;
/*     */   public MsgField TS_F_datetime;
/*     */   public MsgField BkOthOldSeq;
/*     */   public MsgField BkHostDate;
            public MsgField BkHostSeq;
            public MsgField BkPlatSeqNo;
            public MsgField TS_F_summry;
/*     */
/*     */   public ResponseMsgHead2()
/*     */   {
/*  17 */     this.BILL_NO = new MsgField(null, 6, 3, "BILL_NO");
/*     */ 
/*  23 */     this.BkAreaNo = new MsgField(null, 12, 3, "BkAreaNo");
/*     */ 
/*  29 */     this.BkPCode = new MsgField(null, 20, 3, "BkPCode");
/*     */ 
/*  35 */     this.BkOthRetCode = new MsgField(null, 10, 3, "BkOthRetCode");
/*     */ 
/*  41 */     this.BkOthRetMsg = new MsgField(null, 128, 3, "BkOthRetMsg");
/*     */ 
/*  47 */     this.BkTxCode = new MsgField(null, 15, 1, "BkTxCode");
/*     */ 
/*  58 */     this.BILL_TxDate = new MsgField(null, 10, 3, "BILL_TxDate");
/*     */ 
/*  64 */     this.TS_F_datetime = new MsgField(null, 20, 3, "TS_F_datetime");
/*     */ 
/*  70 */     this.BkOthOldSeq = new MsgField(null, 20, 3, "BkOthOldSeq");

              this.BkHostDate = new MsgField(null, 10, 3, "BkHostDate");

              this.BkHostSeq = new MsgField(null, 15, 3, "BkHostSeq");

              this.BkPlatSeqNo = new MsgField(null, 12, 3, "BkPlatSeqNo");

              this.TS_F_summry = new MsgField(null, 32, 3, "TS_F_summry");
/*     */   }
/*     */ 
/*     */   public int analyzeMsg(byte[] msg)
/*     */   {
/*  79 */     int pos = 7;//加上transcode长度
/*     */
/*  81 */     byte[] billNoByte = new byte[this.BILL_NO.getLength()];
/*  82 */     System.arraycopy(msg, pos, billNoByte, 0, billNoByte.length);
/*  83 */     this.BILL_NO.setFieldValue(new String(billNoByte));
/*  84 */     pos += billNoByte.length;
/*     */ 
/*  86 */     byte[] bkAreaNoByte = new byte[this.BkAreaNo.getLength()];
/*  87 */     System.arraycopy(msg, pos, bkAreaNoByte, 0, bkAreaNoByte.length);
/*  88 */     this.BkAreaNo.setFieldValue(new String(bkAreaNoByte));
/*  89 */     pos += bkAreaNoByte.length;
/*     */ 
/*  91 */     byte[] bkpCodeByte = new byte[this.BkPCode.getLength()];
/*  92 */     System.arraycopy(msg, pos, bkpCodeByte, 0, bkpCodeByte.length);
/*  93 */     this.BkPCode.setFieldValue(new String(bkpCodeByte));
/*  94 */     pos += bkpCodeByte.length;
/*     */ 
/*  96 */     byte[] bkOthRetCodeByte = new byte[this.BkOthRetCode.getLength()];
/*  97 */     System.arraycopy(msg, pos, bkOthRetCodeByte, 0, bkOthRetCodeByte.length);
/*  98 */     this.BkOthRetCode.setFieldValue(new String(bkOthRetCodeByte));
/*  99 */     pos += bkOthRetCodeByte.length;
/*     */ 
/* 101 */     byte[] bkOthRetMsgByte = new byte[this.BkOthRetMsg.getLength()];
/* 102 */     System.arraycopy(msg, pos, bkOthRetMsgByte, 0, bkOthRetMsgByte.length);
/* 103 */     this.BkOthRetMsg.setFieldValue(new String(bkOthRetMsgByte));
/* 104 */     pos += bkOthRetMsgByte.length;
/*     */ 
/* 106 */     byte[] bkTxCodeByte = new byte[this.BkTxCode.getLength()];
/* 107 */     System.arraycopy(msg, pos, bkTxCodeByte, 0, bkTxCodeByte.length);
/* 108 */     this.BkTxCode.setFieldValue(new String(bkTxCodeByte));
/* 109 */     pos += bkTxCodeByte.length;

              byte[] bill_TxDateByte = new byte[this.BILL_TxDate.getLength()];
/* 107 */     System.arraycopy(msg, pos, bill_TxDateByte, 0, bill_TxDateByte.length);
/* 108 */     this.BILL_TxDate.setFieldValue(new String(bill_TxDateByte));
/* 109 */     pos += bill_TxDateByte.length;

              byte[] ts_F_datetimeByte = new byte[this.TS_F_datetime.getLength()];
/* 107 */     System.arraycopy(msg, pos, ts_F_datetimeByte, 0, ts_F_datetimeByte.length);
/* 108 */     this.TS_F_datetime.setFieldValue(new String(ts_F_datetimeByte));
/* 109 */     pos += ts_F_datetimeByte.length;

              byte[] bkOthOldSeqByte = new byte[this.BkOthOldSeq.getLength()];
/* 107 */     System.arraycopy(msg, pos, bkOthOldSeqByte, 0, bkOthOldSeqByte.length);
/* 108 */     this.BkOthOldSeq.setFieldValue(new String(bkOthOldSeqByte));
/* 109 */     pos += bkOthOldSeqByte.length;

              byte[] bkHostDateByte = new byte[this.BkHostDate.getLength()];
/* 107 */     System.arraycopy(msg, pos, bkHostDateByte, 0, bkHostDateByte.length);
/* 108 */     this.BkHostDate.setFieldValue(new String(bkHostDateByte));
/* 109 */     pos += bkHostDateByte.length;

              byte[] bkHostSeqByte = new byte[this.BkHostSeq.getLength()];
/* 107 */     System.arraycopy(msg, pos, bkHostSeqByte, 0, bkHostSeqByte.length);
/* 108 */     this.BkHostSeq.setFieldValue(new String(bkHostSeqByte));
/* 109 */     pos += bkHostSeqByte.length;

              byte[] bkPlatSeqNoByte = new byte[this.BkPlatSeqNo.getLength()];
/* 107 */     System.arraycopy(msg, pos, bkPlatSeqNoByte, 0, bkPlatSeqNoByte.length);
/* 108 */     this.BkPlatSeqNo.setFieldValue(new String(bkPlatSeqNoByte));
/* 109 */     pos += bkPlatSeqNoByte.length;

              byte[] ts_F_summryByte = new byte[this.TS_F_summry.getLength()-7];
/* 107 */     System.arraycopy(msg, pos, ts_F_summryByte, 0, ts_F_summryByte.length);
/* 108 */     this.TS_F_summry.setFieldValue(new String(ts_F_summryByte));
/* 109 */     pos += (ts_F_summryByte.length);
/*     */ 
/* 136 */     return pos;
/*     */   }
/*     */ 
/*     */   public List<MsgField> getHeadData()
/*     */   {
/* 145 */     List fieldList = new ArrayList();
/* 146 */     fieldList.add(this.BILL_NO);
/* 147 */     fieldList.add(this.BkAreaNo);
/* 148 */     fieldList.add(this.BkPCode);
/* 149 */     fieldList.add(this.BkOthRetCode);
/* 150 */     fieldList.add(this.BkOthRetMsg);
/* 151 */     fieldList.add(this.BkTxCode);
/* 149 */     fieldList.add(this.BILL_TxDate);
/* 150 */     fieldList.add(this.TS_F_datetime);
/* 151 */     fieldList.add(this.BkOthOldSeq);
              fieldList.add(this.BkHostDate);
              fieldList.add(this.BkHostSeq);
              fieldList.add(this.BkPlatSeqNo);
              fieldList.add(this.TS_F_summry);

/* 157 */     return fieldList;
/*     */   }
/*     */ }

/* Location:           F:\鐢靛瓙閾惰寮?鍙戝洟闃焅鍓崱e鍔╂墜\HBNX_BusinessSystem\HBNX_BusinessSystem.jar
 * Qualified Name:     com.centerm.msg.response.ResponseMsgHead
 * JD-Core Version:    0.5.4
 */