/*  
 * @(#)BINOLMOMAN05_Service.java    1.0 2011-7-29     
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

package com.cherry.mo.man.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.service.BaseService;

public class BINOLMOMAN05_Service extends BaseService {

	/**
	 * 想U盘信息表中插入数据
	 * 
	 * 
	 * */
	public void insertUdiskInfo(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLMOMAN05.insertUdiskInfo");
	}
	
	/**
	 * 检查品牌是否存在
	 * 
	 * 
	 * */
	public String isBrandExist(String brandCode){
		return (String) baseServiceImpl.get(brandCode, "BINOLMOMAN05.isBrandExist");
	}
	
	/**
	 * 取得品牌下所有品牌list
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getBrandInfoList(Map<String,Object> map){
		return (List<Map<String, Object>>) baseServiceImpl.get(map, "BINOLMOMAN05.getBrandInfoList");
	}
	/**
	 * 检查U盘是否存在
	 * 
	 * 
	 * */
	public String isUdiskExist(Map<String,Object> map){
		return (String) baseServiceImpl.get(map, "BINOLMOMAN05.isUdiskExist");
	}
	/**
	 * 检查员工是否存在是否存在
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> isEmployeeExist(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map, "BINOLMOMAN05.isEmployeeExist");
	}
	
	/**
	 * 根据U盘SN号、组织以及部门取得相应的U盘信息
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getUdiskInfo(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map, "BINOLMOMAN05.getUdiskInfo");
	}
}
