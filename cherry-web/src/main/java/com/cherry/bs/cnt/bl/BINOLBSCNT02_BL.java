/*	
 * @(#)BINOLBSCNT02_BL.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cnt.service.BINOLBSCNT02_Service;
import com.cherry.bs.dep.service.BINOLBSDEP02_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * 	柜台详细画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT02_BL {
	
	/** 柜台详细画面Service */
	@Resource
	private BINOLBSCNT02_Service binOLBSCNT02_Service;
	
	/** 部门详细画面Service */
	@Resource
	private BINOLBSDEP02_Service binOLBSDEP02_Service;
	
	/**
	 * 取得柜台详细信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		
		// 取得柜台详细信息
		Map<String, Object> counterInfo = binOLBSCNT02_Service.getCounterInfo(map);
		if(counterInfo != null && !counterInfo.isEmpty()) {
			map.put("organizationId", counterInfo.get("organizationId"));
			map.put("higherDepartPath", counterInfo.get("higherDepartPath"));
			// 取得管辖或者关注指定柜台的人的信息
			counterInfo.put("employeeList", binOLBSCNT02_Service.getEmployeeList(map));
//			// 取得上级部门信息
//			counterInfo.put("higherDepart", binOLBSCNT02_Service.getHigherDepart(map));
			String expiringDate = ConvertUtil.getString(counterInfo.get("expiringDate"));
			
			String defExpirDate = CherryConstants.longLongAfter + " " + CherryConstants.maxTime;
			if(expiringDate.equals(defExpirDate)){
				// 截取到期日：年月日
				counterInfo.put("expiringDateDate", "");
				// 截取到期日：时分秒
				counterInfo.put("expiringDateTime", "");
				counterInfo.put("expiringDate", "");
				
			} else {
				if(!"".equals(expiringDate)){
					// 截取到期日：年月日
					counterInfo.put("expiringDateDate", expiringDate.substring(0,10));
					// 截取到期日：时分秒
					counterInfo.put("expiringDateTime", expiringDate.substring(expiringDate.length()-8,expiringDate.length()));
				}
			}
			
			
			// 取得所属部门的员工
			counterInfo.put("employeeInDepartList", binOLBSDEP02_Service.getEmployeeInDepartList(map));
		}
		
		return counterInfo;
	}
	
	/**
	 * 取得柜台事件信息总数
	 * 
	 * @param map
	 * @return
	 */
	public int getCounterEventCount(Map<String, Object> map) {
		
		// 取得柜台事件信息总数
		return binOLBSCNT02_Service.getCounterEventCount(map);
	}
	
	/**
	 * 取得柜台事件信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterEventList(Map<String, Object> map) {
		
		// 取得柜台事件信息List
		return binOLBSCNT02_Service.getCounterEventList(map);
	}
	
	/**
	 * 取得柜台方案信息总数
	 * @author ZhaoChaoFan
	 * @param map
	 * @return
	 */
	public int getCounterSolutionCount(Map<String, Object> map) {
		// 取得柜台方案信息总数
		return binOLBSCNT02_Service.getCounterSolutionCount(map);
	}
	
	/**
	 * 取得柜台方案信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterSolutionList(Map<String, Object> map) {
		// 取得柜台方案信息List
		return binOLBSCNT02_Service.getCounterSolutionList(map);
	}

}
