/*
 * @(#)BINOLBSDEP08_BL.java     1.0 2011.2.10
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.service.BINOLBSDEP04_Service;
import com.cherry.bs.dep.service.BINOLBSDEP91_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;

/**
 * 组织编辑画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP08_BL {
	
	/** 组织信息管理共通Service */
	@Resource
	private BINOLBSDEP91_Service binOLBSDEP91_Service;
	
	/** 添加部门画面Service */
	@Resource
	private BINOLBSDEP04_Service binOLBSDEP04_Service;
	
	/**
	 * 查询组织信息
	 * 
	 * @param map 查询条件
	 * @return 组织信息
	 */
	public Map<String, Object> getOrganization(Map<String, Object> map) {
		
		// 查询组织信息
		return binOLBSDEP91_Service.getOrganization(map);
	}
	
	/**
	 * 
	 * 更新组织
	 * 
	 * @param map 更新条件
	 */
	public void tran_updateOrganization(Map<String, Object> map) throws Exception {
		
		// 取得系统时间
		String sysDate = binOLBSDEP91_Service.getSYSDate();
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		int result = binOLBSDEP91_Service.updateOrganizationInfo(map);
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
		// 查询顶级部门信息
		Map<String, Object> higherDepartMap = binOLBSDEP04_Service.getHigherDepart(map);
		Map<String, Object> orgMap = new HashMap<String, Object>();
		orgMap.putAll(map);
		// 部门ID
		orgMap.put("organizationId", higherDepartMap.get("organizationId"));
		// 部门名称
		if(map.get("orgNameChinese") != null && !"".equals(map.get("orgNameChinese"))) {
			orgMap.put("departName", map.get("orgNameChinese"));
		}
		// 部门简称
		if(map.get("orgNameShort") != null && !"".equals(map.get("orgNameShort"))) {
			orgMap.put("departNameShort", map.get("orgNameShort"));
		}
		// 部门英文名称
		if(map.get("orgNameForeign") != null && !"".equals(map.get("orgNameForeign"))) {
			orgMap.put("nameForeign", map.get("orgNameForeign"));
		}
		// 部门英文简称
		if(map.get("orgNameForeignShort") != null && !"".equals(map.get("orgNameForeignShort"))) {
			orgMap.put("nameShortForeign", map.get("orgNameForeignShort"));
		}
		result = binOLBSDEP91_Service.updateOrganization(orgMap);
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
	}

}
