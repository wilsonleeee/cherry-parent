/*
 * @(#)BINOLBSRES01_BL.java     1.0 2014/10/29
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
package com.cherry.bs.wem.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.wem.interfaces.BINOLBSWEM02_IF;
import com.cherry.bs.wem.service.BINOLBSWEM02_Service;


/**
 * 设置订货折扣
 * 
 * @author 张博
 * @version 1.0 2015/08/13
 */
public class BINOLBSWEM02_BL implements BINOLBSWEM02_IF{
	
	@Resource(name="binOLBSWEM02_Service")
	private BINOLBSWEM02_Service binOLBSWEM02_Service;

	@Override
	public void save(List<Map<String, Object>> list) {
		binOLBSWEM02_Service.save(list);
	}

	@Override
	public List<Map<String, Object>> getDiscountConfList() {
		return binOLBSWEM02_Service.getDiscountConfList();
	}
		
}