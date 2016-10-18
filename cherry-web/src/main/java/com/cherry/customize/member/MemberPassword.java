package com.cherry.customize.member;

import com.cherry.cm.core.SpringBeanManager;


/**
 * 会员密码加解密专用类
 * 各品牌会员加解密的方式可能不同，使用此类做为统一入口，方便编程。
 * 各品牌可以实现自己的加解密算法类，但是必须实现ISecretMempsw接口，且配置在xml中时，命名必须形如：
 * <bean id="memberPassword_XXX" class="com.cherry.customize.member.MemberPassword_YYY"/>
 * 其中XXX必须是品牌代码,全大写，YYY则随意，只是用于区分不同的类名，统一设置为品牌代码最为明了。
 * @author dingyongchang
 *
 */
public  class MemberPassword{

	public  static String encrypt(String brandCode,String srcData) throws Exception {
		Object ob = SpringBeanManager.getBean("memberPassword_"+brandCode.toUpperCase());
		if(null!=ob){
			//如果品牌配置了加解密专用类
			IMemberPassword secretMempsw = (IMemberPassword)ob;		
			return secretMempsw.encrypt(srcData);
		}
		//没有配置加解密专用类则原样返回
		return srcData;
	}

	public static String decrypt(String brandCode,String encryptData)throws Exception {
		Object ob = SpringBeanManager.getBean("memberPassword_"+brandCode.toUpperCase());
		if(null!=ob){
			//如果品牌配置了加解密专用类
			IMemberPassword secretMempsw = (IMemberPassword)ob;		
			return secretMempsw.decrypt(encryptData);
		}
		//没有配置加解密专用类则原样返回
		return encryptData;
	}
}