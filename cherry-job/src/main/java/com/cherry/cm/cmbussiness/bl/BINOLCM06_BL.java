/*
 * @(#)BINOLCM06_BL.java     1.0 2011/06/29
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */

package com.cherry.cm.cmbussiness.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM06_Service;

/**
 * 工作流 共通 BL
 * 
 * @author hub
 * @version 1.0 2011.06.29
 */
public class BINOLCM06_BL {
	
	@Resource
	private BINOLCM06_Service binOLCM06_Service;
	
	/**
	 * 取得配置数据库品牌List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			配置数据库品牌List
	 */
	public List<Map<String, Object>> getConfBrandInfoList(Map<String, Object> map) {
		return binOLCM06_Service.getConfBrandInfoList(map);
	}
	
	/**
	 * 取得品牌信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			品牌信息
	 */
	public Map<String, Object> getOSBrandInfo(Map<String, Object> map) {
		return binOLCM06_Service.getOSBrandInfo(map);
	}
	
	/**
	 * 插入文件储存表
	 * 
	 * @param map
	 * @return
	 */
	public void insertFileStore(Map<String, Object> map) {
		binOLCM06_Service.insertFileStore(map);
	}
	
	/**
	 * 检查是否有指定的工作文件
	 * 
	 * @param map
	 * @return
	 */
	public boolean checkFile(Map<String, Object> map) {
		if (binOLCM06_Service.getBrandJobCount(map) > 0) {
			return true;
		}
		return false;
	}
	
	public String getBussinessDate(Map<String, Object> map) {
		return binOLCM06_Service.getBussinessDate(map);
	}
}
