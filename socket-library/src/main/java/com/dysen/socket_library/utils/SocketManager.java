package com.dysen.socket_library.utils;

import com.dysen.socket_library.response.ResponseMsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */
/*     */
/*     */public class SocketManager
{

	private String bm="GBK";

	public byte[] transmitMsg(String ip, int port, int timeout, byte[] msg, String str) throws UnknownHostException, IOException {
	    logMsg(msg);

	    long startTime = System.currentTimeMillis();
	    long curTime = startTime;

	    Socket socket = new Socket(ip, port);
	    socket.setSoTimeout(timeout);
	    OutputStream os = socket.getOutputStream();
		  String utf=  new String(msg,"utf-8");
		  String utf1=  new String(str.getBytes("utf-8"),"utf-8");
		  String gbk=  new String(str.getBytes("gbk"),"gbk");
//		  BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
//				  os,bm));
		OutputStreamWriter osw=new OutputStreamWriter(os,bm);
		  BufferedReader bff = new BufferedReader(new InputStreamReader(
				  socket.getInputStream()));
//		  LogUtils.d("hut","发送给后台数据="+str);
//		  LogUtils.d("hut","request1="+utf);
//		  LogUtils.d("hut","request2="+utf1);
//		  LogUtils.d("hut","request3="+gbk);
	   os.write(msg);
//	    os.write(str.getBytes());
//	    os.flush();

	    InputStream is = socket.getInputStream();
	    ResponseMsg responseMsg = new ResponseMsg();
	    byte[] length = new byte[responseMsg.GDTA_ITEMDATA_LENGTH.getLength()];

	    int readLength = 0;
	    do
	    {
	      curTime = System.currentTimeMillis();
	      if ((curTime - startTime) > timeout)
	      {
	        socket.close();
	        return null;
	      }

	      int readLen = is.read(length, readLength, length.length - readLength);
	      if (readLen == -1)
	      {
	        if (readLength == length.length)
	        {
	          break;
	        }

	        socket.close();
	        return null;
	      }

	      readLength += readLen;
	    }
	    while (readLength < length.length);

	    responseMsg.GDTA_ITEMDATA_LENGTH.setFieldValue(new String(length));
	    int len = Integer.parseInt(responseMsg.GDTA_ITEMDATA_LENGTH.getValue());
	    byte[] retMsg = new byte[responseMsg.GDTA_ITEMDATA_LENGTH.getLength() + len];
	    System.arraycopy(length, 0, retMsg, 0, length.length);

	    readLength = 0;
	    do
	    {
	      curTime = System.currentTimeMillis();
	      if ((curTime - startTime) > timeout)
	      {
	        socket.close();
	        return null;
	      }

	      int readLen = is.read(retMsg, responseMsg.GDTA_ITEMDATA_LENGTH.getLength() + readLength, len - readLength);
	      if (readLen == -1)
	      {
	        if (readLength == len)
	        {
	          break;
	        }

	        socket.close();
	        return null;
	      }

	      readLength += readLen;
	    }
	    while (readLength < len);

	    socket.close();
	    logMsg(retMsg);

 	    return retMsg;
	  }

	  private void logMsg(byte[] msg)
	  {
	    String logInfo = " ";
	    for (int i = 0; i < msg.length; ++i)
	    {
	      logInfo = logInfo + FormatUtil.byteToHexString(msg[i], true) + new String(".");
	    }
	  }
	}

/*
 * Location: F:\鐢靛瓙閾惰寮?鍙戝洟闃焅鍓崱e鍔╂墜\HBNX_BusinessSystem\HBNX_BusinessSystem.jar
 * Qualified Name: com.centerm.network.SocketManager JD-Core Version: 0.5.4
 */