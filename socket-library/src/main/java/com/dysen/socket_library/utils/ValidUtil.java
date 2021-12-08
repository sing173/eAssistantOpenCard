package com.dysen.socket_library.utils;

public class ValidUtil {
	public static byte[] MacCode(byte[] data, byte[] key)
	  {
	    byte[] originData = null;
	    if (data.length % 8 != 0)
	    {
	      originData = new byte[data.length + (8 - data.length % 8)];
	      for (int i = 0; i < originData.length; ++i)
	      {
	        originData[i] = 0;
	      }
	      System.arraycopy(data, 0, originData, 0, data.length);
	    }
	    else
	    {
	      originData = data;
	    }

	    byte[] tmpByteBuf = new byte[8];
	    for (int i = 0; i < originData.length / 8; ++i)
	    {
	      tmpByteBuf[0] = (byte)(tmpByteBuf[0] ^ originData[(i * 8)]);
	      tmpByteBuf[1] = (byte)(tmpByteBuf[1] ^ originData[(i * 8 + 1)]);
	      tmpByteBuf[2] = (byte)(tmpByteBuf[2] ^ originData[(i * 8 + 2)]);
	      tmpByteBuf[3] = (byte)(tmpByteBuf[3] ^ originData[(i * 8 + 3)]);
	      tmpByteBuf[4] = (byte)(tmpByteBuf[4] ^ originData[(i * 8 + 4)]);
	      tmpByteBuf[5] = (byte)(tmpByteBuf[5] ^ originData[(i * 8 + 5)]);
	      tmpByteBuf[6] = (byte)(tmpByteBuf[6] ^ originData[(i * 8 + 6)]);
	      tmpByteBuf[7] = (byte)(tmpByteBuf[7] ^ originData[(i * 8 + 7)]);
	    }

	    String tmpHexByteBuf = "";
	    for (int i = 0; i < tmpByteBuf.length; ++i)
	    {
	      tmpHexByteBuf = tmpHexByteBuf + FormatUtil.byteToHexString(tmpByteBuf[i], true);
	    }

	    byte[] tmpPreHexBuf = new byte[8];
	    System.arraycopy(tmpHexByteBuf.getBytes(), 0, tmpPreHexBuf, 0, tmpPreHexBuf.length);
	    byte[] tmpCryptPreHexBuf = DesUtil.des_crypt(key, tmpPreHexBuf);
	    byte[] tmpLastHexBuf = new byte[8];
	    System.arraycopy(tmpHexByteBuf.getBytes(), 8, tmpLastHexBuf, 0, tmpLastHexBuf.length);

	    byte[] tmpXorBuf = new byte[8];
	    for (int i = 0; i < tmpXorBuf.length; ++i)
	    {
	      tmpXorBuf[i] = (byte)(tmpCryptPreHexBuf[i] ^ tmpLastHexBuf[i]);
	    }
	    byte[] tmpCryptXorBuf = DesUtil.des_crypt(key, tmpXorBuf);

	    String tmpHexCryptXorBuf = "";
	    for (int i = 0; i < tmpCryptXorBuf.length; ++i)
	    {
	      tmpHexCryptXorBuf = tmpHexCryptXorBuf + FormatUtil.byteToHexString(tmpCryptXorBuf[i], true);
	    }

	    byte[] result = new byte[8];
	    System.arraycopy(tmpHexCryptXorBuf.getBytes(), 0, result, 0, result.length);
	    return result;
	  }
}
