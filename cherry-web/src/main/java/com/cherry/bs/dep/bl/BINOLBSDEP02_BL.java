/*
 * @(#)BINOLBSDEP02_BL.java     1.0 2010/10/27
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

package com.cherry.bs.dep.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.service.BINOLBSDEP02_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;

/**
 * 部门详细画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSDEP02_BL {
	
	/** 部门详细画面Service */
	@Resource
	private BINOLBSDEP02_Service binOLBSDEP02_Service;
	
	/**
	 * 查询部门信息
	 * 
	 * @param map 查询条件
	 * @return 部门信息
	 */
	public Map<String, Object> getOrganizationInfo(Map<String, Object> map) {
		// 查询部门信息
		Map<String, Object> organizationMap = binOLBSDEP02_Service.getOrganizationInfo(map);
		
		String expiringDate = ConvertUtil.getString(organizationMap.get("expiringDate"));
		
		String defExpirDate = CherryConstants.longLongAfter + " " + CherryConstants.maxTime;
		
		if(expiringDate.equals(defExpirDate)){
			// 截取到期日：年月日
			organizationMap.put("expiringDateDate", "");
			// 截取到期日：时分秒
			organizationMap.put("expiringDateTime", "");
			organizationMap.put("expiringDate", "");
			
		} else {
			if(!"".equals(expiringDate)){
				// 截取到期日：年月日
				organizationMap.put("expiringDateDate", expiringDate.substring(0,10));
				// 截取到期日：时分秒
				organizationMap.put("expiringDateTime", expiringDate.substring(expiringDate.length()-8,expiringDate.length()));
			}
		}
		
		
		return organizationMap;
	}
	
	
	
	/**
	 * 查询部门地址List
	 * 
	 * @param map 查询条件
	 * @return 部门地址List
	 */
	public List<Map<String, Object>> getDepartAddressList(Map<String, Object> map) {
		
		// 查询部门地址List
		return binOLBSDEP02_Service.getDepartAddressList(map);
	}
	
	/**
	 * 取得部门联系人List
	 * 
	 * @param map 查询条件
	 * @return 部门联系人List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getDepartContactList(Map<String, Object> map) throws Exception {
		// 取得部门联系人List
		List<Map<String, Object>> departContactList = binOLBSDEP02_Service.getDepartContactList(map);
		// 解密手机号码
		decryptListData(map,departContactList);
		return departContactList;
	}
	
	/**
	 * 根据部门ID取得部门类型
	 * 
	 * @param map 查询条件
	 * @return 部门类型
	 */
	public String getDepartType(Map<String, Object> map) {
		
		// 根据部门ID取得部门类型
		return binOLBSDEP02_Service.getDepartType(map);
	}
	
	/**
	 * 取得所属部门的员工
	 * 
	 * @param map 查询条件
	 * @return 部门的员工
	 */
	public List<Map<String, Object>> getEmployeeInDepartList(Map<String, Object> map) {
		
		// 取得所属部门的员工
		return binOLBSDEP02_Service.getEmployeeInDepartList(map);
	}
	
	/**
	 * 取得管辖或者关注指定部门的人的信息
	 * 
	 * @param map 查询条件
	 * @return 管辖或者关注指定部门的人的信息
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		
		// 取得管辖或者关注指定部门的人的信息
		return binOLBSDEP02_Service.getEmployeeList(map);
	}
	/**
	 * 解密指定的人员信息List
	 * 
	 * @param employeeList
	 * @throws Exception
	 */
	private void decryptListData(Map<String, Object> map,List<Map<String, Object>> employeeList)
			throws Exception {
		if (null != employeeList && employeeList.size() > 0) {
			for (Map<String, Object> employeeInfo : employeeList) {
				//品牌Code
				String  brandCode = ConvertUtil.getString(map.get("brandCode"));
				// 手机号码解密
				if (!CherryChecker.isNullOrEmpty(employeeInfo.get("mobilePhone"), true)) {
					String  mobilePhone = ConvertUtil.getString(employeeInfo.get("mobilePhone"));
					employeeInfo.put("mobilePhone",CherrySecret.decryptData(brandCode,mobilePhone));
				}
				// 电话解密
				if(!CherryChecker.isNullOrEmpty(employeeInfo.get("phone"),true)){
					String phone = ConvertUtil.getString(employeeInfo.get("phone"));
					employeeInfo.put("phone", CherrySecret.decryptData(brandCode,phone));
				}
				// email解密
				if(!CherryChecker.isNullOrEmpty(employeeInfo.get("email"),true)){
					String phone = ConvertUtil.getString(employeeInfo.get("email"));
					employeeInfo.put("email", CherrySecret.decryptData(brandCode,phone));
				}
			}
		}
	}
}
