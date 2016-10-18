/*
 * @(#)BINOLBSDEP12_BL.java     1.0 2011.2.10
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

import com.cherry.bs.dep.service.BINOLBSDEP92_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;

/**
 * 品牌编辑画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP12_BL {
	
	/** 品牌信息管理共通Service */
	@Resource
	private BINOLBSDEP92_Service binOLBSDEP92_Service;
	
	/**
	 * 取得品牌信息
	 * 
	 * @param map 查询条件
	 * @return 品牌信息
	 */
	public Map<String, Object> getBrandInfo(Map<String, Object> map) {
		
		// 取得品牌信息
		return binOLBSDEP92_Service.getBrandInfo(map);
	}
	
	/**
	 * 
	 * 更新品牌
	 * 
	 * @param map 更新条件
	 */
	public void tran_updateBrandInfo(Map<String, Object> map) throws Exception {
		
		// 取得系统时间
		String sysDate = binOLBSDEP92_Service.getSYSDate();
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		int result = binOLBSDEP92_Service.updateBrandInfo(map);
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
		Map<String, Object> orgMap = new HashMap<String, Object>();
		orgMap.putAll(map);
		// 原部门代码
		orgMap.put("oldDepartCode", map.get("oldBrandCode"));
		// 部门名称
		if(map.get("brandNameChinese") != null && !"".equals(map.get("brandNameChinese"))) {
			orgMap.put("departName", map.get("brandNameChinese"));
		}
		// 部门简称
		if(map.get("brandNameShort") != null && !"".equals(map.get("brandNameShort"))) {
			orgMap.put("departNameShort", map.get("brandNameShort"));
		}
		// 部门英文名称
		if(map.get("brandNameForeign") != null && !"".equals(map.get("brandNameForeign"))) {
			orgMap.put("nameForeign", map.get("brandNameForeign"));
		}
		// 部门英文简称
		if(map.get("brandNameForeignShort") != null && !"".equals(map.get("brandNameForeignShort"))) {
			orgMap.put("nameShortForeign", map.get("brandNameForeignShort"));
		}
		// 更新部门信息
		result = binOLBSDEP92_Service.updateOrganizationInfo(orgMap);
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
	}

}
