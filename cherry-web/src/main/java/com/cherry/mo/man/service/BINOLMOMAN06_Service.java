/*  
 * @(#)BINOLMOMAN06_Service.java    1.0 2011-7-28     
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLMOMAN06_Service extends BaseService {
	
	/**
	 * 取得U盘信息list
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getUdiskList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN06.getUdiskList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得U盘信息总数
	 * 
	 * 
	 * */
	public int getUdiskCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN06.getUdiskCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * U盘与员工绑定
	 * 
	 * 
	 * */
	public void employeeBind(Map<String,Object> map){
		baseServiceImpl.update(map, "BINOLMOMAN06.employeeBindOrUnbind");
	}
	
	/**
	 * 根据员工code查询员工ID
	 * 
	 * 
	 * */
	public Integer getEmployeeId(Map<String,Object> map){
		return (Integer) baseServiceImpl.get(map, "BINOLMOMAN06.getEmployeeId");
	}
	
	/**
	 * 批量解除员工绑定
	 * 
	 * 
	 * */
	public void employeeUnbind(List<Map<String,Object>> list){
		baseServiceImpl.updateAll(list, "BINOLMOMAN06.employeeBindOrUnbind");
	}
	
	/**
	 * 逻辑删除U盘信息
	 * 
	 * 
	 * */
	public void deleteUdisk(List<Map<String,Object>> list){
		baseServiceImpl.updateAll(list, "BINOLMOMAN06.deleteUdisk");
	}
	
	/**
	 * 取得U盘信息
	 * 
	 * */
	public Map<String,Object> getUdiskInfo(int uDiskId){
		return (Map<String, Object>) baseServiceImpl.get(uDiskId,"BINOLMOMAN06.getUdiskInfo");
	}
}
