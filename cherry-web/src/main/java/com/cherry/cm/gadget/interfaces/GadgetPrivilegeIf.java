package com.cherry.cm.gadget.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface GadgetPrivilegeIf extends ICherryInterface {
	
	/**
	 * 保存数据权限到MongoDB
	 * 
	 * @param map 保存内容
	 */
	public void savePrivilegeToMongoDB(Map<String, Object> map) throws Exception;

}
