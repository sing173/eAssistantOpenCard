/*     */ package com.dysen.socket_library.request;

import com.dysen.socket_library.msg.MsgField;

import java.util.ArrayList;
import java.util.List;

/*     */
/*     */ 
/*     */ public class RequestMsgHead
/*     */ {
/*     */   public MsgField RRSpecificNo;
/*     */   public MsgField HB_Branch_Number;
/*     */   public MsgField HB_Workstation_Number;
/*     */   public MsgField HB_Teller_Number;
/*     */   public MsgField BkPinNode;
/*     */   public MsgField GDTA_SVCNAME;
/*     */   public MsgField HB_FLAG2;
/*     */   public MsgField HB_FLAG4;
/*     */   public MsgField HB_Supervisor_ID;
/*     */   public MsgField HB_UUID_NO;
/*     */   public MsgField AuthSupervisor_ID;
/*     */   public MsgField AuthSpeck;
/*     */   public MsgField AuthAmtFlag;
/*     */   public MsgField AuthSpecSign;
/*     */   public MsgField AuthFlag;
/*     */   public MsgField AuthOthSeqNo;
/*     */   public MsgField AuthSeqNo;
/*     */   public MsgField AuthAmt;
/*     */   public MsgField AuthElementName;
/*     */ 
/*     */   public RequestMsgHead()
/*     */   {
/*  17 */     this.RRSpecificNo = new MsgField(null, 10, 3, "RRSpecificNo");
/*     */ 
/*  23 */     this.HB_Branch_Number = new MsgField(null, 5, 3, "HB_Branch_Number");
/*     */ 
/*  29 */     this.HB_Workstation_Number = new MsgField(null, 3, 3, "HB_Workstation_Number");
/*     */ 
/*  35 */     this.HB_Teller_Number = new MsgField(null, 7, 3, "HB_Teller_Number");
/*     */ 
/*  41 */     this.BkPinNode = new MsgField(null, 10, 3, "BkPinNode");
/*     */ 
/*  47 */     this.GDTA_SVCNAME = new MsgField(null, 6, 3, "GDTA_SVCNAME");
/*     */ 
/*  54 */     this.HB_FLAG2 = new MsgField("0", 1, 3, "HB_FLAG2");
/*     */ 
/*  61 */     this.HB_FLAG4 = new MsgField("5", 1, 3, "HB_FLAG4");
/*     */ 
/*  67 */     this.HB_Supervisor_ID = new MsgField(null, 7, 3, "HB_Supervisor_ID");
/*     */ 
/*  73 */     this.HB_UUID_NO = new MsgField(null, 32, 3, "HB_UUID_NO");
/*     */ 
/*  79 */     this.AuthSupervisor_ID = new MsgField(null, 7, 3, "AuthSupervisor_ID");
/*     */ 
/*  85 */     this.AuthSpeck = new MsgField(null, 100, 3, "AuthSpeck");
/*     */ 
/*  91 */     this.AuthAmtFlag = new MsgField(null, 1, 3, "AuthAmtFlag");
/*     */ 
/*  98 */     this.AuthSpecSign = new MsgField("0", 1, 3, "AuthSpecSign");
/*     */ 
/* 105 */     this.AuthFlag = new MsgField("0", 1, 3, "AuthFlag");
/*     */ 
/* 111 */     this.AuthOthSeqNo = new MsgField(null, 20, 3, "AuthOthSeqNo");
/*     */ 
/* 117 */     this.AuthSeqNo = new MsgField(null, 20, 3, "AuthSeqNo");
/*     */ 
/* 124 */     this.AuthAmt = new MsgField("0.00", 18, 1, "AuthAmt");
/*     */ 
/* 131 */     this.AuthElementName = new MsgField("centerm", 7, 3, "AuthElementData");
/*     */   }
/*     */ 
/*     */   public List<MsgField> getHeadData()
/*     */   {
/* 139 */     List fieldList = new ArrayList();
/* 140 */     fieldList.add(this.RRSpecificNo);
/* 141 */     fieldList.add(this.HB_Branch_Number);
/* 142 */     fieldList.add(this.HB_Workstation_Number);
/* 143 */     fieldList.add(this.HB_Teller_Number);
/* 144 */     fieldList.add(this.BkPinNode);
/* 145 */     fieldList.add(this.GDTA_SVCNAME);
/* 146 */     fieldList.add(this.HB_FLAG2);
/* 147 */     fieldList.add(this.HB_FLAG4);
/* 148 */     fieldList.add(this.HB_Supervisor_ID);
/* 149 */     fieldList.add(this.HB_UUID_NO);
/* 150 */     fieldList.add(this.AuthSupervisor_ID);
/* 151 */     fieldList.add(this.AuthSpeck);
/* 152 */     fieldList.add(this.AuthAmtFlag);
/* 153 */     fieldList.add(this.AuthSpecSign);
/* 154 */     fieldList.add(this.AuthFlag);
/* 155 */     fieldList.add(this.AuthOthSeqNo);
/* 156 */     fieldList.add(this.AuthSeqNo);
/* 157 */     fieldList.add(this.AuthAmt);
/* 158 */     fieldList.add(this.AuthElementName);
/*     */ 
/* 160 */     return fieldList;
/*     */   }
/*     */ }

/* Location:           F:\鐢靛瓙閾惰寮?鍙戝洟闃焅鍓崱e鍔╂墜\HBNX_BusinessSystem\HBNX_BusinessSystem.jar
 * Qualified Name:     com.centerm.msg.request.RequestMsgHead
 * JD-Core Version:    0.5.4
 */