/*     */ package com.dysen.socket_library.response;
/*     */ 
/*     */ /*     */

import android.content.Context;

import com.dysen.socket_library.data.GlobalData;
import com.dysen.socket_library.msg.MsgField;
import com.dysen.socket_library.utils.XmlUtil;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResponseMsgBody
/*     */ {
/*  21 */   private Map<String, Integer> fieldIndexMap = new HashMap();
/*  22 */   private List<MsgField> fieldList = new ArrayList();
/*  23 */   private String xmlDir = GlobalData.PROJECT_PATH + "msg" + GlobalData.SEPARATOR + "response" + GlobalData.SEPARATOR;
/*     */ 
/*     */   public ResponseMsgBody(String MSG_CODE,Context context)
/*     */     throws MalformedURLException, DocumentException
/*     */   {
			InputStream ins = null;
			//String path="C:\\Users\\Administrator\\Desktop\\response_640002.xml";
				try {
					ins = context.getAssets().open("response_"+MSG_CODE+".xml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
/*  33 */     Document document = XmlUtil.read(ins);
/*  34 */     Element rootElement = document.getRootElement();
/*  35 */     List fieldElementList = rootElement.elements("MsgField");
/*  36 */     for (int i = 0; i < fieldElementList.size(); ++i)
/*     */     {
/*  38 */       Element fieldElement = (Element)fieldElementList.get(i);
/*  39 */       String id = fieldElement.attributeValue("id");
/*  40 */       String lengthValue = fieldElement.attributeValue("length");
/*  41 */       String typeValue = fieldElement.attributeValue("type");
/*  42 */       String defaultValue = fieldElement.attributeValue("default");
/*  43 */       String sub = fieldElement.attributeValue("sub");
/*  44 */       if (sub == null)
/*     */       {
/*  46 */         sub = "";
/*     */       }
/*     */ 
/*  49 */       int length = Integer.parseInt(lengthValue);
/*     */       int type;
/*  51 */       switch (typeValue)
/*     */       {
/*     */       case "N":
/*  55 */         type = 1;
/*  56 */         break;
/*     */       case "AN":
/*  60 */         type = 2;
/*  61 */         break;
/*     */       case "A_String":
/*  65 */         type = 3;
/*  66 */         break;
/*     */       default:
/*  70 */         type = 3;
/*     */       }
/*     */ 
/*  75 */       Object subFieldList = null;
/*  76 */       if (sub.equals("true"))
/*     */       {
/*  78 */         List subElementList = fieldElement.elements("SubField");
/*  79 */         subFieldList = new ArrayList();
/*  80 */         for (int j = 0; j < subElementList.size(); ++j)
/*     */         {
/*  82 */           Element subElement = (Element)subElementList.get(j);
/*  83 */           String subId = subElement.attributeValue("id");
/*  84 */           String subLengthValue = subElement.attributeValue("length");
/*  85 */           String subTypeValue = subElement.attributeValue("type");
/*  86 */           String subDefaultValue = subElement.attributeValue("default");
/*     */ 
/*  88 */           int subLength = Integer.parseInt(subLengthValue);
/*     */           int subType;
/*  90 */           switch (subTypeValue)
/*     */           {
/*     */           case "N":
/*  94 */             subType = 1;
/*  95 */             break;
/*     */           case "AN":
/*  99 */             subType = 2;
/* 100 */             break;
/*     */           case "A_String":
/* 104 */             subType = 3;
/* 105 */             break;
/*     */           default:
/* 109 */             subType = 3;
/*     */           }
/*     */ 
/* 114 */           MsgField subField = new MsgField(subDefaultValue, subLength, subType, subId);
/* 115 */           ((List)subFieldList).add(subField);
/*     */         }
/*     */       }
/*     */ 
/* 119 */       MsgField field = new MsgField(defaultValue, length, type, id);
/* 120 */       field.setSubField((List)subFieldList);
/* 121 */       field.setSubFieldList(new ArrayList());
/* 122 */       this.fieldList.add(field);
/* 123 */       this.fieldIndexMap.put(id, Integer.valueOf(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   public MsgField getMsgField(String id)
/*     */   {
/* 134 */     Integer index = (Integer)this.fieldIndexMap.get(id);
/* 135 */     if (index == null)
/*     */     {
/* 137 */       return null;
/*     */     }
/*     */ 
/* 140 */     return (MsgField)this.fieldList.get(index.intValue());
/*     */   }
/*     */ 
/*     */   public List<MsgField> getBodyData()
/*     */   {
/* 149 */     return this.fieldList;
/*     */   }
/*     */ }

/* Location:           F:\鐢靛瓙閾惰寮?鍙戝洟闃焅鍓崱e鍔╂墜\HBNX_BusinessSystem\HBNX_BusinessSystem.jar
 * Qualified Name:     com.centerm.msg.response.ResponseMsgBody
 * JD-Core Version:    0.5.4
 */