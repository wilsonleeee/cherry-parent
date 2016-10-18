
/*  
 * @(#)UdiskSynchroService.java    1.0 2011-10-31     
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
package com.cherry.synchro.mo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.synchro.common.BaseSynchroService;

@SuppressWarnings("unchecked")
public class UdiskSynchroService {

	@Resource
	private BaseServiceImpl baseServiceImpl;
	@Resource
	private BaseSynchroService baseSynchroService;
	
	/**
	 * 根据员工ID获取员工基本信息以及其所属岗位(类别)信息
	 * 
	 * */
	public Map<String,Object> getEmplyeeInfo(int employeeId)
	{
		return (Map<String, Object>) baseServiceImpl.get(employeeId,"UdiskSynchro.getEmplyeeInfo");
	}
	
	/**
	 *根据员工ID获取该员工所管辖和关注的所有的柜台CODE 
	 * 
	 * */
	public List<Map<String,Object>> getCounterCode(int employeeId){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("employeeId", employeeId);
		param.put("businessType", "0");
		param.put("operationType", "0");
		return baseServiceImpl.getList(param, "UdiskSynchro.getCounterCode");
	}
	
	/**
	 * U盘绑定
	 * 
	 * */
	public void bindUdisk(Map<String,Object> paramMap){
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "UdiskSynchro.bindUdisk");
		baseSynchroService.execProcedure(paramMap);
	}
	
	/**
	 * 调用存放过程进行解除U盘与员工之间的绑定关系或者逻辑删除U盘信息
	 * 
	 * */
	public void unbindUdisk(Map<String,Object> paramMap){
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "UdiskSynchro.unbindUdisk");
		baseSynchroService.execProcedure(paramMap);
	}
	
	/**
	 * 添加U盘
	 * 
	 * */
	public void addUdisk(Map<String,Object> paramMap){
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "UdiskSynchro.addUdisk");
		baseSynchroService.execProcedure(paramMap);
	}
	
	/**
	 * 根据岗位类别取得岗位级别
	 * 
	 * */
	public int getPositionGrade(Map<String,Object> paramMap){
		return (Integer)baseServiceImpl.get(paramMap, "UdiskSynchro.getPositionGrade");
	}
}
