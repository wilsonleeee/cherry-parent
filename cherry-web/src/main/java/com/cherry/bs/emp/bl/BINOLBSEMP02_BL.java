/*		
 * @(#)BINOLBSEMP02_BL.java     1.0 2010/12/07		
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
package com.cherry.bs.emp.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.emp.service.BINOLBSEMP02_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;

/**
 * 员工详细 BL
 * 
 * @author lipc
 * @version 1.0 2010.12.07
 */
public class BINOLBSEMP02_BL {

	@Resource(name="binOLBSEMP02_Service")
	private BINOLBSEMP02_Service binolbsemp02Service;
	/**
	 * 取得员工信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> getEmployeeInfo (Map<String, Object> map) throws Exception {
		
		// 取得员工信息
		Map<String, Object> employeeInfo = binolbsemp02Service.getEmployeeInfo(map);
		if(employeeInfo != null && !employeeInfo.isEmpty()) {
			if(employeeInfo.get("higher") != null && !CherryConstants.DUMMY_VALUE.equals(employeeInfo.get("higher"))) {
				map.put("higher", employeeInfo.get("higher"));
				// 取得直属上级信息
				Map<String, Object> supervisor = binolbsemp02Service.getSupervisor(map);
				if(supervisor != null && !supervisor.isEmpty()) {
					employeeInfo.put("higherName", supervisor.get("employeeName"));
				}
			}
			// 所属品牌code【解密参数】
			String brandCode = ConvertUtil.getString(employeeInfo.get("brandCode"));
//			String brandCode = "-9999";
			// 对身份证、电话、email解密显示
			if(!CherryChecker.isNullOrEmpty(employeeInfo.get("identityCard"),true)){
				String identityCard = ConvertUtil.getString(employeeInfo.get("identityCard"));
				employeeInfo.put("identityCard", CherrySecret.decryptData(brandCode,identityCard));
			}
			if(!CherryChecker.isNullOrEmpty(employeeInfo.get("phone"),true)){
				String phone = ConvertUtil.getString(employeeInfo.get("phone"));
				employeeInfo.put("phone", CherrySecret.decryptData(brandCode,phone));
			}
			if(!CherryChecker.isNullOrEmpty(employeeInfo.get("mobilePhone"),true)){
				String mobilePhone = ConvertUtil.getString(employeeInfo.get("mobilePhone"));
				employeeInfo.put("mobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
			}
			if(!CherryChecker.isNullOrEmpty(employeeInfo.get("email"),true)){
				String email = ConvertUtil.getString(employeeInfo.get("email"));
				employeeInfo.put("email", CherrySecret.decryptData(brandCode,email));
			}
		}
		return employeeInfo;
	}
	
	/**
	 * 取得员工地址List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmpAddressList (Map<String, Object> map) {
		
		return binolbsemp02Service.getEmpAddressList(map);
	}
	
	/**
	 * 取得员工入离职List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmpQuitList (Map<String, Object> map) {
		
		return binolbsemp02Service.getEmpQuitList(map);
	}
	
	/**
	 * 取得员工部门、岗位信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPostDistList (Map<String, Object> map) {
		
		return binolbsemp02Service.getPostDistList(map);
	}
	
	/**
	 * 取得直属上级
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSupervisor (Map<String, Object> map) {
		
		return binolbsemp02Service.getSupervisor(map);
	}
	
	/**
	 * 取得直属下级List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getJuniorList (Map<String, Object> map) {
		
		return binolbsemp02Service.getJuniorList(map);
	}
	
	/**
	 * 取得管辖部门List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeDepartList (Map<String, Object> map) {
		
		return binolbsemp02Service.getEmployeeDepartList(map);
	}
	
	/**
	 * 取得关注用户List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getLikeEmployeeList (Map<String, Object> map) {
		
		return binolbsemp02Service.getLikeEmployeeList(map);
	}
	
    /**
     * 取得BAS考勤信息总数
     * 
     * @param map
     * @return BAS考勤信息总数
     */
    public int getBASAttendanceCount(Map<String, Object> map) {
        return binolbsemp02Service.getBASAttendanceCount(map);
    }
	
    /**
     * 取得BAS考勤信息List
     * 
     * @param map
     * @return BAS考勤信息List
     */
    public List<Map<String, Object>> getBASAttendanceList (Map<String, Object> map) {
        return binolbsemp02Service.getBASAttendanceList(map);
    }
    
    /**
     * 取得部门权限总数
     * 
     * @param map 查询条件
     * @return 返回部门权限总数
     */
    public int getDepartPrivilegeCount(Map<String, Object> map) {
        return binolbsemp02Service.getDepartPrivilegeCount(map);
    }
	
    /**
     * 取得部门权限List
     * 
     * @param map 查询条件
     * @return 返回部门权限List
     */
    public List<Map<String, Object>> getDepartPrivilegeList (Map<String, Object> map) {
        return binolbsemp02Service.getDepartPrivilegeList(map);
    }
    
    /**
     * 取得人员权限总数
     * 
     * @param map 查询条件
     * @return 返回人员权限总数
     */
    public int getEmployeePrivilegeCount(Map<String, Object> map) {
        return binolbsemp02Service.getEmployeePrivilegeCount(map);
    }
	
    /**
     * 取得人员权限List
     * 
     * @param map 查询条件
     * @return 返回人员权限List
     */
    public List<Map<String, Object>> getEmployeePrivilegeList (Map<String, Object> map) {
        return binolbsemp02Service.getEmployeePrivilegeList(map);
    }
    
    /**
	 * 取得被关注用户List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBeLikedEmployeeList (Map<String, Object> map) {
		
		return binolbsemp02Service.getBeLikedEmployeeList(map);
	}
}
