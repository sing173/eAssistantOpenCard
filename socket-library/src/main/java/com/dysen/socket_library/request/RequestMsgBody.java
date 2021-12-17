/*     */
package com.dysen.socket_library.request;
/*     */
/*     */

import android.content.Context;

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
/*     */ public class RequestMsgBody
        /*     */ {
    /*     */   public final String MSG_CODE;
    /*  26 */   private Map<String, Integer> fieldIndexMap = new HashMap();
    /*  27 */   private List<MsgField> fieldList = new ArrayList();

    /*  28 */   //private String xmlDir = GlobalData.PROJECT_PATH + "msg" + GlobalData.SEPARATOR + "request" + GlobalData.SEPARATOR;
    /*     */
    /*     */
    public RequestMsgBody(String MSG_CODE, Context context)
    /*     */     throws MalformedURLException, DocumentException
    /*     */ {
        /*  38 */
        this.MSG_CODE = MSG_CODE;
        /*     */      //String responseXmlPath="file:///android_asset/request_"+MSG_CODE+".xml";
        InputStream ins = null;
        try {
            ins = context.getAssets().open("request_" + MSG_CODE + ".xml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //String path="C:\\Users\\Administrator\\Desktop\\request_640002.xml";
        /*  40 */     //Document document = XmlUtil.read(this.xmlDir + MSG_CODE + ".xml");
        /* 	   */
        Document document = XmlUtil.read(ins);
        /*  41 */
        Element rootElement = document.getRootElement();
        /*  42 */
        List fieldElementList = rootElement.elements("MsgField");
        /*  43 */
        for (int i = 0; i < fieldElementList.size(); ++i)
            /*     */ {
            /*  45 */
            Element fieldElement = (Element) fieldElementList.get(i);
            /*  46 */
            String id = fieldElement.attributeValue("id");
            /*  47 */
            String lengthValue = fieldElement.attributeValue("length");
            /*  48 */
            String typeValue = fieldElement.attributeValue("type");
            /*  49 */
            String defaultValue = fieldElement.attributeValue("default");
            /*  51 */
            int length = Integer.parseInt(lengthValue);
            /*     */
            int type;
            /*  53 */
            switch (typeValue)
                /*     */ {
                /*     */
                case "N":
                    /*  57 */
                    type = 1;
                    /*  58 */
                    break;
                /*     */
                case "AN":
                    /*  62 */
                    type = 2;
                    /*  63 */
                    break;
                /*     */
                case "A_String":
                    /*  67 */
                    type = 3;
                    /*  68 */
                    break;
                /*     */
                default:
                    /*  72 */
                    type = 3;
                    /*     */
            }
            /*     */
            /*  77 */
            MsgField field = new MsgField(defaultValue, length, type, id);
            /*  78 */
            this.fieldList.add(field);
            /*  79 */
            this.fieldIndexMap.put(id, Integer.valueOf(i));
            /*     */
        }
        /*     */
    }

    /*     */
    /*     */
    public MsgField getMsgField(String id)
    /*     */ {
        /*  90 */
        Integer index = (Integer) this.fieldIndexMap.get(id);
        /*  91 */
        if (index == null)
            /*     */ {
            /*  93 */
            return null;
            /*     */
        }
        /*     */
        /*  96 */
        return (MsgField) this.fieldList.get(index.intValue());
        /*     */
    }

    /*     */
    /*     */
    public List<MsgField> getBodyData()
    /*     */ {
        /* 105 */
        return this.fieldList;
        /*     */
    }
    /*     */
}

/* Location:           F:\鐢靛瓙閾惰寮?鍙戝洟闃焅鍓崱e鍔╂墜\HBNX_BusinessSystem\HBNX_BusinessSystem.jar
 * Qualified Name:     com.centerm.msg.request.RequestMsgBody
 * JD-Core Version:    0.5.4
 */