
/*  
 * @(#)BINOLSTCM10_Service.java    1.0 2011-11-21     
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
package com.cherry.st.common.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * @author zhanggl
 * 
 * */

public class BINOLSTCM10_Service extends BaseService {

	/**
	 * 根据仓库ID取得对应的部门,带权限
	 * 
	 * */
	public List<Map<String,Object>> getDepartInfoList(Map<String,Object> param){
		param.put("userId", param.get("BIN_UserID"));
		param.put("counterKind", param.get("TestType"));
		return baseServiceImpl.getList(param,"BINOLSTCM10.getDepartInfoList");
	}
	
	/**
	 * 根据仓库ID取得对应的部门总数,带权限
	 * 
	 * */
	public int getDepartInfoCount(Map<String,Object> param){
		param.put("userId", param.get("BIN_UserID"));
		param.put("counterKind", param.get("TestType"));
		param.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM10.getDepartInfoCount");
		return baseServiceImpl.getSum(param);
	}
	
	/**
	 * 根据仓库ID取得与之关联的所有部门的上级部门(带权限或者不带权限)List,带有测试部门和正式部门控制
	 * 
	 * */
	public List<Map<String,Object>> getSupDepartList(Map<String,Object> param){
		param.put("userId", param.get("BIN_UserID"));
		param.put("counterKind", param.get("TestType"));
		return baseServiceImpl.getList(param,"BINOLSTCM10.getSupDepartList");
	}
	
	/**
	 * 根据仓库ID取得与之关联的所有部门的上级部门(带权限或者不带权限)总数,带有测试部门和正式部门控制
	 * 
	 * */
	public int getSupDeparCount(Map<String,Object> param){
		param.put("userId", param.get("BIN_UserID"));
		param.put("counterKind", param.get("TestType"));
		param.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM10.getSupDeparCount");
		return baseServiceImpl.getSum(param);
	}
	
	/**
	 * 根据仓库ID取得与其关联的所有部门的下级部门(带有权限或不带权限)List,带有测试部门和正式部门控制
	 * 
	 * */
	public List<Map<String,Object>> getChilDepartList(Map<String,Object> param){
		param.put("userId", param.get("BIN_UserID"));
		param.put("counterKind", param.get("TestType"));
		return baseServiceImpl.getList(param,"BINOLSTCM10.getChilDepartList");
	}
	
	/**
	 * 根据仓库ID取得与其关联的所有部门的下级部门(带有权限或不带权限)总数,带有测试部门和正式部门控制
	 * 
	 * */
	public int getChildDepartCount(Map<String,Object> param){
		param.put("userId", param.get("BIN_UserID"));
		param.put("counterKind", param.get("TestType"));
		param.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM10.getChildDepartCount");
		return baseServiceImpl.getSum(param);
	}
	
	/**
	 * 根据部门ID取得其对应的部门类型
	 * 
	 * 
	 * */
	public Map<String,Object> getDepartTypeByID(Integer ID){
		return (Map<String, Object>) baseServiceImpl.get(ID,"BINOLSTCM10.getDepartTypeByID");
	}
	
}
