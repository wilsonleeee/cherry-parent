/*
 * @(#)BINOLBSDEP09_BL.java     1.0 2011.2.10
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

import com.cherry.bs.dep.service.BINOLBSDEP91_Service;
import com.cherry.cm.core.CherryException;

/**
 * 组织删除画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP09_BL {
	
	/** 组织信息管理共通Service */
	@Resource
	private BINOLBSDEP91_Service binOLBSDEP91_Service;
	
	/**
	 * 
	 * 伦理删除组织
	 * 
	 * @param map 更新条件
	 */
	@SuppressWarnings("unchecked")
	public void tran_deleteOrganization(Map<String, Object> map) throws Exception {
		
		List<String> orgList = (List)map.get("organizationInfoId");
		if(orgList != null && !orgList.isEmpty()) {
			// 伦理删除组织
			int result = binOLBSDEP91_Service.deleteOrganization(map);
			if(result != orgList.size()) {
				throw new CherryException("ECM00011");
			}
		} else {
			throw new CherryException("ECM00011");
		}
	}

}
