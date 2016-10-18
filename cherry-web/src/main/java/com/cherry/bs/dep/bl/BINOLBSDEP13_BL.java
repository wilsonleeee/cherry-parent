/*
 * @(#)BINOLBSDEP13_BL.java     1.0 2011.2.10
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

import com.cherry.bs.dep.service.BINOLBSDEP92_Service;
import com.cherry.cm.core.CherryException;

/**
 * 品牌删除画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP13_BL {
	
	/** 品牌信息管理共通Service */
	@Resource
	private BINOLBSDEP92_Service binOLBSDEP92_Service;
	
	/**
	 * 
	 * 伦理删除品牌
	 * 
	 * @param map 更新条件
	 */
	@SuppressWarnings("unchecked")
	public void tran_deleteBrandInfo(Map<String, Object> map) throws Exception {
		
		List<String> brandList = (List)map.get("brandInfoId");
		if(brandList != null && !brandList.isEmpty()) {
			// 伦理删除品牌
			int result = binOLBSDEP92_Service.deleteBrandInfo(map);
			if(result != brandList.size()) {
				throw new CherryException("ECM00011");
			}
		} else {
			throw new CherryException("ECM00011");
		}
	}

}
