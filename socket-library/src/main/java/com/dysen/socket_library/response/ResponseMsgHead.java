package com.dysen.socket_library.response;

import com.dysen.socket_library.msg.MsgField;

import java.util.ArrayList;
import java.util.List;

;

/*     */
/*     */ 
/*     */ public class ResponseMsgHead
/*     */ {
/*     */   public MsgField AuthSeqNo;
/*     */   public MsgField AuthErrorType;
/*     */   public MsgField AuthErrorFlag;
/*     */   public MsgField BkSeqNo;
/*     */   public MsgField AuthCapability;
/*     */   public MsgField BkNum1;
/*     */   public List<MsgField> LoopField;
/*     */   public MsgField AuthErrorCode;
/*     */   public MsgField AuthErrorMessage;
/*     */   public MsgField AuthSealMessage;
/*     */ 
/*     */   public ResponseMsgHead()
/*     */   {
/*  17 */     this.AuthSeqNo = new MsgField(null, 20, 3, "AuthSeqNo");
/*     */ 
/*  23 */     this.AuthErrorType = new MsgField(null, 5, 3, "AuthErrorType");
/*     */ 
/*  29 */     this.AuthErrorFlag = new MsgField(null, 5, 3, "AuthErrorFlag");
/*     */ 
/*  35 */     this.BkSeqNo = new MsgField(null, 10, 3, "BkSeqNo");
/*     */ 
/*  41 */     this.AuthCapability = new MsgField(null, 3, 3, "AuthCapability");
/*     */ 
/*  47 */     this.BkNum1 = new MsgField(null, 2, 1, "BkNum1");
/*     */ 
/*  52 */     this.LoopField = new ArrayList();
/*     */ 
/*  58 */     this.AuthErrorCode = new MsgField(null, 6, 3, "AuthErrorCode");
/*     */ 
/*  64 */     this.AuthErrorMessage = new MsgField(null, 120, 3, "AuthErrorMessage");
/*     */ 
/*  70 */     this.AuthSealMessage = new MsgField(null, 120, 3, "AuthSealMessage");
/*     */   }
/*     */ 
/*     */   public int analyzeMsg(byte[] msg)
/*     */   {
/*  79 */     int pos = 0;
/*     */ 
/*  81 */     byte[] authSeqNoByte = new byte[this.AuthSeqNo.getLength()];
/*  82 */     System.arraycopy(msg, pos, authSeqNoByte, 0, authSeqNoByte.length);
/*  83 */     this.AuthSeqNo.setFieldValue(new String(authSeqNoByte));
/*  84 */     pos += authSeqNoByte.length;
/*     */ 
/*  86 */     byte[] authErrorTypeByte = new byte[this.AuthErrorType.getLength()];
/*  87 */     System.arraycopy(msg, pos, authErrorTypeByte, 0, authErrorTypeByte.length);
/*  88 */     this.AuthErrorType.setFieldValue(new String(authErrorTypeByte));
/*  89 */     pos += authErrorTypeByte.length;
/*     */ 
/*  91 */     byte[] authErrorFlagByte = new byte[this.AuthErrorFlag.getLength()];
/*  92 */     System.arraycopy(msg, pos, authErrorFlagByte, 0, authErrorFlagByte.length);
/*  93 */     this.AuthErrorFlag.setFieldValue(new String(authErrorFlagByte));
/*  94 */     pos += authErrorFlagByte.length;
/*     */ 
/*  96 */     byte[] bkSeqNoByte = new byte[this.BkSeqNo.getLength()];
/*  97 */     System.arraycopy(msg, pos, bkSeqNoByte, 0, bkSeqNoByte.length);
/*  98 */     this.BkSeqNo.setFieldValue(new String(bkSeqNoByte));
/*  99 */     pos += bkSeqNoByte.length;
/*     */ 
/* 101 */     byte[] authCapabilityByte = new byte[this.AuthCapability.getLength()];
/* 102 */     System.arraycopy(msg, pos, authCapabilityByte, 0, authCapabilityByte.length);
/* 103 */     this.AuthCapability.setFieldValue(new String(authCapabilityByte));
/* 104 */     pos += authCapabilityByte.length;
/*     */ 
/* 106 */     byte[] bkNum1Byte = new byte[this.BkNum1.getLength()];
/* 107 */     System.arraycopy(msg, pos, bkNum1Byte, 0, bkNum1Byte.length);
/* 108 */     this.BkNum1.setFieldValue(new String(bkNum1Byte));
/* 109 */     pos += bkNum1Byte.length;
/*     */ 
/* 111 */     int loopCount = Integer.parseInt(this.BkNum1.getValue());
/* 112 */     for (int i = 1; i <= loopCount; ++i)
/*     */     {
/* 114 */       byte[] authErrorCodeByte = new byte[this.AuthErrorCode.getLength()];
/* 115 */       System.arraycopy(msg, pos, authErrorCodeByte, 0, authErrorCodeByte.length);
/* 116 */       MsgField authErrorCodeField = new MsgField(null, this.AuthErrorCode.getLength(), 3, this.AuthErrorCode.getName() + "_" + i);
/* 117 */       authErrorCodeField.setFieldValue(new String(authErrorCodeByte));
/* 118 */       this.LoopField.add(authErrorCodeField);
/* 119 */       pos += authErrorCodeByte.length;
/*     */ 
/* 121 */       byte[] authErrorMessageByte = new byte[this.AuthErrorMessage.getLength()];
/* 122 */       System.arraycopy(msg, pos, authErrorMessageByte, 0, authErrorMessageByte.length);
/* 123 */       MsgField authErrorMessageField = new MsgField(null, this.AuthErrorMessage.getLength(), 3, this.AuthErrorMessage.getName() + "_" + i);
/* 124 */       authErrorMessageField.setFieldValue(new String(authErrorMessageByte));
/* 125 */       this.LoopField.add(authErrorMessageField);
/* 126 */       pos += authErrorMessageByte.length;
/*     */ 
/* 128 */       byte[] authSealMessageByte = new byte[this.AuthSealMessage.getLength()];
/* 129 */       System.arraycopy(msg, pos, authSealMessageByte, 0, authSealMessageByte.length);
/* 130 */       MsgField authSealMessageField = new MsgField(null, this.AuthSealMessage.getLength(), 3, this.AuthSealMessage.getName() + "_" + i);
/* 131 */       authSealMessageField.setFieldValue(new String(authSealMessageByte));
/* 132 */       this.LoopField.add(authSealMessageField);
/* 133 */       pos += authSealMessageByte.length;
/*     */     }
/*     */ 
/* 136 */     return pos;
/*     */   }
/*     */ 
/*     */   public List<MsgField> getHeadData()
/*     */   {
/* 145 */     List fieldList = new ArrayList();
/* 146 */     fieldList.add(this.AuthSeqNo);
/* 147 */     fieldList.add(this.AuthErrorType);
/* 148 */     fieldList.add(this.AuthErrorFlag);
/* 149 */     fieldList.add(this.BkSeqNo);
/* 150 */     fieldList.add(this.AuthCapability);
/* 151 */     fieldList.add(this.BkNum1);
/* 152 */     for (int i = 0; i < this.LoopField.size(); ++i)
/*     */     {
/* 154 */       fieldList.add(this.LoopField.get(i));
/*     */     }
/* 149 */     fieldList.add(this.AuthErrorCode);
/* 150 */     fieldList.add(this.AuthErrorMessage);
/* 151 */     fieldList.add(this.AuthSealMessage);

/* 157 */     return fieldList;
/*     */   }
/*     */ }

/* Location:           F:\鐢靛瓙閾惰寮?鍙戝洟闃焅鍓崱e鍔╂墜\HBNX_BusinessSystem\HBNX_BusinessSystem.jar
 * Qualified Name:     com.centerm.msg.response.ResponseMsgHead
 * JD-Core Version:    0.5.4
 */