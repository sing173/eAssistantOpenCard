/*     */ package com.dysen.socket_library.response;


import android.util.Log;

import com.dysen.socket_library.msg.MsgField;
import com.dysen.socket_library.utils.LogUtils;
import com.dysen.socket_library.utils.ParamUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

/*     */
/*     */ public class ResponseMsg2
/*     */ {
/*  16 */   public MsgField GDTA_ITEMDATA_LENGTH = new MsgField(null, 6, 1, "GDTA_ITEMDATA_LENGTH");
/*     */
/*  23 */   public MsgField TRAN_CODE = new MsgField("", 7, 3, "TRAN_CODE");
/*     */
/*  46 */   public ResponseMsgHead2 responseMsgHead = new ResponseMsgHead2();
/*     */   public ResponseMsgBody responseMsgBody;
/*     */
/*     */   public ResponseMsg2()
/*     */   {
/*     */   }
/*     */
/*     */   public ResponseMsg2(byte[] msg, ResponseMsgBody body)
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
/*  80 */     //pos += trancodeByte.length;
/*     */
    		  int msgLength = Integer.parseInt(this.GDTA_ITEMDATA_LENGTH.getValue());
     		  int dataLength = msgLength;
			  byte[] dataByte = new byte[dataLength];
			  try {
				  System.arraycopy(msg, pos, dataByte, 0, dataByte.length);
				  int headLength = this.responseMsgHead.analyzeMsg(dataByte);
				  pos += headLength;
			  }catch (Exception e){
				  e.printStackTrace();
			  }
/*     */
/* 104 */     this.responseMsgBody = body;
/* 105 */     List fieldList = this.responseMsgBody.getBodyData();
/* 106 */     for (int i = 0; i < fieldList.size(); ++i)
/*     */     {
/* 108 */       MsgField field = (MsgField)fieldList.get(i);
/* 109 */       byte[] tmpByte = new byte[field.getLength()];
				try {
/* 110 */
					System.arraycopy(msg, pos+7, tmpByte, 0, tmpByte.length);
				}catch (Exception e){
					LogUtils.i("---exception---"+e.toString());
				}
			String  respmsg ="";
		try {
			//后台编码GBK
			respmsg = new String(tmpByte,"utf-8");
//			LogUtils.d("hut","后台输出123="+respmsg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e){
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