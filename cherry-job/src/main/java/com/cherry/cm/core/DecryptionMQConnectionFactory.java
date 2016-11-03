package com.cherry.cm.core;

import org.apache.activemq.spring.ActiveMQConnectionFactory;

/**
 * MQ连接信息解密
 * @author huzude
 *
 */
public class DecryptionMQConnectionFactory extends ActiveMQConnectionFactory{
	
	/**
	 * 重置MQ密码信息为明文
	 */
	public void setPassword(String pwd) {
		String decodePwd = DesDecode(pwd);
		super.setPassword(decodePwd);
	}
	
	/**
	 * 重置MQ用户名信息为明文
	 */
	public void setUserName(String userName) {
		String decodeUser = DesDecode(userName);
		super.setUserName(decodeUser);
	}
	/**
	 * 重置MQ连接字符串信息为明文
	 */
	public void setBrokerURL(String url) {
		if (url.equals("failover://tcp://localhost:61616")){
			return;
		}
		String decodeUrl = DesDecode(url);
		super.setBrokerURL(decodeUrl);
	}
	
	
	/* 根据MQ配置文件密码密文得到明文 */
	public String DesDecode(String mi) {
		String strDes ="";
		try {
			DESPlus des = new DESPlus("binkun");//自定义密钥
			strDes = des.decrypt(mi);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return strDes;
	}
}
