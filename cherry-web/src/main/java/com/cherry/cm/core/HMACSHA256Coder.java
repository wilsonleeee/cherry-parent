package com.cherry.cm.core;

import java.security.Security;
import java.security.MessageDigest;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

/**
 * HmacSHA消息摘要组件
 * @author shenzg
 *
 */
public abstract class HMACSHA256Coder {

   /**
    * 初始化HmacSHA256密钥
    * @return byte[]密钥
    * @throws Exception
    */
	public static byte[] initHmacSHA256Key() throws Exception{
	// 加入BouncyCastleProvider支持
	Security.addProvider(new BouncyCastleProvider());
	// 初始化KeyGenerator
	KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
	// 产生私钥
	SecretKey secretKey = keyGenerator.generateKey();
	// 获取私钥
	return secretKey.getEncoded();
	
	}
	
	/**
	 * HmacSHA256消息摘要
	 * @param data 待做消息摘要处理的数据
	 * @param 密钥
	 * @return byte[]消息摘要
	 * @throws Exception
	 * 
	 */
	public static byte[] encodeHmacSHA256(byte[] data,byte[] key) throws Exception{
		
		// 加入BouncyCastleProvider支持
		Security.addProvider(new BouncyCastleProvider());
		// 还原密钥
		SecretKey secretKey = new SecretKeySpec(key,"HmacSHA256");
		// 实例化mac
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		// 初始化mac
		mac.init(secretKey);
		// 执行消息摘要
		return mac.doFinal(data);
		
	}
	
	/**
	 * HmacSHA256Hex消息摘要
	 * @param data 待做消息摘要处理的数据
	 * @param 密钥
	 * @return String消息摘要
	 * @throws Exception
	 */
	public static String encodeHmacSHA256Hex(byte[] data,byte[] key) throws Exception{
		
		// 执行消息摘要
		byte[] b = encodeHmacSHA256(data,key);
		// 16进制转换
		return new String(Hex.encode(b));
	}
	
	
	/**
	 * SHA256消息摘要
	 * @param data 待做消息摘要处理的数据
	 * @return byte[]消息摘要
	 * @throws Exception
	 * 
	 */
	public static byte[] encodeSHA256(byte[] data) throws Exception{
		
		// 加入BouncyCastleProvider支持
		Security.addProvider(new BouncyCastleProvider());
		// 初始化MessageDigest
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		// 执行消息摘要
		return md.digest(data);
		
	}
	
	
	/**
	 * SHA256Hex消息摘要
	 * @param data 待做消息摘要处理的数据
	 * @return String消息摘要
	 * @throws Exception
	 */
	public static String encodeSHA256Hex(byte[] data) throws Exception{
		
		// 执行消息摘要
		byte[] b = encodeSHA256(data);
		// 16进制转换
		return new String(Hex.encode(b));
	}
	
	
	
}
