/*     */ package com.dysen.socket_library.msg;
/*     */ 
/*     */

import java.io.UnsupportedEncodingException;
import java.util.List;

/*     */
/*     */
/*     */ 
/*     */ public class MsgField
implements Cloneable
{
private String value;
private String fieldValue;
private int length;
private int type;
private String name;
private List<MsgField> subField;
private List<MsgField> subFieldList;
public static final int N = 1;
public static final int AN = 2;
public static final int A_String = 3;
  private String bm="gbk";//设置编码

public MsgField(String value, int length, int type, String name)
{
  this.length = length;
  this.type = type;
  setValue(value);

  if (name == null)
  {
    this.name = "";
  }
  this.name = name;
}

public String getValue()
{
  return this.value;
}

public void setValue(String value) {
//  //修改赋值编码
//  byte[] valuegbk;
//  if (value != null) {
//    try {
//      valuegbk = value.getBytes();
//      LogUtils.d("hut", "前端发送给后台：=" + new String(valuegbk, "gbk"));
//    } catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    } catch (RuntimeException r) {
//
//    }
//  }

  this.value = value;
  this.fieldValue = value;

  if (this.fieldValue == null) {
    this.fieldValue = "";
  }

  if (this.length < 0) {
    this.length = 0;
  }

  switch (this.type) {
    case 1:
      if (this.fieldValue.getBytes().length > this.length) {
        byte[] tmpValue = new byte[this.length];
        System.arraycopy(this.fieldValue.getBytes(), this.fieldValue.getBytes().length - this.length, tmpValue, 0, tmpValue.length);
        this.fieldValue = new String(tmpValue);
      }
      while (true) {
        if (this.fieldValue.length() >= this.length)
          return;
        this.fieldValue = ("0" + this.fieldValue);
      }
    case 2:
      int dotIndex = this.fieldValue.indexOf(46);
      if (dotIndex == -1) {
        this.fieldValue += ".00";
      } else if (dotIndex == this.fieldValue.length() - 2) {
        this.fieldValue += "0";
      }

      if (this.fieldValue.getBytes().length > this.length) {
        byte[] tmpValue = new byte[this.length];
        System.arraycopy(this.fieldValue.getBytes(), this.fieldValue.getBytes().length - this.length, tmpValue, 0, tmpValue.length);
        this.fieldValue = new String(tmpValue);
      }
      while (true) {
        if (this.fieldValue.length() >= this.length)
          return;
        this.fieldValue = ("0" + this.fieldValue);
      }
    case 3:
      if (this.fieldValue.length() > this.length) {
        byte[] tmpValue = new byte[this.length];
        try {
          System.arraycopy(this.fieldValue.getBytes(bm), 0, tmpValue, 0, tmpValue.length);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        this.fieldValue = new String(tmpValue);
        while (true) {
          try {
            if (this.fieldValue.getBytes(bm).length >= this.length)
              return;
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
          this.fieldValue += " ";
        }
      }

      if (this.fieldValue.getBytes().length > this.length) {
        byte[] tmpValue = new byte[this.length];
        try {
          System.arraycopy(this.fieldValue.getBytes(bm), 0, tmpValue, 0, tmpValue.length);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        } catch (Exception e){
          e.printStackTrace();
        }
        this.fieldValue = new String(tmpValue);
      }

      try {
        while (this.fieldValue.getBytes(bm).length < this.length) {
          this.fieldValue += " ";
        }
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
  }
}
public String getFieldValue()
{
  return this.fieldValue;
}

public void setFieldValue(String fieldValue)
{
  if (fieldValue == null)
  {
    this.value = null;
    return;
  }

  if (this.length < 0)
  {
    this.length = 0;
  }

  this.fieldValue = fieldValue;
  byte [] fileByte=null;
  try {
     fileByte=fieldValue.getBytes("gbk");
  } catch (UnsupportedEncodingException e) {
    e.printStackTrace();
  }
  switch (this.type)
  {
    case 1:
      if (this.fieldValue.getBytes().length > this.length)
      {
        byte[] tmpValue = new byte[this.length];
        System.arraycopy(this.fieldValue.getBytes(), this.fieldValue.getBytes().length - this.length, tmpValue, 0, tmpValue.length);
        this.fieldValue = new String(tmpValue);
      }break;
    case 2:
      if (this.fieldValue.getBytes().length > this.length)
      {
        byte[] tmpValue = new byte[this.length];
        System.arraycopy(this.fieldValue.getBytes(), this.fieldValue.getBytes().length - this.length, tmpValue, 0, tmpValue.length);
        this.fieldValue = new String(tmpValue);
      }break;
    case 3:
      try {
        byte[] fileValuebyte=this.fieldValue.getBytes("gbk");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      if (this.fieldValue.getBytes().length > this.length)
      {
        byte[] tmpValue = new byte[this.length];
        System.arraycopy(this.fieldValue.getBytes(), 0, tmpValue, 0, tmpValue.length);
        this.fieldValue = new String(tmpValue);
      }break;
    default:
      if (this.fieldValue.getBytes().length > this.length)
      {
        byte[] tmpValue = new byte[this.length];
        System.arraycopy(this.fieldValue.getBytes(), 0, tmpValue, 0, tmpValue.length);
          this.fieldValue = new String(tmpValue);
      }
  }

  this.value = fieldValue.trim();
  switch (this.type)
  {
  case 1:
    while (true) {
      if ((this.value.indexOf("0") != 0) || (this.value.length() == 1))
        return;
      this.value = this.value.substring(1, this.value.length());
    }
  case 2:
    while ((this.value.indexOf("0") == 0) && (this.value.length() != 1))
    {
      this.value = this.value.substring(1, this.value.length());
    }

    int dotIndex = this.value.indexOf(46);
    if (dotIndex == -1)
    {
      this.value += ".00"; return;
    }
    if (dotIndex != this.value.length() - 2)
      return;
    this.value += "0"; break;
  case 3:
  }
}

public List<MsgField> getSubField()
{
  return this.subField;
}

public void setSubField(List<MsgField> subField)
{
  this.subField = subField;
}

public List<MsgField> getSubFieldList()
{
  return this.subFieldList;
}

public void setSubFieldList(List<MsgField> subFieldList)
{
  this.subFieldList = subFieldList;
}

public int getLength()
{
  return this.length;
}

public String getName()
{
  return this.name;
}

public void setName(String name)
{
  this.name = name;
}

public void logFieldInfo()
{
  if (this.subFieldList == null)
    return;
  for (MsgField subField : this.subFieldList)
  {
    subField.logFieldInfo();
  }
}

public void logFieldInfo(int index)
{
  if (this.subFieldList == null)
    return;
  for (MsgField subField : this.subFieldList)
  {
    subField.logFieldInfo();
  }
}

public Object clone()
{
  MsgField obj = null;
  try
  {
    obj = (MsgField)super.clone();
  }
  catch (CloneNotSupportedException e)
  {
    e.printStackTrace();
  }

  return obj;
}
}