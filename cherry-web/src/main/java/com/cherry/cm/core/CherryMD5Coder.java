package com.cherry.cm.core;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import org.bouncycastle.util.encoders.Hex;

/**
 * MD5摘要加密算法
 * @author shenzhigang
 */
public abstract class CherryMD5Coder {
 // 密钥算法
 public static final String KEY_ALGORITHM = "MD5";
 
 
 /**
  * 加密数据
  * @param data 待加密数据
  * @return 加密后的数据
  * */
 public static String encryptMD5(String data) throws Exception{
	 // 初始化摘要
	 MessageDigest md5 = MessageDigest.getInstance(KEY_ALGORITHM);
	 
	 byte[]  md5B = md5.digest(data.getBytes(Charset.forName("UTF-8"))); 
	 return new String(Hex.encode(md5B));
 }
 

 
 public static void main(String[] args) throws Exception {
 String source = "123456";
 String md5Expect = "e10adc3949ba59abbe56e057f20f883e";
//	 String source = "Encodes the given data with base64." +
//	 "This encoding is designed to make binary data survive transport through transport layers that are not 8-bit clean, such as mail bodies." +
//	 "Base64-encoded data takes about 33% more space than the original data.";
  System.out.println("原文"+"(" + source.length()+ "):" + source);
  System.out.println("预计摘要"+"(" + md5Expect.length()+ "):" + md5Expect);
  String encryptData = encryptMD5(source);
  System.out.println(  "实际摘要"+"(" + encryptData.length()+ "):" + encryptData);

 }
 	
 
}

