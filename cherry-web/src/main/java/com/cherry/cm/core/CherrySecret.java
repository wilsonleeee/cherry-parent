package com.cherry.cm.core;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class CherrySecret {

	private static HashMap<String, CherryKeyStore> keyMap = new HashMap<String, CherryKeyStore>();
	private static final String DES_KEY = "pekon";
	// 密钥算法
	public static final String KEY_ALGORITHM = "RSA";
	

	// RSA密钥长度 ，默认是1024，长度必须是64的整数倍，范围在512~65536之间
	private static final int KEY_SIZE = 512;

	/**
	 * 非对称性加密算法RSA获取密钥对
	 * 
	 * @return
	 * @throws Exception
	 */
	private static KeyPair initKeyPairRSA() throws Exception {
		// 实例化密钥对生成器
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		// 初始化密钥对生成器
		keyPairGen.initialize(KEY_SIZE);
		// 生成密钥对
		KeyPair keypair = keyPairGen.generateKeyPair();
		return keypair;
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
		// 取得公钥
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		// 加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
		// 取得私钥
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		// 加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 公钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
		// 取得公钥
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		// 解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
		// 取得私钥
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		// 解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * DES加密
	 * 
	 * @param strKey
	 *            密钥
	 * @param arrB
	 *            待加密
	 * @return
	 * @throws Exception
	 */
	private static byte[] encryptDES(String strKey, byte[] arrB) throws Exception {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		Key key = getKeyDES(strKey.getBytes());

		Cipher encryptCipher = Cipher.getInstance("DES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);
		return encryptCipher.doFinal(arrB);
	}

	private static byte[] decryptDES(String strKey, byte[] arrB) throws Exception {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		Key key = getKeyDES(strKey.getBytes());

		Cipher decryptCipher = Cipher.getInstance("DES");
		decryptCipher.init(Cipher.DECRYPT_MODE, key);
		return decryptCipher.doFinal(arrB);
	}

	static Key getKeyDES(byte[] arrBTmp) throws Exception {
		// 创建一个空的8位字节数组（默认值为0）
		byte[] arrB = new byte[8];

		// 将原始字节数组转换为8位
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		// 生成密钥
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

		return key;
	}

	private static byte[] initkeyAES(String seed) throws Exception {
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance("AES"); // 实例化密钥生成器
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		SecureRandom secureRandom;
        if (seed != null && !"".equals(seed)) {
            secureRandom = new SecureRandom(seed.getBytes("UTF-8"));
            kg.init(128,secureRandom);
        } else {
        	kg.init(128); // 初始化密钥生成器:AES要求密钥长度为128,192,256位
        }
		SecretKey secretKey = kg.generateKey(); // 生成密钥
		return secretKey.getEncoded(); // 获取二进制密钥编码形式
	}

	/**
	 * 用品牌自己的key进行AES加密
	 * 
	 * @param brandCode
	 * @param srcData
	 * @return
	 * @throws Exception
	 */
	public static String encryptData(String brandCode, String srcData) throws Exception {
		if(null==srcData||"".equals(srcData)){
			return srcData;
		}
		
		if(!keyMap.containsKey(brandCode)){
			return srcData;
		}
		CherryKeyStore store = keyMap.get(brandCode);
		if (store == null || store.getSecretKey() == null) {
			throw new Exception("Cherry SecretKey missing");
		}

		try {
			return store.encryptAES(srcData);
		} catch (Exception e) {
			throw new Exception("Cherry SecretKey encryptData error");
		}
	}

	/**
	 * 用品牌自己的key进行AES解密
	 * 
	 * @param brandCode
	 * @param srcData
	 * @return
	 * @throws Exception
	 */
	public static String decryptData(String brandCode, String srcData) throws Exception {
		if(null==srcData||"".equals(srcData)||"null".equalsIgnoreCase(srcData)){
			return srcData;
		}	
		
		if(!keyMap.containsKey(brandCode)){
			return srcData;
		}

		CherryKeyStore store = keyMap.get(brandCode);
		if (store == null || store.getSecretKey() == null) {
			throw new Exception("Cherry SecretKey missing");
		}

		try {
			return store.decryptAES(srcData);
		} catch (Exception e) {
			throw new Exception("Cherry SecretKey decryptData error");
		}
	}

//	/**
//	 * 加密数据，返回Base64编码的格式
//	 * 
//	 * @param srcData
//	 * @return
//	 * @throws Exception
//	 */
//	public static String encryptData(String srcData) throws Exception {
//		return encryptData("-9999", srcData);
//	}
//
//	/**
//	 * 解密数据，要求参数是Base64编码的格式
//	 * 
//	 * @param srcData
//	 * @return
//	 * @throws Exception
//	 */
//	public static String decryptData(String srcData) throws Exception {
//		return decryptData("-9999", srcData);
//	}

	public static void putKeyStore(CherryKeyStore store) throws Exception {
		if (store.getPublicKeyBase64() != null && !"".equals(store.getPublicKeyBase64())) {
			store.setPublicKey(decryptDES(DES_KEY, Base64.decodeBase64(store.getPublicKeyBase64())));
		}
		if (store.getPrivateKeyBase64() == null || "".equals(store.getPrivateKeyBase64())) {
			throw new Exception("Cherry PrivateKey missing");
		}

		store.setPrivateKey(decryptDES(DES_KEY, Base64.decodeBase64(store.getPrivateKeyBase64())));
		// 公钥加密 私钥解密
		store.setSecretKey(new SecretKeySpec(decryptByPrivateKey(Base64.decodeBase64(store.getSecretKeyBase64()), store.getPrivateKey()), "AES"));
		// 使用PKCS7Padding填充方式,这里就得这么写了(即调用BouncyCastle组件实现)
		// 实例化Cipher对象，它用于完成实际的加密操作
		Cipher cipherEn = Cipher.getInstance("AES/ECB/PKCS5Padding");
		// 初始化Cipher对象，设置为加密模式
		cipherEn.init(Cipher.ENCRYPT_MODE, store.getSecretKey());
		store.setCipherEncryptAES(cipherEn);

		Cipher cipherDe = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipherDe.init(Cipher.DECRYPT_MODE, store.getSecretKey());
		store.setCipherDecryptAES(cipherDe);

		keyMap.put(store.getBrandCode(), store);
	}

	public static CherryKeyStore getKeyStore(String brandCode) {
		return keyMap.get(brandCode);
	}
	
	/**
	 * 判断一个品牌是否开启了加密
	 * @param brandCode
	 * @return
	 */
	public static boolean isOpenEncrypt(String brandCode){
		if(keyMap.containsKey(brandCode)){
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
//		KeyPair keypair = initKeyPairRSA();
//		PublicKey pubKey = keypair.getPublic();
//		PrivateKey priKey = keypair.getPrivate();
//		System.out.println("RSA公钥Base64编码：");
//		System.out.println(Base64.encodeBase64String(pubKey.getEncoded()));
//		System.out.println("RSA私钥Base64编码：");
//		System.out.println(Base64.encodeBase64String(priKey.getEncoded()));
//		// //
//		System.out.println("RSA公钥 DES加密后再Base64编码：");
//		System.out.println(Base64.encodeBase64String(encryptDES("pekon", pubKey.getEncoded())));
//		System.out.println("RSA私钥 DES加密后再Base64编码：");
//		System.out.println(Base64.encodeBase64String(encryptDES("pekon", priKey.getEncoded())));
//		// // //
//		byte[] aesKey = initkeyAES(null);
//		System.out.println("AESKey Base64编码后：");
//		System.out.println(Base64.encodeBase64String(aesKey));
//		System.out.println("AESKey RSA公钥加密再Base64编码：");
//		System.out.println(Base64.encodeBase64String(encryptByPublicKey(aesKey, pubKey.getEncoded())));
//		System.out.println(Base64.encodeBase64String(decryptByPrivateKey(encryptByPublicKey(aesKey, pubKey.getEncoded()), priKey.getEncoded())));


		CherryKeyStore store = new CherryKeyStore();
		store.setPrivateKeyBase64("90im8otkTdy2UdglBio2t+2VC3XbqSspwdSIzR1dpyP48xyCdsAs90zGM7EnsG7C+MKXFQFw/GmaPavZJFj7dMbGlivd52jZPWFUOXDQ3fwItDsoQ0M3y72iP7IeNKaGxcGmWAOpixbcJ4L1SLZR79kEmNSyMEMsJFc2hQkSYuvrrJeK4b8vK2zu/NOufapfEMf/zvQH5NjmTOK5GMSI4dCY+EOR+4xscgU4swmelfvQ8HptAygrwKi03TH2wi0ICwDdnm549HQ8s8xJAVB7za0BKyEd7tdYpgvOHMHS16MC0Z7aXc87aFMkoueqenQt6UDtRWfLtdcIp7ibtZdpCpLS4BDmfuax2tdONtaWaXYtHpdvhRHgXkJpAdA/fMRlHuOVoO1JooKzvC5f9zP98hBgmmpuMrd9KnJl6CgYIRZRhn6Jrnh4yw5+VzFNY2e475MvXMs3bL4=");
		store.setPublicKeyBase64("m2TL/qytzruMEUXHnjJlqNEjkihi2722+xr/YcdG7MCXhxhEamRTFM6rh8Pja32tgOGOXTPOBs+FkToqbn43uyvirge0pU+JVbB5ziOQl7t4t1uDY47ACfPj9t1lq5Rt");
		store.setBrandCode("-9999");
		store.setSecretKeyBase64("gqf/hsGeZjO3+T3esg8sWRUOElhh0GmH1rQmRIf65OCF+AvlnyIHm2gIKb2IErV8z03CAwjb4bemxjqDZUZzjg==");

		putKeyStore(store);
//		// store.setSecretKey(new SecretKeySpec(initkeyAES(),"AES"));
		String str1 = "fxcYqRZPbLVlKztuqH2qWQ==";
//		String str2 = CherrySecret.encryptData(str1);
		String str3 = CherrySecret.decryptData("-9999",str1);
//		System.out.println(str1);
//		System.out.println(str2);
		System.out.println(str3);
//		System.out.println(str1.equals(str3));


	}
	
	
	  /** 
	    * Convert byte[] to hex string. 把字节数组转化为字符串 
	    * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。 
	    * @param src byte[] data 
	    * @return hex string 
	    */     
	   public static String bytesToHexString(byte[] src){  
	       StringBuilder stringBuilder = new StringBuilder("");  
	       if (src == null || src.length <= 0) {  
	           return null;  
	       }  
	       for (int i = 0; i < src.length; i++) {  
	           int v = src[i] & 0xFF;  
	           String hv = Integer.toHexString(v);  
	           if (hv.length() < 2) {  
	               stringBuilder.append(0);  
	           }  
	           stringBuilder.append(hv+" ");  
	       }  
	       return stringBuilder.toString();  
	   }  
	   /** 
	    * Convert hex string to byte[]   把为字符串转化为字节数组 
	    * @param hexString the hex string 
	    * @return byte[] 
	    */  
	   public static byte[] hexStringToBytes(String hexString) {  
	       if (hexString == null || hexString.equals("")) {  
	           return null;  
	       }  
	       hexString = hexString.toUpperCase();  
	       int length = hexString.length() / 2;  
	       char[] hexChars = hexString.toCharArray();  
	       byte[] d = new byte[length];  
	       for (int i = 0; i < length; i++) {  
	           int pos = i * 2;  
	           d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	       }  
	       return d;  
	   }  
	   /** 
	    * Convert char to byte     
	    * @param c char 
	    * @return byte  
	   */  
	    private static byte charToByte(char c) {  
	       return (byte) "0123456789ABCDEF".indexOf(c);  
	   } 
}

class CherryKeyStore {
	private String orgCode;
	private String brandCode;
	private String publicKeyBase64;
	private String privateKeyBase64;
	private String secretKeyBase64;
	private byte[] publicKey;
	private byte[] privateKey;
	private Key secretKey;
	private Cipher cipherEncryptAES;
	private Cipher cipherDecryptAES;
	public static boolean exceptionFlag = false;

	public String encryptAES(String data) throws Exception {
		if(exceptionFlag){
			throw new Exception("品牌"+brandCode+"加解密异常");
		}
		return Base64.encodeBase64String(cipherEncryptAES.doFinal(data.getBytes(Charset.forName("UTF-8"))));
	}

	public String decryptAES(String data) throws Exception {
		if(exceptionFlag){
			throw new Exception("品牌"+brandCode+"加解密异常");
		}
		return new String(cipherDecryptAES.doFinal(Base64.decodeBase64(data)),Charset.forName("UTF-8"));
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getPublicKeyBase64() {
		return publicKeyBase64;
	}

	public void setPublicKeyBase64(String publicKeyBase64) {
		this.publicKeyBase64 = publicKeyBase64;
	}

	public String getPrivateKeyBase64() {
		return privateKeyBase64;
	}

	public void setPrivateKeyBase64(String privateKeyBase64) {
		this.privateKeyBase64 = privateKeyBase64;
	}

	public byte[] getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(byte[] publicKey) {
		this.publicKey = publicKey;
	}

	public byte[] getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(byte[] privateKey) {
		this.privateKey = privateKey;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getSecretKeyBase64() {
		return secretKeyBase64;
	}

	public void setSecretKeyBase64(String secretKeyBase64) {
		this.secretKeyBase64 = secretKeyBase64;
	}

	public Key getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(Key secretKey) {
		this.secretKey = secretKey;
	}

	public Cipher getCipherEncryptAES() {
		return cipherEncryptAES;
	}

	public void setCipherEncryptAES(Cipher cipherEncryptAES) {
		this.cipherEncryptAES = cipherEncryptAES;
	}

	public Cipher getCipherDecryptAES() {
		return cipherDecryptAES;
	}

	public void setCipherDecryptAES(Cipher cipherDecryptAES) {
		this.cipherDecryptAES = cipherDecryptAES;
	}
}
