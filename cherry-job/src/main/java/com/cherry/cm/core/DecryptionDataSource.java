package com.cherry.cm.core;

import org.logicalcobwebs.proxool.ProxoolDataSource;

/**
 * 重写org.logicalcobwebs.proxool.ProxoolDataSource 重置数据库密码为明文
 * 
 * @author huzd
 * 
 */
public class DecryptionDataSource extends ProxoolDataSource {
	/**
	 * 重置数据库密码信息为明文
	 */
	public void setPassword(String pwd) {
		String decodePwd = DesDecode(pwd);
		super.setPassword(decodePwd);
	}
	
    public void setMaximumConnectionLifetime(long maximumConnectionLifetime)
    {
    	//.maximumConnectionLifetime = maximumConnectionLifetime;
    	super.setMaximumConnectionLifetime((int)maximumConnectionLifetime);
    }
    
    /** 
    * @see ConnectionPoolDefinitionIF#getHouseKeepingSleepTime 
    *此处将int类型改为long类型 
    */  
   public void setHouseKeepingSleepTime(long houseKeepingSleepTime) {  
        super.setHouseKeepingSleepTime((int)houseKeepingSleepTime);  
   } 
    
	/**
	 * 重置数据库用户名信息为明文
	 */
	public void setUser(String user) {
		String decodeUser = DesDecode(user);
		super.setUser(decodeUser);
	}
	/**
	 * 重置数据库链接字符串信息为明文
	 */
	public void setDriverUrl(String url) {
		String decodeUrl = DesDecode(url);
		super.setDriverUrl(decodeUrl);
	}
	

	/* 根据数据库配置文件密码密文得到明文 */
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
