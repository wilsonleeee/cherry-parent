/*
 * @(#)BINOLSSPRM12_BL.java     1.0 2010/11/29
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
package com.cherry.ss.prm.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ss.prm.service.BINOLSSPRM12_Service;

/**
 * 
 * 促销品类别BL
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM12_BL {
	
	@Resource
	private BINOLSSPRM12_Service binolssprm12_Service;
	
	/**
	 * 取得促销品类别信息
	 * 
	 * @param map
	 * @return
	 */
	public Map searchPrmCategoryInfo(Map<String, Object> map) {
		Map<String, Object> cateInfo ;
		// 查询类别信息
		cateInfo = binolssprm12_Service.getPrmCategoryInfo(map);
		Map<String, Object> hpnMap = new HashMap<String, Object>();
		// 直属上级类别节点位置
		hpnMap.put("path", cateInfo.get("higherCategoryPath"));
		// 查询直属上级类别名称
		String hpn = binolssprm12_Service.getHigherCategoryName(hpnMap);
		cateInfo.put("higherCategoryName", hpn);
		// 取得促销品类别信息
		return  cateInfo;
	}

}
