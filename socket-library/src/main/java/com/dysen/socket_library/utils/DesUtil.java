package com.dysen.socket_library.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

public class DesUtil {

	  private static String des = "DES/ECB/NoPadding";
	  private static String triDes = "DESede/ECB/NoPadding";

	  public static byte[] des_crypt(byte[] key, byte[] data)
	  {
	    try
	    {
	      KeySpec keySpec = new DESKeySpec(key);
	      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
	      SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

	      Cipher cipher = Cipher.getInstance(des);
	      cipher.init(1, secretKey);
	      return cipher.doFinal(data);
	    }
	    catch (InvalidKeyException ike)
	    {
	      return null;
	    }
	    catch (NoSuchAlgorithmException nsae)
	    {

	      return null;
	    }
	    catch (InvalidKeySpecException ikse)
	    {

	      return null;
	    }
	    catch (NoSuchPaddingException nspe)
	    {

	      return null;
	    }
	    catch (BadPaddingException bpe)
	    {

	      return null;
	    }
	    catch (IllegalBlockSizeException ibse)
	    {
	    }return null;
	  }

	  public static byte[] des_decrypt_(byte[] key, byte[] data)
	  {
	    try
	    {
	      KeySpec keySpec = new DESKeySpec(key);
	      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
	      SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

	      Cipher cipher = Cipher.getInstance(des);
	      cipher.init(2, secretKey);
	      return cipher.doFinal(data);
	    }
	    catch (InvalidKeyException ike)
	    {
	      return null;
	    }
	    catch (NoSuchAlgorithmException nsae)
	    {
	      return null;
	    }
	    catch (InvalidKeySpecException ikse)
	    {
	    }
	    catch (NoSuchPaddingException nspe)
	    {
	      return null;
	    }
	    catch (BadPaddingException bpe)
	    {
	      return null;
	    }
	    catch (IllegalBlockSizeException ibse)
	    {
	    }return null;
	  }

	  public static byte[] trides_crypt(byte[] key, byte[] data)
	  {
	    byte[] triKey = new byte[24];
	    byte[] triData = null;

	    if (key.length == 16)
	    {
	      System.arraycopy(key, 0, triKey, 0, key.length);
	      System.arraycopy(key, 0, triKey, key.length, triKey.length - key.length);
	    }
	    else
	    {
	      System.arraycopy(key, 0, triKey, 0, triKey.length);
	    }

	    int dataLen = data.length;
	    if (dataLen % 8 != 0)
	    {
	      dataLen = dataLen - dataLen % 8 + 8;
	    }
	    if (dataLen != 0)
	    {
	      triData = new byte[dataLen];
	    }
	    for (int i = 0; i < dataLen; ++i)
	    {
	      triData[i] = 0;
	    }
	    System.arraycopy(data, 0, triData, 0, data.length);
	    try
	    {
	      KeySpec keySpec = new DESedeKeySpec(triKey);
	      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
	      SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

	      Cipher cipher = Cipher.getInstance(triDes);
	      cipher.init(1, secretKey);
	      return cipher.doFinal(triData);
	    }
	    catch (InvalidKeyException ike)
	    {
	      return null;
	    }
	    catch (NoSuchAlgorithmException nsae)
	    {
	      return null;
	    }
	    catch (InvalidKeySpecException ikse)
	    {
	      return null;
	    }
	    catch (NoSuchPaddingException nspe)
	    {
	      return null;
	    }
	    catch (BadPaddingException bpe)
	    {
	      return null;
	    }
	    catch (IllegalBlockSizeException ibse)
	    {
	    }return null;
	  }

	  public static byte[] trides_decrypt(byte[] key, byte[] data)
	  {
	    byte[] triKey = new byte[24];
	    byte[] triData = null;

	    if (key.length == 16)
	    {
	      System.arraycopy(key, 0, triKey, 0, key.length);
	      System.arraycopy(key, 0, triKey, key.length, triKey.length - key.length);
	    }
	    else
	    {
	      System.arraycopy(key, 0, triKey, 0, triKey.length);
	    }

	    int dataLen = data.length;
	    if (dataLen % 8 != 0)
	    {
	      dataLen = dataLen - dataLen % 8 + 8;
	    }
	    if (dataLen != 0)
	    {
	      triData = new byte[dataLen];
	    }
	    for (int i = 0; i < dataLen; ++i)
	    {
	      triData[i] = 0;
	    }
	    System.arraycopy(data, 0, triData, 0, data.length);
	    try
	    {
	      KeySpec keySpec = new DESedeKeySpec(triKey);
	      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
	      SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

	      Cipher cipher = Cipher.getInstance(triDes);
	      cipher.init(2, secretKey);
	      return cipher.doFinal(triData);
	    }
	    catch (InvalidKeyException ike)
	    {
	      return null;
	    }
	    catch (NoSuchAlgorithmException nsae)
	    {
	      return null;
	    }
	    catch (InvalidKeySpecException ikse)
	    {
	      return null;
	    }
	    catch (NoSuchPaddingException nspe)
	    {
	      return null;
	    }
	    catch (BadPaddingException bpe)
	    {
	      return null;
	    }
	    catch (IllegalBlockSizeException ibse)
	    {
	    }return null;
	  }
}
