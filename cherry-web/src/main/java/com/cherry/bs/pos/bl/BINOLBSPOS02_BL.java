/*
 * @(#)BINOLBSPOS02_BL.java     1.0 2010/10/27
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

package com.cherry.bs.pos.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.service.BINOLBSPOS02_Service;

/**
 * 岗位详细画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS02_BL {
	
	/** 岗位详细画面Service */
	@Resource
	private BINOLBSPOS02_Service binOLBSPOS02_Service;
	
	/**
	 * 查询岗位信息
	 * 
	 * @param map 查询条件
	 * @return 岗位信息
	 */
	public Map<String, Object> getPositionInfo(Map<String, Object> map) {
		
		// 查询岗位信息
		Map<String, Object> positionInfo = binOLBSPOS02_Service.getPositionInfo(map);
		Map<String, Object> hpnMap = new HashMap<String, Object>();
		// 直属上级岗位节点位置
		hpnMap.put("path", positionInfo.get("higherPositionPath"));
		// 查询直属上级岗位名称
		String hpn = binOLBSPOS02_Service.getHigherPositionName(hpnMap);
		positionInfo.put("higherPositionName", hpn);
		List<Map<String, Object>> employeeList = binOLBSPOS02_Service.getEmployeeByPos(map);
		positionInfo.put("employeeList", employeeList);
		return  positionInfo;
	}
	
	/**
	 * 查询员工信息
	 * 
	 * @param map 查询条件
	 * @return 用户信息
	 */
	public Map<String, Object> getEmployeeInfo(Map<String, Object> map) {
		
		// 查询员工信息
		Map<String, Object> employeeInfo = binOLBSPOS02_Service.getEmployeeInfo(map);
		// 取得员工管辖的柜台List
		List<Map<String, Object>> counterList = binOLBSPOS02_Service.getCounterList(map);
		employeeInfo.put("counterList", counterList);
		return  employeeInfo;
	}
	
	/**
	 * 查询柜台信息
	 * 
	 * @param map 查询条件
	 * @return 柜台信息
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		
		// 查询柜台信息
		Map<String, Object> counterInfo = binOLBSPOS02_Service.getCounterInfo(map);
		
		return  counterInfo;
	}

}
