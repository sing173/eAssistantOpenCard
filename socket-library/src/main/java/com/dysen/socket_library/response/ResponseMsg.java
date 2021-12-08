/*     */ package com.dysen.socket_library.response;


import com.dysen.socket_library.msg.MsgField;
import com.dysen.socket_library.utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

/*     */
/*     */ public class ResponseMsg
/*     */ {
/*  16 */   public MsgField GDTA_ITEMDATA_LENGTH = new MsgField(null, 6, 1, "GDTA_ITEMDATA_LENGTH");
/*     */ 
/*  23 */   public MsgField TRAN_CODE = new MsgField("00", 2, 3, "TRAN_CODE");
/*     */ 
/*  29 */   public MsgField GDTA_SVCNAME = new MsgField(null, 6, 3, "GDTA_SVCNAME");
/*     */ 
/*  35 */   public MsgField ERR_RET = new MsgField(null, 20, 3, "ERR_RET");
/*     */ 
/*  41 */   public MsgField ERR_MSG = new MsgField(null, 80, 3, "ERR_MSG");
/*     */ 
/*  46 */   public ResponseMsgHead responseMsgHead = new ResponseMsgHead();
/*     */   public ResponseMsgBody responseMsgBody;
/*     */ 
/*     */   public ResponseMsg()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ResponseMsg(byte[] msg, ResponseMsgBody body)
/*     */     throws InstantiationException, IllegalAccessException
/*     */   {
/*  70 */     int pos = 0;
/*     */ 
/*  72 */     byte[] lengthByte = new byte[this.GDTA_ITEMDATA_LENGTH.getLength()];
/*  73 */     System.arraycopy(msg, pos, lengthByte, 0, lengthByte.length);
/*  74 */     this.GDTA_ITEMDATA_LENGTH.setFieldValue(new String(lengthByte));
/*  75 */     pos += lengthByte.length;
/*     */ 
/*  77 */     byte[] trancodeByte = new byte[this.TRAN_CODE.getLength()];
/*  78 */     System.arraycopy(msg, pos, trancodeByte, 0, trancodeByte.length);
/*  79 */     this.TRAN_CODE.setFieldValue(new String(trancodeByte));
/*  80 */     pos += trancodeByte.length;
/*     */ 
/*  82 */     byte[] svcnameByte = new byte[this.GDTA_SVCNAME.getLength()];
/*  83 */     System.arraycopy(msg, pos, svcnameByte, 0, svcnameByte.length);
/*  84 */     this.GDTA_SVCNAME.setFieldValue(new String(svcnameByte));
/*  85 */     pos += svcnameByte.length;
/*     */ 
/*  87 */     byte[] retByte = new byte[this.ERR_RET.getLength()];
/*  88 */     System.arraycopy(msg, pos, retByte, 0, retByte.length);
/*  89 */     this.ERR_RET.setFieldValue(new String(retByte));
/*  90 */     pos += retByte.length;
/*     */ 
/*  92 */     byte[] msgByte = new byte[this.ERR_MSG.getLength()];
/*  93 */     System.arraycopy(msg, pos, msgByte, 0, msgByte.length);
				//updata by hutian 2018/02/26
				String  respHeadermsg ="";
				try {
					//后台返回报文头错误信息 GBK
					respHeadermsg = new String(msgByte,"gbk");
						LogUtils.d("hut","后台返回报文头="+respHeadermsg);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
	//updata by hutian 2018/02/26
/*  94 */    this.ERR_MSG.setFieldValue(respHeadermsg);
/*  95 */     pos += msgByte.length;
/*     */ 
/*  97 */     int msgLength = Integer.parseInt(this.GDTA_ITEMDATA_LENGTH.getValue());
/*  98 */     int dataLength = msgLength - this.TRAN_CODE.getLength() - this.GDTA_SVCNAME.getLength() - this.ERR_RET.getLength() - this.ERR_MSG.getLength();
/*  99 */     byte[] dataByte = new byte[dataLength];
/* 100 */     System.arraycopy(msg, pos, dataByte, 0, dataByte.length);
/* 101 */     int headLength = this.responseMsgHead.analyzeMsg(dataByte);
/* 102 */     pos += headLength;
/*     */ 
/* 104 */     this.responseMsgBody = body;
/* 105 */     List fieldList = this.responseMsgBody.getBodyData();
/* 106 */     for (int i = 0; i < fieldList.size(); ++i)
/*     */     {
/* 108 */       MsgField field = (MsgField)fieldList.get(i);
/* 109 */       byte[] tmpByte = new byte[field.getLength()];
/* 110 */       System.arraycopy(msg, pos, tmpByte, 0, tmpByte.length);
			String  respmsg ="";
		try {
			//后台编码GBK
			respmsg = new String(tmpByte,"gbk");
//			LogUtils.d("hut","后台输出123="+respmsg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
/* 111 */       field.setFieldValue(respmsg);
/* 112 */       pos += tmpByte.length;
/*     */ 
/* 114 */       List subFieldList = field.getSubField();
/* 115 */       List subFieldLoop = field.getSubFieldList();
/* 116 */       if (subFieldList == null)
/*     */         continue;
/* 118 */       for (int j = 1; j <= Integer.parseInt(field.getValue()); ++j)
/*     */       {
/* 120 */         for (int k = 0; k < subFieldList.size(); ++k)
/*     */         {
/* 122 */           MsgField subField = (MsgField)((MsgField)subFieldList.get(k)).clone();
/* 123 */           byte[] subTmpByte = new byte[subField.getLength()];
/* 124 */           System.arraycopy(msg, pos, subTmpByte, 0, subTmpByte.length);
/* 125 */           subField.setFieldValue(new String(subTmpByte));
/* 126 */           subField.setName(subField.getName() + "_" + j);
/* 127 */           pos += subTmpByte.length;
/* 128 */           subFieldLoop.add(subField);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 134 */     logMsgInfo();
/*     */   }
/*     */ 
/*     */   private void logMsgInfo()
/*     */   {
/* 142 */     this.GDTA_ITEMDATA_LENGTH.logFieldInfo();
/* 143 */     this.TRAN_CODE.logFieldInfo();
/* 144 */     this.GDTA_SVCNAME.logFieldInfo();
/* 145 */     this.ERR_RET.logFieldInfo();
/* 146 */     this.ERR_MSG.logFieldInfo();
/* 147 */     for (int i = 0; i < this.responseMsgHead.getHeadData().size(); ++i)
/*     */     {
/* 149 */       ((MsgField)this.responseMsgHead.getHeadData().get(i)).logFieldInfo();
/*     */     }
/* 151 */     for (int i = 0; i < this.responseMsgBody.getBodyData().size(); ++i)
/*     */     {
/* 153 */       ((MsgField)this.responseMsgBody.getBodyData().get(i)).logFieldInfo();
/*     */     }
/*     */   }
/*     */ }

/* Location:           F:\鐢靛瓙閾惰寮?鍙戝洟闃焅鍓崱e鍔╂墜\HBNX_BusinessSystem\HBNX_BusinessSystem.jar
 * Qualified Name:     com.centerm.msg.response.ResponseMsg
 * JD-Core Version:    0.5.4
 */