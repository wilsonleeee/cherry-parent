/*
 * @(#)BINOLSSPRM08_BL.java     1.0 2010/11/29
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

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ss.prm.service.BINOLSSPRM08_Service;

/**
 * 
 * 促销品分类BL
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM08_BL {
	
	@Resource
	private BINOLSSPRM08_Service binolssprm08_Service;
	
	/**
	 * 取得促销品类别信息
	 * 
	 * @param map
	 * @return
	 */
	public Map searchPrmTypeInfo(Map<String, Object> map) {
		// 取得促销品分类信息
		return binolssprm08_Service.getPrmTypeInfo(map);
	}

}
