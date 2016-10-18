package com.cherry.cm.core;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


/**
 * AES对称加密算法
 * @author shenzhigang
 */
public abstract class CherryAESCoder {
 // 密钥算法
 public static final String KEY_ALGORITHM = "AES";
 
 //加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,在128位--16字节下兼容C#的PKCS7Padding
 public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
 
	private static final String ALGORITHM = "RSA";
	
	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	
	private static final String DEFAULT_CHARSET = "UTF-8";
	
	//将Cipher以密钥做键存放起来
	private static Map<String,Cipher> enCipherMap= new ConcurrentHashMap<String,Cipher>();
	private static Map<String,Cipher> deCipherMap= new ConcurrentHashMap<String,Cipher>();
	
	static Cipher getEnCipher(String key) {
		if (enCipherMap.containsKey(key)) {
			return enCipherMap.get(key);
		}
		try {
			//使用PKCS7Padding填充方式,这里就得这么写了(即调用BouncyCastle组件实现)		
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);// 实例化Cipher对象，它用于完成实际的加密操作
			//使用PKCS7Padding填充方式,这里就得这么写了(即调用BouncyCastle组件实现)
			Key k = toKey(Base64.decodeBase64(key));
			cipher.init(Cipher.ENCRYPT_MODE, k);// 加密模式
			enCipherMap.put(key, cipher);
			return cipher;
		} catch (Exception ex) {
			return null;
		}
	}

	static Cipher getDeCipher(String key) {
		if (deCipherMap.containsKey(key)) {
			return deCipherMap.get(key);
		}
		try {
			// 如果没有该密钥对应的cipher，则新建并存放
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);// 实例化Cipher对象，它用于完成实际的加密操作
			Key k = toKey(Base64.decodeBase64(key));
			cipher.init(Cipher.DECRYPT_MODE, k);// 解密模式
			deCipherMap.put(key, cipher);
			return cipher;
		} catch (Exception ex) {
			return null;
		}
	}

		
	
 /**
  * 生成密钥
  */
 public static String initkey() throws Exception{
	 KeyGenerator kg = null;  
	 try {  
		 kg = KeyGenerator.getInstance(KEY_ALGORITHM); //实例化密钥生成器
     } catch (NoSuchAlgorithmException e) {  
         e.printStackTrace();  
         return null;  
     }  
	  kg.init(128);                                              //初始化密钥生成器:AES要求密钥长度为128,192,256位
	  SecretKey secretKey = kg.generateKey();                    //生成密钥
	  return Base64.encodeBase64String(secretKey.getEncoded());  //获取二进制密钥编码形式
 }
 
 
 /**
  * 转换密钥
  */
 public static Key toKey(byte[] key) throws Exception{
  return new SecretKeySpec(key, KEY_ALGORITHM);
 }
 
 
 /**
  * 加密数据
  * @param data 待加密数据
  * @param key  密钥
  * @return 加密后的数据
  * */
 public static String encrypt(String data, String key) throws Exception{
  //Key k = toKey(Base64.decodeBase64(key));                           //还原密钥
  //使用PKCS7Padding填充方式,这里就得这么写了(即调用BouncyCastle组件实现)
  //Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
  //System.out.println(getCipher().hashCode());
 // Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);              
 // encryptCipher.init(Cipher.ENCRYPT_MODE, k);                               //初始化Cipher对象，设置为加密模式
  return Base64.encodeBase64String(getEnCipher(key).doFinal(data.getBytes(Charset.forName("UTF-8")))); //执行加密操作。加密后的结果通常都会用Base64编码进行传输
 }
 
 
 /**
  * 解密数据
  * @param data 待解密数据
  * @param key  密钥
  * @return 解密后的数据
  * */
 public static String decrypt(String data, String key) throws Exception{
//  Key k = toKey(Base64.decodeBase64(key));
//  Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
//  cipher.init(Cipher.DECRYPT_MODE, k);                          //初始化Cipher对象，设置为解密模式
  return new String(getDeCipher(key).doFinal(Base64.decodeBase64(data)),Charset.forName("UTF-8")); //执行解密操作
 }
 
 
	public static void main(String[] args) throws Exception {
		String source = "Base64是一种基于64个可打印字符来表示二进制数据的表示方法。由于2的6次方等于64，所以每6个位元为一个单元，对应某个可打印字符。三个字节有24个位元，对应于4个Base64单元，即3个字节需要用4个可打印字符来表示。它可用来作为电子邮件的传输编码。在Base64中的可打印字符包括字母A-Z、a-z、数字0-9 ，这样共有62个字符，此外两个可打印符号在不同的系统中而不同。"
				+ "一些如uuencode的其他编码方法，和之后binhex的版本使用不同的64字符集来代表6个二进制数字，但是它们不叫Base64。"
				+ "Base64常用于在通常处理文本数据的场合，表示、传输、存储一些二进制数据。包括MIME的email，email via MIME, 在XML中存储复杂数据";
		// String source = "Encodes the given data with base64." +
		// "This encoding is designed to make binary data survive transport through transport layers that are not 8-bit clean, such as mail bodies."
		// +
		// "Base64-encoded data takes about 33% more space than the original data.";
		System.out.println("原文" + "(" + source.length() + "):" + source);

		// String key = initkey();//"KwBHRgqFEygN1VZC2TR7Qw=="
		// System.out.println(key);
		String key = "KwBHRgqFEygN1VZC2TR7Qw==";
		System.out.println(encrypt("abc123处理文本",key));

		// System.out.println("start");
		// long startTime = System.currentTimeMillis();
		// for(int i=0;i<1;i++){
		// encrypt(source,key);
		// }
		// long executionTime = System.currentTimeMillis() - startTime;
		// System.out.println("end:"+executionTime);
//		 MyThread mt1 = new MyThread();
//		 mt1.start();
//		
//		 MyThread2 mt2 = new MyThread2();
//		 mt2.start();

	}

 public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					com.cherry.cm.pay.bl.Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return com.cherry.cm.pay.bl.Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
}
class MyThread extends Thread{
	public void run(){
		try { 
			for(int i=0;i<100000;i++){
			System.out.println(i+":" +CherryAESCoder.encrypt("123456", "KwBHRgqFEygN1VZC2TR7Qw=="));
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
class MyThread2 extends Thread{
	public void run(){
		try { 
			for(int i=0;i<100000;i++){
			System.out.println(i+":" +CherryAESCoder.decrypt("a8Fa5rQmNMXCVh6pLj79oQ==", "KwBHRgqFEygN1VZC2TR7Qw=="));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
